package com.sddevops.jenkins_maven.eclipse;
import java.util.logging.Logger; 
import java.util.logging.Level;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
    	
   Song song = new Song ("1", "Eric", "Test Song", 3.45); 
   SongCollection collection = new SongCollection(); 
   collection.addSong (song);
   Logger logger = Logger.getLogger (App.class.getName());
   logger.log (Level. INFO, collection.getYearCreated());
   logger.log (Level. INFO, collection.getFullDateCreated());
  
    }
}
