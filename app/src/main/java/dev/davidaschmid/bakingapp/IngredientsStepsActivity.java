package dev.davidaschmid.BakingApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import dev.davidaschmid.BakingApp.StepsAdapter.StepsAdapterOnClickHandler;
import dev.davidaschmid.BakingApp.model.RecipeModel;
import dev.davidaschmid.BakingApp.utilities.NetworkUtils;

public class IngredientsStepsActivity extends AppCompatActivity {
    private int DEFAULT_POSITION = -1;
    Intent intent;
    public static RecipeModel mRecipeModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_steps);
        intent = getIntent();
        int position = intent.getIntExtra(MainActivity.POSITION, DEFAULT_POSITION);
        mRecipeModel = MainActivity.recipeList.get(position);
        IngredientsStepsFragment.mStepsAdapter.setmRecipeModel(mRecipeModel);
        //NetworkUtils.downloadVideos();
        int testing = 0;
        testing = 1;
        testing = 2;

    }
    public void setRecipeModel(RecipeModel recipeModel){mRecipeModel = recipeModel;}

}
