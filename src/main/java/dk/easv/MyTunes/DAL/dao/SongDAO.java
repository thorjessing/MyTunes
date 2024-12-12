package dk.easv.MyTunes.DAL.dao;

import dk.easv.MyTunes.BE.Song;
import dk.easv.MyTunes.DAL.DBConnecter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SongDAO implements ISongDAO {
    private final DBConnecter dbConnecter;

    public SongDAO() throws Exception {
        try {
            this.dbConnecter = new DBConnecter();
        } catch (Exception e) {
            throw new Exception("Der skete en fejl ved forbindelse til databasen");
        }
    }

    @Override
    public List<Song> getAllSongs() throws Exception {
        List<Song> songs = new ArrayList<>();

        String query = "SELECT * FROM songs";

        try (Connection conn = dbConnecter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String artist = rs.getString("artist");
                int duration = rs.getInt("duration");
                String genre = rs.getString("genre");
                String path = rs.getString("path");
                //String album = rs.getString("album");
                int releaseYear = rs.getInt("ReleaseYear");
                String name = rs.getString("name");

                songs.add(new Song(id, name, artist, path, "album", genre, duration, releaseYear));
            }

            return songs;
        } catch (Exception e) {
            throw new Exception("Kunne ikke få fat i alle sange fra databasen");
        }
    }

    @Override
    public Song createSong(Song song) throws Exception {
        String query = """
                INSERT INTO Songs (name, artist, path, genre, duration, releaseYear)
                       VALUES(?, ?, ?, ?, ?, ?);
                """;

        try (Connection conn = dbConnecter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, song.getName());
            stmt.setString(2, song.getArtist());
            stmt.setString(3, song.getPath());
            stmt.setString(4, song.getGenre());
            stmt.setInt(5, song.getDuration());
            stmt.setInt(6, 0);

            int rowsAffected = stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    song.setId(rs.getInt(1));
                }
            }

            return song;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Song editSong(Song song) throws Exception {
        String query = """
                    UPDATE Songs
                    SET genre= ? ,path= ?, duration= ?,artist= ?,ReleaseYear= ?,name = ?
                    WHERE id = ?;
                    """;

        try (Connection conn = dbConnecter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, song.getGenre());
            stmt.setString(2, song.getPath());
            stmt.setInt(3, song.getDuration());
            stmt.setString(4, song.getArtist());
            stmt.setInt(5, 0);
            stmt.setString(6, song.getName());

            stmt.setInt(7, song.getId());
            stmt.executeUpdate();

            return song;
        } catch (Exception e) {
            throw new Exception("Kunne ikke redigere sangen");
        }
    }

    @Override
    public boolean deleteSong(Song song) throws Exception {
        String sql = "DELETE FROM Songs WHERE id = ?";
        try (Connection conn = dbConnecter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, song.getId());
            stmt.executeUpdate();

            return true;
        } catch (Exception e) {
            throw new Exception("Kunne ikke slette sang");
        }
    }

}
