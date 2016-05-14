package edu.uw.singhh17.project_fresh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import edu.uw.singhh17.project_fresh.Adapters.IngredientsAdapter;
import edu.uw.singhh17.project_fresh.Adapters.PantryAdapter;
import edu.uw.singhh17.project_fresh.Model.Ingredient;
import edu.uw.singhh17.project_fresh.Model.PantryData;
import edu.uw.singhh17.project_fresh.Model.RecipeObject;
import edu.uw.singhh17.project_fresh.Model.ShoppingObject;
import edu.uw.singhh17.project_fresh.Utils.DownloadImageTask;
import edu.uw.singhh17.project_fresh.Utils.Food2ForkClient;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeDetail.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RecipeDetail extends Fragment {

    private int recipeTime;
    private String recipeDiff;
    private String recipeName;
    private String recipeImgUrl;
    private String recipeId;
    private TextView instructions;
    private ArrayList<String> pantryList;
    private ArrayList<String> shoppingList;
    private OnFragmentInteractionListener mListener;

    public RecipeDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("RECIPE");

        final View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);


        pantryList = getPantryItems();
        shoppingList = getShoppingList();

        TextView name = (TextView) rootView.findViewById(R.id.recipe_dname);
        TextView time = (TextView) rootView.findViewById(R.id.recipe_dtime);
        TextView difficulty = (TextView) rootView.findViewById(R.id.recipe_ddifficulty);
        instructions = (TextView) rootView.findViewById(R.id.recipe_instructions);
        final Button addButton = (Button) rootView.findViewById(R.id.add_ingred_button);

        ImageView img = (ImageView) rootView.findViewById(R.id.recipe_dimage);

        ImageLoader imageLoader = ImageLoader.getInstance();
        MemoryCache mc = imageLoader.getMemoryCache();

        List<Bitmap> list = MemoryCacheUtils.findCachedBitmapsForImageUri(recipeImgUrl, mc);

        if (!list.isEmpty()) {
            Log.d("FOUND", "onCreateView: " + "TRUEEEEE");
            img.setImageBitmap(list.get(0));
        } else {
            Log.d("FOUND", "onCreateView: " + "FALSE");

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).build();
            imageLoader.displayImage(recipeImgUrl, img, options);
        }


//        if (mc.keys().contains(recipeImgUrl)) {
//            Log.d("FOUND", "onCreateView: " + "TRUEEEEE");
//            img.setImageBitmap(mc.get(recipeImgUrl));
//        } else {
//            Log.d("FOUND", "onCreateView: " + "FALSE");
//
//            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).build();
//
//            imageLoader.displayImage(recipeImgUrl, img, options);
//        }


//        new DownloadImageTask(img)
//                .execute(recipeImgUrl);

        name.setText(recipeName);
        time.setText(recipeTime + " min");

        if (recipeDiff.equals("Easy")) {
            difficulty.setTextColor(ContextCompat.getColor(getContext(), R.color.ci_green));
        } else if (recipeDiff.equals("Medium")) {
            difficulty.setTextColor(ContextCompat.getColor(getContext(), R.color.ci_orange));
        } else {
            difficulty.setTextColor(ContextCompat.getColor(getContext(), R.color.ci_red));
        }

        difficulty.setText(recipeDiff);

        final ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

        IngredientsAdapter igAdapter = new IngredientsAdapter(getActivity(), R.layout.row_recipe_ingredients, ingredients);
        final AdapterView igView = (AdapterView) rootView.findViewById(R.id.igList);
        igView.setAdapter(igAdapter);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipes");
        query.getInBackground(recipeId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {

                    List<String> instructs = object.getList("Instructions");
                    Map<String, String> ingred = object.getMap("Ingredients");

                    for (String key : ingred.keySet()) {
                        ingredients.add(new Ingredient(key, ingred.get(key)));
                        ((BaseAdapter) igView.getAdapter()).notifyDataSetChanged();
//                      igAdapter.add(new Ingredient(key, ingred.get(key)));
                    }

                    for (int i = 0; i < instructs.size(); i++ ) {
                        instructions.append((i + 1) + ") " + instructs.get(i));
                        instructions.append("\n");
                        instructions.append("\n");

                    }
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final ArrayList<String> shopNeed  = new ArrayList<String>();
                Log.d("PANTRYLISTEXTRA", "done: " + pantryList.toString());

                for (Ingredient x : ingredients) {
                    String name = x.getName();
                    String amount = x.getAmount();

                    if (!pantryList.contains(name) || !shoppingList.contains(name)) {
                        shopNeed.add(name);
                        Log.d("RECIPE SHOPPING", "onClick: " + name);
                    }
                }

                ParseQuery<ParseObject> query = ParseQuery.getQuery("ShoppingList");
                query.getInBackground("IAf5ywmpFM", new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {

                            Map<String, Boolean> ingred = object.getMap("shoppingList");

                            for (String name : shopNeed) {
                                ingred.put(name, false);
                            }

                            object.put("shoppingList", ingred);
                            object.saveInBackground();
                        }
                    }
                });
                addButton.setText("Added to Shopping List");
                addButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.actionBarGreen));
                addButton.setTextColor(Color.WHITE);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            recipeTime = getArguments().getInt("recipeTime");
            recipeDiff = getArguments().getString("recipeDiff");
            recipeName = getArguments().getString("recipeName");
            recipeImgUrl = getArguments().getString("recipeImg");
            recipeId = getArguments().getString("recipeId");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
    }

    private ArrayList<String> getPantryItems() {
        final ArrayList<String> pantry = new ArrayList<>();
        ParseQuery<ParseObject> pQuery = ParseQuery.getQuery("Pantry");
        pQuery.orderByAscending("daysLeft");
        pQuery.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {

                        for (int i = 0; i < objects.size(); i++) {
                            ParseObject p = objects.get(i);
                            pantry.add(p.getString("item"));
                            Log.d("PANTRYLIST", "done: " + p.getString("item"));

                        }
                    }

                }
            }
        });
        return pantry;
    }

    private ArrayList<String> getShoppingList() {

        final ArrayList<String> shopping = new ArrayList<>();

        ParseQuery<ParseObject> sQuery = ParseQuery.getQuery("ShoppingList");
        sQuery.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (int i = 0; i < objects.size(); i++) {
                            ParseObject p = objects.get(i);

                            for (String name : p.getMap("shoppingList").keySet()) {
                                shopping.add(name);
                                Log.d("ShoppingList", "done: " + name);
                            }
                        }
                    }
                }
            }
        });

        return shopping;
    }
}
