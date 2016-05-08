package edu.uw.singhh17.project_fresh;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.util.Map;

import edu.uw.singhh17.project_fresh.Utils.DownloadImageTask;


public class ItemInfo extends Fragment {

    private String name;
    private int expireInfo;
    private String imageUrl;
    private String nutritionUrl;
    private String quantity;

    private OnFragmentInteractionListener mListener;

    public ItemInfo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("ITEM");
        if (getArguments() != null) {
            name = getArguments().getString("name");
            expireInfo = getArguments().getInt("expireInfo");
            imageUrl = getArguments().getString("imageUrl");
            nutritionUrl = getArguments().getString("nutritionUrl");
            quantity = getArguments().getString("quantity");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View rootView = inflater.inflate(R.layout.fragment_item_info, container, false);

        TextView itemName = (TextView) rootView.findViewById(R.id.itemName);
        TextView expInfo = (TextView) rootView.findViewById(R.id.expireInfo);
        ImageView itemImg = (ImageView) rootView.findViewById(R.id.itemImage);
        ImageView nutritionLabel = (ImageView) rootView.findViewById(R.id.nutrtionLabel);
        TextView itemQuantity = (TextView) rootView.findViewById(R.id.itemAmount);
        final Button addItem = (Button) rootView.findViewById(R.id.iteminfo_add_button);

        ImageView indicator = (ImageView) rootView.findViewById(R.id.colorIndicator);
        GradientDrawable bgShape = (GradientDrawable)indicator.getBackground();

        itemName.setText(name);
        itemQuantity.setText(quantity);

        new DownloadImageTask(itemImg)
                .execute(imageUrl);

        new DownloadImageTask(nutritionLabel)
                .execute(nutritionUrl);

        if (expireInfo > 7) {
            Log.d("TEST days left", "getView: " + expireInfo);
            bgShape.setColor(ContextCompat.getColor(getContext(), R.color.ci_green));
        } else if (expireInfo > 3) {
            bgShape.setColor(ContextCompat.getColor(getContext(), R.color.ci_yellow));
        } else if (expireInfo > 0){
            bgShape.setColor(ContextCompat.getColor(getContext(), R.color.ci_orange));
        } else {
            bgShape.setColor(ContextCompat.getColor(getContext(), R.color.ci_red));
        }

        if (expireInfo > 0) {
            expInfo.setText("Expires in " + Integer.toString(expireInfo) + " days");
        } else {
            expInfo.setText("Expired");
        }

        addItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("ShoppingList");
                query.getInBackground("IAf5ywmpFM", new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {

                            Map<String, Boolean> shoppingList = object.getMap("shoppingList");
                            shoppingList.put(name, false);

                            object.put("shoppingList", shoppingList);
                            object.saveInBackground();
                            addItem.setText("Added to Shopping List");
                            addItem.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.actionBarGreen));
                            addItem.setTextColor(Color.WHITE);
                        }
                    }
                });
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
