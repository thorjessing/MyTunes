package dk.easv.MyTunes.GUI.PopUp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PlaylistCreate {

    public void showPopup() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PopupCreatePlaylist.fxml"));
            Parent root = loader.load();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Create Playlist");

            Scene scene = new Scene(root);
            popupStage.setScene(scene);


            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
