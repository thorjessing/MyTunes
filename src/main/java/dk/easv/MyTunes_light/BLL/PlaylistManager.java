package dk.easv.MyTunes_light.BLL;

import dk.easv.MyTunes_light.BE.Playlist;
import dk.easv.MyTunes_light.BE.Song;
import dk.easv.MyTunes_light.DAL.dao.IPlaylistDAO;
import dk.easv.MyTunes_light.DAL.dao.ISongDAO;
import dk.easv.MyTunes_light.DAL.dao.PlaylistDAO;
import dk.easv.MyTunes_light.DAL.dao.SongDAO;
import dk.easv.MyTunes_light.GUI.Model.PlaylistModel;

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
