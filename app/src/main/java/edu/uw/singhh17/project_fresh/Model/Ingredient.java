package edu.uw.singhh17.project_fresh.Model;

/**
 * Created by harpreetsingh on 4/30/16.
 */
public class Ingredient {

    private String name;
    private String amount;

    public Ingredient(String name, String amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return this.name;
    }

    public String getAmount() {
        return this.amount;
    }
}
