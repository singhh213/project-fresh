package edu.uw.singhh17.project_fresh.Utils;

import android.os.AsyncTask;
import android.util.Log;

import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by harpreetsingh on 4/20/16.
 */
public class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }


    @Override
    protected Boolean doInBackground(String... params) {
        String data = null;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if(urlConnection.getResponseCode() == 200)
            {
                // if response code = 200 ok
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                // Read the BufferedInputStream
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line);
                }
                data = sb.toString();

                Log.d("FOOD DATA", "doInBackground: " + data);
                JSONObject obj = new JSONObject(data);


                ParseObject newObject = new ParseObject("Pantry");
                newObject.put("item", obj.getString("item_name"));
                newObject.put("brand", obj.getString("brand_name"));
                newObject.put("daysLeft", 8);
                newObject.put("quantity", "2 bags");
                newObject.put("nutritionUrl", "http://i5.walmartimages.com/dfw/dce07b8c-f277/k2-_df6c917d-8c2a-470e-9c60-2a50916d0333.v1.jpg");
                newObject.put("imageUrl", "http://i5.walmartimages.com/dfw/dce07b8c-75f2/k2-_e5b52ece-66a6-4a7f-9e5a-91b370054c71.v2.jpg");

                newObject.saveInBackground();

                // End reading...............

                // Disconnect the HttpURLConnection
                urlConnection.disconnect();
            }
            else
            {
                Log.d("HTTPCONNECTION", "onActivityResult: " + "some other response code");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    protected void onPostExecute(Boolean result) {

    }
}
