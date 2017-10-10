package com.flexdinesh.scrabble;

import java.io.*;
import java.util.*;

public class Dictionary {

    Map<String, List<String>> wordDictionary;

    public Dictionary() {
        wordDictionary = new HashMap<String, List<String>>();
    }

    public List<String> getWordList(String sortedWord) {
        List<String> wordlist = wordDictionary.get(sortedWord);
        return wordlist;
    }

    public void loadDictionary() {
        List<String> textList = loadTextListFromFile();

        for (String word: textList) {
            String sortedWord = sortWord(word);
            if (wordDictionary.containsKey(sortedWord)) {
                List<String> wordList = wordDictionary.get(sortedWord);
                wordList.add(word);
            } else {
                List<String> wordList = new ArrayList<String>();
                wordList.add(word);
                wordDictionary.put(sortedWord, wordList);
            }
        }

        System.out.println("Dictionary loaded");
    }

    public String sortWord(String word){
        char[] charArray = word.toCharArray();
        Arrays.sort(charArray);
        return String.valueOf(charArray);
    }

    public List<String> loadTextListFromFile(){
        List<String> arr = new ArrayList<String>();


        try (BufferedReader br = new BufferedReader(new
                InputStreamReader(getClass().getClassLoader().getResourceAsStream("dictionary.txt"))))
        {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                arr.add(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return arr;
    }
}
