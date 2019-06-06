package dev.davidaschmid.BakingApp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {
    private String[] mRecipeNames;
    private RecipeAdapterOnClickHandler mClickHandler;
    public interface RecipeAdapterOnClickHandler{
        void onClick(int position);
    }
    public RecipeAdapter(){

    }
    public RecipeAdapter(RecipeAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }
    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mRecipeTextView;
        public RecipeAdapterViewHolder(View view){
            super(view);
            mRecipeTextView = view.findViewById(R.id.recipe_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }
    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.card_view_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterViewHolder recipeAdapterViewHolder, int position) {
        recipeAdapterViewHolder.mRecipeTextView.setText(mRecipeNames[position]);
    }

    @Override
    public int getItemCount() {
        if (null == mRecipeNames){
            return 0;
        }
        return mRecipeNames.length;
    }
    public void setmRecipeNames(String[] recipeNames){
        mRecipeNames = recipeNames;
        notifyDataSetChanged();
    }
}
