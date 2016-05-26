package edu.uw.singhh17.project_fresh;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
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
import java.util.List;
import java.util.Map;

import edu.uw.singhh17.project_fresh.Adapters.CombineRecipeAdapter;
import edu.uw.singhh17.project_fresh.Model.CombineRecipeObject;
import edu.uw.singhh17.project_fresh.Model.RecipeShoppingObject;


public class CombineRecipeView extends Fragment {

    private OnFragmentInteractionListener mListener;
    private CombineRecipeAdapter crAdapter;
    private AdapterView adapterView;
    ArrayList<CombineRecipeObject> recipeList;
    private String recipesAdded;

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


        final Map<String, CombineRecipeObject> map = new HashMap<>();

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
        if (getArguments() != null) {
            recipesAdded = getArguments().getString("recipesAdded");
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
