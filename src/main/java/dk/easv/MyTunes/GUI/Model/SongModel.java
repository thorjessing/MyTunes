package dk.easv.MyTunes.GUI.Model;

import dk.easv.MyTunes.BE.Song;
import dk.easv.MyTunes.BLL.SongManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    public Song createSong(Song song) throws Exception {
        return songManager.createSong(song);
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public Song editSong(Song song) throws Exception {
        Song updatedSong = songManager.editSong(song);

        System.out.println(updatedSong.getName());

        if (updatedSong != null) {
            for (int i = 0; i < allSongs.size(); i++) {
                if (allSongs.get(i).getId() == updatedSong.getId()) {
                    allSongs.set(i, updatedSong);
                    break;
                }
            }
        }

        return updatedSong;
    }

    public boolean deleteSong(Song song) throws Exception {
        boolean deleted = songManager.deleteSong(song);

        if (deleted) {
            allSongs.remove(song);
        }

        return deleted;
    }
}
