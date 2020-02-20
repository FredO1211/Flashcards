package com.example.flashcards;

public class FlashcardItem {
    private String polishMiningOfWord;
    private String englishMiningOfWord;
    private int flashcardId;
    private int points=0;
    private boolean favourite;

    public FlashcardItem(String polishMiningOfWord, String englishMiningOfWord, int flashcardId) {
        this.polishMiningOfWord = polishMiningOfWord;
        this.englishMiningOfWord = englishMiningOfWord;
        this.flashcardId = flashcardId;
    }

    public String getPolishMiningOfWord() {
        return polishMiningOfWord;
    }

    public String getEnglishMiningOfWord() {
        return englishMiningOfWord;
    }

    public int getFlashcardId() {
        return flashcardId;
    }

    public void addpoint(int value){
        points +=value;
    }

    public int getPoints() {
        return points;
    }
}
