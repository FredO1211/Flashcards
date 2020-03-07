package com.example.flashcards;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class WrithingFlashcardActivity extends AppCompatActivity {

    Database db= new Database(this);

    TextView askingTextView;
    TextView correctAnswer;
    EditText answerEditText;
    Button checkButton;
    ImageView askingLanguageFlag;
    ImageView answerLanguageFlag;

    private int collectionId;
    int i;

    private void getRandomWord(FlashcardItem flashcardItem){
        Random random = new Random();
        int getRandomNumber = random.nextInt(2);
        switch (getRandomNumber){
            case 0:
                askingTextView.setText(flashcardItem.getPolishMiningOfWord());
                askingLanguageFlag.setImageResource(R.drawable.polish_flag);
                answerLanguageFlag.setImageResource(R.drawable.english_flag);
                correctAnswer.setText(flashcardItem.getEnglishMiningOfWord());
                break;
            case 1:
                askingTextView.setText(flashcardItem.getEnglishMiningOfWord());
                answerLanguageFlag.setImageResource(R.drawable.polish_flag);
                askingLanguageFlag.setImageResource(R.drawable.english_flag);
                correctAnswer.setText(flashcardItem.getPolishMiningOfWord());
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkCurrentAnswer(ArrayList<FlashcardItem> flashcardItems){
        if(i>0){
            db.setReviewCounter(0,flashcardItems.get(0).getFlashcardId());
            db.setLastUsingDate(db.getCurrentDate(),flashcardItems.get(0).getFlashcardId());
            i--;
        }

        if(answerEditText.getText().toString().toUpperCase()
                .equals(correctAnswer.getText().toString().toUpperCase())){
            flashcardItems.remove(0);
            if(flashcardItems.isEmpty()){
                finish();
            }
            answerEditText.setTextColor(Color.GREEN);
            checkButton.setText("Next");
        }
        else{
            db.increaseReviewCounterByOne(flashcardItems.get(0).getFlashcardId());
            answerEditText.setTextColor(Color.RED);
            checkButton.setText("Next");
            correctAnswer.setVisibility(View.VISIBLE);
            flashcardItems.add(flashcardItems.get(0));
            flashcardItems.remove(0);
        }
    }
    private void setNextQuestion(ArrayList<FlashcardItem> flashcardItems){
        try {
            getRandomWord(flashcardItems.get(0));
            correctAnswer.setVisibility(View.INVISIBLE);
            answerEditText.setText(null);
            answerEditText.setTextColor(Color.BLACK);
            checkButton.setText("Check");
        } catch (Exception e) {
            this.finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writhing_flashcard);

        askingTextView = findViewById(R.id.askingTextView);
        correctAnswer = findViewById(R.id.correctAnswer);
        answerEditText = findViewById(R.id.answerEditText);
        checkButton = findViewById(R.id.checkButton);
        askingLanguageFlag= findViewById(R.id.askingLanguageFlag);
        answerLanguageFlag = findViewById(R.id.answerLanguageFlag);

        Bundle bundle = getIntent().getExtras();
        collectionId = bundle.getInt("collectionId");

        final ArrayList<FlashcardItem> flashcardItems = db.returnItemsArrayListOfCollectionSortedByPoints(collectionId,10);
        i=flashcardItems.size();

        if(flashcardItems.isEmpty()){
            finish();
        }
        else {
            setNextQuestion(flashcardItems);
        }
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkButton.getText().equals("Next")){
                    setNextQuestion(flashcardItems);
                }
                else {
                    checkCurrentAnswer(flashcardItems);
                }
            }
        });
    }
}
