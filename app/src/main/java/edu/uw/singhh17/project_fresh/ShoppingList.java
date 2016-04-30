package edu.uw.singhh17.project_fresh;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.singhh17.project_fresh.R;

public class ShoppingList extends Fragment {


    private OnFragmentInteractionListener mListener;
    private Map<String, Boolean> pantryShopping;

    public ShoppingList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {

        }
        getActivity().setTitle("SHOPPING LIST");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        pantryShopping.put("Cheddar Cheese", false);
//        pantryShopping.put("Milk", false);
//        pantryShopping.put("Pizza", false);
//        pantryShopping.put("Corn", false);
//        pantryShopping.put("Avocado", false);
//
//        ParseObject newObject = new ParseObject("ShoppingList");
//        newObject.put("user", "Harpreet");
//        newObject.put("shoppingList", pantryShopping);
//        newObject.saveInBackground();

        //keyset (item names) of map turned into string array for list view adapter
        pantryShopping = new HashMap<>();

        final ArrayList<String> shopList = new ArrayList<String>();

        final View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.row_shopping_list, R.id.shopItem, shopList);
        AdapterView adapterView = (AdapterView) rootView.findViewById(R.id.shopList);
        adapterView.setAdapter(adapter);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ShoppingList");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {

                        for (int i = 0; i < objects.size(); i++) {
                            ParseObject p = objects.get(i);

                            pantryShopping = p.getMap("shoppingList");

                            ListView list = (ListView) rootView.findViewById(R.id.shopList);


                            for (String key : pantryShopping.keySet()) {
                                adapter.add(key);







//                                if (pantryShopping.get(key)) {
//                                    TextView item = (TextView) rootView.findViewById(R.id.shopItem);
//                                    item.setPaintFlags(item.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//
//                                }

                            }
//                            for (int j = 0; j < list.getCount(); j++) {
//                                View v = list.getChildAt(j);
//                                TextView tx = (TextView) v.findViewById(R.id.shopItem);
//                                Log.d("STRIKE", "done: " + tx.getText().toString());
//                            }



                        }
                    }

                }

            }
        });




        adapterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView item = (TextView) view.findViewById(R.id.shopItem);

                String food = item.getText().toString();
                Boolean striked = pantryShopping.get(food);

                if(!striked) { //check if boolean for strikethrough for item is true or false to remove strikethrough
                    item.setPaintFlags(item.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    pantryShopping.put(food, true);
                } else {
                    item.setPaintFlags(item.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    pantryShopping.put(food, false);
                }


                ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("ShoppingList");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {

                            objects.get(0).put("shoppingList", pantryShopping);
                            objects.get(0).saveInBackground();
                            // object will be your game score
                        }
                    }

                });


                }
            });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.shopping_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shopping_delete:


                return true;
            case R.id.shopping_add:


                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
    }
}
