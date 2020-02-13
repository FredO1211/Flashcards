package com.example.flashcards;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class FlashcardsMenu extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton addCollectionButton;
    private final Database db = new Database(this);
    ArrayList<FlashcardMenuItem> exampleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = new Intent(this,CreateCollectionActivity.class);
        Bundle extras =getIntent().getExtras();
        final String LOGIN=extras.getString("login");

        setContentView(R.layout.activity_flashcard);
        addCollectionButton = findViewById(R.id.addCollectionButton);

        addCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("login",LOGIN);
                startActivity(intent);
                exampleList=db.returnArrayListOfFlashcardMenuItem(LOGIN);
            }
        });


        exampleList=db.returnArrayListOfFlashcardMenuItem(LOGIN);


        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        adapter= new FlashcardsMenuAdapter(exampleList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
