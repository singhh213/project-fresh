package edu.uw.singhh17.project_fresh;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.singhh17.project_fresh.Adapters.RecipeShoppingAdapter;
import edu.uw.singhh17.project_fresh.Adapters.ShoppingAdapter;
import edu.uw.singhh17.project_fresh.Model.CombineRecipeObject;
import edu.uw.singhh17.project_fresh.Model.RecipeShoppingObject;
import edu.uw.singhh17.project_fresh.Model.ShoppingObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeShoppingList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RecipeShoppingList extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ArrayList<RecipeShoppingObject> shopList;
    private RecipeShoppingAdapter sAdapter;
    private AdapterView adapterView;
    private Map<String, Boolean> pantryShopping;


    public RecipeShoppingList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        pantryShopping = new HashMap<>();

        shopList = new ArrayList<RecipeShoppingObject>();

        final int[] recipeCount = {0};

        final View rootView = inflater.inflate(R.layout.fragment_recipe_shopping_list, container, false);

        sAdapter = new RecipeShoppingAdapter(getActivity(), R.layout.row_recipe_shopping_list, shopList);
        adapterView = (AdapterView)rootView.findViewById(R.id.listView2);
        adapterView.setAdapter(sAdapter);
        final TextView rcDisplay = (TextView) rootView.findViewById(R.id.recipe_count);
        Button combine = (Button) rootView.findViewById(R.id.combineViewButton);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("RSList");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {

                        for (int i = 0; i < objects.size(); i++) {
                            ParseObject p = objects.get(i);

                            if (p.getString("metric").equals("-1")) {
                                recipeCount[0]++;
                            }
                            shopList.add(new RecipeShoppingObject(p.getString("name"), p.getBoolean("striked"), p.getDouble("rawAmount"), p.getString("metric")));

                            ((BaseAdapter) adapterView.getAdapter()).notifyDataSetChanged();

                        }
                    }
                    rcDisplay.setText(Integer.toString(recipeCount[0]) + " Recipes Added");

                }
            }
        });


        adapterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView item = (TextView) view.findViewById(R.id.recipeShopName);

                String food = item.getText().toString();
                Boolean striked = shopList.get(position).striked;
                String metric = shopList.get(position).metric;

                if(!metric.equals("-1")) {
                    if (!striked) { //check if boolean for strikethrough for item is true or false to remove strikethrough
                        item.setPaintFlags(item.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                    pantryShopping.put(food, true);
                        shopList.get(position).striked = true;
//                    shopList.get(position).striked = true;

                    } else {
                        item.setPaintFlags(item.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        shopList.get(position).striked = false;
//                    pantryShopping.put(food, false);
//                    shopList.get(position).striked = false;
                    }
                }
            }
        });


        combine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("recipesAdded", recipeCount[0] + " Recipes Added");
                CombineRecipeView crv = new CombineRecipeView();
                crv.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.shopContainer, crv);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return rootView;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.recipe_shopping_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.recipe_shopping_delete:


                ArrayList<RecipeShoppingObject> dupShopList = new ArrayList<>();

                for (RecipeShoppingObject rs : shopList) {
                    dupShopList.add(rs);
                }

                for (RecipeShoppingObject obj : dupShopList) {

                    if (obj.striked) {
                        shopList.remove(obj);
                    }
                }

                ((BaseAdapter) adapterView.getAdapter()).notifyDataSetChanged();

                return true;
        }
        return super.onOptionsItemSelected(item);

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
