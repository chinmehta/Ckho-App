package com.example.android.ckho;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{

    // Declaring Variables
    private LinearLayout profileSection;
    private Button logOut;
    private SignInButton logIn;
    private TextView Name,EmailId;
    private ImageView profilePicture;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialising Variables
        profileSection = (LinearLayout)findViewById(R.id.profile_section);
        logOut = (Button)findViewById(R.id.logout_button);
        logIn = (SignInButton)findViewById(R.id.login_button);
        Name = (TextView)findViewById(R.id.profile_name);
        EmailId = (TextView)findViewById(R.id.profile_id);
        profilePicture = (ImageView)findViewById(R.id.profile_pic);

        //Set on click listener to login and logout buttons
        logIn.setOnClickListener(this);
        logOut.setOnClickListener(this);

        //hide the user profile section by default
        profileSection.setVisibility(View.GONE);

        //default google sign in option
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).
                enableAutoManage(this,this).
                addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

        //Creating dummy data list of events
        ArrayList<Event> upcomingEvent = new ArrayList<Event>();
        ArrayList<Event> pastEvent = new ArrayList<Event>();

        upcomingEvent.add(new Event("Title3","date3,time","location3"));
        upcomingEvent.add(new Event("Title4","date4,time","location4"));

        pastEvent.add(new Event("Title1","date1,time","location1"));
        pastEvent.add(new Event("Title2","date2,time","location2"));

        EventAdapter pastEventAdapter = new EventAdapter(this,pastEvent);
        EventAdapter upcomingEventAdapter = new EventAdapter(this,upcomingEvent);

        ListView upcomingListView = (ListView)findViewById(R.id.upcoming_list);
        ListView pastListView = (ListView)findViewById(R.id.past_list);

        upcomingListView.setAdapter(upcomingEventAdapter);
        pastListView.setAdapter(pastEventAdapter);


    }

    @Override
    public void onClick(View v) {
        //check which button is clicked
        switch (v.getId())
        {
            //case of login button
            case R.id.login_button:
                LogIn();
                break;
            //case of logout button
            case R.id.logout_button:
                LogOut();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void LogIn()
    {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }

    private void LogOut()
    {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }

    private void handleResult(GoogleSignInResult result)
    {
        if(result.isSuccess())
        {
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            String img_url = account.getPhotoUrl().toString();

            Name.setText(name);
            EmailId.setText(email);
            Glide.with(this).load(img_url).into(profilePicture);
            updateUI(true);
        }
        else {
            updateUI(false);
        }

    }

    private void updateUI(Boolean isLogIn)
    {
        if(isLogIn)
        {
            profileSection.setVisibility(View.VISIBLE);
            logIn.setVisibility(View.GONE);
        }
        else
        {
            profileSection.setVisibility(View.GONE);
            logIn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==REQ_CODE)
        {
            GoogleSignInResult result  = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
}
