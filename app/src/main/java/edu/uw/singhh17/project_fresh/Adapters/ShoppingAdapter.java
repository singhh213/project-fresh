package edu.uw.singhh17.project_fresh.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.uw.singhh17.project_fresh.Model.ShoppingObject;
import edu.uw.singhh17.project_fresh.R;

/**
 * Created by harpreetsingh on 4/23/16.
 */
public class ShoppingAdapter extends ArrayAdapter<ShoppingObject> {

    private Context context;
    private int resource;
    private List<ShoppingObject> objects;

    public ShoppingAdapter(Context context, int resource, List<ShoppingObject> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View row = inflater.inflate(resource, parent, false);

        TextView item = (TextView) row.findViewById(R.id.shopItem);
        item.setText(objects.get(position).name);

        if (objects.get(position).striked) {
            item.setPaintFlags(item.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            item.setPaintFlags(item.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }


        return row;

    }
}
