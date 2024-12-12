package dk.easv.MyTunes.GUI.Controller;

import dk.easv.MyTunes.BE.Playlist;
import dk.easv.MyTunes.GUI.Model.PlaylistModel;
import dk.easv.MyTunes.GUI.ModelHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PopupCreatePlaylist {

    private final PlaylistModel playlistModel;
    @FXML
    private TextField txtFieldName;

    public PopupCreatePlaylist() {
        this.playlistModel = ModelHandler.getInstance().getPlaylistModel();
    }

    @FXML
    private void btnCancel(ActionEvent actionEvent) {
        closeWindow();
    }

    @FXML
    private void btnSave(ActionEvent actionEvent) {
        try {
            Playlist playlist = playlistModel.createPlaylist(new Playlist(txtFieldName.getText()));
            playlistModel.addPlaylist(playlist);
            closeWindow();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    private void closeWindow() {
        Stage stage = (Stage) txtFieldName.getScene().getWindow();
        stage.close();
    }
}
