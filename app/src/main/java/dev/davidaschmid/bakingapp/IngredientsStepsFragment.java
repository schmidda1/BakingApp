package dev.davidaschmid.BakingApp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dev.davidaschmid.BakingApp.StepsAdapter.StepsAdapterOnClickHandler;

public class IngredientsStepsFragment extends Fragment implements StepsAdapterOnClickHandler{
    public static String STEP_POSITION = "step_position";
    public static RecyclerView mRecyclerView;
    public static StepsAdapter mStepsAdapter;
    public static View mVertLine;
    public static int posInSteps;
    public static Bundle savedInstanceStateGlobal;
    private ViewGroup.LayoutParams params1, params2;
    //mandatory empty constructor
    public IngredientsStepsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null){
            posInSteps = savedInstanceState.getInt(STEP_POSITION, 0);
            savedInstanceStateGlobal = savedInstanceState;
        }
        final View rootView = inflater.inflate(R.layout.fragment_ingredients_steps, container, false);
        TextView mIngredientsTV = rootView.findViewById(R.id.ingredients_tv);
        String ingredients = IngredientsStepsActivity.mRecipeModel.getIngredients();
        mIngredientsTV.setText(ingredients);
        mRecyclerView = rootView.findViewById(R.id.steps_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mStepsAdapter = new StepsAdapter(this);
        mRecyclerView.setAdapter(mStepsAdapter);
        mRecyclerView.setHasFixedSize(true);


        return rootView;
    }
    @Override
    public void onClick(int position) {
        posInSteps = position;
        //Toast.makeText(getContext(), "Position = " + position, Toast.LENGTH_LONG).show();
        if (IngredientsStepsActivity.mTwoPane){
            String stepInstruction = IngredientsStepsActivity.mRecipeModel.getSteps().get(position).getDescription();
            StepDetailFragment.mStepInstructionTV.setText(stepInstruction);
            String videoUrl = IngredientsStepsActivity.mRecipeModel.getSteps().get(position).getVideoURL();
            if(videoUrl.equals("")){
                StepDetailFragment.mErrorImage.setVisibility(View.VISIBLE);
            }else {
                StepDetailFragment.initializePlayer();
                StepDetailFragment.prepareExoPlayer(Uri.parse(videoUrl), true);
                StepDetailFragment.mErrorImage.setVisibility(View.INVISIBLE);
            }
        }else {
            launchStepDetailActivity(position);
        }
    }
    public void launchStepDetailActivity(int position){
        Class destinationClass = StepDetailActivity.class;
        Intent intent = new Intent(getContext(), destinationClass);
        intent.putExtra(STEP_POSITION, position);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(STEP_POSITION, posInSteps);
    }

}
