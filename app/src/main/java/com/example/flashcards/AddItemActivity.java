package com.example.flashcards;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddItemActivity extends AppCompatActivity {

    private final Database db = new Database(this);
    private Button addFlashcardButton;
    private EditText englishMiningEditText;
    private EditText polishMiningEditText;
    private int collectionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        polishMiningEditText = findViewById(R.id.polishMiningEditText);
        englishMiningEditText = findViewById(R.id.englishMiningEditText);
        addFlashcardButton = findViewById(R.id.createFlashcardButton);

        Bundle bundle = getIntent().getExtras();
        collectionId = bundle.getInt("collectionID");

        addFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addFlashcard(polishMiningEditText.getText().toString(),englishMiningEditText.getText().toString(),collectionId);
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
