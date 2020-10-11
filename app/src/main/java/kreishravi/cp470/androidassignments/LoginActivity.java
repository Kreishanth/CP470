package kreishravi.cp470.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "Login Activity";
    Button login_button = null;
    EditText email_text = null;
    String file_name = "";
    SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        login_button = findViewById(R.id.login_button);
        email_text = findViewById(R.id.username_edit_text);
        file_name = getString(R.string.default_email);
        sharedPreferences = getSharedPreferences(file_name, MODE_PRIVATE);
        String email_placeholder = sharedPreferences.getString("DefaultEmail", "email@domain.com");
        email_text.setText(email_placeholder);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

    public void login(View v) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        email_text = findViewById(R.id.username_edit_text);
        String new_email = email_text.getText().toString();
        edit.putString("DefaultEmail", new_email);
        edit.commit();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}