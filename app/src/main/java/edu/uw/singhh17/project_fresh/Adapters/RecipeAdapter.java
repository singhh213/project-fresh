package edu.uw.singhh17.project_fresh.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import edu.uw.singhh17.project_fresh.R;
import edu.uw.singhh17.project_fresh.Model.RecipeObject;

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

        new DownloadImageTask(img)
                .execute(objects.get(position).getImgUrl());

        name.setText(objects.get(position).getName());

        time.setText(objects.get(position).getTime() + " min");

        difficulty.setText(objects.get(position).getDifficulty());


        return row;
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
