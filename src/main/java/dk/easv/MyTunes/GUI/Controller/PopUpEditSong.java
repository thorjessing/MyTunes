package dk.easv.MyTunes.GUI.Controller;

import dk.easv.MyTunes.BE.Song;
import dk.easv.MyTunes.GUI.Model.SongModel;
import dk.easv.MyTunes.GUI.ModelHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class PopUpEditSong implements Initializable {

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

    public PopUpEditSong() {
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

            Song selectedSong = ModelHandler.getInstance().getSelectedSong();

            if (selectedSong == null) {
                return;
            }
            selectedSong.setName(title);
            selectedSong.setArtist(artist);
            selectedSong.setGenre(category);
            selectedSong.setDuration(durationInt);
            selectedSong.setPath(file);



            Song song = songModel.editSong(selectedSong);

            closeWindow();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) txtFieldTitle.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Song selectedSong = ModelHandler.getInstance().getSelectedSong();

        if (selectedSong == null) {
            return;
        }

        txtFieldTitle.setText(selectedSong.getName());
        txtFieldArtist.setText(selectedSong.getArtist());
        txtFieldCategory.setText(selectedSong.getGenre());
        txtFieldDuration.setText(String.valueOf(selectedSong.getDuration()));
        txtFieldFile.setText(selectedSong.getPath());
    }
}
