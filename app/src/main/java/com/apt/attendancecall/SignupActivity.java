package com.apt.attendancecall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity{

    boolean isLoading = false;

    ImageView backIcon;

    LinearLayout AlreadyAccount;
    Button signup_btn;

    EditText name, emailId, password, rEnterdPassword;
    TextView invalidDisplay;

    // For firebase authentication
    FirebaseAuth mAuth;

    // For saving details to firebase database
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        backIcon = findViewById(R.id.signup_back_img);

        name = findViewById(R.id.signup_fullname);
        emailId = findViewById(R.id.signup_email_id);
        password = findViewById(R.id.signup_password);
        rEnterdPassword = findViewById(R.id.signup_Repassword);
        invalidDisplay = findViewById(R.id.signup_error);

        AlreadyAccount = findViewById(R.id.login);
        signup_btn = findViewById(R.id.submit_btn);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // For if device user have account
//        if (mAuth.getCurrentUser() != null){
//            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

//        String intentMsg = getIntent().getStringExtra("str_of_resignUp");
//        invalidDisplay.setText(intentMsg);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoading) {
                    invalidDisplay.setText("");

                    signup_btn.setText("Loading....");
                    isLoading = true;

                    TextInputLayout nameLayout = (TextInputLayout) findViewById(R.id.signup_fullnameLayout);
                    TextInputLayout emailIdLayout = (TextInputLayout) findViewById(R.id.signup_email_idLayout);
                    TextInputLayout passwordLayout = (TextInputLayout) findViewById(R.id.signup_passwordLayout);
                    TextInputLayout rEnterPasswordLayout = (TextInputLayout) findViewById(R.id.signup_RepasswordLayout);

                    nameLayout.setErrorEnabled(false);
                    emailIdLayout.setErrorEnabled(false);
                    passwordLayout.setErrorEnabled(false);
                    rEnterPasswordLayout.setErrorEnabled(false);

                    String str_name, str_emailId, str_rEnterPassword, str_password;
                    str_name = name.getText().toString().toLowerCase(Locale.ROOT).trim();
                    str_emailId = emailId.getText().toString().toLowerCase(Locale.ROOT).trim();
                    str_password = password.getText().toString().trim();
                    str_rEnterPassword = rEnterdPassword.getText().toString().trim();

                    boolean allFieldFilled = true;

                    if (str_name.isEmpty()) {
                        signup_btn.setText("Sign Up");
                        isLoading = false;
                        nameLayout.setErrorEnabled(true);
                        nameLayout.setError("* Name field is empty");
                        allFieldFilled = false;
                    }
                    if (str_emailId.isEmpty()) {
                        signup_btn.setText("Sign Up");
                        isLoading = false;
                        emailIdLayout.setErrorEnabled(true);
                        emailIdLayout.setError("* Email id field is empty");
                        allFieldFilled = false;
                    }
                    if (str_password.isEmpty()) {
                        signup_btn.setText("Sign Up");
                        isLoading = false;
                        passwordLayout.setErrorEnabled(true);
                        passwordLayout.setError("* Password field is empty");
                        allFieldFilled = false;
                    }
                    if (str_rEnterPassword.isEmpty()) {
                        signup_btn.setText("Sign Up");
                        isLoading = false;
                        rEnterPasswordLayout.setErrorEnabled(true);
                        rEnterPasswordLayout.setError("* Field is empty");
                        allFieldFilled = false;
                    }
                    if (allFieldFilled) {
                        ValidationOfInput validation = new ValidationOfInput();
                        boolean validName = validation.validatedName(str_name, nameLayout);
                        boolean validEmailId = validation.validatedEmailId(str_emailId, emailIdLayout);
                        boolean validPassword = validation.isValidCreation(str_password, passwordLayout);
                        boolean validRePassword = validation.isPasswordMatched(str_password, str_rEnterPassword, rEnterPasswordLayout);

                        if (validName && validEmailId && validPassword && validRePassword) {


                            // For authentication with email and password
                            mAuth.createUserWithEmailAndPassword(str_emailId, str_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        EncoderDecoder encoder = new EncoderDecoder();

                                        HashMap<String, String> userMap = new HashMap<>();
                                        userMap.put("email", str_emailId);
                                        userMap.put("name", str_name);

                                        root = db.getReference().child("admin_users");
                                        root.child(encoder.encodeUserEmail(str_emailId)).child("details").setValue(userMap);

                                        root = db.getReference().child("admin_users").child(encoder.encodeUserEmail(str_emailId));
                                        root.child("request_to").setValue("users");

                                        name.setText("");
                                        emailId.setText("");
                                        rEnterdPassword.setText("");
                                        password.setText("");

                                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        signup_btn.setText("Sign Up");
                                        isLoading = false;
                                        int index = task.getException().toString().indexOf(":");
//                                    String exception = task.getException().toString().toLowerCase(Locale.ROOT).trim().replace(" ","").substring(index + 1);
                                        invalidDisplay.setText(task.getException().toString().trim().substring(index + 1));
                                    }
                                }
                            });
                        }
                        signup_btn.setText("Sign Up");
                        isLoading = false;
                    }
                }
            }
        });

        AlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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