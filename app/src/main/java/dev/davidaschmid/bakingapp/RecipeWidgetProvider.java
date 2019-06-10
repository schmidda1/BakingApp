package dev.davidaschmid.BakingApp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class RecipeWidgetProvider extends AppWidgetProvider {
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                String ingedients, int appWidgetId){
        Intent intent;
        intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setTextViewText(R.id.widget_tv, ingedients);
        views.setOnClickPendingIntent(R.id.widget_tv, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);


    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        String ingredients = "Click to launch RecipeApp!";
        updateRecipeWidgets(context, appWidgetManager, ingredients, appWidgetIds);
        //RecipeService.startActionUpdateRecipeWidgets(context, ingredients);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           String ingredients, int[] appWidgetIds){
        for(int appWidgetId: appWidgetIds){
            updateAppWidget(context, appWidgetManager, ingredients, appWidgetId);
        }
    }




}
