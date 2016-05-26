package edu.uw.singhh17.project_fresh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.uw.singhh17.project_fresh.Adapters.RecipeAdapter;
import edu.uw.singhh17.project_fresh.Model.RecipeObject;
import edu.uw.singhh17.project_fresh.Utils.DownloadImageTask;


public class ItemInfo extends Fragment {

    private String name;
    private int expireInfo;
    private String imageUrl;
    private String nutritionUrl;
    private String quantity;
    private String itemType;
    private OnFragmentInteractionListener mListener;

    public ItemInfo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("ITEM");
        if (getArguments() != null) {
            name = getArguments().getString("name");
            expireInfo = getArguments().getInt("expireInfo");
            imageUrl = getArguments().getString("imageUrl");
            nutritionUrl = getArguments().getString("nutritionUrl");
            quantity = getArguments().getString("quantity");
            itemType = getArguments().getString("itemType");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View rootView = inflater.inflate(R.layout.fragment_item_info, container, false);

        TextView itemName = (TextView) rootView.findViewById(R.id.itemName);
        TextView expInfo = (TextView) rootView.findViewById(R.id.expireInfo);
        ImageView itemImg = (ImageView) rootView.findViewById(R.id.itemImage);
        ImageView nutritionLabel = (ImageView) rootView.findViewById(R.id.nutrtionLabel);
        TextView itemQuantity = (TextView) rootView.findViewById(R.id.itemAmount);
        final Button addItem = (Button) rootView.findViewById(R.id.iteminfo_add_button);

        ImageView indicator = (ImageView) rootView.findViewById(R.id.colorIndicator);
        GradientDrawable bgShape = (GradientDrawable)indicator.getBackground();

        itemName.setText(name);
        itemQuantity.setText(quantity);



        ImageLoader imageLoader = ImageLoader.getInstance();
        MemoryCache mc = imageLoader.getMemoryCache();
        List<Bitmap> list = MemoryCacheUtils.findCachedBitmapsForImageUri(imageUrl, mc);

        if (!list.isEmpty()) {
            itemImg.setImageBitmap(list.get(0));
        } else {
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).build();
            imageLoader.displayImage(imageUrl, itemImg, options);
        }

        if (name.contains("French Bread")) {
            nutritionLabel.setImageResource(R.drawable.french_bread);
        } else if (name.contains("Chicken")) {
            nutritionLabel.setImageResource(R.drawable.chicken);
        } else if (name.contains("Eggs")) {
            nutritionLabel.setImageResource(R.drawable.eggs);
        } else if (name.contains("Flour")) {
            nutritionLabel.setImageResource(R.drawable.flour);
        } else if (name.contains("Yogurt")) {
            nutritionLabel.setImageResource(R.drawable.yogurt);
        } else if (name.contains("Cheese")) {
            nutritionLabel.setImageResource(R.drawable.cheese);
        } else if (name.contains("Milk")) {
            nutritionLabel.setImageResource(R.drawable.milk);
        } else if (name.contains("Wine")) {
            nutritionLabel.setImageResource(R.drawable.wine);
        } else if (name.contains("Olive Oil")) {
            nutritionLabel.setImageResource(R.drawable.oliveoil);
        } else if (name.contains("Syrup")) {
            nutritionLabel.setImageResource(R.drawable.hersheysyrup);
        } else if (name.contains("Bread")) {
            nutritionLabel.setImageResource(R.drawable.bread);
        } else if (name.contains("Strawberr")) {
            nutritionLabel.setImageResource(R.drawable.strawberries);
        } else {
            List<Bitmap> list2 = MemoryCacheUtils.findCachedBitmapsForImageUri(nutritionUrl, mc);

            if (!list2.isEmpty()) {
                Log.d("FOUND", "onCreateView: " + "TRUEEEEE");
                nutritionLabel.setImageBitmap(list2.get(0));
            } else {
                Log.d("FOUND", "onCreateView: " + "FALSE");

                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).build();
                imageLoader.displayImage(nutritionUrl, nutritionLabel, options);
            }
        }


//        new DownloadImageTask(itemImg)
//                .execute(imageUrl);
//
//        new DownloadImageTask(nutritionLabel)
//                .execute(nutritionUrl);

        if (expireInfo > 7) {
            Log.d("TEST days left", "getView: " + expireInfo);
            bgShape.setColor(ContextCompat.getColor(getContext(), R.color.ci_green));
        } else if (expireInfo > 3) {
            bgShape.setColor(ContextCompat.getColor(getContext(), R.color.ci_yellow));
        } else if (expireInfo > 0){
            bgShape.setColor(ContextCompat.getColor(getContext(), R.color.ci_orange));
        } else {
            bgShape.setColor(ContextCompat.getColor(getContext(), R.color.ci_red));
        }

        if (expireInfo > 0) {
            expInfo.setText("Expires in " + Integer.toString(expireInfo) + " days");
        } else {
            expInfo.setText("Expired");
        }


        final ArrayList<RecipeObject> recipeData = new ArrayList<RecipeObject>();

        final RecipeAdapter recipeAdapter = new RecipeAdapter(getActivity(), R.layout.row_recipe, recipeData);
        GridView gv = (GridView) rootView.findViewById(R.id.item_info_recipes);
        gv.setFocusable(false);

        gv.setAdapter(recipeAdapter);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipes");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        recipeAdapter.clear();
                        int count = 0;
                        for (int i = 0; i < objects.size(); i++) {
                            ParseObject p = objects.get(i);
                            Log.d("DONE", "done: " + "adding items");


                            if (p.getMap("Ingredients").keySet().contains(itemType) && count < 2) {
                                Log.d("DONE", "done: " + "adding items");
                                count++;
                                recipeAdapter.add(new RecipeObject(p.getString("Name"), p.getString("ImageUrl"),
                                        p.getInt("CookTime"), p.getString("Difficulty"), p.getString("ServingsCals"), p.getObjectId()));


                            }
                        }
                    }
                }
            }
        });


        gv.setOnItemClickListener(new GridView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("recipeId", recipeData.get(position).getRecipeId());
                bundle.putInt("recipeTime", recipeData.get(position).getTime());
                bundle.putString("recipeDiff", recipeData.get(position).getDifficulty());
                bundle.putString("recipeName", recipeData.get(position).getName());
                bundle.putString("recipeImg", recipeData.get(position).getImgUrl());
                bundle.putString("servingSize", recipeData.get(position).getServingsCals());
                RecipeDetail recipeDetail = new RecipeDetail();
                recipeDetail.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.container1, recipeDetail);
                ft.addToBackStack(null);
                ft.commit();
            }
        });










        addItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("ShoppingList");
                query.getInBackground("IAf5ywmpFM", new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {

                            Map<String, Boolean> shoppingList = object.getMap("shoppingList");
                            shoppingList.put(name, false);

                            object.put("shoppingList", shoppingList);
                            object.saveInBackground();
                            addItem.setText("Added to Shopping List");
                            addItem.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.actionBarGreen));
                            addItem.setTextColor(Color.WHITE);
                        }
                    }
                });
            }
        });

        return rootView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("ITEM");
    }

    /**
     *
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

    }
}
