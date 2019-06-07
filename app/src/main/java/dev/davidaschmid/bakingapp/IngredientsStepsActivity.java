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
    private int DEFAULT_POSITION = 0;
    private final String POSITION_KEY = "position_key";
    private final String TWOPANE_KEY = "twopane_key";
    Intent intent;
    public static boolean mTwoPane;
    Button mPreviousStep;
    Button mNextStep;
    int stepPosition;
    public static RecipeModel mRecipeModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            mTwoPane = savedInstanceState.getBoolean(TWOPANE_KEY);
            stepPosition = savedInstanceState.getInt(POSITION_KEY);
        }else{
            intent = getIntent();
            stepPosition = intent.getIntExtra(MainActivity.POSITION, DEFAULT_POSITION);
        }
        IngredientsStepsFragment.posInSteps = stepPosition;
        setContentView(R.layout.activity_ingredients_steps);

        mRecipeModel = MainActivity.recipeList.get(stepPosition);
        IngredientsStepsFragment.mStepsAdapter.setmRecipeModel(mRecipeModel);
        setTitle(mRecipeModel.getName());
        if (findViewById(R.id.step_instruction_fragment) != null){
            //stepPosition = IngredientsStepsFragment.posInSteps;
            mTwoPane = true;
            //String stepInstruction =
            //StepDetailFragment.mStepInstructionTV.setText();
        }else{
            mTwoPane = false;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION_KEY, stepPosition);
        outState.putBoolean(TWOPANE_KEY, mTwoPane);
    }

    public void setRecipeModel(RecipeModel recipeModel){mRecipeModel = recipeModel;}


}
