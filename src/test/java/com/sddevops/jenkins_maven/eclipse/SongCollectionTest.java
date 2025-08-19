package com.sddevops.jenkins_maven.eclipse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;

class SongCollectionTest {

    private SongCollection collection;
    private Song s1, s2, s3, s4;
    private final int songCollectionSize = 4;

    @BeforeEach
    void setUp() {
        collection = new SongCollection();
        s1 = new Song("001", "good 4 u", "Olivia Rodrigo", 3.59);
        s2 = new Song("002", "Peaches", "Justin Bieber", 3.18);
        s3 = new Song("003", "MONTERO", "Lil Nas", 2.3);
        s4 = new Song("004", "bad guy", "Billie Eilish", 3.14);
        collection.addSong(s1);
        collection.addSong(s2);
        collection.addSong(s3);
        collection.addSong(s4);
    }

    @AfterEach
    void tearDown() {
        collection = null;
    }

    @Test
    void testGetSongs() {
        List<Song> songs = collection.getSongs();
        assertEquals(songCollectionSize, songs.size(),4);
    }

    @Test
    void testAddSongWhenFull() {
        SongCollection smallCollection = new SongCollection(1);
        smallCollection.addSong(new Song("1", "First", "Artist", 3.0));
        smallCollection.addSong(new Song("2", "Second", "Artist", 3.0));
        assertEquals(1, smallCollection.getSongs().size());
    }

    @Test
    void testSortSongsByTitle() {
        SongCollection sc = new SongCollection();
        sc.addSong(new Song("2", "B Song", "Artist", 3.5));
        sc.addSong(new Song("1", "A Song", "Artist", 3.5));
        sc.sortSongsByTitle();
        assertEquals("A Song", sc.getSongs().get(0).getTitle());
    }

    @Test
    void testSortSongsBySongLength() {
        SongCollection sc = new SongCollection();
        sc.addSong(new Song("1", "Song A", "Artist 1", 4.0));
        sc.addSong(new Song("2", "Song B", "Artist 2", 2.5));
        sc.sortSongsBySongLength();
        assertEquals(4.0, sc.getSongs().get(0).getSongLength());
        assertEquals(2.5, sc.getSongs().get(1).getSongLength());
    }

    @Test
    void testFindSongById() {
        Song song = new Song("123", "Test Song", "Artist", 3.0);
        collection.addSong(song);
        assertEquals(song, collection.findSongsById("123"));
        assertNull(collection.findSongsById("999"));
    }

    @Test
    void testFindSongByTitle() {
        Song song = new Song("123", "Test Song", "Artist", 3.0);
        collection.addSong(song);
        assertEquals(song, collection.findSongByTitle("Test Song"));
        assertNull(collection.findSongByTitle("Nonexistent"));
    }

    @ParameterizedTest
    @CsvSource({
        "001, Mock Song, Mock Artist, 4.25",
        "002, Love Story, Taylor Swift, 3.55",
        "003, Grenade, Bruno Mars, 3.95",
        "004, Photograph, Ed Sheeran, 4.12"
    })
    void testFetchSongOfTheDayParameterized(String id, String title, String artiste, double length) throws Exception {
        String mockJson = String.format("{\"id\":\"%s\",\"title\":\"%s\",\"artiste\":\"%s\",\"songLength\":%s}",
                                         id, title, artiste, length);

        SongCollection sc = spy(new SongCollection());
        doReturn(mockJson).when(sc).fetchSongJson();

        Song song = sc.fetchSongOfTheDay();
        assertNotNull(song);

        if ("Taylor Swift".equals(artiste)) {
            assertEquals("TS", song.getArtiste());
            assertEquals(1, sc.getSongs().size());
        } else if ("Bruno Mars".equals(artiste)) {
            assertEquals("BM", song.getArtiste());
            assertEquals(1, sc.getSongs().size());
        } else {
            assertEquals(artiste, song.getArtiste());
            assertEquals(0, sc.getSongs().size());
        }
    }

    @Test
    void testFetchSongOfTheDayWithNullJson() throws Exception {
        SongCollection sc = spy(new SongCollection());
        doReturn(null).when(sc).fetchSongJson();
        assertNull(sc.fetchSongOfTheDay());
    }

    @Test
    void testFetchSongOfTheDayWithException() throws Exception {
        SongCollection sc = spy(new SongCollection());
        doThrow(new RuntimeException("API error")).when(sc).fetchSongJson();
        assertNull(sc.fetchSongOfTheDay());
        assertEquals(0, sc.getSongs().size());
    }

    @Test
    void testGetYearCreated() {
        LocalDateTime mockDate = LocalDateTime.of(2024, Month.JUNE, 18, 16, 30);
        try (MockedStatic<LocalDateTime> mocked = mockStatic(LocalDateTime.class)) {
            mocked.when(LocalDateTime::now).thenReturn(mockDate);
            SongCollection sc = new SongCollection();
            assertEquals("2024", sc.getYearCreated());
        }
    }

    @Test
    void testGetFullDateCreated() {
        LocalDateTime mockDate = LocalDateTime.of(2025, Month.DECEMBER, 14, 16, 25);
        try (MockedStatic<LocalDateTime> mocked = mockStatic(LocalDateTime.class)) {
            mocked.when(LocalDateTime::now).thenReturn(mockDate);
            SongCollection sc = new SongCollection();
            assertEquals("14/12/2025", sc.getFullDateCreated());
        }
    }

    @Test
    void testCompareCollectionDates() {
        LocalDateTime date1 = LocalDateTime.of(2025, Month.DECEMBER, 12, 20, 30);
        LocalDateTime date2 = LocalDateTime.of(2025, Month.DECEMBER, 14, 16, 25);

        try (MockedStatic<LocalDateTime> mocked = mockStatic(LocalDateTime.class)) {
            mocked.when(LocalDateTime::now).thenReturn(date1);
            SongCollection first = new SongCollection();

            mocked.when(LocalDateTime::now).thenReturn(date2);
            SongCollection second = new SongCollection();

            assertEquals("My collection is older!", first.compareCollection(second));
            assertEquals("My collection is newer!", second.compareCollection(first));

            mocked.when(LocalDateTime::now).thenReturn(date2);
            SongCollection same1 = new SongCollection();
            mocked.when(LocalDateTime::now).thenReturn(date2);
            SongCollection same2 = new SongCollection();

            assertEquals("My collection was created at the same time!", same1.compareCollection(same2));
        }
    }

    @Test
    void testFetchSongJsonExceptionBranch() throws Exception {
        SongCollection sc = new SongCollection();
        Field field = SongCollection.class.getDeclaredField("urlString");
        field.setAccessible(true);
        field.set(sc, "http://invalid.url");
        assertNull(sc.fetchSongJson());
    }
}
