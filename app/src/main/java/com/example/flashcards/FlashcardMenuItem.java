package com.example.flashcards;

public class FlashcardMenuItem {
    private int logoImageViewResource;
    private String nameCollection;
    private String counterElementsText;
    private int collectionId;

    public FlashcardMenuItem(boolean favourite, String nameCollection, String counterElementsText, int collectionId) {
        if(favourite){
            this.logoImageViewResource = R.drawable.ic_star_gold_40dp;
        }
        else{
            this.logoImageViewResource = R.drawable.ic_star_border_black_40dp;
        }
        this.nameCollection = nameCollection;
        this.counterElementsText = counterElementsText;
        this.collectionId = collectionId;
    }

    public int getLogoImageViewResource() {
        return logoImageViewResource;
    }

    public String getNameCollection() {
        return nameCollection;
    }

    public String getCounterElementsText() {
        return counterElementsText;
    }

    public int getCollectionId() {
        return collectionId;
    }
}
