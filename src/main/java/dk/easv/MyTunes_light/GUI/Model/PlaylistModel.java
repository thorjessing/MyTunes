package dk.easv.MyTunes_light.GUI.Model;


import dk.easv.MyTunes_light.BE.Playlist;
import dk.easv.MyTunes_light.BE.Song;
import dk.easv.MyTunes_light.BLL.PlaylistManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

}
