package edu.uw.singhh17.project_fresh.Model;

/**
 * Created by harpreetsingh on 4/30/16.
 */
public class RecipeObject {

    private String name;
    private String imgUrl;
    private int time;
    private String difficulty;
    private String recipeId;
    private String servingsCals;


    public RecipeObject(String name, String imgUrl, int time, String difficulty, String servingsCals, String recipeId) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.time = time;
        this.difficulty = difficulty;
        this.recipeId = recipeId;
        this.servingsCals = servingsCals;
    }

    public String getName() {
        return this.name;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public int getTime() {
        return this.time;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    public String getRecipeId() {
        return this.recipeId;
    }

    public String getServingsCals() {
        return this.servingsCals;
    }

}
