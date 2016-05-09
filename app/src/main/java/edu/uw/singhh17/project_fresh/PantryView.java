package edu.uw.singhh17.project_fresh;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.WrapperListAdapter;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuAdapter;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import edu.uw.singhh17.project_fresh.Adapters.PantryAdapter;
import edu.uw.singhh17.project_fresh.Model.PantryData;


public class PantryView extends Fragment implements ItemInfo.OnFragmentInteractionListener {

    private OnFragmentInteractionListener mListener;
    private PantryAdapter pAdapter;
    private ArrayList<PantryData> parseData;

    public PantryView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        android.support.v7.app.ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
//        LayoutInflater li = LayoutInflater.from(getActivity());
//        View customView = li.inflate(R.layout.custom_action_bar, null);
//        ab.setCustomView(customView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("PANTRY");

        parseData = new ArrayList<>();

        final View rootView = inflater.inflate(R.layout.fragment_pantry_view, container, false);

        SwipeMenuListView listView = (SwipeMenuListView) rootView.findViewById(R.id.listView);

        SwipeMenuCreator creator = new SwipeMenuCreator() {


            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(100);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete_button);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        listView.setMenuCreator(creator);
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);

        pAdapter = new PantryAdapter(getActivity(), R.layout.row_pantry, parseData);
        final AdapterView pantryView = (AdapterView)rootView.findViewById(R.id.listView);
        pantryView.setAdapter(pAdapter);



        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                if(index == 0) {
                    Log.d("DELETE Button test", "onMenuItemClick: " + position + " " + parseData.get(position).objectId);

//                    PantryData toRemove =
//                    Log.d("Remove pantry", "done: " + toRemove.name + " " + position);
//                    pAdapter.remove(pAdapter.getItem(position));


                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Pantry");
                    query.getInBackground(parseData.get(position).objectId, new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            if (object != null) {
                                Log.d("score", "" + parseData.get(position).objectId);
//                                object.deleteInBackground(new DeleteCallback() {
//                                    @Override
//                                    public void done(ParseException e) {
//
////                                        PantryData toRemove = pAdapter.getItem(position);
////                                        Log.d("Remove pantry", "done: " + toRemove.name + " " + position);
////
////                                        pAdapter.remove(toRemove);
////                                        parseData.remove(new PantryData(null,0,null,null,null,parseData.get(position).objectId));
////                                        getPantryItems();
////                                        ((BaseAdapter) ((SwipeMenuAdapter) pantryView.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();
//
//
//                                    }
//                                });

                                object.deleteInBackground();


//                                for (PantryData x : parseData) {
//                                    Log.d("pantryleftover", "done: " + x.name+ " " + x.objectId);
//                                }
//                                ((BaseAdapter) ((SwipeMenuAdapter) pantryView.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();
//                                getPantryItems();
                            }
                        }
                    });

                    parseData.remove(position);
                    pAdapter.notifyDataSetChanged();
                    pantryView.setAdapter(pAdapter);

                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        getPantryItems();

        pantryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TEST CLICK", "onItemClick: " + position);
                Bundle bundle = new Bundle();
                bundle.putString("name", parseData.get(position).name);
                bundle.putInt("expireInfo", parseData.get(position).daysLeft);
                bundle.putString("imageUrl", parseData.get(position).imageUrl);
                bundle.putString("nutritionUrl", parseData.get(position).nutritionLabel);
                bundle.putString("quantity", parseData.get(position).quantity);
                ItemInfo itemObj = new ItemInfo();
                itemObj.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.container1, itemObj);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void getPantryItems() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Pantry");
        query.orderByAscending("daysLeft");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        pAdapter.clear();

                        for (int i = 0; i < objects.size(); i++) {
                            ParseObject p = objects.get(i);

                            parseData.add(new PantryData(p.getString("brand") + " " + p.getString("item"), p.getInt("daysLeft"),
                                    p.getString("imageUrl"), p.getString("nutritionUrl"), p.getString("quantity"), p.getObjectId()));

//                            pAdapter.add(new PantryData(p.getString("brand") + " " + p.getString("item"), p.getInt("daysLeft"),
//                                    p.getString("imageUrl"), p.getString("nutritionUrl"), p.getString("quantity"), p.getObjectId()));

                        }
                    }
                }
            }
        });
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

    }
}
