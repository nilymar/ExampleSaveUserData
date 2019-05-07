package com.example.android.examplesaveuserdata;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    String firstName = "";
    String surname = "";
    EditText firstNameEdit;
    EditText surnameEdit;
    Button saveButton;
    private SharedPreferences myPreferences; //variable for the preferences file location
    private SharedPreferences.Editor myEditor; //variable for the preferences file editor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstNameEdit = findViewById(R.id.first_name);
        surnameEdit = findViewById(R.id.surname);
        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferences(); // see the method below
            }
        });
        //when the app starts - the restore method is called so that if the user saved his Preferences they are shown on screen//
        restorePreferences();
    }

    //displaying the saved names on screen
    private void displayNames() {
        firstNameEdit.setText(firstName);
        surnameEdit.setText(surname);
    }

    //this method is called when the user exit the app
    private void exit() {
        this.finish();
    }

    // this method is called when the user press keyDown to exit the app - it user the exit_dialog.xml layout
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // only if there was something in the editText
            if ((firstNameEdit.getText().toString().equals(firstName)) &&
                    (surnameEdit.getText().toString().equals(surname))) {
                exit();
                return true;
            } else { // creating the alert dialog only a name of one of the names was changed
                // to inflate the prompt view from the current view
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                // the dialog view is set to the layout file exit_dialog.xml
                View promptsView = layoutInflater.inflate(R.layout.exit_dialog, null);
                //creating a new alert dialog builder
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                // setting  exit_dialog.xml to alertDialog builder
                alertDialogBuilder.setView(promptsView);
                // finding the quit and save button
                final Button quitAndSave = (Button) promptsView.findViewById(R.id.quit_save);
                // finding the just quit button
                final Button justQuit = (Button) promptsView.findViewById(R.id.just_exit);
                // finding the quit and clear button
                final Button quitAndClear = (Button) promptsView.findViewById(R.id.quit_and_clear);
                // creating the alert dialog
                final AlertDialog alertDialog = alertDialogBuilder.create();
                // setting the dialog window buttons - okButton for the saving of the user input and cancelButton for not saving
                quitAndSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //when the user click the button the names are saved before exit
                        savePreferences();
                        exit();
                        // Dismiss the alert dialog
                        alertDialog.cancel();
                    }
                });
                justQuit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //when the user click "just quit" his preferences are not touched//
                        exit();
                        // Dismiss the alert dialog
                        alertDialog.cancel();
                    }
                });
                quitAndClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //when the user click "Quit and clear" his preferences are deleted before exit//
                        clearSavedPreferences();
                        exit();
                        // Dismiss the alert dialog
                        alertDialog.cancel();
                    }
                });
                // showing it on screen
                alertDialog.show();
                return true;
            }
        } else return super.onKeyDown(keyCode, event);
    }

    // this is the method that restores the data that was saved when the user quit the app
    public void restorePreferences() {
        myPreferences = this.getPreferences(Context.MODE_PRIVATE);
        // if there was a firstName saved - restore it, otherwise use ""
        if (myPreferences.contains("firstName")) {
            firstName = myPreferences.getString("firstName", "");
        } else firstName = "";
        // if there was a surname saved - restore it, otherwise use ""
        if (myPreferences.contains("surname")) {
            surname = myPreferences.getString("surname", "");
        } else surname = "";
        // display the names on screen
        displayNames();
    }

    // This method is called when the user press save data(onclick in onCreate) or save in the exit dialog
    public void savePreferences() {
        myPreferences = this.getPreferences(Context.MODE_PRIVATE);
        myEditor = myPreferences.edit();
        myEditor.clear().apply();
        // save the names in the preferences file
        firstName = firstNameEdit.getText().toString();
        surname = surnameEdit.getText().toString();
        myEditor.putString("firstName", firstName);
        myEditor.putString("surname", surname);
        // apply changes to the file
        myEditor.apply();
    }

    // This method is for erasing the data that was stored in the app preferences
    public void clearSavedPreferences() {
        myPreferences = this.getPreferences(Context.MODE_PRIVATE);
        myEditor = myPreferences.edit();
        myEditor.clear().apply();
    }

    //this method saves the data in the app for rotation case
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("firstName", firstNameEdit.getText().toString());
        outState.putString("surname", surnameEdit.getText().toString());
    }

    // this method will restore all the data saves in the previous method, when rotating the device
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        firstNameEdit.setText(savedInstanceState.getString("firstName"));
        surnameEdit.setText(savedInstanceState.getString("surname"));
    }
}
