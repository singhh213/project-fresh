package edu.uw.singhh17.project_fresh.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.singhh17.project_fresh.Model.RecipeShoppingObject;
import edu.uw.singhh17.project_fresh.Model.ShoppingObject;
import edu.uw.singhh17.project_fresh.R;
import edu.uw.singhh17.project_fresh.RecipeShoppingList;

/**
 * Created by harpreetsingh on 5/24/16.
 */
public class RecipeShoppingAdapter extends ArrayAdapter<RecipeShoppingObject> {

    private Context context;
    private int resource;
    private List<RecipeShoppingObject> objects;

    public RecipeShoppingAdapter(Context context, int resource, List<RecipeShoppingObject> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View row = inflater.inflate(resource, parent, false);

        TextView itemText = (TextView) row.findViewById(R.id.recipeShopName);
        TextView amountText = (TextView) row.findViewById(R.id.recipeShopAmount);

        double amount = objects.get(position).amount;
        String name = objects.get(position).name;

        final Map<Double, String> fractions = new HashMap<Double, String>();

        fractions.put(0.5, "1/2");
        fractions.put(0.33, "1/3");
        fractions.put(0.25, "1/4");
        fractions.put(0.75, "3/4");
        fractions.put(0.66, "2/3");

        if (amount == -1.0) {
            itemText.setTextSize(22);
            itemText.setText(name);
            itemText.setTextColor(Color.WHITE);
            amountText.setTextSize(22);
            amountText.setText(">");
            amountText.setTextColor(Color.WHITE);
            row.setBackgroundColor(Color.parseColor("#7cae41"));

        } else {
            itemText.setText(name);
            if (fractions.containsKey(amount)) {
                amountText.setText(fractions.get(amount) + " " + objects.get(position).metric);
            } else {
                amountText.setText((int)amount + " " + objects.get(position).metric);
            }
        }


//
//        if (objects.get(position).striked) {
//            item.setPaintFlags(item.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        } else {
//            item.setPaintFlags(item.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
//        }


        return row;
    }
}
