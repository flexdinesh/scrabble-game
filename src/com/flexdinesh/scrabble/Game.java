package com.flexdinesh.scrabble;

import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class Game {
    private Dictionary dictionary;
    private Map<String, Alphabet> alphabets;
    private Set<String> matchedWords;
    private Map<Integer, List<String>> scoreMap;

    public Game() {
        alphabets = new HashMap<String, Alphabet>();
        dictionary = new Dictionary();
        System.out.println("-----------------------------");
        dictionary.loadDictionary();
        this.loadAlphabets();
        System.out.println("-----------------------------");
    }

    public void playGame() {

        boolean continueGame = true;

        while (continueGame) {
            try {
                System.out.println("Enter the word in player's rack: ");
                String rackWord = getInput();
                System.out.println("The following input is optional. If you don't want to enter a word, just hit the enter key.");
                System.out.println("Enter the word that currently exists on the board: ");
                String optionalWord = getInput();
                scrabble(rackWord.toUpperCase(), optionalWord.toUpperCase());

                System.out.println("\nDo you want to continue playing the game? (y/n)");
                String userInput = getInput();
                if (userInput.equalsIgnoreCase("y") || userInput.isEmpty()) {
                    continue;
                } else {
                    System.out.println("Hope you enjoyed the game. Have a nice day!");
                    break;
                }

            } catch (Exception e) {
                System.out.println();
                System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                System.out.println(e.getMessage());
                System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXX");

                System.out.println("\nDo you want to continue playing the game? (y/n)");
                String userInput = getInput();
                if (userInput.equalsIgnoreCase("y") || userInput.isEmpty()) {
                    continue;
                } else {
                    System.out.println("Hope you enjoyed the game. Have a nice day!");
                    break;
                }
            }
        }

    }

    public void scrabble(String rackWord, String optionalWord) throws Exception {
        matchedWords = new HashSet<>();

        Rack rack = new Rack(rackWord, optionalWord);
        this.applyLetterCountRuleOnRack(rack);
        List<String> permutedSortedWordList = rack.getPermutedSortedWordList();

        for (String sortedWord : permutedSortedWordList) {
            List<String> wordList = dictionary.getWordList(sortedWord);
            if (wordList != null) {
                for (String word : wordList) {
                    matchedWords.add(word);
                }
            }
        }

        matchedWords = filterOutWordsWithoutPrefix(matchedWords, rack.getPrefix());
        matchedWords = applyWordRulesFilterAfterMatching(matchedWords);

        int maxScore = getMaxScoreFromMatchedWords(matchedWords);

        if (matchedWords.size() > 0) {
            String highestScoringWord = scoreMap.get(maxScore).get(0);
            System.out.println("-----------------------------");
            System.out.println("Highest scoring word: " + highestScoringWord);
            System.out.println("Score: " + maxScore);
            System.out.println("-----------------------------");
            tallyRemainingAlphabetsCount(highestScoringWord);
        } else {
            throw new Exception("No match found");
        }
    }

    public int getMaxScoreFromMatchedWords(Set<String> words) {
        Iterator<String> iterator = matchedWords.iterator();
        scoreMap = new HashMap<Integer, List<String>>();
        List<String> wordList;
        int maxScore = 0;

        while (iterator.hasNext()) {
            String word = iterator.next();
            int wordScore = 0;
            for (char ch : word.toCharArray()) {
                Alphabet a = alphabets.get(Character.toString(ch));
                int letterScore = (int) a.getScore();
                wordScore += letterScore;
            }

            if (scoreMap.get(wordScore) != null)
                wordList = scoreMap.get(wordScore);
            else
                wordList = new ArrayList<>();

            if (wordScore > maxScore) {
                maxScore = wordScore;
            }
            wordList.add(word);
            Collections.sort(wordList);
            scoreMap.put(wordScore, wordList);
        }

        return maxScore;
    }

    public Set<String> filterOutWordsWithoutPrefix(Set<String> matchedWords, String prefix) {

        Iterator<String> iterator = matchedWords.iterator();
        while (iterator.hasNext()) {
            String word = iterator.next();
            if (!word.startsWith(prefix)) {
                iterator.remove();
            } else if (word.equals(prefix)) {
                iterator.remove();
            }
        }
        return matchedWords;
    }

    public void applyLetterCountRuleOnRack(Rack rack) throws Exception {
        Map<String, Integer> letterCountMap = rack.getLetterCountMap();

        for (char c : rack.getSortedWord().toCharArray()) {
            String letter = Character.toString(c);
            Alphabet a = alphabets.get(letter);

            if (a.getCount() <= 0) {
                String strOne = "No " + "'" + letter + "'" + " available in board!";
                String strTwo = "Input cannot have " + letterCountMap.get(letter) + " " + "'" + letter + "'";
                throw new Exception(strOne + "\n" + strTwo);
            }
            else if (letterCountMap.get(letter) > a.getCount()) {
                String strOne = "Only " + a.getCount() + " " + "'" + letter + "'" + " available in board!";
                String strTwo = "Input cannot have " + letterCountMap.get(letter) + " " + "'" + letter + "'";
                throw new Exception(strOne + "\n" + strTwo);
            }
        }

    }

    public Set<String> applyWordRulesFilterAfterMatching(Set<String> matchedWords) {

        Iterator<String> iterator = matchedWords.iterator();
        while (iterator.hasNext()) {
            String word = iterator.next();
            if (word.length() < 2)
                iterator.remove();
            if (word.length() > 15)
                iterator.remove();
        }
        return matchedWords;
    }

    public void tallyRemainingAlphabetsCount(String highestScoringWord) {
        for (char c : highestScoringWord.toCharArray()) {
            String letter = String.valueOf(c);
            alphabets.get(letter).decreaseCountByOne();
        }
    }

    public Map<String, Alphabet> loadAlphabets() {
        JSONParser parser = new JSONParser();
        try {
            BufferedReader br = new BufferedReader(new
                    InputStreamReader(getClass().getClassLoader().getResourceAsStream("letters.json")));
            Object obj = parser.parse(br);
            JSONArray jsonArray = (JSONArray) obj;
            Iterator<JSONObject> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                JSONObject jsonObject = (JSONObject) iterator.next();
                String letter = (String) jsonObject.get("letter");
                long score = (Long) jsonObject.get("score");
                long count = (Long) jsonObject.get("count");
                Alphabet alphabet = new Alphabet(letter, score, count);
                alphabets.put(letter, alphabet);
            }
            System.out.println("Letter counts and scores loaded");
            return alphabets;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getInput() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String word = null;
        try {
            word = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return word;
    }

    public void showOutput(String output) {
        System.out.println("Your output is :: " + output);
    }
}
