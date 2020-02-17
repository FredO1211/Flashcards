package com.example.flashcards;

import android.content.Intent;
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
    private Database db;
    private FlashcardsMenu flashcardsMenu;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    public static class FlashcardsMenuViewHolder extends  RecyclerView.ViewHolder{

        public ImageView logoImageView;
        public TextView collectionNameTextView;
        public TextView numberOfElementsTextView;
        public ImageView playImageView;
        public ImageView deleteImageView;
        public ImageView editImageView;

        public FlashcardsMenuViewHolder(@NonNull View itemView, final Database db, final FlashcardsMenu flashcardsMenu, final OnItemClickListener onItemClickListener) {
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
                    if(onItemClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            onItemClickListener.onDeleteClick(position);
                        }
                    }
                    flashcardsMenu.recreate();
                }
            });
            editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(onItemClickListener != null){
//                        int position = getAdapterPosition();
//                        if(position != RecyclerView.NO_POSITION){
//                            flashcardsMenu.startActivity(flashcardsMenu.editCollectionActivity);
//                        }
//                    }
                }
            });
        }
    }


    public FlashcardsMenuAdapter(ArrayList<FlashcardMenuItem> flashcardMenuItemArrayList, Database db,FlashcardsMenu flashcardsMenu){
        this.flashcardsMenu=flashcardsMenu;
        this.db=db;
        this.flashcardMenuItemArrayList=flashcardMenuItemArrayList;
    }

    @NonNull
    @Override
    public FlashcardsMenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_menu_item,viewGroup,false);
        FlashcardsMenuViewHolder flashcardsMenuViewHolder = new FlashcardsMenuViewHolder(view, db, flashcardsMenu,this.onItemClickListener);
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
        return this.flashcardMenuItemArrayList.size();
    }
}
