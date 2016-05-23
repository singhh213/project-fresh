package edu.uw.singhh17.project_fresh.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
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

import java.io.InputStream;
import java.util.List;

import edu.uw.singhh17.project_fresh.R;
import edu.uw.singhh17.project_fresh.Model.RecipeObject;
import edu.uw.singhh17.project_fresh.Utils.DownloadImageTask;

/**
 * Created by harpreetsingh on 4/30/16.
 */
public class RecipeAdapter extends ArrayAdapter<RecipeObject>{


    private Context context;
    private int resource;
    private List<RecipeObject> objects;


//    public RecipeAdapter(Context context, String[] names, int[] images) {
//        this.names = names;
//        this.images = images;
//        this.context = context;
//    }

    public RecipeAdapter(Context context, int resource, List<RecipeObject> objects) {

        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

//        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
//                Activity.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.row_recipe, null);

        TextView name = (TextView) row.findViewById(R.id.recipe_title);
        ImageView img = (ImageView) row.findViewById(R.id.recipe_img);
        TextView time = (TextView) row.findViewById(R.id.recipe_time);
        TextView difficulty = (TextView) row.findViewById(R.id.recipe_difficulty);

        String imageUrl = objects.get(position).getImgUrl();

        ImageLoader imageLoader = ImageLoader.getInstance();
        MemoryCache mc = imageLoader.getMemoryCache();
        List<Bitmap> list = MemoryCacheUtils.findCachedBitmapsForImageUri(imageUrl, mc);

        if (!list.isEmpty()) {
            img.setImageBitmap(list.get(0));
        } else {
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).build();
            imageLoader.displayImage(imageUrl, img, options);
        }



//        new DownloadImageTask(img)
//                .execute(objects.get(position).getImgUrl());

        name.setText(objects.get(position).getName());

        time.setText(objects.get(position).getTime() + " min");

        String diff = objects.get(position).getDifficulty();

        if (diff.equals("Easy")) {
            difficulty.setTextColor(ContextCompat.getColor(context, R.color.ci_green));
        } else if (diff.equals("Medium")) {
            difficulty.setTextColor(ContextCompat.getColor(context, R.color.ci_orange));
        } else {
            difficulty.setTextColor(ContextCompat.getColor(context, R.color.ci_red));
        }

        difficulty.setText(diff);

        return row;
    }
}
