package dk.easv.MyTunes.DAL.dao;

import dk.easv.MyTunes.BE.Song;

import java.util.List;

public interface ISongDAO {
    List<Song> getAllSongs() throws Exception;
}
