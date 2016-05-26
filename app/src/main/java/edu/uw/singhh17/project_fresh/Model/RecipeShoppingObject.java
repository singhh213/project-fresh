package edu.uw.singhh17.project_fresh.Model;

/**
 * Created by harpreetsingh on 5/24/16.
 */
public class RecipeShoppingObject {

    public Boolean striked;
    public String name;
    public double amount;
    public String metric;

    public RecipeShoppingObject(String name, Boolean striked, double amount, String metric) {
        this.name = name;
        this.striked = striked;
        this.amount = amount;
        this.metric = metric;
    }

    public RecipeShoppingObject(String name, double amount) {
        this.name = name;
        this.striked = false;
        this.amount = amount;
        this.metric = "";
    }


    @Override
    public boolean equals(Object o) {
        RecipeShoppingObject other = (RecipeShoppingObject) o;
        if (this.name.equals(other.name) && this.striked == other.striked && this.amount == other.amount) {
            return true;
        } else {
            return false;
        }
    }
}
