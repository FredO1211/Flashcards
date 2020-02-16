package com.example.flashcards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class CreateCollectionActivity extends AppCompatActivity {

    private final Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_collection);

        final Button createCollectionButton = findViewById(R.id.createCollectionButton);
        final EditText collectionNameEditText = findViewById(R.id.collectionNameEditText);
        Bundle extras =getIntent().getExtras();
        final String LOGIN=extras.getString("login");

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
