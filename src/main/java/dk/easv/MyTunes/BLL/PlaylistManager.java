package dk.easv.MyTunes.BLL;

import dk.easv.MyTunes.BE.Playlist;
import dk.easv.MyTunes.BE.Song;
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

    public boolean updatePlaylist(Playlist playlist, Song song) throws Exception {
        return this.playlistDAO.updatePlaylist(playlist, song);
    }

    public boolean updateSongOrder(Playlist playlist, Song currentSong, Song nextSong) throws Exception {
        return this.playlistDAO.updateSongOrder(playlist, currentSong, nextSong);
    }

    public Playlist editPlaylist(Playlist playlist) throws Exception {
        return this.playlistDAO.editPlaylist(playlist);
    }
}
