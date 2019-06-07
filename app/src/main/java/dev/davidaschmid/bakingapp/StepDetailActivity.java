package dev.davidaschmid.BakingApp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import dev.davidaschmid.BakingApp.model.RecipeModel;

public class StepDetailActivity extends AppCompatActivity {
    Intent intent;
    int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        intent = getIntent();
        int position = intent.getIntExtra(IngredientsStepsFragment.STEP_POSITION, DEFAULT_POSITION);
        setTitle(StepsAdapter.mRecipeModel.getName());
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == 2){//landscape mode
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            if(actionBar != null){
                actionBar.hide();
            }
        }
        /*else{
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            if(actionBar != null){
                actionBar.show();

        }*/

        //StepsAdapter.mRecipeModel.getSteps().get(position);

    }
    public void onClickPreviousStep(View v){
        StepDetailFragment.context2.onDestroy();
        int position = IngredientsStepsFragment.posInSteps;
        position--;
        if (position >= 0){
            IngredientsStepsFragment.mRecyclerView.findViewHolderForAdapterPosition(position).itemView.performClick();

        }

    }
    public void onClickNextStep(View v){
        StepDetailFragment.context2.onDestroy();
        int position = IngredientsStepsFragment.posInSteps;
        int max = StepsAdapter.mRecipeModel.getSteps().size()-1;
        position++;
        if (position <= max) {
            IngredientsStepsFragment.mRecyclerView.findViewHolderForAdapterPosition(position).itemView.performClick();
        }

    }
}
