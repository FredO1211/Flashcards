package com.example.flashcards;

public class FlashcardItem {
    private String polishMiningOfWord;
    private String englishMiningOfWord;
    private int favouriteFlashcardResource;
    private int flashcardId;


    public FlashcardItem(String polishMiningOfWord, String englishMiningOfWord, int flashcardId, boolean favourite) {
        if(favourite){
            this.favouriteFlashcardResource =R.drawable.ic_star_gold_40dp;
        }
        else{
            this.favouriteFlashcardResource =R.drawable.ic_star_border_black_40dp;
        }
        this.polishMiningOfWord = polishMiningOfWord;
        this.englishMiningOfWord = englishMiningOfWord;
        this.flashcardId = flashcardId;
    }

    public int getFavouriteFlashcardResource() {
        return favouriteFlashcardResource;
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
