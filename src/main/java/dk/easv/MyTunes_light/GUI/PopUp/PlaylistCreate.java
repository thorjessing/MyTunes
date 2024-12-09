package dk.easv.MyTunes_light.GUI.PopUp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PlaylistCreate {

    public void showPopup() {
        try {
            // Indlæs FXML-filen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PopupCreatePlaylist.fxml"));
            Parent root = loader.load();

            // Opret en ny stage til popup
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL); // Gør det til en modal dialog
            popupStage.setTitle("Create Playlist"); // Titel på popup

            // Tilføj scene til stage
            Scene scene = new Scene(root);
            popupStage.setScene(scene);

            // Vis popup
            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace(); // Fejlmeddelelse til debugging
        }
    }
}
