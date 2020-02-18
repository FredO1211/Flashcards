package com.example.flashcards;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class FlashcardsMenu extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FlashcardsMenuAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton addCollectionButton;
    private final Database db = new Database(this);
    public ArrayList<FlashcardMenuItem> collectionsList;
    private final int REQUEST_RECREATE=0;
    public String login;
    private Intent writhingFlashcardIntent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        final Intent createCollectionIntent = new Intent(this,CreateCollectionActivity.class);
        final Intent editCollectionIntent = new Intent(this,EditCollectionActivity.class);
        writhingFlashcardIntent = new Intent(this, WrithingFlashcardActivity.class);
        Bundle extras =getIntent().getExtras();
        final String LOGIN=extras.getString("login");
        this.login=LOGIN;

        addCollectionButton= findViewById(R.id.addCollectionButton);
        addCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCollectionIntent.putExtra("login",LOGIN);
                startActivityForResult(createCollectionIntent,REQUEST_RECREATE);
            }
        });

        collectionsList=db.returnCollectionsArrayList(LOGIN);

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        adapter= new FlashcardsMenuAdapter(collectionsList, db,this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FlashcardsMenuAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                db.deleteCurrentCollection(collectionsList.get(position).getCollectionId());
            }

            @Override
            public void onEditClick(int position) {
                editCollectionIntent.putExtra("collection",db.getCollectionIdUsingIndex(LOGIN,position));
                startActivity(editCollectionIntent);
            }

            @Override
            public void onPlayClick(int position) {
                AlertDialog alertDialog=dialogBox().create();
                alertDialog.show();
            }
        });
    }

    AlertDialog.Builder dialogBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Choose learning way")
                .setCancelable(true)
                .setPositiveButton("By reading", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("By writhing", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(writhingFlashcardIntent);
                    }
                });
        return builder;
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
