package edu.uw.singhh17.project_fresh.Utils;

import android.util.Log;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;



/**
 * Created by harpreetsingh on 4/27/16.
 */
public class Food2ForkClient {



    private final String API_BASE_URL = "http://food2fork.com/api/search?key=5253bcb47c6a8f4bbe9624f2388bc2e4";

    private AsyncHttpClient client1;

    public Food2ForkClient() {
        this.client1 = new AsyncHttpClient();

    }

    public void getRecept(JsonHttpResponseHandler handler, String link){
//        String link1 = "&q=" + link;
//        String url = getApiUrl(link1);
        Log.d("search", link);
        client1.get(link, handler);
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }
}
