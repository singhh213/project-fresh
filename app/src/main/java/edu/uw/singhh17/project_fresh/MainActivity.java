package edu.uw.singhh17.project_fresh;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import edu.uw.singhh17.project_fresh.Utils.JSONAsyncTask;

public class MainActivity extends AppCompatActivity implements PantryView.OnFragmentInteractionListener,
        ItemInfo.OnFragmentInteractionListener, ShoppingList.OnFragmentInteractionListener,
        Scan.OnFragmentInteractionListener, Recipe.OnFragmentInteractionListener, RecipeDetail.OnFragmentInteractionListener,
        AddItemDialog.NoticeDialogListener {


    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        final PantryView pantry = new PantryView();
        ft.add(R.id.container1, pantry);
        ft.commit();

        ImageButton home = (ImageButton) findViewById(R.id.home);
        ImageButton scan = (ImageButton) findViewById(R.id.scan);
        ImageButton recipe = (ImageButton) findViewById(R.id.recipe);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
//                FragmentTransaction ft = fragmentManager.beginTransaction();
//                ft.replace(R.id.container1, new Scan());
//                ft.addToBackStack(null);
//                ft.commit();
                new IntentIntegrator(MainActivity.this).initiateScan();
//                new IntentIntegrator().initiateScan();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, intent);

        if (scanningResult != null) {
            String upc = scanningResult.getContents();

            Log.d("SCAN TEST", "onActivityResult: " + upc);

            String baseUrl = "https://api.nutritionix.com/v1_1/item?upc=";

            String appKeys = "&appId=b2e72327&appKey=6ddd5472e2b2d1eb169b693934d53474";

            String urlString = baseUrl + upc + appKeys;

//            new JSONAsyncTask().execute(urlString);
//"85239"

            if (upc != null) {


                ParseObject newObject = new ParseObject("Pantry");


                if (upc.contains("85239")) {

                    newObject.put("item", "French Bread");
                    newObject.put("brand", "Market Pantry");
                    newObject.put("daysLeft", 1);
                    newObject.put("quantity", "1 Roll");
                    newObject.put("nutritionUrl", "http://i5.walmartimages.com/dfw/dce07b8c-f277/k2-_df6c917d-8c2a-470e-9c60-2a50916d0333.v1.jpg");
                    newObject.put("imageUrl", "http://scene7.targetimg1.com/is/image/Target/14826441?wid=480&hei=480");
                    newObject.saveInBackground();
//                newObject.saveInBackground();


                } else if (upc.contains("15756")) {

//                ParseObject newObject = new ParseObject("Pantry");
                    newObject.put("item", "Strawberries");
                    newObject.put("brand", "Driscoll");
                    newObject.put("daysLeft", 7);
                    newObject.put("quantity", "1 Pound");
                    newObject.put("nutritionUrl", "http://i5.walmartimages.com/dfw/dce07b8c-f277/k2-_df6c917d-8c2a-470e-9c60-2a50916d0333.v1.jpg");
                    newObject.put("imageUrl", "http://scene7.targetimg1.com/is/image/Target/13208903?wid=480&hei=480");
//                newObject.saveInBackground();


                } else {

//                ParseObject newObject = new ParseObject("Pantry");
                    newObject.put("item", "Chocolate Syrup");
                    newObject.put("brand", "Hershey's Lite");
                    newObject.put("daysLeft", 34);
                    newObject.put("quantity", "24 Ounces");
                    newObject.put("nutritionUrl", "http://i5.walmartimages.com/dfw/dce07b8c-f277/k2-_df6c917d-8c2a-470e-9c60-2a50916d0333.v1.jpg");
                    newObject.put("imageUrl", "http://i5.walmartimages.com/dfw/dce07b8c-ef9a/k2-_4df1efe7-5aea-4405-ba35-e4e853aea8f6.v1.jpg");


                }
                newObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        final PantryView pantry = new PantryView();
                        ft.replace(R.id.container1, pantry);
                        ft.commit();
                    }
                });

            }
//            finish();
//            startActivity(getIntent());


//            String s = "http://www.google.com/search?q=";
//            s += scanningResult.getContents();

//            Intent myIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
//            startActivity(myIntent1);

        } else {
            Toast.makeText(MainActivity.this,
                    "No scan data received!", Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public void onBackPressed() {
//        int count = getSupportFragmentManager().getBackStackEntryCount();
//
//        if (count == 0) {
//            super.onBackPressed();
//            //additional code
//        } else {
//            getSupportFragmentManager().popBackStack();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_shopping:
                Intent intent = new Intent(MainActivity.this, ShoppingActivity.class);
                startActivity(intent);
//                FragmentTransaction ft = fragmentManager.beginTransaction();
//                ft.replace(R.id.container1, new ShoppingList());
//                ft.addToBackStack(null);
//                ft.commit();

                return true;
            case R.id.action_settings:
                Intent intent2 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent2);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //Checking for fragment count on backstack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public void onDialogPositiveClick(android.support.v4.app.DialogFragment dialog) {

    }
}
