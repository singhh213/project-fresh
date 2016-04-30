package edu.uw.singhh17.project_fresh.Model;

/**
 * Created by harpreetsingh on 4/30/16.
 */
public class RecipeObject {

    private String name;
    private String imgUrl;
    private int time;
    private String difficulty;


    public RecipeObject(String name, String imgUrl, int time, String difficulty) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.time = time;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getTime() {
        return time;
    }

    public String getDifficulty() {
        return difficulty;
    }

}
