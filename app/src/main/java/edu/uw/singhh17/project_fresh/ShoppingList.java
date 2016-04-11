package edu.uw.singhh17.project_fresh;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import edu.uw.singhh17.project_fresh.R;

public class ShoppingList extends Fragment {


    private OnFragmentInteractionListener mListener;

    public ShoppingList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        getActivity().setTitle("SHOPPING LIST");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        String shopList[] = new String[] {
                "Cheddar Cheese",
                "Milk",
                "Pizza",
                "Corn",
                "Avacado"
        };

        final View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.row_shopping_list, R.id.shopItem, shopList);
        AdapterView adapterView = (AdapterView) rootView.findViewById(R.id.shopList);
        adapterView.setAdapter(adapter);

        adapterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView item = (TextView) view.findViewById(R.id.shopItem);

                if(true) { //check if boolean for strikethrough for item is true or false to remove strikethrough
                    item.setPaintFlags(item.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    item.setPaintFlags(item.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
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

    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
    }
}
