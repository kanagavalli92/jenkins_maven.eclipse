package com.sddevops.jenkins_maven.eclipse;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

 class SongTest {

    private Song song1;
    private Song song2;
    private Song song3;

    @BeforeEach
    void setUp() {
        song1 = new Song("1", "Shape of You", "Ed Sheeran", 4.24);
        song2 = new Song("2", "Perfect", "Ed Sheeran", 4.40);
        song3 = new Song("1", "Shape of You", "Ed Sheeran", 4.24); 
    }

    @Test
    void testEquals_sameSongObjects_shouldBeEqual() {
        assertEquals(song1, song3);  
    }

    @Test
    void testEquals_differentSongObjects_shouldNotBeEqual() {
        assertNotEquals(song1, song2);  
    }

    @Test
    void testHashCode_sameSongObjects_shouldBeEqual() {
        assertEquals(song1.hashCode(), song3.hashCode());  
    }

    @Test
    void testHashCode_differentSongObjects_shouldNotBeEqual() {
        assertNotEquals(song1.hashCode(), song2.hashCode());  
    }

    @Test
    void testGettersAndSetters() {
        Song song = new Song("3", "Hello", "Adele", 5.02);

        assertEquals("3", song.getId());
        assertEquals("Hello", song.getTitle());
        assertEquals("Adele", song.getArtiste());
        assertEquals(5.02, song.getSongLength());

        // update values
        song.setId("4");
        song.setTitle("Someone Like You");
        song.setArtiste("Adele Adkins");
        song.setSongLength(4.45);

        assertEquals("4", song.getId());
        assertEquals("Someone Like You", song.getTitle());
        assertEquals("Adele Adkins", song.getArtiste());
        assertEquals(4.45, song.getSongLength());
    }

    @Test
    void testToString() {
        assertEquals("Shape of You by Ed Sheeran", song1.toString());
    }
}