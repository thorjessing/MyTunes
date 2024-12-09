package dk.easv.MyTunes_light.BLL;

import dk.easv.MyTunes_light.BE.Song;
import dk.easv.MyTunes_light.DAL.dao.ISongDAO;
import dk.easv.MyTunes_light.DAL.dao.SongDAO;

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
