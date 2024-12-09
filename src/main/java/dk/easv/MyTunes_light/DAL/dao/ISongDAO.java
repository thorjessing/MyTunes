package dk.easv.MyTunes_light.DAL.dao;

import dk.easv.MyTunes_light.BE.Song;

import java.util.List;

public interface ISongDAO {
    List<Song> getAllSongs() throws Exception;
}
