package dk.easv.MyTunes.GUI;

import dk.easv.MyTunes.BE.Playlist;
import dk.easv.MyTunes.BE.Song;
import dk.easv.MyTunes.GUI.Model.PlaylistModel;
import dk.easv.MyTunes.GUI.Model.SongModel;

public class ModelHandler {
    private static ModelHandler instance;

    private PlaylistModel playlistModel;
    private SongModel songModel;

    private Playlist selectedPlaylist;
    private Song selectedSong;

    private ModelHandler() throws Exception {
        try {
            playlistModel = new PlaylistModel();
            songModel = new SongModel();
        } catch (Exception e) {
            throw new Exception("Kunne ikke oprette ModelHandlere");
        }

    }


    public static ModelHandler getInstance() {
        if (instance == null) {
            try {
                instance = new ModelHandler();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return instance;
    }

    public PlaylistModel getPlaylistModel() {
        return playlistModel;
    }

    public SongModel getSongModel() {
        return songModel;
    }


    public Playlist getSelectedPlaylist() {
        return selectedPlaylist;
    }

    public void setSelectedPlaylist(Playlist selectedPlaylist) {
        this.selectedPlaylist = selectedPlaylist;
    }

    public Song getSelectedSong() {
        return selectedSong;
    }

    public void setSelectedSong(Song selectedSong) {
        this.selectedSong = selectedSong;
    }
}
