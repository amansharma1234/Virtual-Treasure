/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband3 for more book information.
***/
package edu.neu.madcourse.amansharma;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class GameBoggle extends Activity {
   private static final String TAG = "Sudoku";

   public static final String KEY_BOARD = "Initial Board";
   private static final String PREF_BOARD = "boggle" ;
   
   
   String puz;
   private char[] wordstr;
   Shuffle s = new Shuffle();
   
   private final String words = "AAAABBCCDDEEEEFFGGHHIIIIJKKLLMMNNOOOOPPQRRSSSTTTUUUUVWXYZ";
   private final String wordShuffled = s.shuffle(words).substring(0, 16); 
   private BoggleView boggleView;

  

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
     
      
      puz =  wordShuffled;
      wordstr = fromPuzzleString(puz);
      boggleView = new BoggleView(this);
      setContentView(boggleView);
      boggleView.requestFocus();
      
      
      
      // If the activity is restarted, do a continue next time
      getIntent().putExtra(KEY_BOARD, PREF_BOARD);
     
   }

   @Override
   protected void onResume() {
      super.onResume();
   //   Music.play(this, R.raw.game);
   }

   @Override
   protected void onPause() {
      super.onPause();
      Log.d(TAG, "onPause");
      MusicBoggle.stop(this); 
      // Save the current puzzle
      getPreferences(MODE_PRIVATE).edit().putString(PREF_BOARD,toPuzzleString(wordstr)).commit();
   }
   
   static private String toPuzzleString(char[] puz) {
	      StringBuilder buf = new StringBuilder();
	      for (char element : puz) {
	         buf.append(element);
	      }
	      return buf.toString();
	   }

   /** Convert a puzzle string into an array */
   static protected char[] fromPuzzleString(String string) {
      char[] puz = new char[string.length()];
      for (int i = 0; i < puz.length; i++) {
         puz[i] = (char) (string.charAt(i));
      }
      return puz;
   }

 
   /** Return the tile at the given coordinates 
 * @return */
   private  char getTile(int x, int y) {
      return wordstr[y * 4 + x];
   }

/** Return a string for the tile at the given coordinates */
   protected String getTileString(int x, int y) {
      char v = getTile(x, y);
      
      return String.valueOf(v);
   }
   
	
}
