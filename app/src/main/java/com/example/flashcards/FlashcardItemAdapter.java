package com.example.flashcards;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FlashcardItemAdapter extends RecyclerView.Adapter<FlashcardItemAdapter.FlashcardItemViewHolder> {

    private OnItemClickListener onItemClickListener;
    private EditCollectionActivity editCollectionActivity;

    private ArrayList<FlashcardItem> flashcardItemsArrayList;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onFavouriteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static class FlashcardItemViewHolder extends  RecyclerView.ViewHolder{
        public TextView polishMiningTextView;
        public TextView englishMiningTextView;
        public ImageView deleteItemImageView;
        public ImageView favouriteImageView;

        public FlashcardItemViewHolder(@NonNull View itemView, final EditCollectionActivity editCollectionActivity, final OnItemClickListener onItemClickListener) {
            super(itemView);

            polishMiningTextView= itemView.findViewById(R.id.polishMiningTextView);
            englishMiningTextView= itemView.findViewById(R.id.englishMiningTextView);
            deleteItemImageView = itemView.findViewById(R.id.deleteItemImageView);
            favouriteImageView = itemView.findViewById(R.id.favouriteFlashcardImageView);

            deleteItemImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            onItemClickListener.onDeleteClick(position);
                        }
                    }
                    editCollectionActivity.recreate();
                }
            });

            favouriteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            onItemClickListener.onFavouriteClick(position);
                        }
                    }
                    editCollectionActivity.recreate();
                }
            });
        }
    }

    public FlashcardItemAdapter(ArrayList<FlashcardItem> flashcardItemsArrayList, EditCollectionActivity editCollectionActivity) {
        this.flashcardItemsArrayList = flashcardItemsArrayList;
        this.editCollectionActivity = editCollectionActivity;
    }

    @NonNull
    @Override
    public FlashcardItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_collection_item,viewGroup,false);
        FlashcardItemViewHolder flashcardItemViewHolder = new FlashcardItemViewHolder(view, editCollectionActivity,this.onItemClickListener);
        return flashcardItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardItemViewHolder flashcardItemViewHolder, int i) {
        FlashcardItem currentItem= flashcardItemsArrayList.get(i);

        flashcardItemViewHolder.favouriteImageView.setImageResource(currentItem.getFavouriteFlashcardResource());
        flashcardItemViewHolder.polishMiningTextView.setText(currentItem.getPolishMiningOfWord());
        flashcardItemViewHolder.englishMiningTextView.setText(currentItem.getEnglishMiningOfWord());
    }

    @Override
    public int getItemCount() {
        return this.flashcardItemsArrayList.size();
    }
}
