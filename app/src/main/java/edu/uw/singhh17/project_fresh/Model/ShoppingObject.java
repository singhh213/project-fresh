package edu.uw.singhh17.project_fresh.Model;

/**
 * Created by harpreetsingh on 5/1/16.
 */
public class ShoppingObject {

    public Boolean striked;
    public String name;

    public ShoppingObject(String name, Boolean striked) {
        this.name = name;
        this.striked = striked;
    }

    public ShoppingObject(String name) {
        this.name = name;
        this.striked = false;
    }


    @Override
    public boolean equals(Object o) {
        ShoppingObject other = (ShoppingObject) o;
        if (this.name.equals(other.name) && this.striked == other.striked) {
            return true;
        } else {
            return false;
        }
    }
}
