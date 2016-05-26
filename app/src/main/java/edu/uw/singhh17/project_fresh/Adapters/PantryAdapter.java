package edu.uw.singhh17.project_fresh.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.uw.singhh17.project_fresh.Model.PantryData;
import edu.uw.singhh17.project_fresh.R;
import edu.uw.singhh17.project_fresh.Utils.DownloadImageTask;

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
        CircleImageView itemImg = (CircleImageView) row.findViewById(R.id.itemImage);
        GradientDrawable bgShape = (GradientDrawable)indicator.getBackground();

        Log.d("PANTRY", "getView: " + objects.get(position).name);

        itemName.setText(objects.get(position).name);
        int daysLeft = objects.get(position).daysLeft;

        String imageUrl = objects.get(position).imageUrl;


        ImageLoader imageLoader = ImageLoader.getInstance();
        MemoryCache mc = imageLoader.getMemoryCache();
        List<Bitmap> list = MemoryCacheUtils.findCachedBitmapsForImageUri(imageUrl, mc);


        if (!list.isEmpty()) {
            itemImg.setImageBitmap(list.get(0));
        } else {

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).build();
            imageLoader.displayImage(imageUrl, itemImg, options);
        }



//        new DownloadImageTask(itemImg)
//                .execute(objects.get(position).imageUrl);

//        Log.d("TEST days left", "getView: " + daysLeft);

        if (daysLeft > 7) {
            bgShape.setColor(ContextCompat.getColor(context, R.color.ci_green));
        } else if (daysLeft > 0) {
            bgShape.setColor(ContextCompat.getColor(context, R.color.ci_yellow));
//        } else if (daysLeft > 0){
//            bgShape.setColor(ContextCompat.getColor(context, R.color.ci_orange));
        } else {
            bgShape.setColor(ContextCompat.getColor(context, R.color.ci_red));
        }

        expInfo.setText("Expires in " + Integer.toString(daysLeft) + " days");

        if (daysLeft > 0) {
            expInfo.setText("Expires in " + Integer.toString(daysLeft) + " days");
        } else {
            expInfo.setText("Expired");
        }

        return row;
    }


}
