package dk.easv.MyTunes.BLL;

import dk.easv.MyTunes.BE.Playlist;
import dk.easv.MyTunes.DAL.dao.IPlaylistDAO;
import dk.easv.MyTunes.DAL.dao.PlaylistDAO;

import java.util.List;

public class    PlaylistManager {
    private final IPlaylistDAO playlistDAO;

    public PlaylistManager() throws Exception {
        this.playlistDAO = new PlaylistDAO() {
        };
    }

    public List<Playlist> getAllPlaylist() throws Exception {
        return this.playlistDAO.getAllPlaylistWithSongs();
    }

    public Playlist createPlaylist(Playlist playlist) throws Exception {
        return this.playlistDAO.createPlaylist(playlist);
    }

    public boolean deletePlaylist(Playlist playlist) throws Exception {
        return this.playlistDAO.deletePlaylist(playlist);
    }
}
