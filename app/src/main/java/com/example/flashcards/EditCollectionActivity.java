package com.example.flashcards;

import android.content.Intent;
import android.support.annotation.Nullable;
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
    private final int REQUEST_RECREATE=1;
    public int collectionId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_colection);

        final Intent addFlashcardItem = new Intent(this, AddItemActivity.class);
        Bundle bundle = getIntent().getExtras();
        collectionId = bundle.getInt("collection");

        itemsList=db.returnItemsArrayListOfCollection(collectionId);

        addItemButton = findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFlashcardItem.putExtra("collectionID",collectionId);
                startActivityForResult(addFlashcardItem,REQUEST_RECREATE);
            }
        });

        recyclerView=findViewById(R.id.flashcardRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        adapter= new FlashcardItemAdapter(itemsList,db,this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FlashcardItemAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                db.deleteCurrentItem(itemsList.get(position).getFlashcardId());
            }
        });
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
