package edu.uw.singhh17.project_fresh.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

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

        combineName.setText(objects.get(position).name);
        combineAmount.setText(objects.get(position).amount + " " + objects.get(position).metric);

        return row;

    }

}
