package dev.davidaschmid.BakingApp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dev.davidaschmid.BakingApp.model.RecipeModel;
import dev.davidaschmid.BakingApp.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {
    public static final String POSITION = "position";
    private final String TAG = MainActivity.class.getSimpleName();
    private JSONObject jsonObject;
    private String response;
    public static ArrayList<RecipeModel> recipeList;
    JSONArray mRecipesJson;
    CardView mRecipeCard;
    TextView mRecipeName;
    private RecyclerView mRecipeRV;
    String recipesJson;
    String recipesJsonStr;
    String[] recipeNames;
    int asyncFinished;
    private Gson gson;
    private RecipeAdapter mRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecipeRV = findViewById(R.id.main_recycler_view);
        int columns = numberOfColumns();

        GridLayoutManager layoutManager = new GridLayoutManager(this, columns);
        mRecipeRV.setLayoutManager(layoutManager);
        mRecipeRV.setHasFixedSize(true);
        mRecipeAdapter = new RecipeAdapter(this);
        mRecipeRV.setAdapter(mRecipeAdapter);
        asyncFinished = 0;
        response = NetworkUtils.getNetworkResourceJson();
        try {
            recipeList = getRecipes(response);
        }catch(JSONException e){
            e.printStackTrace();
        }
        recipeNames = createLabels();
        mRecipeAdapter.setmRecipeNames(recipeNames);

    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        double widthDivider = 308;
        int dpi = displayMetrics.densityDpi;
        //convert to dp
        double width = displayMetrics.widthPixels*160.0/dpi;
        int nColumns = (int)(width / widthDivider);
        if (nColumns < 1) return 1;
        return nColumns;
    }
    private static String getSizeName(Context context) {
        int screenLayout = context.getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return "small";
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return "normal";
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return "large";
            case 4: // Configuration.SCREENLAYOUT_SIZE_XLARGE is API >= 9
                return "xlarge";
            default:
                return "undefined";
        }
    }
    void launchNextActivity(int position){
        IngredientsStepsActivity.mRecipeModel = recipeList.get(position);
        Class destinationClass = IngredientsStepsActivity.class;
        Intent intent = new Intent(this, destinationClass);
        intent.putExtra(POSITION, position);
        String widgetTitle = IngredientsStepsActivity.mRecipeModel.getName();
        widgetTitle = "*** " + widgetTitle + " ***" + "\n";
        String ingredients = IngredientsStepsActivity.mRecipeModel.getIngredients();
        ingredients = widgetTitle + ingredients;
        RecipeService.startActionUpdateRecipeWidgets(this, ingredients);
        startActivity(intent);

    }
    String[] createLabels(){
        String[] recipeNames = new String[recipeList.size()];
        for (int i = 0; i < recipeList.size(); ++i){
            recipeNames[i] = recipeList.get(i).getName();
        }
        return recipeNames;
    }

    @Override
    public void onClick(int position) {
        Context context = this;
        launchNextActivity(position);
    }
    ArrayList<RecipeModel> getRecipes(String response)throws JSONException {
        Gson gson = new Gson();
        ArrayList<RecipeModel> recipes;
        RecipeModel recipe;
        JSONArray jsonArray = new JSONArray(response);
        int size = jsonArray.length();
        recipes = new ArrayList<>(size);
        for(int i = 0; i < size; ++i){
            String jsonObject = jsonArray.getJSONObject(i).toString();
            recipe = gson.fromJson(jsonObject, RecipeModel.class);
            recipes.add(recipe);
        }
        return recipes;
    }

}
