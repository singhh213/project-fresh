package edu.uw.singhh17.project_fresh;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by harpreetsingh on 4/9/16.
 */
public class PantryAdapter extends ArrayAdapter<PantryData> {

    private Context context;
    private int resource;
    private PantryData[] objects;

    public PantryAdapter(Context context, int resource, PantryData[] objects) {
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

        itemName.setText(objects[position].name);
        expInfo.setText(Integer.toString(objects[position].daysLeft));
        return row;
    }


}
