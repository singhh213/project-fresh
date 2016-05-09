package edu.uw.singhh17.project_fresh;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
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
import edu.uw.singhh17.project_fresh.Model.PantryData;
import edu.uw.singhh17.project_fresh.Model.RecipeObject;
import edu.uw.singhh17.project_fresh.Model.ShoppingObject;
import edu.uw.singhh17.project_fresh.Utils.Food2ForkClient;

public class Recipe extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RecipeAdapter recipeAdapter;

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
                                    if (p.getString("Name").toLowerCase().contains(query.toLowerCase())) {
                                        recipeAdapter.add(new RecipeObject(p.getString("Name"), p.getString("ImageUrl"),
                                                p.getInt("CookTime"), p.getString("Difficulty"), p.getObjectId()));
                                    }
                                }
                            }
                        }
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        final ArrayList<RecipeObject> recipeData = new ArrayList<RecipeObject>();

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

        gv.setOnItemClickListener(new GridView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
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

        final ImageButton filterButton = (ImageButton) rootView.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getActivity(), filterButton);
                popup.getMenuInflater().inflate(R.menu.recipe_filter_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.alpha_filter:

                                filterRecipes("Name");
                                return true;
                            case R.id.cooktime_filter:

                                filterRecipes("CookTime");
                                return true;
                            case R.id.difficulty_filter:

                                filterRecipes("DifficultyNumber");
                                return true;
//
//                            case R.id.favorites_filter:
//                                return true;

                            case R.id.expiration_filter:
                                getExpiringRecipes();
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });


        return rootView;
    }

    public void filterRecipes(String sortKey) {
        ParseQuery<ParseObject> dQuery = ParseQuery.getQuery("Recipes");
        dQuery.orderByAscending(sortKey);
        dQuery.findInBackground(new FindCallback<ParseObject>() {
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

    private void getExpiringRecipes() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Pantry");
        query.orderByAscending("daysLeft");
//        query.whereLessThan("daysLeft", 4);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    final ArrayList<String> names = new ArrayList<>();
                    if (objects.size() > 0) {


                        for (int i = 0; i < objects.size(); i++) {
                            ParseObject p = objects.get(i);
                            names.add(p.getString("item"));

                        }
                        Log.d("idk", "done: " + names.toString());

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipes");
                        query.findInBackground(new FindCallback<ParseObject>() {

                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    if (objects.size() > 0) {
                                        recipeAdapter.clear();
                                        for (int i = 0; i < objects.size(); i++) {
                                            ParseObject p = objects.get(i);

                                            Map<String, String> ingredients = p.getMap("Ingredients");

                                            String name1 = names.get(0);
                                            String name2 = names.get(1);
                                            String name3 = names.get(2);

                                            Log.d("NAMES", "done: " + name1 + " " + name2 + " " + name3);

                                            if (ingredients.keySet().contains(name1) || ingredients.keySet().contains(name2) || ingredients.keySet().contains(name3)) {
                                                Log.d("RECIPES", "done: " + p.getString("Name"));
                                                recipeAdapter.add(new RecipeObject(p.getString("Name"), p.getString("ImageUrl"),
                                                        p.getInt("CookTime"), p.getString("Difficulty"), p.getObjectId()));
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }

                }
            }
        });
    }
}
