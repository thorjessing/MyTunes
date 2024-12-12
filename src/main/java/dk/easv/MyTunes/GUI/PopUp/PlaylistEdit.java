package dk.easv.MyTunes.GUI.PopUp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PlaylistEdit {

    private Stage popupStage;

    public void showPopup() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PopupEditPlaylist.fxml"));
            Parent root = loader.load();

            popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Edit Playlist");

            Scene scene = new Scene(root);
            popupStage.setScene(scene);

            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPopupOpen() {
        return popupStage != null && popupStage.isShowing();
    }
}
