package com.example.flashcards;

public class FlashcardItem {
    private String polishMiningOfWord;
    private String englishMiningOfWord;

    public FlashcardItem(String polishMiningOfWord, String englishMiningOfWord) {
        this.polishMiningOfWord = polishMiningOfWord;
        this.englishMiningOfWord = englishMiningOfWord;
    }

    public String getPolishMiningOfWord() {
        return polishMiningOfWord;
    }

    public String getEnglishMiningOfWord() {
        return englishMiningOfWord;
    }
}
