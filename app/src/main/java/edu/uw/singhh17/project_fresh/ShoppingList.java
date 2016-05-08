package edu.uw.singhh17.project_fresh;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.uw.singhh17.project_fresh.Adapters.PantryAdapter;
import edu.uw.singhh17.project_fresh.Adapters.ShoppingAdapter;
import edu.uw.singhh17.project_fresh.Model.ShoppingObject;
import edu.uw.singhh17.project_fresh.R;

public class ShoppingList extends Fragment implements AddItemDialog.NoticeDialogListener {


    private OnFragmentInteractionListener mListener;
    public static final int DIALOG_FRAGMENT = 1;
    private Map<String, Boolean> pantryShopping;

    private ArrayList<ShoppingObject> shopList;
    private ShoppingAdapter sAdapter;
    private AdapterView adapterView;

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

        //keyset (item names) of map turned into string array for list view adapter
        pantryShopping = new HashMap<>();

        shopList = new ArrayList<ShoppingObject>();

        final View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        sAdapter = new ShoppingAdapter(getActivity(), R.layout.row_shopping_list, shopList);
        adapterView = (AdapterView)rootView.findViewById(R.id.shopList);
        adapterView.setAdapter(sAdapter);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ShoppingList");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {

                        for (int i = 0; i < objects.size(); i++) {
                            ParseObject p = objects.get(i);

                            pantryShopping = p.getMap("shoppingList");

                            for (String key : pantryShopping.keySet()) {
                                ShoppingObject x = new ShoppingObject(key, pantryShopping.get(key));
                                shopList.add(x);
                            }
                            ((BaseAdapter) adapterView.getAdapter()).notifyDataSetChanged();

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

                if (!striked) { //check if boolean for strikethrough for item is true or false to remove strikethrough
                    item.setPaintFlags(item.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    pantryShopping.put(food, true);
                    shopList.get(position).striked = true;

                } else {
                    item.setPaintFlags(item.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    pantryShopping.put(food, false);
                    shopList.get(position).striked = false;
                }

                saveShoppingList();
                ((BaseAdapter) adapterView.getAdapter()).notifyDataSetChanged();
            }
        });

        return rootView;
    }

    private void saveShoppingList() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("ShoppingList");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {

                    objects.get(0).put("shoppingList", pantryShopping);
                    objects.get(0).saveInBackground();
                }
            }

        });
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

                Iterator it = pantryShopping.entrySet().iterator();
                while (it.hasNext())
                {
                    Map.Entry x = (Map.Entry) it.next();
                    if ((Boolean) x.getValue()) {
                        Log.d("test", "onOptionsItemSelected: " + "remove meeeeee");
                        ShoppingObject delete = new ShoppingObject((String) x.getKey(), true);
                        shopList.remove(delete);

                        Log.d("TEST ", "onOptionsItemSelected: " + shopList.toString());
                        it.remove();
                    }
                }

                saveShoppingList();

                ((BaseAdapter) adapterView.getAdapter()).notifyDataSetChanged();

                return true;
            case R.id.shopping_add:

                android.app.FragmentManager fragmentManager = getActivity().getFragmentManager();
                AddItemDialog addItem = new AddItemDialog();
//                addItem.setTargetFragment(fragmentManager.findFragmentById(R.id.container1), 0);
//        addItem.setTargetFragment(this, 0);
                addItem.show(fragmentManager, "dialog");

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case DIALOG_FRAGMENT:

                if (resultCode == Activity.RESULT_OK) {
                    // After Ok code.
//                    EditText editText = (EditText) findViewById(R.id.project_name);
                    Log.d("TEST", "onActivityResult: it works");

                    String item = data.getStringExtra("item");
                    pantryShopping.put(item, false);
                    saveShoppingList();
                    shopList.add(new ShoppingObject(item));
                    ((BaseAdapter) adapterView.getAdapter()).notifyDataSetChanged();

                } else if (resultCode == Activity.RESULT_CANCELED){
                    // After Cancel code.
                }

                break;
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Log.d("test", "onDialogPositiveClick: it works");
    }

    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
    }
}
