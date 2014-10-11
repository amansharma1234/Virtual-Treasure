package edu.neu.madcourse.amansharma;

import java.util.Enumeration;


public class Tries
{
    static TrieNode createTree()
    {
        return(new TrieNode(false));
    }
    
    static void insertWord(TrieNode root, String word)
    {
        int offset = 65;
        int l = word.length();
        char[] letters = word.toCharArray();
        TrieNode curNode = root;
        
        for (int i = 0; i < l; i++)
        {
            if (!curNode.links.containsKey((char) (letters[i]-offset)))
                curNode.links.put((char) (letters[i]-offset), new TrieNode(i == l-1 ? true : false));
            curNode = curNode.links.get((char) (letters[i]-offset));
        }
    }

    static boolean find(TrieNode root, String word)
    {
        char[] letters = word.toCharArray();
        int l = letters.length;
        int offset = 65;
        TrieNode curNode = root;
        
        int i;
        for (i = 0; i < l; i++)
        {
            if (curNode == null)
                return false;
            curNode = curNode.links.get((char) (letters[i]-offset));
        }
        
        if (i == l && curNode == null)
            return false;
        
        if (curNode != null && !curNode.fullWord)
            return false;
        
        return true;
    }
    
    static void printTree(TrieNode root, int level, char[] branch)
    {
        if (root == null)
            return;
        
        for (Character key:root.links.keySet())
        {
        	branch[level] = key;
        	printTree(root.links.get(key), level+1, branch);
        	
        }
        
        if (root.fullWord)
        {
            for (int j = 1; j <= level; j++)
                System.out.print(branch[j]);
            System.out.println();
        }
    }
    
    
    
    
}

//public class Tries {
//
//    //root node
//    private TrieNode r;
//
//    public Tries () {
//        r = new TrieNode();
//    }
//
//    public boolean has(String word) {
//        return r.has(word);
//    }
//
//    public void insert(String word) {
//        r.insert(word);
//    }
//
//    public String toString() {
//        return r.toString();
//    }
//}
