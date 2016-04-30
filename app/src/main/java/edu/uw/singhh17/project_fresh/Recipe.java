package edu.uw.singhh17.project_fresh;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import cz.msebera.android.httpclient.Header;
import edu.uw.singhh17.project_fresh.Adapters.RecipeAdapter;
import edu.uw.singhh17.project_fresh.Model.RecipeObject;
import edu.uw.singhh17.project_fresh.Utils.Food2ForkClient;

public class Recipe extends Fragment {

    private OnFragmentInteractionListener mListener;
    private String recipesSearch = "http://food2fork.com/api/get?key=5253bcb47c6a8f4bbe9624f2388bc2e4&rId=";
    private final String API_BASE_URL = "http://food2fork.com/api/search?key=5253bcb47c6a8f4bbe9624f2388bc2e4";
    private Food2ForkClient client;
    private RecipeAdapter recipeAdapter;
    private Random timeR;


    public Recipe() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("RECIPE");

        final View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        timeR = new Random();

        final ArrayList<RecipeObject> recipeData = new ArrayList<RecipeObject>();


//        String[] names = {"Toast", "Mac & Cheese", "Pizza", "Cake", "Alfredo Pasta"};
//        int[] images = {R.drawable.ic_home_button, R.drawable.ic_recipe_button, R.drawable.ic_settings_button, R.drawable.ic_scan_button, R.drawable.ic_shopping_list};

        recipeAdapter = new RecipeAdapter(getActivity(), R.layout.row_recipe, recipeData);
        GridView gv = (GridView) rootView.findViewById(R.id.gridView);
        gv.setAdapter(recipeAdapter);

        fetchRecipes(API_BASE_URL);

        return rootView;
    }

    public void fetchRecipes(String link) {
        client = new Food2ForkClient();
        client.getRecept(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray items = null;
                try {
                    items = response.getJSONArray("recipes");

                    for (int i = 0; i < items.length(); i++) {

                        String name = items.getJSONObject(i).getString("title");
                        String imgUrl = items.getJSONObject(i).getString("image_url");

                        recipeAdapter.add(new RecipeObject(name, imgUrl, timeRandomizer(), difficultyRandomizer()));

                    }
                    Log.d("RECIPES TEST", "onSuccess: " + items.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, link);

    }

    private int timeRandomizer() {
        int x = timeR.nextInt(65 - 5) + 5;
        return x % 5 * 5 + 5;
    }

    private String difficultyRandomizer() {
        return "Easy";
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

    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
    }
}
