package edu.neu.madcourse.amansharma;
import java.util.TimerTask;

public class timer extends TimerTask {
	
	
	   long startTime = 0;
	   String time ;
	   
	   
	   
	   public void run() {
		   
		   
		   long millis = System.currentTimeMillis() - startTime;
		   int seconds = (int) (millis / 1000);
		   int minutes = seconds / 60;
		   seconds     = seconds % 60;
		   time = (String.format("%d:%02d", minutes, seconds));

       
   }
	   public String returnTime()
	   {
		return time;
		   
	   }
}