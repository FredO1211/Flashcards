package com.example.flashcards;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FlashcardsMenuAdapter extends RecyclerView.Adapter<FlashcardsMenuAdapter.FlashcardsMenuViewHolder> {
    private ArrayList<FlashcardMenuItem> flashcardMenuItemArrayList;

    public static class FlashcardsMenuViewHolder extends  RecyclerView.ViewHolder{
        public ImageView logoImageView;
        public TextView collectionNameTextView;
        public TextView numberOfElementsTextView;
        public ImageView playImageView;
        public ImageView deleteImageView;
        public ImageView editImageView;

        public FlashcardsMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            logoImageView = itemView.findViewById(R.id.logoImageView);
            collectionNameTextView = itemView.findViewById(R.id.collectionNameTextView);
            numberOfElementsTextView = itemView.findViewById(R.id.numberOfElementsTextView);
            playImageView = itemView.findViewById(R.id.playImageView);
            deleteImageView = itemView.findViewById(R.id.deleteImageView);
            editImageView = itemView.findViewById(R.id.editImageView);

            playImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


    public FlashcardsMenuAdapter(ArrayList<FlashcardMenuItem> flashcardMenuItemArrayList){
        this.flashcardMenuItemArrayList=flashcardMenuItemArrayList;
    }

    @NonNull
    @Override
    public FlashcardsMenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_menu_item,viewGroup,false);
        FlashcardsMenuViewHolder flashcardsMenuViewHolder = new FlashcardsMenuViewHolder(view);
        return  flashcardsMenuViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardsMenuViewHolder flashcardsMenuViewHolder, int i) {
        FlashcardMenuItem currentItem = flashcardMenuItemArrayList.get(i);

        flashcardsMenuViewHolder.logoImageView.setImageResource(currentItem.getLogoImageViewResource());
        flashcardsMenuViewHolder.numberOfElementsTextView.setText(currentItem.getCounterElementsText());
        flashcardsMenuViewHolder.collectionNameTextView.setText(currentItem.getNameCollection());
    }

    @Override
    public int getItemCount() {
        return flashcardMenuItemArrayList.size();
    }
}
