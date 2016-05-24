package edu.uw.singhh17.project_fresh;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ShoppingActivity extends AppCompatActivity implements ShoppingList.OnFragmentInteractionListener, RecipeShoppingList.OnFragmentInteractionListener {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.shopContainer, new ShoppingList());
        ft.commit();

        final TextView pantryList = (TextView) findViewById(R.id.pantryShop);
        pantryList.setPaintFlags(pantryList.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        final TextView recipeList = (TextView) findViewById(R.id.recipeShop);

        pantryList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.shopContainer, new ShoppingList());
                ft.commit();
                pantryList.setPaintFlags(pantryList.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                recipeList.setPaintFlags(recipeList.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));



            }
        });

        recipeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.shopContainer, new RecipeShoppingList());
                ft.commit();
                recipeList.setPaintFlags(recipeList.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                pantryList.setPaintFlags(pantryList.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));

            }
        });


    }
}
