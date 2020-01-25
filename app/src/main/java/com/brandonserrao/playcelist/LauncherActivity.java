package com.brandonserrao.playcelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import static com.brandonserrao.playcelist.MainActivity.AUTH_TOKEN_REQUEST_CODE;
import static com.brandonserrao.playcelist.MainActivity.CLIENT_ID;

public class LauncherActivity extends AppCompatActivity {

    public boolean isloggedin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }

    public void routeToMain() {
        // Example routing
        if (isloggedin) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    // log in SpotifyWeb
    public void LogIntoSpotify(View view) {
        if (!isloggedin) {
            // no login
            Log.e("SPOTIFZ", "Web-login");
            RequestToken();
            Log.e("SPOTIFY", "Web-login succ");
        } else {
            Toast.makeText(this, "ELSE", Toast.LENGTH_SHORT).show();
        }
        routeToMain();
    }

    public void RequestToken() {
        View contextView = findViewById(R.id.btn_LogIn);
        Snackbar.make(contextView, R.string.btnWorking, Snackbar.LENGTH_SHORT)
                .show();
        /*
         Todo THIS DOESN'T WORK YET: problem with getAuthenticationRequest
         final AuthenticationRequest request = getAuthenticationRequest(AuthenticationResponse.Type.TOKEN);
         AuthenticationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request);
        */
    }

    //Todo this has to be removed once the login -> to main works.
    public void OnClickStartMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}