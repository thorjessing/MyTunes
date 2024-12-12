package dk.easv.MyTunes.GUI.Controller;

import dk.easv.MyTunes.BE.Song;
import dk.easv.MyTunes.GUI.Model.SongModel;
import dk.easv.MyTunes.GUI.ModelHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class PopUpCreateSong {

    private final SongModel songModel;

    @FXML
    private TextField txtFieldTitle;
    @FXML
    private TextField txtFieldArtist;
    @FXML
    private TextField txtFieldCategory;
    @FXML
    private TextField txtFieldDuration;
    @FXML
    private TextField txtFieldFile;

    private String path;

    public PopUpCreateSong() {
        this.songModel = ModelHandler.getInstance().getSongModel();
    }

    @FXML
    private void btnChooseFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));

        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            String fileName = file.getName();

            txtFieldFile.setText(fileName);
        }
    }




    @FXML
    private void btnCancel(ActionEvent actionEvent) {
        closeWindow();
    }

    @FXML
    private void btnSave(ActionEvent actionEvent) {
        try {
            String title = txtFieldTitle.getText();
            String artist = txtFieldArtist.getText();
            String category = txtFieldCategory.getText();
            String duration = txtFieldDuration.getText();
            String file = txtFieldFile.getText();

            if (title.isEmpty() || artist.isEmpty() || category.isEmpty() || duration.isEmpty()) {
                return;
            }

            int durationInt = Integer.parseInt(duration);

            Song song = songModel.createSong(new Song(title, artist, file, "", category, durationInt, 0));
            songModel.addSong(song);
            closeWindow();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) txtFieldTitle.getScene().getWindow();
        stage.close();
    }

}
