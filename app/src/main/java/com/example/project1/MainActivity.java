package com.example.project1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // variables for buttons
    protected Button buttonOne;
    protected Button buttonTwo;

    // variable to hold name data
    protected String nameData = "";
    // variable to hold result status of intent resolution
    protected int setNameResult;

    // Create a activity result launcher by adding a contract with OS and passing a callback
    ActivityResultLauncher<Intent> startSetNameResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onActivityResult(ActivityResult result) {
                int resultCode = result.getResultCode();
                // Check for different result codes
                if(resultCode == Activity.RESULT_OK){
                    Intent intent = result.getData();
                    // set the value of variable to data passed by another activity in Intent
                    nameData = intent.getStringExtra("nameData");
                    setNameResult = R.string.success_text;
                } else if(resultCode == Activity.RESULT_CANCELED) {
                    Intent intent = result.getData();
                    // set the value of variable to data passed by another activity in Intent
                    nameData = intent.getStringExtra("nameData");
                    setNameResult = R.string.failure_text;
                }
            }
    });

    // This method shows a toast message
    protected void showToastMsg(String msg, int duration) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, duration);
        toast.show();
    }

    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // need to be added
        super.onCreate(savedInstanceState);

        // restore the data
        if (savedInstanceState != null) {
            nameData = savedInstanceState.getString("nameData");
            setNameResult = savedInstanceState.getInt("setNameResult");
        }

        // Inflate the activity_main layout
        setContentView(R.layout.activity_main);

        // reference the elements
        buttonOne =(Button) findViewById(R.id.buttonOne);
        buttonTwo = (Button) findViewById(R.id.buttonTwo);

        // Set onClick Listeners for both the buttons
        buttonOne.setOnClickListener(buttonOneClickListener);
        buttonTwo.setOnClickListener(buttonTwoClickListener);
    }

    // Saves the state/data in case OS destroys and restarts the activity
    @Override
    protected void onSaveInstanceState(Bundle outputState) {

        //need to be added
        super.onSaveInstanceState(outputState);

        outputState.putString("nameData", nameData);
        outputState.putInt("setNameResult", setNameResult);
    }

    // Create a listener for first button
    public View.OnClickListener buttonOneClickListener = v -> {
        openSetNameActivity();
    };

    // Create a listener for second button
    public View.OnClickListener buttonTwoClickListener = v -> {
        if(setNameResult == R.string.success_text) {
            // send data to contacts app
            Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
            intent.putExtra(ContactsContract.Intents.Insert.NAME, nameData);
            intent.putExtra("finishActivityOnSaveCompleted", true);
            // start the activity to open contacts app
            startActivity(intent);
        } else if(setNameResult == R.string.failure_text) {
            // create a toast message and show it
            String toastMsg = getString(R.string.failure_toast_msg_prefix) + nameData;
            int toastMsgDuration = Toast.LENGTH_SHORT;
            showToastMsg(toastMsg, toastMsgDuration);
        }
    };

    // Create an intent and starts the second activity for result
    protected void openSetNameActivity() {
        Intent intent = new Intent(MainActivity.this, SetNameActivity.class);
        startSetNameResult.launch(intent);
    }
}