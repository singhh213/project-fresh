package edu.uw.singhh17.project_fresh.Model;

/**
 * Created by harpreetsingh on 4/9/16.
 */
public class PantryData {

    //need to add image and color field and create constructor for them.
    //Probably need string to concatenate daysLeft

    public String name;
    public int daysLeft;
    //public image
    //public color

    public PantryData(String name, int daysLeft) {
        this.name = name;
        this.daysLeft = daysLeft;
    }
}
