package edu.uw.singhh17.project_fresh;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by harpreetsingh on 4/9/16.
 */
public class PantryAdapter extends ArrayAdapter<PantryData> {

    private Context context;
    private int resource;
    private ArrayList<PantryData> objects;

    public PantryAdapter(Context context, int resource, ArrayList<PantryData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View row = inflater.inflate(resource, parent, false);

        TextView itemName = (TextView) row.findViewById(R.id.name);
        TextView expInfo = (TextView) row.findViewById(R.id.expireInfo);
        ImageView indicator = (ImageView) row.findViewById(R.id.colorIndicator);
        GradientDrawable bgShape = (GradientDrawable)indicator.getBackground();

        itemName.setText(objects.get(position).name);
        int daysLeft = objects.get(position).daysLeft;
        Log.d("TEST days left", "getView: " + daysLeft);

        if (daysLeft > 5) {
            Log.d("TEST days left", "getView: " + daysLeft);
            bgShape.setColor(Color.GREEN);
        } else if (daysLeft > 3){
            bgShape.setColor(Color.YELLOW);
        } else {
            bgShape.setColor(Color.RED);
        }

        expInfo.setText("Expires in " + Integer.toString(daysLeft) + " days");

        return row;
    }


}
