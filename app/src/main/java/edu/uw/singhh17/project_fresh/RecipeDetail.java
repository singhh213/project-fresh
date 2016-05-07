package edu.uw.singhh17.project_fresh;

import android.content.Context;
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

    private String recipeUrl;
    private int recipeTime;
    private String recipeDiff;
    private String recipeName;
    private String recipeImgUrl;
    private String recipeId;
    private TextView name;
    private TextView time;
    private TextView difficulty;
    private TextView instructions;
    private IngredientsAdapter igAdapter;
    private OnFragmentInteractionListener mListener;

    public RecipeDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        getActivity().setTitle("RECIPE");

        final View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        final ArrayList<String> pantryList = new ArrayList<>();
        final ArrayList<String> shoppingList = new ArrayList<>();

        ParseQuery<ParseObject> pQuery = ParseQuery.getQuery("Pantry");
        pQuery.orderByAscending("daysLeft");
        pQuery.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {

                        for (int i = 0; i < objects.size(); i++) {
                            ParseObject p = objects.get(i);
                            pantryList.add(p.getString("item"));
                            Log.d("PANTRYLIST", "done: " + p.getString("item"));

                        }
                    }

                }
            }
        });

        ParseQuery<ParseObject> sQuery = ParseQuery.getQuery("ShoppingList");
        sQuery.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {

                        for (int i = 0; i < objects.size(); i++) {
                            ParseObject p = objects.get(i);

                            for (String name : p.getMap("shoppingList").keySet()) {
                                shoppingList.add(name);
                                Log.d("ShoppingList", "done: " + name);
                            }



                        }
                    }

                }

            }
        });

        name = (TextView) rootView.findViewById(R.id.recipe_dname);
        time = (TextView) rootView.findViewById(R.id.recipe_dtime);
        difficulty = (TextView) rootView.findViewById(R.id.recipe_ddifficulty);
        instructions = (TextView) rootView.findViewById(R.id.recipe_instructions);
        Button addButton = (Button) rootView.findViewById(R.id.add_ingred_button);


        ImageView img = (ImageView) rootView.findViewById(R.id.recipe_dimage);


        new DownloadImageTask(img)
                .execute(recipeImgUrl);

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

        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final ArrayList<String> shopNeed  = new ArrayList<String>();
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

                            ParseObject p = object;
                            Map<String, Boolean> ingred = p.getMap("shoppingList");

                            for (String key : ingred.keySet()) {
                                Log.d("REAL MAP", "done: " + ingred);
                            }

                            for (String name : shopNeed) {
                                ingred.put(name, false);
                            }

                            object.put("shoppingList", ingred);
                            object.saveInBackground();

                        }

                    }
                });

            }
        });



        igAdapter = new IngredientsAdapter(getActivity(), R.layout.row_recipe_ingredients, ingredients);
        final AdapterView igView = (AdapterView) rootView.findViewById(R.id.igList);
        igView.setAdapter(igAdapter);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipes");
        query.getInBackground(recipeId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {

                    ParseObject p = object;

                    List<String> instructs = p.getList("Instructions");
                    Map<String, String> ingred = p.getMap("Ingredients");

                    for (String key : ingred.keySet()) {
                        ingredients.add(new Ingredient(key, ingred.get(key)));
                        ((BaseAdapter) igView.getAdapter()).notifyDataSetChanged();

//                                igAdapter.add(new Ingredient(key, ingred.get(key)));

                    }

                    for (int i = 0; i < instructs.size(); i++ ) {
                        instructions.append((i + 1) + ") " + instructs.get(i));
                        instructions.append("\n");
                        instructions.append("\n");

                    }
                }

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
            recipeUrl = getArguments().getString("recipeUrl");
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

    /**
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
//         TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
    }


}
