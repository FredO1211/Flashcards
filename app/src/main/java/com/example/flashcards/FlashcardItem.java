package com.example.flashcards;

public class FlashcardItem {
    private String polishMiningOfWord;
    private String englishMiningOfWord;
    private int flashcardId;

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
}
