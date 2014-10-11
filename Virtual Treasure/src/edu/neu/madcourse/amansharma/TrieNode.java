package edu.neu.madcourse.amansharma;

import java.util.Hashtable;
import java.util.TreeMap;

//public class TrieNode {
//
//    //make child nodes
//    private TrieNode[] c;
//    //flag for end of word
//    private boolean flag = false;
//
//    public TrieNode() {
//        c = new TrieNode[26]; //1 for each letter in alphabet
//    }
//
//    protected void insert(String word) {
//        int val = word.charAt(0) - 'A';
//
//        //if the value of the child node at val is null, make a new node
//                //there to represent the letter
//        if (c[val] == null) {
//            c[val] = new TrieNode();
//        }
//
//        //if word length > 1, then word is not finished being added.
//        //otherwise, set the flag to true so we know a word ends there.
//        if (word.length() > 1) {
//        	c[val].has(word.substring(1));
//        	int p = 0;
//        	int j =p;
//        } else {
//            c[val].flag = true;
//        }
//		
//    }
//
//    public boolean has(String word) {
//        int val = word.charAt(0) - 'A';
//        if (c[val]!=null && word.length()>1) {
//        	return c[val].has(word.substring(1));
//        } else if (c[val].flag==true && word.length()==1) {
//            return true;
//        }
//
//        return false;
//    }
//
//    public String toString() { 
//        return "";
//    }
//}

class TrieNode
{
    TreeMap<Character, TrieNode> links;
    boolean fullWord;
    
    TrieNode(boolean fullWord)
    {
        links = new TreeMap<Character, TrieNode>();
        this.fullWord = fullWord;
    }
}