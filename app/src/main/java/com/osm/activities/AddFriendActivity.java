package com.osm.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.osm.API.APIResponse;
import com.osm.API.OSM;
import com.osm.R;

public class AddFriendActivity extends AppCompatActivity {

    private TextView inputFriendUsername;

    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        inputFriendUsername = (TextView) findViewById(R.id.inputFriendUsername);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_friend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuAddFriend) {
            username = inputFriendUsername.getText().toString();
            OSM.getInstance(getApplicationContext()).addFriend(username, new APIResponse.FriendListener() {
                @Override
                public void onSuccess(String response) {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
