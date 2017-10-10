package com.flexdinesh.scrabble;

import java.util.*;

public class Rack {
    private String word;
    private String prefix;
    private String sortedWord;
    private List<String> permutedSortedWordList;

    private Map<String, Integer> letterCountMap;

    public Rack(String word, String prefix) throws Exception {
        this.word = word;
        this.prefix = prefix;
        this.sortWord();
        this.validateInput();
        permutedSortedWordList = new ArrayList<String>();
    }

    public void validateInput() throws Exception {

        if (this.word.length() < 1)
            throw new Exception("Players must have a minimum of one letter on their rack!");
        if (this.word.length() > 7)
            throw new Exception("Players can only have a maximum of seven letters on their rack!");

    }

    public void sortWord(){
        char[] charArray = (prefix + word).toCharArray();
        Arrays.sort(charArray);
        sortedWord = String.valueOf(charArray);
    }

    void combine(String sortedWord, StringBuffer outstr, int index) {

        for (int i = index; i < sortedWord.length(); i++) {
            outstr.append(sortedWord.charAt(i));
            permutedSortedWordList.add(outstr.toString());
            combine(sortedWord, outstr, i + 1);
            outstr.deleteCharAt(outstr.length() - 1);
        }
    }

    public List<String> getPermutedSortedWordList() {

        combine(sortedWord, new StringBuffer(), 0);
        return permutedSortedWordList;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSortedWord() {
        return sortedWord;
    }

    public void setSortedWord(String sortedWord) {
        this.sortedWord = sortedWord;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Map<String, Integer> getLetterCountMap() {
        letterCountMap = new HashMap<String, Integer>();
        for (char c : sortedWord.toCharArray()) {
            String letter = String.valueOf(c);

            if (letterCountMap.containsKey(letter))
                letterCountMap.put(letter, letterCountMap.get(letter) + 1);
            else
                letterCountMap.put(letter, 1);
        }
        return letterCountMap;
    }

}
