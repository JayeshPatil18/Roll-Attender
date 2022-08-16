package com.example.attendancecall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String str_emailId, str_password;

    EditText emailId, password;
    TextView invalidDisplay;

    LinearLayout DoNotAccount;
    Button login_btn;

    // For firebase authentication
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailId = findViewById(R.id.login_email_id);
        password = findViewById(R.id.login_password);
        invalidDisplay = findViewById(R.id.login_error);

        DoNotAccount = findViewById(R.id.signup);
        login_btn = findViewById(R.id.login_btn);

        // For firebase authentication
        mAuth = FirebaseAuth.getInstance();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                invalidDisplay.setText("");

                TextInputLayout emailLayout = (TextInputLayout)findViewById(R.id.login_emailIdLayout);
                TextInputLayout passwordLayout = (TextInputLayout)findViewById(R.id.login_passwordLayout);

                emailLayout.setErrorEnabled(false);
                passwordLayout.setErrorEnabled(false);

                str_emailId = emailId.getText().toString().toLowerCase(Locale.ROOT).trim();
                str_password = password.getText().toString().trim();

                boolean allFieldFilled = true;


                if(str_emailId.isEmpty()){
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError("* Email id field is empty");
                    allFieldFilled = false;
                }
                if (str_password.isEmpty()){
                    passwordLayout.setErrorEnabled(true);
                    passwordLayout.setError("* Password field is empty");
                    allFieldFilled = false;
                }
                if(allFieldFilled){

                    ValidationOfInput validation = new ValidationOfInput();
                    boolean validUsername = validation.validatedEmailId(str_emailId, emailLayout);
                    boolean validPassword = validation.isValidPasswordForLogin(str_password, passwordLayout);

                    if (validUsername && validPassword){

                        mAuth.signInWithEmailAndPassword(str_emailId,str_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    emailId.setText("");
                                    password.setText("");

                                    AdminInfo adminInfo = new AdminInfo();
                                    adminInfo.setAdminEmailId(str_emailId);
                                    adminInfo.setAdminPassword(str_password);

                                    Intent intent = new Intent(MainActivity.this, VerifyingEmail.class);
                                    intent.putExtra("email_str",str_emailId);
                                    intent.putExtra("password_str",str_password);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    int index = task.getException().toString().indexOf(":");
                                    String exception = task.getException().toString().toLowerCase(Locale.ROOT).replace(" ","").trim().substring(index + 1);
                                    if (exception.contains("passwordisinvalid")){
                                        invalidDisplay.setText("Invalid password!");
                                    }else if (exception.contains("nouserrecord")){
                                        invalidDisplay.setText("User not exist!");
                                    }else if (exception.contains("badly")){
                                        invalidDisplay.setText("Invalid input!");
                                    }else{
                                        invalidDisplay.setText(task.getException().toString().trim().substring(index + 1));
                                    }
                                }
                            }
                        });


                    }
                }
            }
        });

        DoNotAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}