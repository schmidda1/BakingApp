package dev.davidaschmid.BakingApp.utilities;

import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import dev.davidaschmid.BakingApp.AppExecutors;
import dev.davidaschmid.BakingApp.model.RecipeModel;
import retrofit2.Response;

public class NetworkUtils {
    private static int asyncFinished;
    private static Response<JSONObject> response = null;
    private static ArrayList<RecipeModel> recipeList;
    static JSONObject jsonArray;
    static String responseStr;
    static String finalUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    static final String TAG = NetworkUtils.class.getSimpleName();

    //https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json

    public static String getNetworkResourceJson(){
        asyncFinished = 0;
        URL url = buildUrl();
        AppExecutors.getInstance().networkIO().execute(new Runnable(){
            @Override
            public void run() {
                try {
                    responseStr = getResponseFromHttpUrl(url);
                    asyncFinished = 1;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        while (asyncFinished != 1){
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return responseStr;
    }
    public static URL buildUrl() {

        Uri builtUri = Uri.parse(finalUrl).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}

