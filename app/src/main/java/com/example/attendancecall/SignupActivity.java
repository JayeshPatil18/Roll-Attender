package com.example.attendancecall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity{
    LinearLayout AlreadyAccount;
    Button signup_btn;

    EditText name, emailId, phoneNo, password;
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

        name = findViewById(R.id.signup_fullname);
        emailId = findViewById(R.id.signup_email_id);
        phoneNo = findViewById(R.id.signup_phoneno);
        password = findViewById(R.id.signup_password);
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

        String intentMsg = getIntent().getStringExtra("str_of_resignUp");
        invalidDisplay.setText(intentMsg);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invalidDisplay.setText("");

                TextInputLayout nameLayout = (TextInputLayout)findViewById(R.id.signup_fullnameLayout);
                TextInputLayout emailIdLayout = (TextInputLayout)findViewById(R.id.signup_email_idLayout);
                TextInputLayout phoneNoLayout = (TextInputLayout)findViewById(R.id.signup_phonenoLayout);
                TextInputLayout passwordLayout = (TextInputLayout)findViewById(R.id.signup_passwordLayout);

                nameLayout.setErrorEnabled(false);
                emailIdLayout.setErrorEnabled(false);
                phoneNoLayout.setErrorEnabled(false);
                passwordLayout.setErrorEnabled(false);

                String str_name, str_emailId, str_phoneNo, str_password;
                str_name = name.getText().toString().toLowerCase(Locale.ROOT).trim();
                str_emailId = emailId.getText().toString().toLowerCase(Locale.ROOT).trim();
                str_phoneNo = phoneNo.getText().toString().trim();
                str_password = password.getText().toString().trim();

                boolean allFieldFilled = true;

                if(str_name.isEmpty()){
                    nameLayout.setErrorEnabled(true);
                    nameLayout.setError("* Name field is empty");
                    allFieldFilled = false;
                }
                if(str_emailId.isEmpty()){
                    emailIdLayout.setErrorEnabled(true);
                    emailIdLayout.setError("* Email id field is empty");
                    allFieldFilled = false;
                }
                if (str_phoneNo.isEmpty()){
                    phoneNoLayout.setErrorEnabled(true);
                    phoneNoLayout.setError("* Phone no field is empty");
                    allFieldFilled = false;
                }
                if (str_password.isEmpty()){
                    passwordLayout.setErrorEnabled(true);
                    passwordLayout.setError("* Password field is empty");
                    allFieldFilled = false;
                }
                if (allFieldFilled){
                    ValidationOfInput validation = new ValidationOfInput();
                    boolean validName = validation.validatedName(str_name, nameLayout);
                    boolean validUsername = validation.validatedEmailId(str_emailId, emailIdLayout);
                    boolean validPhoneNo = validation.validatedPhoneNo(str_phoneNo, phoneNoLayout);
                    boolean validPassword = validation.isValidCreation(str_password, passwordLayout);

                    if (validName && validUsername && validPhoneNo && validPassword){


                        // For authentication with email and password
                        mAuth.createUserWithEmailAndPassword(str_emailId,str_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    EncoderDecoder encoder = new EncoderDecoder();

                                    HashMap<String, String> userMap = new HashMap<>();
                                    userMap.put("email", str_emailId);
                                    userMap.put("name", str_name);
                                    userMap.put("phone_no", str_phoneNo);
                                    userMap.put("password", str_password);

                                    root = db.getReference().child("admin_users");

                                    root.child(encoder.encodeUserEmail(str_emailId)).child("details").setValue(userMap);

                                    name.setText("");
                                    emailId.setText("");
                                    phoneNo.setText("");
                                    password.setText("");

                                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }else{
                                    int index = task.getException().toString().indexOf(":");
                                    String exception = task.getException().toString().toLowerCase(Locale.ROOT).trim().replace(" ","").substring(index + 1);
                                    invalidDisplay.setText(task.getException().toString().trim().substring(index + 1));
                                }
                            }
                        });
                    }
                }

            }
        });

        AlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}