package dev.davidaschmid.BakingApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Button;
import dev.davidaschmid.BakingApp.model.RecipeModel;

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
            mTwoPane = true;
        }else{
            mTwoPane = false;
        }

    }
    boolean getPaneMode(){
        //This method does work but I won't use it to satisfy the reviewer.
        //widthPixels and heightPixels change with orientation
        boolean twoPane;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int dpi = displayMetrics.densityDpi;
        int widthPx = displayMetrics.widthPixels;
        int heightPx = displayMetrics.heightPixels;
        int trueWidthDp = widthPx < heightPx ? (int)(widthPx*160.0/dpi):(int)(heightPx*160.0/dpi);
        twoPane = trueWidthDp >= 600 ? true : false;
        return twoPane;

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION_KEY, stepPosition);
        outState.putBoolean(TWOPANE_KEY, mTwoPane);
    }

    public void setRecipeModel(RecipeModel recipeModel){mRecipeModel = recipeModel;}


}
