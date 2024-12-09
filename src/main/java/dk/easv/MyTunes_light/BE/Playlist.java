package dk.easv.MyTunes_light.BE;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private int id;
    private String name;
    private List<Song> songs;

    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<Song>();
    }

    public Playlist(int id, String name) {
        this.id = id;
        this.name = name;
        this.songs = new ArrayList<Song>();
    }

    public Playlist(int id, String name, ArrayList<Song> songs) {
        this.id = id;
        this.name = name;
        this.songs = songs;
    }

    public Playlist(String name, List<Song> songs) {
        this.name = name;
        this.songs = songs;
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void removeSong(Song song) {
        songs.remove(song);
    }

    public String getName() {
        return this.name;
    }

    public List<Song> getSongs() {
        return this.songs;
    }

    public int getSongCount() {
        return this.songs.size();
    }

    public String getDuration() {
        int total = 0;
        for (Song song : songs)
            total += song.getDuration();

        return String.format("%02dm %02ds", total / 60, total % 60);
    }

    public void setId(int anInt) {
        this.id = anInt;
    }

    public int getId() {
        return id;
    }
}
