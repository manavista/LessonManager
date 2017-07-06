package jp.manavista.developbase;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((ThisApplication) getApplication()).getAppComponent().inject(this);

        String val = preferences.getString("start_view","null");

        Log.d("preference", val);
    }
}
