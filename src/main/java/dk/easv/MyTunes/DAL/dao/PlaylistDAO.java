package dk.easv.MyTunes.DAL.dao;

import dk.easv.MyTunes.BE.Playlist;
import dk.easv.MyTunes.BE.Song;
import dk.easv.MyTunes.DAL.DBConnecter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlaylistDAO implements IPlaylistDAO {
    private final DBConnecter dbConnecter;

    public PlaylistDAO() throws Exception {
        try {
            this.dbConnecter = new DBConnecter();
        } catch (Exception e) {
            throw new Exception("Der skete en fejl ved forbindelse til databasen");
        }
    }

    @Override
    public List<Playlist> getAllPlaylist() throws Exception {
        List<Playlist> playlists = new ArrayList<>();

        String query = "SELECT * FROM Playlist ORDER by id";

        try (Connection conn = dbConnecter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");

                playlists.add(new Playlist(id, name));
            }

            return playlists;
        } catch (Exception e) {
            throw new Exception("Kunne ikke få fat i alle playlister fra databasen");
        }
    }

    @Override
    public List<Playlist> getAllPlaylistWithSongs() throws Exception {
        List<Playlist> playlists = new ArrayList<>();

        String query = "SELECT * FROM Playlist ORDER by id";

        try (Connection conn = dbConnecter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                ArrayList<Song> songs = this.getAllPlaylistSongs(id);
                playlists.add(new Playlist(id, name, songs));
            }

            return playlists;
        } catch (Exception e) {
            throw new Exception("Kunne ikke få fat i alle playlister med sange fra databasen");
        }
    }

    @Override
    public ArrayList<Song> getAllPlaylistSongs(int playlistId) throws Exception {
        ArrayList<Song> songs = new ArrayList<>();

        String query = """
                       
                SELECT Playlist_songs.order_id, id, Songs.artist, Songs.duration, Songs.genre, Songs.path, Songs.ReleaseYear, Songs.name
                       FROM Playlist_songs
                       JOIN Songs ON Playlist_songs.Song_id = Songs.id
                       WHERE Playlist_songs.Playlist_id = ?
                       ORDER BY id
                        """;

        try (Connection conn = dbConnecter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, playlistId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String artist = rs.getString("artist");
                int duration = rs.getInt("duration");
                String genre = rs.getString("genre");
                String path = rs.getString("path");
                //String album = rs.getString("album");
                int releaseYear = rs.getInt("ReleaseYear");
                String name = rs.getString("name");
                int order_id = rs.getInt("order_id");

                songs.add(new Song(id, name, artist, path, "album", genre, duration, releaseYear, order_id));
            }

            return songs;
        } catch (Exception e) {
            throw new Exception("Kunne få fat i alle sange fra playlist Id: " + playlistId + " fra databasen");
        }

    }

    @Override
    public Playlist createPlaylist(Playlist playlist) throws Exception {
        String query = """
                INSERT INTO Playlist (name)
                       VALUES(?);
                """;

        try (Connection conn = dbConnecter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, playlist.getName());

            int rowsAffected = stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    playlist.setId(rs.getInt(1));
                }
            }

            return playlist;
        } catch (Exception e) {
            throw new Exception("Kunne ikke oprette playlist i databas");
        }
    }

    @Override
    public boolean deletePlaylist(Playlist playlist) throws Exception {
        String query = """
                DELETE FROM Playlist WHERE id = ?
                """;

        try (Connection conn = dbConnecter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, playlist.getId());
            stmt.executeUpdate();

            return true;
        } catch (Exception e) {
            throw new Exception("Kunne ikke slette playlist fra database");
        }
    }

    @Override
    public boolean updatePlaylist(Playlist playlist, Song song) throws Exception {
        String query = "INSERT INTO Playlist_songs (Playlist_id,Song_id,order_id) VALUES(?,?,?)";
        try (Connection conn = dbConnecter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, playlist.getId());
            stmt.setInt(2, song.getId());

            stmt.setInt(3, 1);
            stmt.executeUpdate();

            return true;
        } catch (Exception e) {
            throw new Exception("Kunne ikke tilføje sange til playlisten");
        }

    }

    @Override
    public boolean updateSongOrder(Playlist playlist, Song currentSong, Song nextSong) throws Exception {
        String sql = "UPDATE Playlist_songs SET order_id = ? WHERE Song_id = ? AND Playlist_id = ?;";

        Connection conn = null;
        try {
            conn = dbConnecter.getConnection();
            conn.setAutoCommit(false);  // Start a transaction

            int orderIDNew = currentSong.getOrder();
            int orderIDOld = nextSong.getOrder();

            try (PreparedStatement stmtOld = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmtOld.setInt(1, currentSong.getOrder());
                stmtOld.setInt(2, nextSong.getId());
                stmtOld.setInt(3, playlist.getId());
                stmtOld.executeUpdate();
            }

            try (PreparedStatement stmtNew = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmtNew.setInt(1, nextSong.getOrder());
                stmtNew.setInt(2, currentSong.getId());
                stmtNew.setInt(3, playlist.getId());
                stmtNew.executeUpdate();
            }

            conn.commit();

            currentSong.setOrder(orderIDOld);
            nextSong.setOrder(orderIDNew);

            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();  // If an error occurs, rollback the transaction
                } catch (SQLException ex) {
                    ex.printStackTrace();  // Handle rollback exception
                }
            }
            e.printStackTrace();
            throw new Exception("Kunne ikke opdatere playlist ordren i databasen", e);
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public Playlist editPlaylist(Playlist playlist) throws Exception {
        String query = """
                UPDATE Playlist
                SET name = ?
                    WHERE id = ?;
                """;

        try (Connection conn = dbConnecter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, playlist.getName());
            stmt.setInt(2, playlist.getId());
            stmt.executeUpdate();

            return playlist;
        } catch (Exception e) {
            throw new Exception("Kunne ikke opdatere playlisten");
        }
    }

    public boolean deleteSongFromPlaylist(Playlist playlist, Song song) throws Exception {
        String sql = "DELETE FROM Playlist_songs WHERE playlist_id = ? AND song_id = ?";
        try (Connection conn = dbConnecter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playlist.getId());
            stmt.setInt(2, song.getId());
            stmt.executeUpdate();

            return true;
        } catch (Exception e) {
            throw new Exception("Kunne ikke slette sang fra playliste");
        }
    }
}

