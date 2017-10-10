package com.flexdinesh.scrabble;

public class Alphabet {
    private String letter;
    private long score;
    private long count;

    public Alphabet(String letter, long score, long count) {
        this.letter = letter;
        this.score = score;
        this.count = count;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void decreaseCountByOne() {
        if(this.count >= 1)
            this.count = this.count - 1;
    }

    @Override
    public String toString() {
        return "Alphabet{" +
                "letter='" + letter + '\'' +
                ", score=" + score +
                ", count=" + count +
                '}';
    }
}
