package dk.easv.MyTunes.GUI.Controller;

import dk.easv.MyTunes.BE.Playlist;
import dk.easv.MyTunes.BE.Song;
import dk.easv.MyTunes.BLL.MediaHandler;
import dk.easv.MyTunes.GUI.Model.PlaylistModel;
import dk.easv.MyTunes.GUI.Model.SongModel;
import dk.easv.MyTunes.GUI.ModelHandler;
import dk.easv.MyTunes.GUI.PopUp.PlaylistCreate;
import dk.easv.MyTunes.GUI.PopUp.PlaylistEdit;
import dk.easv.MyTunes.GUI.PopUp.SongCreate;
import dk.easv.MyTunes.GUI.PopUp.SongEdit;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.*;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;

import java.util.Comparator;

import java.util.ResourceBundle;

import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

public class MyTunesController implements Initializable {

    @FXML
    public Button btnAddSong;
    @FXML
    private TableView<Song> tblViewSongs;
    @FXML
    private TableColumn<Song, String> songDurationCol;
    @FXML
    private TableColumn<Song, String> songGenreCol;
    @FXML
    private TableColumn<Song, String> songArtistCol;
    @FXML
    private TableColumn<Song, String> songTitleCol;

    @FXML
    private TableView<Playlist> tblViewPlaylist;

    private MediaHandler mediaHandler;

    private SongModel songModel;
    private PlaylistModel playlistModel;
    private ObservableList<Song> observableListSongs;
    private ObservableList<Playlist> observableListPlaylists;
    private ObservableList<Song> observablePlaylistSongs;

    @FXML
    private TableColumn<Playlist, String> playlistNameCol;
    @FXML
    private TableColumn<Playlist, Integer> playlistSongsCol;
    @FXML
    private TableColumn<Playlist, String> playlistDurationCol;
    @FXML
    private ListView<Song> listViewPlaylistSongs;
    @FXML
    private Button PauseBTN;
    @FXML
    private Slider slidrVol;
    @FXML
    private Slider sliderTime;
    @FXML
    private Label lblCurrentDuration;
    @FXML
    private Label lblMaxDuration;

    private boolean wasDragged;
    private boolean wasClicked;

    private int amountMoved;
    @FXML
    private Label lblSongName;
    @FXML
    private TextField txtFieldSeach;
    @FXML
    private Button btnEditSong;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            observableListSongs = songModel.getSongs();
            observableListPlaylists = playlistModel.getPlaylists();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        observablePlaylistSongs = FXCollections.observableArrayList();

        populateSongs();
        populatePlaylists();
        volume();
        searchHandler();
    }

    public MyTunesController() throws Exception {
        songModel = ModelHandler.getInstance().getSongModel();
        playlistModel = ModelHandler.getInstance().getPlaylistModel();
        mediaHandler = new MediaHandler();
    }


    private void populateSongs() {
        songTitleCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        songGenreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        songDurationCol.setCellValueFactory(new PropertyValueFactory<>("length"));
        songArtistCol.setCellValueFactory(new PropertyValueFactory<>("artist"));
        tblViewSongs.setItems(observableListSongs);
    }


    private void populatePlaylists() {
        playlistNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        playlistSongsCol.setCellValueFactory(new PropertyValueFactory<>("songCount"));
        playlistDurationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));

        tblViewPlaylist.setItems(observableListPlaylists);

        listViewPlaylistSongs.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Song song, boolean empty) {
                super.updateItem(song, empty);
                setText(empty || song == null ? null : song.getArtist() + " - " + song.getName());
            }
        });


        observableListPlaylists.addListener((ListChangeListener<Playlist>) change -> {
            while (change.next()) {
                if (change.wasUpdated()) {
                    tblViewPlaylist.refresh();
                }
            }
        });

        registerPlaylistChange();
    }

    private void registerPlaylistChange() {
        tblViewPlaylist.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, playlist) -> {
            if (playlist != null) {
                ModelHandler.getInstance().setSelectedPlaylist(playlist);
                observablePlaylistSongs.setAll(playlist.getSongs());
                observablePlaylistSongs.sort(Comparator.comparingInt(Song::getOrder));
                listViewPlaylistSongs.setItems(observablePlaylistSongs);
            }
        });
    }

    @FXML
    private void handleCreatePlaylist() {
        PlaylistCreate popup = new PlaylistCreate();
        popup.showPopup();
    }

    @FXML
    private void handleAddSongToPlaylist() throws Exception {
        Song selectedSong = tblViewSongs.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = tblViewPlaylist.getSelectionModel().getSelectedItem();
        int idx = tblViewPlaylist.getSelectionModel().getSelectedIndex();
        if (selectedSong != null && selectedPlaylist != null) {
            boolean update = playlistModel.updatePlaylist(idx, selectedPlaylist, selectedSong);
            if (!update)
                return;

            observablePlaylistSongs.setAll(selectedPlaylist.getSongs());
            listViewPlaylistSongs.setItems(observablePlaylistSongs);
            tblViewPlaylist.refresh();
        }
    }

    @FXML
    private void btnRemovePlaylist() {
        Playlist selectedPlaylist = tblViewPlaylist.getSelectionModel().getSelectedItem();

        if (selectedPlaylist != null) {
            try {

                playlistModel.removePlaylist(selectedPlaylist);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private MediaPlayer getSongMediaPlayer() {
        Song song = listViewPlaylistSongs.getSelectionModel().getSelectedItem();
        if (song == null) return null;

        return song.getMediaPlayer();
    }

    private void volume() {
        slidrVol.setMin(0);
        slidrVol.setMax(100);

        slidrVol.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                MediaPlayer currentSong = getSongMediaPlayer();

                if (currentSong == null) return;

                currentSong.setVolume(newValue.doubleValue() / 100);
            }
        });
    }

    public void playNextSong(boolean isNext) {
        int currentIndex = listViewPlaylistSongs.getSelectionModel().getSelectedIndex();

        int next = isNext ? 1 : -1;
        int nextSongIndex = currentIndex + next;
        int size = listViewPlaylistSongs.getItems().size();

        if (nextSongIndex < 0 || nextSongIndex >= size)
            return;

        Song nextSong = listViewPlaylistSongs.getItems().get(nextSongIndex);

        listViewPlaylistSongs.getSelectionModel().select(nextSong);

        playSong(nextSong.getMediaPlayer());
    }

    @FXML
    private void playSongBtn(ActionEvent actionEvent) {
        playSong(getSongMediaPlayer());
    }

    private void playSong(MediaPlayer currentSong) {
        Song song = listViewPlaylistSongs.getSelectionModel().getSelectedItem();
        if (currentSong == null) return;

        currentSong.setVolume(slidrVol.getValue() / 100);

        mediaHandler.playSong(currentSong, false);

        boolean isPlaying = currentSong.getStatus().equals(MediaPlayer.Status.PLAYING);
        PauseBTN.setText(isPlaying ? ">" : "II");

        lblSongName.setText(song.getArtist() + " - " + song.getName());

        updatePlayerControls(currentSong, song);
    }

    private void updatePlayerControls(MediaPlayer mediaPlayer, Song song) {
        double songLength = mediaPlayer.getTotalDuration().toSeconds();
        sliderTime.setMax(songLength);
        lblMaxDuration.setText(song.getLengthString());

        sliderTime.valueChangingProperty().addListener((observable, oldValue, isChanging) -> {
            wasDragged = isChanging;
            if (!wasDragged) {
                mediaPlayer.seek(new Duration(sliderTime.getValue() * 1000));
                lblCurrentDuration.setText(mediaHandler.getTimeFromDouble(sliderTime.getValue()));
            }
        });

        sliderTime.valueProperty().addListener((observable, oldTime, newTime) -> {
            if (wasDragged && sliderTime.isValueChanging()) {
                lblCurrentDuration.setText(mediaHandler.getTimeFromDouble(newTime.doubleValue()));
            }

            wasClicked = Math.abs(oldTime.doubleValue() - newTime.doubleValue()) > 10;

            if (!sliderTime.isValueChanging() && !wasDragged && wasClicked)
                mediaPlayer.seek(new Duration(newTime.doubleValue() * 1000));
        });

        mediaPlayer.currentTimeProperty().addListener((observable, oldTime, newTime) -> {
            if (wasDragged && wasClicked)
                return;

            boolean playNextSong = sliderTime.getMax() - 1 < newTime.toSeconds();
            if (playNextSong)
                playNextSong(true);

            lblCurrentDuration.setText(mediaHandler.getTimeFromDouble(newTime.toSeconds()));
            sliderTime.setValue(newTime.toSeconds());
        });
    }

    @FXML
    private void btnPlayNext(ActionEvent actionEvent) {
        playNextSong(true);
    }

    @FXML
    private void btnPrevSong(ActionEvent actionEvent) {
        playNextSong(false);
    }

    private void searchHandler() {
        txtFieldSeach.textProperty().addListener((observable, oldValue, newValue) -> {
            tblViewSongs.setItems(songModel.searchSong(newValue));
        });
    }

    @FXML
    private void btnCloseWindow(ActionEvent actionEvent) {
        System.exit(0);
    }

    @FXML
    private void btnMoveUp(ActionEvent actionEvent) {
        int currentIndex = listViewPlaylistSongs.getSelectionModel().getSelectedIndex();

        int nextSongIndex = currentIndex - 1;
        int size = listViewPlaylistSongs.getItems().size();

        if (nextSongIndex < 0 || nextSongIndex >= size)
            return;

        Song currentSong = listViewPlaylistSongs.getSelectionModel().getSelectedItem();
        Song nextSong = listViewPlaylistSongs.getItems().get(nextSongIndex);

        if (currentSong == null || nextSong == null)
            return;

        Playlist currentPlaylist = tblViewPlaylist.getSelectionModel().getSelectedItem();

        if (currentPlaylist == null)
            return;

        try {
            playlistModel.updateSongOrder(currentPlaylist, currentSong, nextSong);
            observablePlaylistSongs.setAll(currentPlaylist.getSongs());
            observablePlaylistSongs.sort(Comparator.comparingInt(Song::getOrder));
            listViewPlaylistSongs.setItems(observablePlaylistSongs);
            listViewPlaylistSongs.getSelectionModel().select(nextSong);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void btnMoveDown(ActionEvent actionEvent) {
        int currentIndex = listViewPlaylistSongs.getSelectionModel().getSelectedIndex();

        int nextSongIndex = currentIndex + 1;
        int size = listViewPlaylistSongs.getItems().size();

        if (nextSongIndex < 0 || nextSongIndex >= size)
            return;

        Song currentSong = listViewPlaylistSongs.getSelectionModel().getSelectedItem();
        Song nextSong = listViewPlaylistSongs.getItems().get(nextSongIndex);

        if (currentSong == null || nextSong == null)
            return;

        Playlist currentPlaylist = tblViewPlaylist.getSelectionModel().getSelectedItem();

        if (currentPlaylist == null)
            return;

        try {
            playlistModel.updateSongOrder(currentPlaylist, currentSong, nextSong);
            observablePlaylistSongs.setAll(currentPlaylist.getSongs());
            observablePlaylistSongs.sort(Comparator.comparingInt(Song::getOrder));
            listViewPlaylistSongs.setItems(observablePlaylistSongs);
            listViewPlaylistSongs.getSelectionModel().select(nextSong);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void btnEditPlaylist(ActionEvent actionEvent) {
        PlaylistEdit popup = new PlaylistEdit();
        popup.showPopup();
    }

    @FXML
    private void btnDeleteSongPlaylist(ActionEvent actionEvent) {
        Playlist selectedPlaylist = tblViewPlaylist.getSelectionModel().getSelectedItem();
        Song selectedSong = listViewPlaylistSongs.getSelectionModel().getSelectedItem();

        if (selectedPlaylist != null && selectedSong != null) {
            try {
                playlistModel.deleteSongFromPlaylist(selectedPlaylist, selectedSong);

                selectedPlaylist.removeSong(selectedSong);
                observablePlaylistSongs.remove(selectedSong);

                tblViewPlaylist.refresh();

                showAlert("Sletning lykkedes", "Sangen blev fjernet fra playlisten.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Fejl", "Kunne ikke slette sangen fra playlisten.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Advarsel", "Vælg en sang og en playliste for at slette.", Alert.AlertType.WARNING);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void btnCreateSong(ActionEvent actionEvent) {
        SongCreate songCreate = new SongCreate();
        songCreate.showPopup();
    }

    @FXML
    private void btnEditSong(ActionEvent actionEvent) {
        Song selected = tblViewSongs.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Fejl", "Ingen sang valgt.", Alert.AlertType.ERROR);
            return;
        }

        ModelHandler.getInstance().setSelectedSong(selected);

        SongEdit songEdit = new SongEdit();
        songEdit.showPopup();
    }

    @FXML
    private void btnDeleteSong(ActionEvent actionEvent) {
        Song selectedSong = tblViewSongs.getSelectionModel().getSelectedItem();

        if (selectedSong == null) {
            showAlert("Fejl", "Ingen sang valgt.", Alert.AlertType.ERROR);
            return;
        }

        try {
            songModel.deleteSong(selectedSong);

            Playlist playlist = tblViewPlaylist.getSelectionModel().getSelectedItem();
            if (playlist != null) {
                playlist.getSongs().remove(selectedSong);

                observablePlaylistSongs.setAll(playlist.getSongs());
                observablePlaylistSongs.sort(Comparator.comparingInt(Song::getOrder));
                listViewPlaylistSongs.setItems(observablePlaylistSongs);
            }

            tblViewSongs.setItems(songModel.getSongs());
            tblViewPlaylist.refresh();

            tblViewSongs.refresh();

        } catch (Exception e) {
            showAlert("Fejl", e.getMessage(), Alert.AlertType.ERROR);

        }
    }
}


