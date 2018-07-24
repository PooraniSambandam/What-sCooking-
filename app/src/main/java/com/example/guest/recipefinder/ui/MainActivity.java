package com.example.guest.recipefinder.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.guest.recipefinder.Constants;
import com.example.guest.recipefinder.R;
import com.example.guest.recipefinder.models.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.findRecipesButton) Button mFindRecipesButton;
    @Bind(R.id.ingredient1EditText) EditText mIngredient1EditText;

    //@Bind(R.id.calToEditText) EditText calTo;
    //@Bind(R.id.calFromEditText) EditText calFrom;
    //@Bind(R.id.ingredient2EditText) EditText mIngredient2EditText;

    private ValueEventListener mUserRefListener;
    private Firebase mUserRef;
    private String mUId;
    private SharedPreferences mSharedPreferences;
    private String idxValue,idxValue1, gte,lte,calRange;

    @Bind(R.id.introTextView)TextView mWelcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.findRecipesButton:
                String ingredient1 = mIngredient1EditText.getText().toString();
                //gte = "gte "+calTo.getText().toString();
                //lte = "lte "+calFrom.getText().toString();
                //calRange = gte+","+lte;
                //String ingredient2 = mIngredient2EditText.getText().toString();
                Intent recipesIntent = new Intent(MainActivity.this, RecipeListActivity.class);
                recipesIntent.putExtra("ingredient1", ingredient1);
                recipesIntent.putExtra("idxValue",idxValue);
                recipesIntent.putExtra("idxValue1",idxValue1);
                //recipesIntent.putExtra("calRange",calRange);
                //recipesIntent.putExtra("ingredient2", ingredient2);
                startActivity(recipesIntent);
                break;
            default:
                break;
        }
    }

    public void onRadioButtonClicked(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.balanced:
                if (checked)
                    idxValue = "balanced";
                break;
            case R.id.high_protein:
                if (checked)
                    idxValue = "high-protein";
                break;
            case R.id.high_fibre:
                if (checked)
                    idxValue = "high-fibre";
                break;
            case R.id.low_fat:
                if (checked)
                    idxValue = "low-fat";
                break;
            case R.id.low_carb:
                if (checked)
                    idxValue = "low-carb";
                break;
            case R.id.low_sodium:
                if (checked)
                    idxValue = "low-sodium";
                break;
            default:
                idxValue="";

        }
    }

    public void onRadioButtonClicked1(View view) {

        // Is the button now checked?
        boolean checked1 = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.peanut:
                if (checked1)
                    idxValue1 = "peanut-free";
                break;
            case R.id.tree_nut:
                if (checked1)
                    idxValue1 = "tree-nut-free";
                break;
            case R.id.soy:
                if (checked1)
                    idxValue1 = "soy-free";
                break;
            case R.id.fish:
                if (checked1)
                    idxValue1 = "fish-free";
                break;
            case R.id.shellfish:
                if (checked1)
                    idxValue1 = "shellfish-free";
                break;
            default:
                idxValue = "";
                break;

        }
    }

}
