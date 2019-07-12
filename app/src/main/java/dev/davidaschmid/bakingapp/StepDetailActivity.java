package dev.davidaschmid.BakingApp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import dev.davidaschmid.BakingApp.StepDetailFragment;

import com.google.android.exoplayer2.util.Util;

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
        int position = IngredientsStepsFragment.posInSteps;

        position--;
        if (position >= 0){
            updateStepDetailViews(position);

        }

    }
    public void onClickNextStep(View v){

        int position = IngredientsStepsFragment.posInSteps;
        int max = IngredientsStepsActivity.mRecipeModel.getSteps().size()-1;
        position++;
        if (position <= max) {
            updateStepDetailViews(position);
        }

    }
    private void updateStepDetailViews(int position){
        RecipeModel.Step step = IngredientsStepsActivity.mRecipeModel.getStepGivenIndex(position);
        StepDetailFragment.mStepInstructionTV.setText(step.getDescription());
        String videoUrl = step.getVideoURL();
        if (videoUrl.equals("")){
            StepDetailFragment.mErrorImage.setVisibility(View.VISIBLE);
            StepDetailFragment.initializePlayer();
        } else {
            StepDetailFragment.mErrorImage.setVisibility(View.INVISIBLE);
            StepDetailFragment.initializePlayer();
            StepDetailFragment.prepareExoPlayer(Uri.parse(videoUrl), true);
        }
        IngredientsStepsFragment.posInSteps = position;

    }
}
