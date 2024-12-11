package dk.easv.MyTunes.GUI.Model;


import dk.easv.MyTunes.BE.Playlist;
import dk.easv.MyTunes.BE.Song;
import dk.easv.MyTunes.BLL.PlaylistManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;

public class PlaylistModel {
    private final ObservableList<Playlist> playlists;
    private final PlaylistManager playlistManager;

    public PlaylistModel() throws Exception {
        try {
            playlistManager = new PlaylistManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        playlists = FXCollections.observableArrayList();
        playlists.addAll(playlistManager.getAllPlaylist());

    }

    public Playlist createPlaylist(Playlist playlist) throws Exception {
        return playlistManager.createPlaylist(playlist);
    }

    public ObservableList<Playlist> getPlaylists() {
        return playlists;
    }

    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
    }

    public void removePlaylist(Playlist playlist) throws Exception {
        boolean deletedb = playlistManager.deletePlaylist(playlist);

        if (deletedb)
            playlists.remove(playlist);
    }

    public void addSongToPlaylist(Playlist playlist, Song song) {
        playlist.addSong(song);
    }

    public void removeSongFromPlaylist(Playlist playlist, Song song) {
        playlist.removeSong(song);
    }

    public boolean updateSongOrder(Playlist playlist, Song currentSong, Song nextSong) throws Exception {
        return playlistManager.updateSongOrder(playlist, currentSong, nextSong);
    }


    public boolean updatePlaylist(int idx, Playlist playlist, Song song) throws Exception {
        boolean didUpdateDB = playlistManager.updatePlaylist(playlist, song);

        if (!didUpdateDB)
            return false;

        addSongToPlaylist(playlist, song);
        return true;
    }

}
