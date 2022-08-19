package com.example.attendancecall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;

public class EditProfile extends AppCompatActivity {

    FirebaseUser firebaseUser;
    FirebaseAuth fAuth;

    ImageView backIcon;
    Button saveChanges_btn;

    EditText name, password, rEnterdPassword;
    TextView invalidDisplay;

    // For saving details to firebase database
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        root = db.getReference().child("admin_users");

        fAuth = FirebaseAuth.getInstance();

        firebaseUser = fAuth.getCurrentUser();

        backIcon = findViewById(R.id.editProfile_back_img);

        name = findViewById(R.id.editProfile_fullname);
        password = findViewById(R.id.editProfile_password);
        rEnterdPassword = findViewById(R.id.editProfile_Repassword);
        invalidDisplay = findViewById(R.id.editProfile_error);

        saveChanges_btn = findViewById(R.id.saveChangeBtn);

        saveChanges_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invalidDisplay.setText("");

                TextInputLayout nameLayout = (TextInputLayout) findViewById(R.id.editProfile_fullnameLayout);
                TextInputLayout passwordLayout = (TextInputLayout) findViewById(R.id.editProfile_passwordLayout);
                TextInputLayout rEnterPasswordLayout = (TextInputLayout) findViewById(R.id.editProfile_RepasswordLayout);

                nameLayout.setErrorEnabled(false);
                passwordLayout.setErrorEnabled(false);
                rEnterPasswordLayout.setErrorEnabled(false);

                ValidationOfEditProfile validationOfEditProfile = new ValidationOfEditProfile();

                String str_name, str_emailId, str_rEnterPassword, str_password;
                str_name = name.getText().toString().toLowerCase(Locale.ROOT).trim();
                str_password = password.getText().toString().trim();
                str_rEnterPassword = rEnterdPassword.getText().toString().trim();

                SharedPreferences sharedPreferences_emailId = getSharedPreferences("login_details",MODE_PRIVATE);
                str_emailId = sharedPreferences_emailId.getString("email_id","null");

                if ((!str_name.isEmpty()) && (!str_password.isEmpty())){
                    validationOfEditProfile.updateName_Password(str_name, str_password, str_rEnterPassword, nameLayout, passwordLayout, rEnterPasswordLayout, str_emailId, fAuth, firebaseUser, root, EditProfile.this);
                }else if ((!str_name.isEmpty()) && str_rEnterPassword.isEmpty()){
                    validationOfEditProfile.updateName(str_name, nameLayout, str_emailId, fAuth, firebaseUser, root, EditProfile.this);
                }else if ((!str_password.isEmpty())){
                    validationOfEditProfile.updatePassword(str_password, str_rEnterPassword, passwordLayout, rEnterPasswordLayout, str_emailId, fAuth, firebaseUser, root, EditProfile.this);
                }else{
                    invalidDisplay.setText("* Please fill details which you want to update");
                }
            }
        });

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
