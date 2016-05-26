package edu.uw.singhh17.project_fresh.Model;

/**
 * Created by harpreetsingh on 5/24/16.
 */
public class CombineRecipeObject {

    public Boolean striked;
    public String name;
    public double amount;
    public String metric;

    public CombineRecipeObject(String name, Boolean striked, double amount, String metric) {
        this.name = name;
        this.striked = striked;
        this.amount = amount;
        this.metric = metric;
    }

    public CombineRecipeObject(String name, double amount, String metric) {
        this.name = name;
        this.striked = false;
        this.amount = amount;
        this.metric = metric;
    }

}
