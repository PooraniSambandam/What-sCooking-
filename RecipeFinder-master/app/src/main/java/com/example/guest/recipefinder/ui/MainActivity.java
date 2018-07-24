package com.example.guest.recipefinder.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.guest.recipefinder.Constants;
import com.example.guest.recipefinder.R;
import com.example.guest.recipefinder.models.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

//import butterknife.Bind;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.data;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    @BindView(R.id.findRecipesButton)
    Button mFindRecipesButton;
    @BindView(R.id.ingredient1EditText)
    EditText mIngredient1EditText;
    // @Bind(R.id.ingredient2EditText) EditText mIngredient2EditText;

    private ValueEventListener mUserRefListener;
    private Firebase mUserRef;
    private String mUId;
    private SharedPreferences mSharedPreferences;
    @BindView(R.id.introTextView)
    TextView mWelcomeTextView;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        t1 = (TextView) findViewById(R.id.textView);

        mFindRecipesButton.setOnClickListener(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUId = mSharedPreferences.getString(Constants.KEY_UID, null);
        mUserRef = new Firebase(Constants.FIREBASE_URL_USERS).child(mUId);

        mUserRefListener = mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mWelcomeTextView.setText("Welcome, " + user.getName() + "!");
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        //Image part later
        ImageButton mSpeakBtn = (ImageButton) findViewById(R.id.imageButton);
        mSpeakBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startVoiceInput();

            }
        });
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }
//voice
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result1 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    t1.setText(result1.get(0));

                }
                break;
            }

        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.findRecipesButton:
                String ingredient1 = mIngredient1EditText.getText().toString();
                // String ingredient2 = mIngredient2EditText.getText().toString();
                Intent recipesIntent = new Intent(MainActivity.this, RecipeListActivity.class);
                recipesIntent.putExtra("ingredient1", ingredient1);
                //recipesIntent.putExtra("ingredient2", ingredient2);
                startActivity(recipesIntent);
                break;
            default:
                break;
        }
    }

}

