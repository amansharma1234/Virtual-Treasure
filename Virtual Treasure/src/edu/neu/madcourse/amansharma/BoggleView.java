/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband3 for more book information.
***/

package edu.neu.madcourse.amansharma;



import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.neu.virtualtreasure.R;


public class BoggleView extends View {
   
   private static final String TAG = "Sudoku";
   private static final String SELX = "selX"; 
   private static final String SELY = "selY";
   private static final String VIEW_STATE = "viewState";
   private static final int ID = 42;
   
   private int prevX = -1;
   private int prevY = -1;

   
   private float width;    // width of one tile
   private float height;   // height of one tile
   private int selX;       // X index of selection
   private int selY;       // Y index of selection
   private final Rect selRect = new Rect();
   
   private int totalScore = 0;
   
   LinearLayout layout = new LinearLayout(this.getContext());
   TextView textViewWord = new TextView(this.getContext()); 
   TextView textViewTimer = new TextView(this.getContext()); 
   TextView textViewScore = new TextView(this.getContext()); 
   LinearLayout.LayoutParams params_word =new LinearLayout.LayoutParams(100, 300);
   LinearLayout.LayoutParams params_score =new LinearLayout.LayoutParams(120, 250); 
   LinearLayout.LayoutParams params_timer =new LinearLayout.LayoutParams(80, 250); 
  
  
   Paint outputString = new Paint(Paint.ANTI_ALIAS_FLAG);
   private final GameBoggle game;
   Canvas c =new Canvas();
   
   int flag [][] = {{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
   int wCounter = 0;
   int wflag [] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,};
   String output;
   String tScore;
   Tries trie = new Tries();
   timer t = new timer();
   ArrayList<String> al = new ArrayList<String>();
   
  
   
   
   
   public BoggleView(Context context) {
      
      super(context);
      this.game = (GameBoggle) context;
     
//      t.run();
//      tv.setText(t.returnTime());
     
      setFocusable(true);
      setFocusableInTouchMode(true);
      
      
      
      // ...,
      setId(ID); 
   }

   @Override
   protected Parcelable onSaveInstanceState() { 
      Parcelable p = super.onSaveInstanceState();
      Log.d(TAG, "onSaveInstanceState");
      Bundle bundle = new Bundle();
      bundle.putInt(SELX, selX);
      bundle.putInt(SELY, selY);
      bundle.putParcelable(VIEW_STATE, p);
      return bundle;
   }
   @Override
   protected void onRestoreInstanceState(Parcelable state) { 
      Log.d(TAG, "onRestoreInstanceState");
      Bundle bundle = (Bundle) state;
      //select(bundle.getInt(SELX), bundle.getInt(SELY));
      super.onRestoreInstanceState(bundle.getParcelable(VIEW_STATE));
   }
   

   @Override
   protected void onSizeChanged(int w, int h, int oldw, int oldh) {
      width = w / 4f;
      height = h / 5f;
      Log.d(TAG, "onSizeChanged: width " + width + ", height "
            + height);
      super.onSizeChanged(w, h, oldw, oldh);
   }

   @Override
   protected void onDraw(Canvas canvas) {
	   c= canvas;
      // Draw the background...
      Paint background = new Paint();
      background.setColor(getResources().getColor(
            R.color.boggle_background));
      canvas.drawRect(0, 0, getWidth(),4 * height , background);
     
      // Draw the board...
      
      // Define colors for the grid lines
      Paint dark = new Paint();
      dark.setColor(getResources().getColor(R.color.boggle_dark));

      Paint hilite = new Paint();
      hilite.setColor(getResources().getColor(R.color.boggle_hilite));

      Paint light = new Paint();
      light.setColor(getResources().getColor(R.color.boggle_light));

//       Draw the major grid lines
      for (int i = 0; i < 5; i++) {
         
         canvas.drawLine(0, i * height, getWidth(), i * height, dark);
         canvas.drawLine(0, i * height + 1, getWidth(), i * height  + 1, hilite);
         canvas.drawLine(i * width, 0, i * width , getHeight() - height, dark);
         canvas.drawLine(i * width + 1 , 0, i * width +1 ,getHeight() - height, hilite);
      } 

      // Draw the numbers...
      // Define color and style for numbers
      Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
      foreground.setColor(getResources().getColor(
            R.color.boggle_foreground));
      foreground.setStyle(Style.FILL);
      foreground.setTextSize(height * 0.6f);
      foreground.setTextScaleX(width / height);
      foreground.setTextAlign(Paint.Align.CENTER);

      // Draw the number in the center of the tile
      FontMetrics fm = foreground.getFontMetrics();
      // Centering in X: use alignment (and X at midpoint)
      float x = width / 2;
      // Centering in Y: measure ascent/descent first
      float y = height / 2 - (fm.ascent + fm.descent) / 2;
      for (int i = 0; i < 4; i++) {
         for (int j = 0; j < 4; j++) {
            canvas.drawText(this.game.getTileString(i, j), i * width + x, j * height + y, foreground);
         }
      }
      
      
      params_word.leftMargin = 10;
      params_word.topMargin = (int) (4 * height) + 10;
     // params_score.bottomMargin = 100;
      
      params_timer.leftMargin = (int) (width)-50;
      params_timer.topMargin = (int) (4 * height) + 10; 
     // params_score.bottomMargin = 100;
      
      params_score.leftMargin = 0;
      params_score.topMargin = (int) (4 * height) + 10;
     // params_score.bottomMargin = 100;
      
      
      textViewWord.setLayoutParams(params_word);									//TextView for word
      textViewWord.setTextSize(17);
      textViewWord.setTextColor(getResources().getColor(android.R.color.white));
      
      textViewTimer.setLayoutParams(params_timer);									//TextView for word
      textViewTimer.setTextSize(17);
      textViewTimer.setTextColor(getResources().getColor(android.R.color.white));
       
   //   textViewTimer. 
      
      textViewScore.setLayoutParams(params_score);
      textViewScore.setTextSize(17);
      textViewScore.setTextColor(getResources().getColor(android.R.color.white));
      
      textViewTimer.setText("Timer");
      
      layout.removeAllViewsInLayout();
      layout.addView(textViewWord);
      layout.addView(textViewTimer);
      layout.addView(textViewScore);

      layout.measure(canvas.getWidth(), canvas.getHeight());
      layout.layout(0, 0, canvas.getWidth(), canvas.getHeight());

      // To place the text view somewhere specific:
      //canvas.translate(0, 0);

      layout.draw(canvas);
      
      Paint selected = new Paint();
      selected.setColor(getResources().getColor(
            R.color.boggle_selected));
      
      for (int i = 0; i < 4; i++) {
          for (int j = 0; j < 4; j++) {
        	  if (1 == flag[i][j])
        	  {
	        	  getRect(i,j,selRect); 	  
	        	  canvas.drawRect(selRect, selected);
        	  }
          }
      }
}

   
   public boolean onTouchEvent(MotionEvent event) {
	    int eventaction = event.getAction();

	    switch (eventaction) {
	        case MotionEvent.ACTION_DOWN:
	        	
	        	int x = (int)(event.getX()/width);
	        	int y = (int)(event.getY()/height);
	        	
	        	if (x >= 4 || y >= 4)
	        	{
     	        	output = ""; 
	        		break; 
	        	}
	        	else
	        	{
	        		MusicBoggle.play(getContext(), R.raw.click);
	        	
	        		output = game.getTileString(x,y); 
	      //  	getRect(x,y,selRect);
	      //  	textView.setTextColor(android.R.color.black);
	        		textViewWord.setText(output);
	        	
	        	
	        		flag [x][y] = 1;
	        	
	        		prevX = x;
	  	        	prevY = y;
	  	        
	  	        	invalidate();
	  	        
	  	        	break;
	        	}

	        case MotionEvent.ACTION_MOVE:
	           	int xm = (int)(event.getX()/width);
	        	int ym = (int)(event.getY()/height);
	        	if (xm >= 4 || ym >= 4)
	        	{ 
	        		
	        		break; 
	        	}
	        	else
	        		if ((flag [xm][ym] == 0) && 
	        				((prevX != -1) && 
	        			     (prevY != -1)))
	        		{
	        			MusicBoggle.play(getContext(), R.raw.click);
	        			output += game.getTileString(xm,ym); 
	        			flag[xm][ym] = 1;
	        			textViewWord.setText(output);
	    	  	        invalidate();
	    	  	        prevX = xm;
	    	  	        prevY = ym;
	    	  	    
	        		}
	        		else 
	        			if ((flag [xm][ym] == 1) &&
	        			((prevX != xm) ||
	        			(prevY != ym)))
	        		{
	        				textViewWord.setText("");
	        				invalidate();
    	        	
	        				for (int i = 0;i < 4; i++)
	        					for (int j = 0;j < 4; j++){
	        						if (flag [i][j] == 1)
	        							flag [i][j] = 0; 
	        					}
    	        		
	        				prevX = -1;
	        				prevY = -1;
    	        		
	        				output = "";	
	        				textViewWord.setText(output);
    	        		
    	        	
	        		}
	        break;

	        case MotionEvent.ACTION_UP:  
	       
	        		invalidate();
	        		
	        		for (int i = 0;i < 4; i++)
	        			for (int j = 0;j < 4; j++){
	        				if (flag [i][j] == 1)
	        					flag [i][j] = 0; 
	            	}
	        		
	        		prevX = -1;
	        		prevY = -1;
	            	
	        		if (output.length() > 0)
	        		{
	        			int score = Search.findWord(this.getContext(),output);
	        			al = Search.arr;
	        			if (wflag [wCounter] == 0)
	        			{
	        				if(score > 0)
	        				{
	        					MusicBoggle.play(getContext(), R.raw.correct);
	        				}
	        				totalScore += score;
	        				tScore = "Total Score  " + Integer.toString(totalScore);
	        				textViewWord.setText(Integer.toString(score));
	        				textViewScore.setText(tScore);
	        				if (al.contains(output))
	        				{
	        					wflag [wCounter] = 1 ;
	        				}
	        				
	        			}
	        			else
	        			{
	        				totalScore += 0;
	        				tScore = "Total Score  " + Integer.toString(totalScore);
	        				textViewWord.setText("0");
	        				textViewScore.setText(tScore);
	        				wCounter ++;
	        			}
	        			
	        			//wflag = Search.wordFlag();
	        			
	        		}
	     
	        		break;
	    }	    
	  	
	    return true; 
	}
   
   private void getRect(int x, int y, Rect rect) {
      rect.set((int) (x * width), (int) (y * height), (int) (x
            * width + width), (int) (y * height + height));

   }
   
   private void getTextView (TextView tv,String out) {
	      tv.layout( 10 , (int) (getHeight() - width + 10) , (int) (getHeight() - width + 10) , getBottom());
	      tv.setText(out);
	     
	   }
   
   // ...
}