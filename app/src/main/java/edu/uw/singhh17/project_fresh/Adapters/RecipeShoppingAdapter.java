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

import java.util.List;

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

        String amount = objects.get(position).amount;
        String name = objects.get(position).name;

        if (amount.equals("-1 -1")) {
            itemText.setTextSize(22);
            itemText.setText(name);
            itemText.setTextColor(Color.WHITE);
            amountText.setTextSize(22);
            amountText.setText(">");
            amountText.setTextColor(Color.WHITE);
            row.setBackgroundColor(Color.parseColor("#9BE52A"));

        } else {
            itemText.setText(name);
            amountText.setText(amount);
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
