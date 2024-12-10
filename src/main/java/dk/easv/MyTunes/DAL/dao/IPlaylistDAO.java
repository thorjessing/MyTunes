package dk.easv.MyTunes.DAL.dao;

import dk.easv.MyTunes.BE.Playlist;
import dk.easv.MyTunes.BE.Song;

import java.util.ArrayList;
import java.util.List;

public interface IPlaylistDAO {
    List<Playlist> getAllPlaylist() throws Exception;
    List<Playlist> getAllPlaylistWithSongs() throws Exception;

    ArrayList<Song> getAllPlaylistSongs(int playlistId) throws Exception;

    Playlist createPlaylist(Playlist playlist) throws Exception;
    boolean deletePlaylist(Playlist playlist) throws Exception;
    boolean updatePlaylist(Playlist playlist, Song song) throws Exception;

}
