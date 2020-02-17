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
    private ArrayList<FlashcardItem> flashcardItemsArrayList;
    private Database db;
    private OnItemClickListener onItemClickListener;
    private EditCollectionActivity editCollectionActivity;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static class FlashcardItemViewHolder extends  RecyclerView.ViewHolder{
        public TextView polishMiningTextView;
        public TextView englishMiningTextView;
        public ImageView deleteItemImageView;

        public FlashcardItemViewHolder(@NonNull View itemView, Database db, final EditCollectionActivity editCollectionActivity, final OnItemClickListener onItemClickListener) {
            super(itemView);
            polishMiningTextView= itemView.findViewById(R.id.polishMiningTextView);
            englishMiningTextView= itemView.findViewById(R.id.englishMiningTextView);
            deleteItemImageView = itemView.findViewById(R.id.deleteItemImageView);
        }
    }

    public FlashcardItemAdapter(ArrayList<FlashcardItem> flashcardItemsArrayList, Database db, EditCollectionActivity editCollectionActivity) {
        this.flashcardItemsArrayList = flashcardItemsArrayList;
        this.db = db;
        this.editCollectionActivity = editCollectionActivity;
    }

    @NonNull
    @Override
    public FlashcardItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_collection_item,viewGroup,false);
        FlashcardItemViewHolder flashcardItemViewHolder = new FlashcardItemViewHolder(view, db,editCollectionActivity,this.onItemClickListener);
        return flashcardItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardItemViewHolder flashcardItemViewHolder, int i) {
        FlashcardItem currentItem= flashcardItemsArrayList.get(i);

        flashcardItemViewHolder.polishMiningTextView.setText(currentItem.getPolishMiningOfWord());
        flashcardItemViewHolder.englishMiningTextView.setText(currentItem.getEnglishMiningOfWord());
    }

    @Override
    public int getItemCount() {
        return this.flashcardItemsArrayList.size();
    }
}
