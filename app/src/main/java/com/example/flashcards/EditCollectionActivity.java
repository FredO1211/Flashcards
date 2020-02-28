package com.example.flashcards;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class EditCollectionActivity extends AppCompatActivity {

    private final Database db = new Database(this);

    private RecyclerView recyclerView;
    private FlashcardItemAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton addItemButton;
    private Button savePropertiesButton;

    public ArrayList<FlashcardItem> itemsList;

    private final int REQUEST_RECREATE=1;
    public int collectionId;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_colection);

        addItemButton = findViewById(R.id.addItemButton);
        savePropertiesButton = findViewById(R.id.savePropertiesButton);
        recyclerView=findViewById(R.id.flashcardRecyclerView);

        final Intent addFlashcardItem = new Intent(this, AddItemActivity.class);

        Bundle bundle = getIntent().getExtras();
        collectionId = bundle.getInt("collectionId");

        itemsList=db.returnItemsArrayListOfCollection(collectionId);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFlashcardItem.putExtra("collectionID",collectionId);
                startActivityForResult(addFlashcardItem,REQUEST_RECREATE);
            }
        });
        savePropertiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        layoutManager= new LinearLayoutManager(this);

        adapter= new FlashcardItemAdapter(itemsList,this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FlashcardItemAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                db.deleteCurrentItem(itemsList.get(position).getFlashcardId());
            }

            @Override
            public void onFavouriteClick(int position) {
                db.setFlashcardFavourite(itemsList.get(position).getFlashcardId());
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==REQUEST_RECREATE){
            if (resultCode == RESULT_OK){
                this.recreate();
            }
        }
    }
}
