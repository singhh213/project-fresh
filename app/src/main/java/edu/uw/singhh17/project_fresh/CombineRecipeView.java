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
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.uw.singhh17.project_fresh.Adapters.CombineRecipeAdapter;
import edu.uw.singhh17.project_fresh.Model.CombineRecipeObject;
import edu.uw.singhh17.project_fresh.Model.RecipeShoppingObject;
import edu.uw.singhh17.project_fresh.Model.ShoppingObject;


public class CombineRecipeView extends Fragment {

    private OnFragmentInteractionListener mListener;
    private CombineRecipeAdapter crAdapter;
    private AdapterView adapterView;
    ArrayList<CombineRecipeObject> recipeList;
    private String recipesAdded;
    private Map<String, CombineRecipeObject> map;

    public CombineRecipeView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View rootView = inflater.inflate(R.layout.fragment_combine_recipe_view, container, false);

        recipeList = new ArrayList<>();

        TextView recipeCount = (TextView) rootView.findViewById(R.id.r_count);
        Button separateView = (Button) rootView.findViewById(R.id.separateViewButton);

        recipeCount.setText(recipesAdded);

        crAdapter = new CombineRecipeAdapter(getActivity(), R.layout.row_combine_recipe, recipeList);
        adapterView = (AdapterView) rootView.findViewById(R.id.listView3);
        adapterView.setAdapter(crAdapter);


        map = new HashMap<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("RSList");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {

                        for (int i = 0; i < objects.size(); i++) {

                            ParseObject p = objects.get(i);
                            String name = p.getString("name");
                            if(!p.getString("metric").equals("-1")){
                                if(!map.containsKey(name)) {
                                    CombineRecipeObject cr = new CombineRecipeObject(name, false, p.getDouble("rawAmount"), p.getString("metric"));
                                    map.put(name, cr);
                                } else {
                                    map.get(name).amount += p.getDouble("rawAmount");
                                }
                            }

                        }

                        for (String key : map.keySet()) {

                            recipeList.add(new CombineRecipeObject(map.get(key).name, map.get(key).striked, map.get(key).amount, map.get(key).metric));

                            ((BaseAdapter) adapterView.getAdapter()).notifyDataSetChanged();
                        }
                    }

                }
            }
        });

        separateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.shopContainer, new RecipeShoppingList());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        adapterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView item = (TextView) view.findViewById(R.id.combine_name);

                String food = item.getText().toString();
//                Boolean striked = map.get(food).striked;
                Boolean striked = recipeList.get(position).striked;


                if (!striked) { //check if boolean for strikethrough for item is true or false to remove strikethrough
                    item.setPaintFlags(item.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                    pantryShopping.put(food, true);
//                    map.get(food).striked = true;
                    recipeList.get(position).striked = true;

//                    shopList.get(position).striked = true;

                } else {
                    item.setPaintFlags(item.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
//                    map.get(food).striked = false;
                    recipeList.get(position).striked = false;

//                    pantryShopping.put(food, false);
//                    shopList.get(position).striked = false;
                }

//                saveShoppingList();
//                ((BaseAdapter) adapterView.getAdapter()).notifyDataSetChanged();
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            recipesAdded = getArguments().getString("recipesAdded");
        }
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

                ArrayList<CombineRecipeObject> dupShopList = new ArrayList<>();


                for (CombineRecipeObject rs : recipeList) {
                    dupShopList.add(rs);
                }

                for (CombineRecipeObject obj : dupShopList) {

                    if (obj.striked) {
                        recipeList.remove(obj);
                    }
                }


                ((BaseAdapter) adapterView.getAdapter()).notifyDataSetChanged();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        menu.clear();
//        inflater.inflate(R.menu.recipe_shopping_menu, menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.shopping_delete:
//
////                Iterator it = pantryShopping.entrySet().iterator();
////                while (it.hasNext()) {
////                    Map.Entry x = (Map.Entry) it.next();
////                    if ((Boolean) x.getValue()) {
////                        Log.d("test", "onOptionsItemSelected: " + "remove meeeeee");
////                        ShoppingObject delete = new ShoppingObject((String) x.getKey(), true);
////                        shopList.remove(delete);
////
////                        Log.d("TEST ", "onOptionsItemSelected: " + shopList.toString());
////                        it.remove();
////                    }
////                }
////
////                saveShoppingList();
////
////                ((BaseAdapter) adapterView.getAdapter()).notifyDataSetChanged();
//
//                return true;
//        }
//        return true;
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
    }
}
