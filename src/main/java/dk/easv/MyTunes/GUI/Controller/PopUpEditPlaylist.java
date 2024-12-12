package dk.easv.MyTunes.GUI.Controller;

import dk.easv.MyTunes.BE.Playlist;
import dk.easv.MyTunes.GUI.Model.PlaylistModel;
import dk.easv.MyTunes.GUI.ModelHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PopUpEditPlaylist {

    private final PlaylistModel playlistModel;
    @FXML
    private TextField txtFieldName;

    public PopUpEditPlaylist() {
        this.playlistModel = ModelHandler.getInstance().getPlaylistModel();
    }

    @FXML
    private void btnCancel(ActionEvent actionEvent) {
        closeWindow();
    }

    @FXML
    private void btnSave(ActionEvent actionEvent) {
        try {
            Playlist selected = ModelHandler.getInstance().getSelectedPlaylist();
            selected.setName(txtFieldName.getText());
            Playlist playlist = playlistModel.editPlaylist(selected);

            PlaylistModel model = ModelHandler.getInstance().getPlaylistModel();

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
