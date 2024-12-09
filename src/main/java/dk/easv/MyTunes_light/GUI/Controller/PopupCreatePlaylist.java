package dk.easv.MyTunes_light.GUI.Controller;

import dk.easv.MyTunes_light.BE.Playlist;
import dk.easv.MyTunes_light.GUI.Model.PlaylistModel;
import dk.easv.MyTunes_light.GUI.ModelHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.desktop.SystemEventListener;

public class PopupCreatePlaylist {

    private final PlaylistModel playlistModel;
    @FXML
    private TextField txtFieldName;

    public PopupCreatePlaylist() {
        this.playlistModel = ModelHandler.getInstance().getPlaylistModel();
    }

    @FXML
    private void btnCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnSave(ActionEvent actionEvent) {
        try {
            Playlist playlist = playlistModel.createPlaylist(new Playlist(txtFieldName.getText()));
            playlistModel.addPlaylist(playlist);
        } catch (Exception e) {
            // håndter den her i gui
            throw new RuntimeException(e);
        }

    }
}
