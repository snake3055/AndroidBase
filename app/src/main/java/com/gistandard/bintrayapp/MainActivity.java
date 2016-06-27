package com.gistandard.bintrayapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gistandard.androidbase.http.BaseResponse;
import com.gistandard.androidbase.http.IResponseListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        SPUtils.setPreferences(this, "");
//        boolean test_boolean = SPUtils.getBoolean("TEST_BOOLEAN", false);
//        SPUtils.putBoolean("TEST_BOOLEAN", false);
//        test_boolean = SPUtils.getBoolean("TEST_BOOLEAN", false);
//        SPUtils.setPreferences(this, "test");
//        test_boolean = SPUtils.getBoolean("TEST_BOOLEAN", false);
//        SPUtils.remove("TEST_BOOLEAN");
//        test_boolean = SPUtils.getBoolean("TEST_BOOLEAN", false);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setAccount("CN0002519840327131109");
        loginRequest.setPassword("12345678aA");
        LoginTask loginTask = new LoginTask(loginRequest, new IResponseListener() {
            @Override
            public void onTaskSuccess(BaseResponse response) {

            }

            @Override
            public void onTaskError(long requestId, int responseCode, String responseMsg) {

            }
        });
        loginTask.excute(this);

        IMLoginTask task = new IMLoginTask(new IMLoginRequest(), new IResponseListener() {
            @Override
            public void onTaskSuccess(BaseResponse response) {

            }

            @Override
            public void onTaskError(long requestId, int responseCode, String responseMsg) {

            }
        });
        task.excute(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
