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
import android.widget.ImageView;
import android.widget.TextView;

import edu.uw.singhh17.project_fresh.Utils.DownloadImageTask;


public class ItemInfo extends Fragment {

    private String name;
    private int expireInfo;
    private String imageUrl;

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

        ImageView indicator = (ImageView) rootView.findViewById(R.id.colorIndicator);
        GradientDrawable bgShape = (GradientDrawable)indicator.getBackground();

        itemName.setText(name);
        new DownloadImageTask(itemImg)
                .execute(imageUrl);

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

        expInfo.setText("Expires in " + Integer.toString(expireInfo) + " days");

//        itemName.setText(name);
//        expInfo.setText(Integer.toString(expireInfo));


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
