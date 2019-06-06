package dev.davidaschmid.BakingApp.utilities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import dev.davidaschmid.BakingApp.model.RecipeModel;

public class RecipeList {
    @SerializedName("recipes")
    @Expose
    private ArrayList<RecipeModel> recipes = null;
    public ArrayList<RecipeModel> getRecipes(){return recipes;}
    public void setRecipes(ArrayList<RecipeModel> recipes){this.recipes = recipes;}
}
