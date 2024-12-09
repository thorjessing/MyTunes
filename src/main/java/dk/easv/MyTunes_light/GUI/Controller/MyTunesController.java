package dk.easv.MyTunes_light.GUI.Controller;

import dk.easv.MyTunes_light.BE.Playlist;
import dk.easv.MyTunes_light.BE.Song;
import dk.easv.MyTunes_light.BLL.MediaHandler;
import dk.easv.MyTunes_light.GUI.Model.PlaylistModel;
import dk.easv.MyTunes_light.GUI.Model.SongModel;
import dk.easv.MyTunes_light.GUI.ModelHandler;
import dk.easv.MyTunes_light.GUI.PopUp.PlaylistCreate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import java.util.ResourceBundle;

public class MyTunesController implements Initializable {

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
    private TableView<Playlist> tblViewPlaylist; // Korrekt Playlist TableView

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
        playlistModel = ModelHandler.getInstance().getPlaylistModel(); // Tilføjet initialisering
        mediaHandler = new MediaHandler();
    }

    //definere kolonner typer og smider data i songs
    private void populateSongs() {
        songTitleCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        songGenreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        songDurationCol.setCellValueFactory(new PropertyValueFactory<>("length"));
        songArtistCol.setCellValueFactory(new PropertyValueFactory<>("artist"));
        tblViewSongs.setItems(observableListSongs);
    }

    //definerer kolonner typer og smider data i playlister
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


        registerPlaylistChange();
    }

    private void registerPlaylistChange() {
        tblViewPlaylist.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, playlist) -> {
            if (playlist != null) {
                observablePlaylistSongs.setAll(playlist.getSongs());
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
    private void handleAddSongToPlaylist() {
        Song selectedSong = tblViewSongs.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = tblViewPlaylist.getSelectionModel().getSelectedItem();

        if (selectedSong != null && selectedPlaylist != null) {
            playlistModel.addSongToPlaylist(selectedPlaylist, selectedSong);
        }
    }

    @FXML
    private void btnRemovePlaylist() {
        Playlist selectedPlaylist = tblViewPlaylist.getSelectionModel().getSelectedItem();

        if (selectedPlaylist != null) {
            try {
                playlistModel.removePlaylist(selectedPlaylist);
            } catch (Exception e) {
                throw new RuntimeException(e);
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

        //updateVolumeControl(mediaPlayer);

        // User interaction (single click or touch)
        sliderTime.valueChangingProperty().addListener((observable, oldValue, isChanging) -> {
            wasDragged = isChanging;
            if (!wasDragged) {
                mediaPlayer.seek(new Duration(sliderTime.getValue() * 1000));
                lblCurrentDuration.setText(mediaHandler.getTimeFromDouble(sliderTime.getValue()));
            }
        });

        // User dragging the slider
        sliderTime.valueProperty().addListener((observable, oldTime, newTime) -> {
            if (wasDragged && sliderTime.isValueChanging()) {
                lblCurrentDuration.setText(mediaHandler.getTimeFromDouble(newTime.doubleValue()));
            }

            // Check if the slider was clicked or dragged
            wasClicked = Math.abs(oldTime.doubleValue() - newTime.doubleValue()) > 10;

            // This causes CMTimeMakeWithSeconds warning... not sure why
            if (!sliderTime.isValueChanging() && !wasDragged && wasClicked)
                mediaPlayer.seek(new Duration(newTime.doubleValue() * 1000));
        });

        // Listener for mediaplayer
        mediaPlayer.currentTimeProperty().addListener((observable, oldTime, newTime) -> {
            if (wasDragged && wasClicked)
                return;

            // Check if it's time to play the next song
            boolean playNextSong = sliderTime.getMax() - 1 < newTime.toSeconds();
            if (playNextSong)
                playNextSong(true);

            // Update UI
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
}


