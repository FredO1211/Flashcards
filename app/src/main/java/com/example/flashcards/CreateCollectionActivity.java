package com.example.flashcards;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateCollectionActivity extends AppCompatActivity {

    private final Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_collection);

        final Button createCollectionButton = findViewById(R.id.createCollectionButton);
        final Button cancelButton = findViewById(R.id.cancelButton);
        final EditText collectionNameEditText = findViewById(R.id.collectionNameEditText);
        Bundle extras =getIntent().getExtras();
        final String LOGIN=extras.getString("login");

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionNameEditText.setText(null);
                finish();
            }
        });

        createCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addCollection(collectionNameEditText.getText().toString(),0,R.drawable.ic_favorite_24dp,db.getUserId(LOGIN));
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
