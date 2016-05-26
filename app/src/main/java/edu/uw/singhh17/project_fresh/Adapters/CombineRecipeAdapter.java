package edu.uw.singhh17.project_fresh.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.singhh17.project_fresh.Model.CombineRecipeObject;
import edu.uw.singhh17.project_fresh.Model.RecipeShoppingObject;
import edu.uw.singhh17.project_fresh.R;

/**
 * Created by harpreetsingh on 5/24/16.
 */
public class CombineRecipeAdapter extends ArrayAdapter<CombineRecipeObject>  {

    private Context context;
    private int resource;
    private List<CombineRecipeObject> objects;

    public CombineRecipeAdapter(Context context, int resource, List<CombineRecipeObject> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View row = inflater.inflate(resource, parent, false);

        TextView combineName = (TextView) row.findViewById(R.id.combine_name);
        TextView combineAmount = (TextView) row.findViewById(R.id.combine_amount);

        final Map<Double, String> fractions = new HashMap<Double, String>();
        fractions.put(0.5, "1/2");
        fractions.put(0.33, "1/3");
        fractions.put(0.25, "1/4");
        fractions.put(0.75, "3/4");
        fractions.put(0.66, "2/3");

        combineName.setText(objects.get(position).name);

        double am = objects.get(position).amount;

        if (fractions.containsKey(am)) {
            combineAmount.setText(fractions.get(am) + " " + objects.get(position).metric);
        } else {
            combineAmount.setText((int) am + " " + objects.get(position).metric);
        }

        return row;

    }

}
