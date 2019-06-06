package dev.davidaschmid.BakingApp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dev.davidaschmid.BakingApp.model.RecipeModel;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsAdapterViewHolder> {
    public static RecipeModel mRecipeModel;
    public StepsAdapterOnClickHandler mClickHandler;
    public static View viewOld = null;
    public static int oldPosition = -1;


    public interface StepsAdapterOnClickHandler{
        void onClick(int position);
    }
    public StepsAdapter(StepsAdapterOnClickHandler clickHandler){ mClickHandler = clickHandler;}
    public class StepsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mStepsTV;
        public StepsAdapterViewHolder(View view){
            super(view);
            mStepsTV = view.findViewById(R.id.recipe_steps_tv);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (viewOld != null){
                TextView textView = viewOld.findViewById(R.id.recipe_steps_tv);
                textView.setBackgroundColor(v.getResources().getColor(R.color.colorPrimaryLight));
            }
            int adapterPosition = getAdapterPosition();
            mStepsTV.setBackgroundColor(v.getResources().getColor(R.color.colorAccent));
            //mStepsTV.setBackgroundColor(R.color.colorAccent);
            oldPosition = adapterPosition;
            viewOld = v;
            mClickHandler.onClick(adapterPosition);
        }
    }
    @NonNull
    @Override
    public StepsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.steps_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new StepsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsAdapterViewHolder stepsAdapterViewHolder, int position) {
        RecipeModel.Step step = mRecipeModel.getSteps().get(position);
        stepsAdapterViewHolder.mStepsTV.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        if(null == mRecipeModel)return 0;
        return mRecipeModel.getSteps().size();
    }
    public void setmRecipeModel(RecipeModel recipeModel){
        mRecipeModel = recipeModel;
        notifyDataSetChanged();
    }

}
