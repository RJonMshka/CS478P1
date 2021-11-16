package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class SetNameActivity extends AppCompatActivity {

    // Edit Full Name Field
    protected EditText editNameField;
    // Variable to store Full Name
    protected String editFieldValue = "";
    // Flag to check if the name is valid or not
    protected boolean isValidFullName = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // needs to be done
        super.onCreate(savedInstanceState);
        // set the view for activity
        setContentView(R.layout.activity_set_name);

        // reference the edit name field element
        editNameField = (EditText) findViewById(R.id.fullName);

        // attach listener to edit Name field element so that when user is done with editing the name, the user returns to previous activity
        editNameField.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // When user presses done (enter key), the activity sets the result
                    setResultForActivity();
                    // finish the activity when user is done entering name
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    // When user back presses the back button, call setResultForActivity method
    @Override
    public void onBackPressed() {
        // Needs to be called
        super.onBackPressed();

        // sets the result for previous activity
        setResultForActivity();
    }

    // This method sets the result for previous activity
    protected void setResultForActivity() {
        // populate variable with user entered name
        editFieldValue = editNameField.getText().toString();
        // create an intent
        Intent intent = new Intent();
        // add data to the intent, here it will be the full name user entered
        intent.putExtra("nameData", editFieldValue);
        isValidFullName = checkForValidName();

        // Check if entered name is valid or not and then sets the result for activity accordingly
        if(isValidFullName) {
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, intent);
        }
    }

    // This method returns true if name is valid (checks if atleast first name and last name are there and consists of only alphabets)
    protected boolean checkForValidName() {
        // remove starting or trailing spaces
        String nameEntered = editFieldValue.trim();
        String[] nameParts = nameEntered.split(" ");
        int namePartsWithText = 0;

        // assign null value to empty string values (for taking care of multiple spaces between each names)
        for (int i = 0 ; i < nameParts.length ; i++) {
            if (nameParts[i].trim().equals("")) {
                nameParts[i] = null;
            }
        }

        // check if name only consists of alphabets
        for (int i = 0 ; i < nameParts.length ; i++) {
            if (nameParts[i] != null && nameParts[i].matches("^[a-zA-Z]*$")) {
                namePartsWithText++;
            }
        }

        // returns true if atleast two strings are there (i.e. atleast first name and last name)
        return namePartsWithText >= 2;
    }

    // This method saves data if OS destroys and restarts the activity (like on orientation change)
    @Override
    protected void onSaveInstanceState(Bundle outputState) {

        // needs to be called
        super.onSaveInstanceState(outputState);

        // save the value entered in edit field
        outputState.putString("editFieldValue", editFieldValue);
    }
}