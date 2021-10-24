package com.google.ar.sceneform.samples.gltf;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText username, password;
    Button signupBtn, loginBtn;
    private DBHelper dbHelper;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        username = (TextInputEditText) findViewById(R.id.login_username);
        password = (TextInputEditText) findViewById(R.id.login_password);

        signupBtn = (Button) findViewById(R.id.to_regi_btn);
        signupBtn.setOnClickListener(this);
        loginBtn = (Button) findViewById(R.id.ar_launch);
        loginBtn.setOnClickListener(this);
        ImageButton shareBtn = (ImageButton) findViewById(R.id.shareButton);
        shareBtn.setOnClickListener(this);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        rememberChecker = (CheckBox) findViewById(R.id.checkBox);
        boolean isRemember = pref.getBoolean("remember_credentials", false);
        if (isRemember) {
            String user = pref.getString("user", "");
            String pass = pref.getString("pass", "");

            username.setText(user);
            password.setText(pass);

            rememberChecker.setChecked(true);
        }

        if (getIntent().getBooleanExtra("regi", false)) {
            username.setText(getIntent().getStringExtra("user"));
            password.setText(getIntent().getStringExtra("pass"));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_regi_btn:
                Intent intentRegistry = new Intent("regiScreen");
                startActivity(intentRegistry);
                break;
            case R.id.ar_launch:
                String user = Objects.requireNonNull(username.getText()).toString();
                String pass = Objects.requireNonNull(password.getText()).toString();

                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("Notice");
                    dialog.setMessage("Please enter name and password.");
                    dialog.show();
                } else {
                    Boolean checkuserpass = dbHelper.checkusernamepassword(user, pass);
                    if (checkuserpass) {
                        editSharedPref(user, pass);
                        Intent intentAr = new Intent("arScreen");
                        intentAr.putExtra("loginName", user);
                        toastToUser("Welcome, " + user + ".");
                        startActivity(intentAr);
                    } else toastToUser("Invalid credentials.");
                }
                break;
            case R.id.shareButton:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Sharing My AR Pet.");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                break;
            default:
                break;
        }
    }

    private void editSharedPref(String user, String pass) {
        editor = pref.edit();
        if (rememberChecker.isChecked()) {
            editor.putBoolean("remember_credentials", true);
            editor.putString("user", user);
            editor.putString("pass", pass);
        } else editor.clear();
        editor.apply();
    }

    private void toastToUser(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}