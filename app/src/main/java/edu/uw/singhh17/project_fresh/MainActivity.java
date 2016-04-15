package edu.uw.singhh17.project_fresh;

import android.app.ActionBar;
import android.content.ClipData;
import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.parse.Parse;
import com.parse.ParseObject;

public class MainActivity extends AppCompatActivity implements PantryView.OnFragmentInteractionListener,
        ItemInfo.OnFragmentInteractionListener, ShoppingList.OnFragmentInteractionListener,
        Scan.OnFragmentInteractionListener, Recipe.OnFragmentInteractionListener {


    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("CIvZ3eD8SlHP0YJQ5cLXSxGyaXazaQqR1h9uwQYC")
                        .server("http://YOUR_PARSE_SERVER:1337/parse")


                        .build()
        );

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        final PantryView pantry = new PantryView();
        ft.add(R.id.container1, pantry);
        ft.commit();

        ImageButton home = (ImageButton) findViewById(R.id.home);
        ImageButton scan = (ImageButton) findViewById(R.id.scan);
        ImageButton recipe = (ImageButton) findViewById(R.id.recipe);

        home.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.container1, pantry);
                ft.commit();
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.container1, new Scan());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        recipe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.container1, new Recipe());
                ft.addToBackStack(null);
                ft.commit();
            }
        });





//        android.support.v7.app.ActionBar ab = getSupportActionBar();
//        LayoutInflater li = LayoutInflater.from(this);
//        View customView = li.inflate(R.layout.custom_action_bar, null);
//        ab.setCustomView(customView);

//        ImageButton settings = (ImageButton) findViewById(R.id.settings);
//        settings.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Log.d("SETTINGS", "onClick: ");
//            }
//        });
//
//        ImageButton shopList = (ImageButton) findViewById(R.id.shopList);
//        shopList.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Log.d("SHOPLIST", "onClick: ");
//            }
//        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_shopping:
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.container1, new ShoppingList());
                ft.addToBackStack(null);
                ft.commit();

                return true;
            case R.id.action_settings:


                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
