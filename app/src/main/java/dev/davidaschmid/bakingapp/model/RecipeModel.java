package dev.davidaschmid.BakingApp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RecipeModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ingredients")
    @Expose
    private ArrayList<Ingredients> ingredients = null;
    @SerializedName("steps")
    @Expose
    private ArrayList<Step> steps = null;
    @SerializedName("servings")
    @Expose
    private int servings;
    @SerializedName("image")
    @Expose
    private String image;
    public RecipeModel(){

    }
    public String getName(){return this.name;}
    public RecipeModel(int id, String name, ArrayList<Ingredients> ingredients, ArrayList<Step> steps,
                       int servings, String image){
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }
    public class Ingredients{
        private float quantity;
        private String measure;
        private String ingredient;
        public Ingredients(){

        }
        public Ingredients(float quantity, String measure, String ingredient){
            this.quantity = quantity;
            this.measure = measure;
            this.ingredient = ingredient;
        }
    }
    public class Videos{
        private int index;
        private String videoUrl;
        Videos(int index, String videoUrl){
            this.index = index;
            this.videoUrl = videoUrl;
        }
        public int getIndex(){return index;}
        public String getVideoUrl(){return videoUrl;}

    }
    public class Step {
        private int id;
        private String shortDescription;
        private String description;
        private String videoURL;
        private String thumbnailURL;
        public Step(){

        }
        public Step(int id, String shortDescription, String description,
                    String videoUrl, String thumbnailUrl){
            this.id = id;
            this.shortDescription = shortDescription;
            this.description = description;
            this.videoURL = videoUrl;
            this.thumbnailURL = thumbnailUrl;
        }
        public String getShortDescription(){return shortDescription;}
        public String getDescription(){return description;}
        public String getVideoURL(){return videoURL;}
        public String getThumbnailURL(){return thumbnailURL;}
    }//end class Step
    public ArrayList<Step> getSteps(){
        return steps;
    }
    public Step getStepGivenIndex(int index){
        return steps.get(index);
    }
    public ArrayList<Videos> getVideoUrls(){
        ArrayList<Videos> videoUrls = new ArrayList<>(steps.size());
        int count = steps.size();
        for (int i = 0; i < count; ++i){
            String videoUrl = steps.get(0).getVideoURL();
            videoUrls.set(i, new Videos(i, videoUrl));
        }
        return videoUrls;
    }
    public String getIngredients(){
        String allTogether = "";
        for (int i = 0; i < ingredients.size(); ++i){
           allTogether += Float.toString(ingredients.get(i).quantity) + " "
           + ingredients.get(i).measure + " "
           + ingredients.get(i).ingredient + ", ";
        }
        return allTogether;
    }
    public String[] getShortDesciptions(){
        String[] descriptions = new String[steps.size()];
        for(int i = 0; i < steps.size(); ++i){
            descriptions[i] = steps.get(i).shortDescription;
        }
        return descriptions;
    }
}
