package dk.easv.MyTunes_light.DAL.dao;

import dk.easv.MyTunes_light.BE.Song;
import dk.easv.MyTunes_light.DAL.DBConnecter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
}
