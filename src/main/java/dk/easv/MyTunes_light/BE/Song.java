package dk.easv.MyTunes_light.BE;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Song {
    private int id;
    private String name;
    private String artist;
    private String album;
    private String genre;
    private int duration;
    private int year;

    private String path;

    private MediaPlayer mediaPlayer;

    public Song(int id, String name, String artist, String path, String album, String genre, int duration, int year) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.path = path;
        this.album = album;
        this.genre = genre;
        this.duration = duration;
        this.year = year;

        createMediaPlayer(new File("songs/" + path));
    }

    public Song(String name, String artist, String path, String album, String genre, int duration, int year) {
        this.name = name;
        this.artist = artist;
        this.path = path;
        this.album = album;
        this.genre = genre;
        this.duration = duration;
        this.year = year;

        createMediaPlayer(new File("songs/" + path));
    }

    private void createMediaPlayer(File f) {
        if(f.exists() && !f.isDirectory())
            this.mediaPlayer = new MediaPlayer(new Media(new File("songs/" + path).toURI().toString()));
        else
            this.name = "[INVALID] " + this.name;
    }

    public boolean isSongReady() {
        return mediaPlayer.getStatus().equals(MediaPlayer.Status.READY);
    }

    public MediaPlayer getMediaPlayer() {
        return this.mediaPlayer;
    }

    public String getLengthString() {
        int m = duration / 60;
        int remainingSec = duration % 60;

        return String.format("%d:%02d", m, remainingSec);
    }

    public String getLength() {
        int m = duration / 60;
        int remainingSec = duration % 60;

        return String.format("%dm %02ds", m, remainingSec);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int value) {
        this.id = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String value) {
        this.artist = value;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(String value) {
        this.album = value;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String value) {
        this.genre = value;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int value) {
        this.duration = value;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", genre='" + genre + '\'' +
                ", duration=" + duration +
                ", year=" + year +
                '}';
    }
}

