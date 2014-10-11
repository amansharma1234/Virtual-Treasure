package edu.neu.madcourse.amansharma;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import edu.neu.virtualtreasure.R;

public class Search {
	InputStream in;
	BufferedReader reader;
	String word;
	static int flag [] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,};
	static int i = 0;
	static ArrayList<String> arr = new ArrayList<String>();
	
	static TrieNode trieNode = Tries.createTree();
	
	public void dictionary(Context context)
	{
		in = context.getResources().openRawResource(R.raw.wordlist);
	    reader = new BufferedReader(new InputStreamReader(in));
	    try {
			
			while ((word = reader.readLine())  != null)
			{
				// word = reader.readLine();	
				if (word.length() < 6)
				{
					Tries.insertWord(trieNode, word);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	    try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println("Here");
	}
	
	public static int findWord(Context context,String searchWord)
    {
		
		if (Tries.find(trieNode, searchWord))
    	{
    	   
			arr.add(searchWord);
			flag [i] = 1;
			i++ ;
			return searchWord.length();
    	}
    	else
    	{
    		
    		return 0;
    	}
    }
	
	public static ArrayList<String> returnArrList()
	{
		return arr;
	
	}
	public static int [] wordFlag()
	{
		return flag;
		
	}
}
