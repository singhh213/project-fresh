package edu.uw.singhh17.project_fresh;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class PantryView extends Fragment implements ItemInfo.OnFragmentInteractionListener {

    private OnFragmentInteractionListener mListener;

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


        final PantryData data[] = new PantryData[] {
                new PantryData("Cheddar Cheese", 2),
                new PantryData("Milk", 2),
                new PantryData("Bacon", 3),
                new PantryData("Egg", 5),
                new PantryData("Yogurt", 7),
                new PantryData("Peanut Butter", 8),
                new PantryData("Bread", 4),
                new PantryData("Crackers", 16),
                new PantryData("Apples", 5),
                new PantryData("Juice", 8)
        };

        final View rootView = inflater.inflate(R.layout.fragment_pantry_view, container, false);


        PantryAdapter pAdapter = new PantryAdapter(getActivity(), R.layout.row_pantry, data);
        AdapterView pantryView = (AdapterView)rootView.findViewById(R.id.pantryList);
        pantryView.setAdapter(pAdapter);

        pantryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TEST CLICK", "onItemClick: " + position);
                Bundle bundle = new Bundle();
                bundle.putString("name", data[position].name);
                bundle.putInt("expireInfo", data[position].daysLeft);
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