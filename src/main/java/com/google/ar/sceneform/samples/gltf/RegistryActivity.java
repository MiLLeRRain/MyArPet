package com.google.ar.sceneform.samples.gltf;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class RegistryActivity extends AppCompatActivity {

    private TextInputEditText username, password, repassword;
    private Button signup;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        username = (TextInputEditText) findViewById(R.id.username);
        password = (TextInputEditText) findViewById(R.id.password);
        repassword = (TextInputEditText) findViewById(R.id.repassword);

        signup = (Button) findViewById(R.id.signup_btn);
        dbHelper = new DBHelper(this);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                Boolean checkuser = dbHelper.checkusername(user);

                if (blankEntry(user, pass)) {
                    toastToUser("Empty Entry, please check.");
                    return;
                }

                else if (!pass.equals(repass)) {
                    toastToUser("The passwords you entered do not match.");
                    return;
                }

                else if (pass.length() < 6) {
                    toastToUser("Password must be at least 6 characters.");
                    return;
                }

                if (!checkuser) {
                    Boolean insert = dbHelper.insertDate(user, pass);
                    if (insert) {
                        toastToUser("Registry completed.");
                    } else toastToUser("Registry failed.");
                } else {
                    toastToUser("The user with same name exists.");
                    return;
                }

                // Go to MainActivity
                Intent intent = new Intent(RegistryActivity.this, MainActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("pass", pass);
                intent.putExtra("regi", true);
                startActivity(intent);
            }
        });
    }

    private void toastToUser(String s) {
        Toast.makeText(RegistryActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    private boolean blankEntry(String user, String pass) {
        return TextUtils.isEmpty(user) || TextUtils.isEmpty(pass);
    }
}
