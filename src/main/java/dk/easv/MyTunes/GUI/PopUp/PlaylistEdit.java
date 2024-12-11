package dk.easv.MyTunes.GUI.PopUp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PlaylistEdit {

    private Stage popupStage; // Reference til popup-vinduet

    public void showPopup() {
        try {
            // Indlæs FXML-filen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PopupEditPlaylist.fxml"));
            Parent root = loader.load();

            // Opret en ny stage til popup
            popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL); // Gør det til en modal dialog
            popupStage.setTitle("Edit Playlist"); // Titel på popup

            // Tilføj scene til stage
            Scene scene = new Scene(root);
            popupStage.setScene(scene);

            // Vis popup
            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace(); // Fejlmeddelelse til debugging
        }
    }

    public boolean isPopupOpen() {
        return popupStage != null && popupStage.isShowing(); // Tjek om popup er åben
    }
}
