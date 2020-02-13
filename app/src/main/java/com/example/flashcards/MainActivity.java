package com.example.flashcards;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Database db = new Database(this);

        final String WRONG_PASSWORD_TEXT="Wrong login data";
        final int DURATION = Toast.LENGTH_SHORT;
        final Toast WRONG_LOGIN_DATA_TOAST = Toast.makeText(this, WRONG_PASSWORD_TEXT, DURATION);
        ContextHolder.setContext(this);

        final TextView LOGIN = findViewById(R.id.loginEditText);
        final TextView PASSWORD = findViewById(R.id.passwordEditText);
        final Button singInButton = findViewById(R.id.singInButton);
        final FloatingActionButton addAccountButton = findViewById(R.id.addAccountButton);

        final Intent flashcardsMenuIntent = new Intent(this,FlashcardsMenu.class);
        final Intent addAccountIntent = new Intent(this,CreateAccountActivity.class);

        singInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Verifier verifier = new Verifier(LOGIN.getText().toString(),PASSWORD.getText().toString(),db);
                if(verifier.verifyAccount()){
                    flashcardsMenuIntent.putExtra("login",LOGIN.getText().toString());
                    startActivity(flashcardsMenuIntent);
                }
                else{
                    PASSWORD.setText(null);
                    WRONG_LOGIN_DATA_TOAST.show();
                }
            }
        });
        addAccountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(addAccountIntent);
            }
        });
    }
}