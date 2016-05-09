package edu.uw.singhh17.project_fresh.Model;

import java.io.File;

/**
 * Created by harpreetsingh on 4/9/16.
 */
public class PantryData {

    //need to add image and color field and create constructor for them.
    //Probably need string to concatenate daysLeft

    public String name;
    public int daysLeft;
    public String imageUrl;
    public String nutritionLabel;
    public String quantity;
    public String objectId;

    public PantryData(String name, int daysLeft, String imageUrl, String nutritionLabel, String quantity, String objectId) {
        this.name = name;
        this.daysLeft = daysLeft;
        this.imageUrl = imageUrl;
        this.nutritionLabel = nutritionLabel;
        this.quantity = quantity;
        this.objectId = objectId;
    }
}
