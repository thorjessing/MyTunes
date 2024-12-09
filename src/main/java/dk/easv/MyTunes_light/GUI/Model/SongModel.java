package dk.easv.MyTunes_light.GUI.Model;

import dk.easv.MyTunes_light.BE.Song;
import dk.easv.MyTunes_light.BLL.SongManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SongModel {

    private ObservableList<Song> songs;
    private SongManager songManager;

    private ObservableList<Song> allSongs;

    public SongModel() throws Exception {

        songManager = new SongManager();

        allSongs = FXCollections.observableArrayList();
        allSongs.addAll(songManager.getAllSongs());
    }

    public ObservableList<Song> getSongs() throws Exception {
        songs = FXCollections.observableArrayList();
        songs.addAll(songManager.getAllSongs());
        return songs;
    }

    public ObservableList<Song> searchSong(String searchWord) {
        List<Song> searchSongs = new ArrayList<>();
        for(Song song : allSongs) {
            String songName = song.getArtist() + " " + song.getName();
            if (songName.toLowerCase().contains(searchWord.toLowerCase())) {
                searchSongs.add(song);
            }
        }
        return FXCollections.observableArrayList(searchSongs);
    }
}
