package com.example.flashcards;

public class FlashcardMenuItem {
    private int logoImageViewResource;
    private String nameCollection;
    private String counterElementsText;

    public FlashcardMenuItem(int logoImageViewResource, String nameCollection, String counterElementsText) {
        this.logoImageViewResource = logoImageViewResource;
        this.nameCollection = nameCollection;
        this.counterElementsText = counterElementsText;
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
}
