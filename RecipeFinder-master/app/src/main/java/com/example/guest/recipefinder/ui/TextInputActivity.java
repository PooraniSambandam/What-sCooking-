package com.example.guest.recipefinder.ui;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import java.util.StringTokenizer;
import com.example.guest.recipefinder.R;
import java.util.StringTokenizer;


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

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.data;


public class TextInputActivity extends AppCompatActivity {

    private TextInputEditText textInputEditTextIngredient;
    private String ingredientsInput;
    private int c;

    //Edamam parameters

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
        setContentView(R.layout.activity_text_input);
        c=0;
        textInputEditTextIngredient = (TextInputEditText)findViewById(R.id.ingredientEdit);

        t1 = (TextView) findViewById(R.id.textView);

        //Edamam
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



    }

    public void btnFunSubmit(View view) {

        Intent TextinputToRecipeAPI = new Intent(TextInputActivity.this, RecipeListActivity.class);
        TextinputToRecipeAPI.putExtra("ingredient1",ingredientsInput);
        startActivity(TextinputToRecipeAPI);
    }

    public void btnFunView(View view) {

        StringTokenizer st = new StringTokenizer(ingredientsInput, ",");
        StringBuffer buffer = new StringBuffer();
        while (st.hasMoreTokens()) {
            buffer.append(st.nextToken());
        }
        showMessage("~ Ingredients Selected ~", buffer.toString());
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


    public void btnFunAdd(View view) {
        if(c==0)
        {
            ingredientsInput = textInputEditTextIngredient.getText().toString().trim();
            textInputEditTextIngredient.setText(null);
            c++;
        }
        else
        {
            ingredientsInput = ingredientsInput +","+textInputEditTextIngredient.getText().toString().trim();
            textInputEditTextIngredient.setText(null);
            c++;
        }
    }

}
