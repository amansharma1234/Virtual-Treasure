package edu.neu.madcourse.amansharma;

import java.util.ArrayList;
import java.util.List;

public class Shuffle {
    
	public String shuffle(String input){
        List<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
		return output.toString();
    }
}