package edu.uw.singhh17.project_fresh;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import edu.uw.singhh17.project_fresh.Model.RecipeObject;
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
    private TextView website;
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

        name = (TextView) rootView.findViewById(R.id.recipe_dname);
        time = (TextView) rootView.findViewById(R.id.recipe_dtime);
        difficulty = (TextView) rootView.findViewById(R.id.recipe_ddifficulty);
        website = (TextView) rootView.findViewById(R.id.recipe_website);

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

        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();


        igAdapter = new IngredientsAdapter(getActivity(), R.layout.row_recipe_ingredients, ingredients);
        AdapterView igView = (AdapterView) rootView.findViewById(R.id.igList);
        igView.setAdapter(igAdapter);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipes");
        query.getInBackground(recipeId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
//                    if (objects.size() > 0) {

//                        for (int i = 0; i < objects.size(); i++) {
                            ParseObject p = object;

                            List<String> instructions = p.getList("Instructions");
                            Map<String, String> ingred = p.getMap("Ingredients");

                            for (String key : ingred.keySet()) {
                                igAdapter.add(new Ingredient(key, ingred.get(key)));

                            }

                            for (int i = 0; i < instructions.size(); i++ ) {
                                website.append((i + 1) + ") " + instructions.get(i));
                                website.append("\n");
                                website.append("\n");

                            }

//                            recipeAdapter.add(new RecipeObject(p.getString("Name"), p.getString("ImageUrl"),
//                                    p.getInt("CookTime"), p.getString("Difficulty"), "12321"));


//                        }
//                    }

                }

            }
        });
//        query.findInBackground(new FindCallback<ParseObject>() {
//
//            @Override
//            public void done(List<ParseObject> objects, ParseException e) {
//                if (e == null) {
//                    if (objects.size() > 0) {
//
//                        for (int i = 0; i < objects.size(); i++) {
//                            ParseObject p = objects.get(i);
//
//                            List<String> instructions = p.getList("Instructions");
//                            Map<String, String> ingred = p.getMap("Ingredients");
//
//                            for (String key : ingred.keySet()) {
//                                igAdapter.add(new Ingredient(key, ingred.get(key)));
//
//                            }
//                            website.setText(instructions.toString());
//
////                            recipeAdapter.add(new RecipeObject(p.getString("Name"), p.getString("ImageUrl"),
////                                    p.getInt("CookTime"), p.getString("Difficulty"), "12321"));
//
//
//                        }
//                    }
//
//                }
//
//            }
//        });

//        fetchRecipes(recipeUrl);

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

    public void fetchRecipes(String link) {
        Food2ForkClient client = new Food2ForkClient();
        client.getRecept(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject item = response.getJSONObject("recipe");

                    JSONArray ingredients = item.getJSONArray("ingredients");

                    Log.d("RECIPE DETAIL", "onSuccess: " + ingredients.toString());


                    for (int i = 0; i < ingredients.length(); i++) {
                        igAdapter.add(new Ingredient(ingredients.get(i).toString(), ""));



                    }
                    website.setText(item.getString("source_url"));




//                    recipeAdapter.clear();
//                    for (int i = 0; i < items.length(); i++) {
//
//                        String name = items.getJSONObject(i).getString("title");
//                        String imgUrl = items.getJSONObject(i).getString("image_url");
//                        String recipeId = items.getJSONObject(i).getString("recipe_id");
//
//                        recipeAdapter.add(new RecipeObject(name, imgUrl, timeRandomizer(), difficultyRandomizer(), recipeId));
//
//                    }

                    Log.d("RECIPES TEST", "onSuccess: " + item.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, recipeUrl);

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
