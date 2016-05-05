package edu.uw.singhh17.project_fresh;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cz.msebera.android.httpclient.Header;
import edu.uw.singhh17.project_fresh.Adapters.RecipeAdapter;
import edu.uw.singhh17.project_fresh.Model.RecipeObject;
import edu.uw.singhh17.project_fresh.Model.ShoppingObject;
import edu.uw.singhh17.project_fresh.Utils.Food2ForkClient;

public class Recipe extends Fragment {

    private OnFragmentInteractionListener mListener;
    private final String API_GET_URL = "http://food2fork.com/api/get?key=5253bcb47c6a8f4bbe9624f2388bc2e4&rId=";
    private final String API_SEARCH_URL = "http://food2fork.com/api/search?key=5253bcb47c6a8f4bbe9624f2388bc2e4&q=";
    private Food2ForkClient client;
    private RecipeAdapter recipeAdapter;
    private Random timeR;
    private Random diffR;
    private final String[] difficulty = {"Easy", "Medium", "Hard"};


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
        getActivity().setTitle("RECIPES");

        final View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        android.widget.SearchView search = (android.widget.SearchView) rootView.findViewById(R.id.searchView);

        search.setSubmitButtonEnabled(true);

        search.setOnCloseListener(new android.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipes");
                query.findInBackground(new FindCallback<ParseObject>() {

                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            if (objects.size() > 0) {
                                recipeAdapter.clear();
                                for (int i = 0; i < objects.size(); i++) {
                                    ParseObject p = objects.get(i);

                                    recipeAdapter.add(new RecipeObject(p.getString("Name"), p.getString("ImageUrl"),
                                            p.getInt("CookTime"), p.getString("Difficulty"), p.getObjectId()));


                                }
                            }

                        }

                    }
                });
                return false;
            }
        });


        search.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {


                ParseQuery<ParseObject> q = ParseQuery.getQuery("Recipes");
                q.findInBackground(new FindCallback<ParseObject>() {

                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            if (objects.size() > 0) {
                                recipeAdapter.clear();
                                for (int i = 0; i < objects.size(); i++) {
                                    ParseObject p = objects.get(i);
//                            Map<String, String> ingredients = p.getMap("Ingredients");
//                            List<String> instructions = p.getList("Instructions");
                                    if (p.getString("Name").toLowerCase().contains(query.toLowerCase())) {
                                        recipeAdapter.add(new RecipeObject(p.getString("Name"), p.getString("ImageUrl"),
                                                p.getInt("CookTime"), p.getString("Difficulty"), p.getObjectId()));
                                    }
                                }
                            }

                        }

                    }
                });

//                fetchRecipes(API_SEARCH_URL + query);
//                Toast.makeText(getContext(), "Our word : " + query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        timeR = new Random();
        diffR = new Random();

        final ArrayList<RecipeObject> recipeData = new ArrayList<RecipeObject>();


//        String[] names = {"Toast", "Mac & Cheese", "Pizza", "Cake", "Alfredo Pasta"};
//        int[] images = {R.drawable.ic_home_button, R.drawable.ic_recipe_button, R.drawable.ic_settings_button, R.drawable.ic_scan_button, R.drawable.ic_shopping_list};

        recipeAdapter = new RecipeAdapter(getActivity(), R.layout.row_recipe, recipeData);
        GridView gv = (GridView) rootView.findViewById(R.id.gridView);
        gv.setAdapter(recipeAdapter);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipes");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        recipeAdapter.clear();
                        for (int i = 0; i < objects.size(); i++) {
                            ParseObject p = objects.get(i);

                            recipeAdapter.add(new RecipeObject(p.getString("Name"), p.getString("ImageUrl"),
                                    p.getInt("CookTime"), p.getString("Difficulty"), p.getObjectId()));


                        }
                    }

                }

            }
        });

//        fetchRecipes(API_SEARCH_URL);

        gv.setOnItemClickListener(new GridView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("recipeUrl", API_GET_URL + recipeData.get(position).getRecipeId());
                bundle.putString("recipeId", recipeData.get(position).getRecipeId());
                bundle.putInt("recipeTime", recipeData.get(position).getTime());
                bundle.putString("recipeDiff", recipeData.get(position).getDifficulty());
                bundle.putString("recipeName", recipeData.get(position).getName());
                bundle.putString("recipeImg", recipeData.get(position).getImgUrl());
                RecipeDetail recipeDetail = new RecipeDetail();
                recipeDetail.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.container1, recipeDetail);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return rootView;
    }

//    public void fetchRecipes(String link) {
//        client = new Food2ForkClient();
//        client.getRecept(new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                JSONArray items = null;
//                try {
//                    items = response.getJSONArray("recipes");
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
//                    Log.d("RECIPES TEST", "onSuccess: " + items.toString());
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, link);
//
//    }

    private int timeRandomizer() {
        int x = timeR.nextInt(75 - 10) + 10;
        return x % 5 * 5 + 10;
    }

    private String difficultyRandomizer() {
        int x = diffR.nextInt(3);
        return difficulty[x];
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
