package com.example.flashcards;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class EditCollectionActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FlashcardItemAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton addItemButton;
    private Button goBackButton;
    private final Database db = new Database(this);
    public ArrayList<FlashcardItem> itemsList;
    //private final int REQUEST_RECREATE=0;
    public String collection;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_colection);

        //Bundle bundle = getIntent().getExtras();
        //collection = bundle.getString("collection");

        addItemButton = findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        itemsList=new ArrayList<FlashcardItem>();
        itemsList.add(new FlashcardItem("aa","bb"));
        itemsList.add(new FlashcardItem("aa1","bb1"));
        itemsList.add(new FlashcardItem("2a","b2b"));
        itemsList.add(new FlashcardItem("2a","b2b"));


        recyclerView=findViewById(R.id.flashcardRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        adapter= new FlashcardItemAdapter(itemsList,db,this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


    }
}
