package dev.davidaschmid.BakingApp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class RecipeService extends IntentService {
    public static final String ACTION_UPDATE_RECIPE_WIDGET = "update_recipe_widget";
    public static final String INGREDIENTS = "ingredients";
    public RecipeService(){super("RecipeService");}
    public static void startActionUpdateRecipeWidgets(Context context, String ingredients){
        Intent intent = new Intent(context, RecipeService.class);
        intent.putExtra(INGREDIENTS, ingredients);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGET);
        context.startService(intent);
    }
    @Override
    protected void onHandleIntent(Intent intent){
        String ingredients;
        if(intent != null){
            final String action = intent.getAction();
            ingredients = intent.getStringExtra(INGREDIENTS);
            if(ACTION_UPDATE_RECIPE_WIDGET.equals(action)){
                handleActionUpdateRecipeWidgets(ingredients);
            }
        }
    }
    private void handleActionUpdateRecipeWidgets(String ingredients){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager, ingredients, appWidgetIds);
    }


}
