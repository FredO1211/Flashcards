package com.example.flashcards;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccountActivity extends AppCompatActivity {

    private final Database db= new Database(this);

    private EditText loginEditText;
    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private CardView acceptCardView;
    private CardView cancelCardView;

    private final int DURATION = Toast.LENGTH_SHORT;

    final String EXISTING_LOGIN_TOAST_TEXT="Login is already exist!";
    final String DIFFERENT_PASSWORDS_TOAST_TEXT ="Passwords aren't the same!";

    private boolean ifEquals(String a,String b){
        return a.equals(b);
    }

    private void addAccount(String name, String login, String password, Database db){
        db.addUser(name,login,password);
    }

    private void verifyingUserData(EditText login,  EditText password, EditText confirmPassword, Database db, Toast loginExisting, Toast differentPasswords){
        if(db.doesTheLoginExist(login.getText().toString())){
            loginExisting.show();
            login.setText(null);
            password.setText(null);
            confirmPassword.setText(null);
        }
        else if(!ifEquals(password.getText().toString(),confirmPassword.getText().toString())){
            differentPasswords.show();
            password.setText(null);
            confirmPassword.setText(null);
        }
        else{
            addAccount(password.getText().toString(),login.getText().toString(),password.getText().toString(),db);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        final Toast EXISTING_LOGIN_DATA_TOAST = Toast.makeText(getApplicationContext(), EXISTING_LOGIN_TOAST_TEXT, DURATION);
        final Toast DIFFERENT_PASSWORDS_DATA_TOAST = Toast.makeText(getApplicationContext(), DIFFERENT_PASSWORDS_TOAST_TEXT, DURATION);

        loginEditText = findViewById(R.id.loginEditText);
        nameEditText = findViewById(R.id.nameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        acceptCardView = findViewById(R.id.acceptCardView);
        cancelCardView = findViewById(R.id.cancelCardView);


        cancelCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEditText.setText(null);
                nameEditText.setText(null);
                passwordEditText.setText(null);
                confirmPasswordEditText.setText(null);
                finish();
            }
        });
        acceptCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyingUserData(loginEditText,passwordEditText,confirmPasswordEditText,db,EXISTING_LOGIN_DATA_TOAST,DIFFERENT_PASSWORDS_DATA_TOAST);
            }
        });

    }
}
