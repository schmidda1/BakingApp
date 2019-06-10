package dev.davidaschmid.BakingApp.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import dev.davidaschmid.BakingApp.AppExecutors;
import dev.davidaschmid.BakingApp.StepsAdapter;
import dev.davidaschmid.BakingApp.model.RecipeModel;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class NetworkUtils {
    private static int asyncFinished;
    private static Response<RecipeList> response = null;
    private static ArrayList<RecipeModel> recipeList;

    //https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json

    public interface ApiService {
        @GET("/topher/2017/May/59121517_baking/{file}")
        Call<RecipeList> getApiData(@Path("file")String file);
    }
    /*
    public interface ApiService2{
        @GET("")
    }*/
    public static ArrayList<RecipeModel> getApiDataUsingRetrofit() {
        asyncFinished = 0;
        String ROOT_URL = "https://d17h27t6h515a5.cloudfront.net";
        final String name;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        final Call<RecipeList> call = service.getApiData("baking.json");
        AppExecutors.getInstance().networkIO().execute(new Runnable(){
            @Override
            public void run() {
                try {
                    response = call.execute();
                    recipeList = response.body().getRecipes();
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
        return recipeList;
    }
    void downloadVideos(){
        ArrayList<RecipeModel.Videos> videoUrls =  StepsAdapter.mRecipeModel.getVideoUrls();

    }
}

