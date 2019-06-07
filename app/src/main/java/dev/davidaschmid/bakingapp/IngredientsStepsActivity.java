package dev.davidaschmid.BakingApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import dev.davidaschmid.BakingApp.StepsAdapter.StepsAdapterOnClickHandler;
import dev.davidaschmid.BakingApp.model.RecipeModel;
import dev.davidaschmid.BakingApp.utilities.NetworkUtils;

public class IngredientsStepsActivity extends AppCompatActivity {
    private int DEFAULT_POSITION = -1;
    Intent intent;
    public static boolean mTwoPane;
    Button mPreviousStep;
    Button mNextStep;

    int stepPosition;
    public static RecipeModel mRecipeModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_steps);
        intent = getIntent();
        int position = intent.getIntExtra(MainActivity.POSITION, DEFAULT_POSITION);
        mRecipeModel = MainActivity.recipeList.get(position);
        IngredientsStepsFragment.mStepsAdapter.setmRecipeModel(mRecipeModel);
        setTitle(mRecipeModel.getName());
        if (findViewById(R.id.step_instruction_fragment) != null){
            stepPosition = IngredientsStepsFragment.posInSteps;
            mTwoPane = true;
            StepDetailFragment.mStepInstructionTV.setVisibility(View.VISIBLE);
            //String stepInstruction =
            //StepDetailFragment.mStepInstructionTV.setText();
        }else{
            mTwoPane = false;
        }

    }
    public void setRecipeModel(RecipeModel recipeModel){mRecipeModel = recipeModel;}

}
