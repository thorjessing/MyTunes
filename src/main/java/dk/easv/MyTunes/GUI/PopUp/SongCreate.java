package dk.easv.MyTunes.GUI.PopUp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SongCreate {

    private Stage popupStage; // Reference til popup-vinduet

    public void showPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PopupNewSong.fxml"));
            Parent root = loader.load();

            popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Create Song");

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
