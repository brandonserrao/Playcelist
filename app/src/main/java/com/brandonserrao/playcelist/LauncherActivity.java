package com.brandonserrao.playcelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.brandonserrao.playcelist.SPUser.SPUser;
import com.google.gson.Gson;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LauncherActivity extends AppCompatActivity {

    //public static final String CLIENT_ID = "fdcc6fcc754e42e3bc7f45f2524816f3";
    public static final String CLIENT_ID = "cff5c927f91e4e9582f97c827f8632dd"; //- use from PC;
    private static final String REDIRECT_URI = "com.brandonserrao.playcelist://callback";
    public SpotifyAppRemote mSpotifyAppRemote;
    public static final int AUTH_TOKEN_REQUEST_CODE = 0x10;
    public static final int AUTH_CODE_REQUEST_CODE = 0x11;
    public SPUser CUser; // user profile

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    public String mAccessToken;
    public String mAccessCode;
    public Call mCall;

    public String CUserName;
    public String CUserUpiclnk;
    public String CUserID;
    public boolean isWebLoggedIn = false;
    public boolean isAppLoggedIn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences pref = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        mAccessToken = pref.getString("mAccessToken", "");
        Log.e("SHARED", "tk+" + mAccessToken);


        isAppLoggedIn=pref.getBoolean("isAppLoggedIn",false);
        if (isAppLoggedIn) Log.e("SHARED", "isAppLoggedIn1");
        else Log.e("SHARED", "isAppLoggedIn0");

        isWebLoggedIn=pref.getBoolean("isWebLoggedIn",false);
        if (isWebLoggedIn) Log.e("SHARED", "isWebLoggedIn1");
        else Log.e("SHARED", "isWebLoggedIn0");

        if(isWebLoggedIn && isAppLoggedIn && mAccessToken!=null) { routeToMain();}

        CUserName = pref.getString("CUserName", "JD");
        Log.e("SHARED", "Name " + CUserName);

        CUserUpiclnk = pref.getString("CUserUpiclnk", "_");
        Log.e("SHARED", "Piclink " + CUserUpiclnk);

        CUserID = pref.getString("CUserID", "_");
        Log.e("SHARED", "CUserID " + CUserID);




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }

    public void routeToMain() {
        // Example routing
        if (isWebLoggedIn && isAppLoggedIn) {
            Intent intent = new Intent(this, MainActivity.class);
          startActivity(intent);
        } else
            { if (isWebLoggedIn) AppLogin(); else RequestToken();}
    }

    // log in SpotifyWeb


    public void RequestToken() {
        final AuthenticationRequest request = getAuthenticationRequest(AuthenticationResponse.Type.TOKEN);
        AuthenticationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    // log in SpotifyWeb
    public void LogIntoSpotify(View view) {
        if (isWebLoggedIn == false) {
            // no login
            Log.e("SPOTIFZ", "Web-login");
            RequestToken();
            Log.e("SPOTIFY", "Web-login succ");

        } else {
            routeToMain();
        }
    }

    private AuthenticationRequest getAuthenticationRequest(AuthenticationResponse.Type type) {
        return new AuthenticationRequest.Builder(CLIENT_ID, type, REDIRECT_URI)
                .setShowDialog(false)
                .setScopes(new String[]{"user-read-email", "user-read-playback-state", "user-read-currently-playing", "user-read-private","playlist-modify-private","playlist-modify-public"})
                .build();
    }

    @Override
    // Spotify web resoinse parser
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);

        if (requestCode == AUTH_TOKEN_REQUEST_CODE) {
            mAccessToken = response.getAccessToken();
            Log.e("Token", "THere" + mAccessToken);


            SharedPreferences pref = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("mAccessToken", mAccessToken);
            editor.commit();

//            Log.e("SHARED", mAccessToken);
            GetUser();
            isWebLoggedIn = true;
            routeToMain();

            editor.putBoolean("isWebLoggedIn", isWebLoggedIn);
            editor.commit();


        } else if (requestCode == AUTH_CODE_REQUEST_CODE) {
            mAccessCode = response.getCode();
            Log.e("Code", "CHere " + mAccessCode);
            GetUser();
            routeToMain();

        }


    } // get user info from Sporify WEB

    public void GetUser() {
        if (mAccessToken != null) {
            final Request request = new Request.Builder()
                    .url("https://api.spotify.com/v1/me") //get user data
                    //.url("https://api.spotify.com/v1/me/player/currently-playing") //get current song
                    .addHeader("Authorization", "Bearer " + mAccessToken)
                    .build();
            cancelCall();
            mCall = mOkHttpClient.newCall(request);
            mCall.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("Response", "Request fail");//Fail
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {

                        final JSONObject jsonObject = new JSONObject(response.body().string());
                        String JsonResponse = jsonObject.toString();
                        Gson gson = new Gson();
                        CUser = gson.fromJson(JsonResponse, SPUser.class);


                        Log.e("Response", "User" + JsonResponse);
                        response.close();
                        CUserName = CUser.getDisplayName();
                        CUserUpiclnk = CUser.getImages().get(0).getUrl();
                        CUserID = CUser.getId();

                        SharedPreferences pref = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("CUserName", CUserName);
                        editor.putString("CUserUpiclnk", CUserUpiclnk);
                        editor.putString("CUserID", CUserID);
                        editor.commit();
                        Log.e("SHARED", CUserName);
                        Log.e("SHARED", CUserUpiclnk);
                       // routeToMain();

                    } catch (JSONException e) {
                        //Fail
                    }
                }
            });
        } else {

        }
        ;
    }

    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

    public void AppLogin() {


        SpotifyAppRemote.connect(
                getApplication(),
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build(),
                new Connector.ConnectionListener() {
                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.e("SPOTIFY", " APP splash connected");
                        isAppLoggedIn = true;
                        Log.e("SPOTIFY", " APP splash flag");
                        SharedPreferences pref = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("isAppLoggedIn", isAppLoggedIn);
                        editor.commit();
                        routeToMain();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("SPOTIFY", " APP conn fail");
                        Log.e("MyActivity", throwable.getMessage(), throwable);
                    }
                });
    }
}

