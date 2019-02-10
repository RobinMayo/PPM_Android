package com.robinmayo.crossingroads.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.robinmayo.crossingroads.Player;
import com.robinmayo.crossingroads.R;


/**
 * A login screen that offers login via email/password.
 */
public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private static UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView nameView;
    private EditText mottoView;
    private View mProgressView;
    FloatingActionButton nameSignInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // Set up the login form.
        nameView = findViewById(R.id.name);
        mottoView = findViewById(R.id.motto);

        mottoView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                Log.d(TAG, "mottoView.setOnEditorActionListener" +
                        "(new TextView.OnEditorActionListener()");
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        nameSignInButton = findViewById(R.id.name_sign_in_button);
        nameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, WorldActivity.class);
                startActivity(intent);
            }
        });
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume("+ Player.getName()+", "+Player.getMotto()+")");

        // Initialise Player Ids :
        if (!Player.getName().equals("") && !Player.getMotto().equals("")) {
            nameView.setText(Player.getName());
            mottoView.setText(Player.getMotto());
        }
    }

    // The activity is note visible.
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause("+nameView.getText().toString()+
                ", "+mottoView.getText().toString()+")");
    }

    // The application is note visible.
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop("+nameView.getText().toString()+
                ", "+mottoView.getText().toString()+")");
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        Log.d(TAG, "attemptLogin()");

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        nameView.setError(null);
        mottoView.setError(null);

        // Store values at the time of the login attempt.
        String name = nameView.getText().toString();
        String motto = mottoView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid motto, if the user entered one.
        if (!TextUtils.isEmpty(motto) && !isMottoValid(motto)) {
            mottoView.setError(getString(R.string.error_invalid_motto));
            focusView = mottoView;
            cancel = true;
        }

        // Check for a valid name.
        if (TextUtils.isEmpty(name)) {
            nameView.setError(getString(R.string.error_field_required));
            focusView = nameView;
            cancel = true;
        } else if (!isNameValid(name)) {
            nameView.setError(getString(R.string.error_invalid_name));
            focusView = nameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            Log.d(TAG, "attemptLogin - new UserLoginTask(name, motto);");

            showProgress(true);
            mAuthTask = new UserLoginTask(name, motto);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isNameValid(String name) {
        return name.length() > 1;
    }

    private boolean isMottoValid(String motto) {
        return motto.length() > 3;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // The ViewPropertyAnimator APIs are not available, so simply show
        // and hide the relevant UI components.
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String name;
        private final String motto;

        UserLoginTask(String name, String motto) {
            this.name = name;
            this.motto = motto;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d(TAG, "doInBackground(...) : "+name+", "+motto+".");

            Player.setName(name);
            Player.setMotto(motto);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            Log.d(TAG, "onPostExecute(...)");
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Log.d(TAG, "onPostExecute(...) : SUCCESS");
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

