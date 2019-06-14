package dev.davidaschmid.BakingApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.exoplayer2.util.Util;

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
    }
    /*
    @Override
    protected void onPause() {
        super.onPause();
        if(Util.SDK_INT <= 23){
            StepDetailFragment.releasePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(Util.SDK_INT > 23){
            StepDetailFragment.releasePlayer();
        }
    }*/


    public void onClickPreviousStep(View v){
        StepDetailFragment.context2.onPause();
        StepDetailFragment.context2.onStop();
        int position = IngredientsStepsFragment.posInSteps;
        position--;
        if (position >= 0){
            //StepDetailFragment.context2.onDestroy();

            IngredientsStepsFragment.mRecyclerView.findViewHolderForAdapterPosition(position).itemView.performClick();

        }

    }
    public void onClickNextStep(View v){
        //StepDetailFragment.context2.onPause();
        //StepDetailFragment.context2.onStop();
        //StepDetailFragment.playerCleanup();
        int position = IngredientsStepsFragment.posInSteps;
        int max = StepsAdapter.mRecipeModel.getSteps().size()-1;
        position++;
        if (position <= max) {
            //StepDetailFragment.releasePlayer();
            //StepDetailFragment.context2.onDestroy();
            //StepDetailFragment.context2.onPause();
            IngredientsStepsFragment.mRecyclerView.findViewHolderForAdapterPosition(position).itemView.performClick();
        }

    }
}
