package dk.easv.MyTunes.BLL;

import dk.easv.MyTunes.BE.Song;
import dk.easv.MyTunes.DAL.dao.ISongDAO;
import dk.easv.MyTunes.DAL.dao.SongDAO;

import java.util.List;

public class    SongManager {
    private final ISongDAO songDAO;

    public SongManager() throws Exception {
        this.songDAO = new SongDAO();
    }

    public List<Song> getAllSongs() throws Exception {
        return this.songDAO.getAllSongs();
    }
}
