package edu.uw.singhh17.project_fresh;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "CIvZ3eD8SlHP0YJQ5cLXSxGyaXazaQqR1h9uwQYC", "zJixVBZ4Fc7KQoNlROEGxNGKlu6NfPvVHrSp3wsV"); // Your Application ID and Client Key are defined elsewhere
    }
}
