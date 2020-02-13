package com.example.flashcards;

import android.view.View;
import android.widget.ImageView;

public class FlashcardMenuItem {
    private int logoImageViewResource;
    private String nameCollection;
    private String counterElementsText;
    private int collectionId;
    private ImageView playImageView;

    public FlashcardMenuItem(int logoImageViewResource, String nameCollection, String counterElementsText, int collectionId) {
        this.logoImageViewResource = logoImageViewResource;
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
}
