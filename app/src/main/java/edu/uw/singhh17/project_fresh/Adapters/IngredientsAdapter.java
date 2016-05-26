package edu.uw.singhh17.project_fresh.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.uw.singhh17.project_fresh.Model.Ingredient;
import edu.uw.singhh17.project_fresh.Model.PantryData;
import edu.uw.singhh17.project_fresh.R;

/**
 * Created by harpreetsingh on 4/30/16.
 */
public class IngredientsAdapter extends ArrayAdapter<Ingredient> {

    private Context context;
    private int resource;
    private ArrayList<Ingredient> ingredients;

//    public IngredientsAdapter(Context context, int resource, ArrayList<Ingredient> objects) {
//        super(context, resource, objects);
//        this.context = context;
//        this.resource = resource;
//        this.ingredients = objects;
//    }

    public IngredientsAdapter(Context context, int resource, ArrayList<Ingredient> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.ingredients = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View row = inflater.inflate(resource, parent, false);

        TextView igName = (TextView) row.findViewById(R.id.ingred_name);
        TextView igAmount = (TextView) row.findViewById(R.id.ingred_amount);

        String name = ingredients.get(position).getName();

        if (name.equals("Milk") || name.equals("Cheddar Cheese") || name.equals("Yogurt") ||
                name.equals("Red Wine") || name.equals("Flour") || name.equals("Bread") ||
                name.equals("Chicken Breast") || name.equals("Eggs") || name.equals("Olive Oil") || name.equals("French Bread")
                || name.equals("Chocolate Syrup") || name.equals("Strawberries"))  {
            igName.setTextColor(Color.parseColor("#65bab7"));
            igAmount.setTextColor(Color.parseColor("#65bab7"));
        } else {
            igName.setTextColor(Color.parseColor("#e8506a"));
            igAmount.setTextColor(Color.parseColor("#e8506a"));
        }

        igName.setText(ingredients.get(position).getName());
        igAmount.setText(ingredients.get(position).getAmount());


        return row;
    }
}
