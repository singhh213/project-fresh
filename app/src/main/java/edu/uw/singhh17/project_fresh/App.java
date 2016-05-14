package edu.uw.singhh17.project_fresh;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;

public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "CIvZ3eD8SlHP0YJQ5cLXSxGyaXazaQqR1h9uwQYC", "zJixVBZ4Fc7KQoNlROEGxNGKlu6NfPvVHrSp3wsV"); // Your Application ID and Client Key are defined elsewhere
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
    }
}
