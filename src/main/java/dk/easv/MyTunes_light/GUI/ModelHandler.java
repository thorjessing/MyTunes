package dk.easv.MyTunes_light.GUI;

import dk.easv.MyTunes_light.GUI.Model.PlaylistModel;
import dk.easv.MyTunes_light.GUI.Model.SongModel;

public class ModelHandler {
    private static ModelHandler instance;

    private PlaylistModel playlistModel;
    private SongModel songModel;

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
}
