package com.example.flashcards;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class ReadingFlashcardActivity extends AppCompatActivity {

    private Database db=new Database(this);

    private TextView polishMiningReadingTextView;
    private TextView englishMiningReadingTextView;
    private CardView confirmCardView;
    private CardView negateCardView;
    private Button showAnswerButton;

    private int collectionId;
    int i = 0;

    private void getRandomWord(ArrayList<FlashcardItem> flashcardItems){
        Random random = new Random();
        int getRandomNumber = random.nextInt(2);
        switch (getRandomNumber){
            case 0:
                polishMiningReadingTextView.setText(flashcardItems.get(0).getPolishMiningOfWord());
                englishMiningReadingTextView.setText(null);
                break;
            case 1:
                polishMiningReadingTextView.setText(null);
                englishMiningReadingTextView.setText(flashcardItems.get(0).getEnglishMiningOfWord());
                break;
        }
    }

    private void getNextWord(ArrayList<FlashcardItem> flashcardItems){
        if(flashcardItems.isEmpty()){
            finish();
        }
        else {
            setShowAnswerButtonAvailable();
            setDialogButtonsUnavailable();
            getRandomWord(flashcardItems);
        }
    }

    private void setDialogButtonsAvailable(){
        confirmCardView.setActivated(true);
        confirmCardView.setVisibility(View.VISIBLE);
        negateCardView.setActivated(true);
        negateCardView.setVisibility(View.VISIBLE);
    }

    private void setDialogButtonsUnavailable(){
        confirmCardView.setActivated(false);
        confirmCardView.setVisibility(View.INVISIBLE);
        negateCardView.setActivated(false);
        negateCardView.setVisibility(View.INVISIBLE);
    }

    private void setShowAnswerButtonAvailable(){
        showAnswerButton.setActivated(true);
        showAnswerButton.setVisibility(View.VISIBLE);
    }

    private void setShowAnswerButtonUnavailable(){
        showAnswerButton.setActivated(false);
        showAnswerButton.setVisibility(View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_flashcard);

        polishMiningReadingTextView=findViewById(R.id.polishMiningReadingTextView);
        englishMiningReadingTextView= findViewById(R.id.englishMiningReadingTextView);
        confirmCardView= findViewById(R.id.confirmCardView);
        negateCardView = findViewById(R.id.negateCardView);
        showAnswerButton = findViewById(R.id.showAnswerButton);

        Bundle bundle = getIntent().getExtras();
        collectionId = bundle.getInt("collectionId");

        final ArrayList<FlashcardItem> flashcardItems = db.returnItemsArrayListOfCollectionSortedByPoints(collectionId,10);

        getNextWord(flashcardItems);

        confirmCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardItems.remove(0);
                getNextWord(flashcardItems);
            }
        });
        negateCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.increaseReviewCounterByOne(flashcardItems.get(0).getFlashcardId());
                flashcardItems.add(flashcardItems.get(0));
                flashcardItems.remove(0);
                getRandomWord(flashcardItems);
                setShowAnswerButtonAvailable();
                setDialogButtonsUnavailable();
            }
        });
        showAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i<flashcardItems.size()){
                    db.setReviewCounter(0,flashcardItems.get(0).getFlashcardId());
                    db.setLastUsingDate(db.getCurrentDate(),flashcardItems.get(0).getFlashcardId());
                    i++;
                }
                polishMiningReadingTextView.setText(flashcardItems.get(0).getPolishMiningOfWord());
                englishMiningReadingTextView.setText(flashcardItems.get(0).getEnglishMiningOfWord());
                setShowAnswerButtonUnavailable();
                setDialogButtonsAvailable();
            }
        });
    }
}
