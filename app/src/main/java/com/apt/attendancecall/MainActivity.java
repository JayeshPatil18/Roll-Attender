package com.apt.attendancecall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    boolean isLoading = false;

    boolean isOldDeviceLoggedIn = false;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root, checkStatusRoot;

    String str_emailId, str_password;

    EditText emailId, password;
    TextView invalidDisplay;

    TextView forgotPassword;

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

        forgotPassword = findViewById(R.id.forgotPasswordLogin);

        DoNotAccount = findViewById(R.id.signup);
        login_btn = findViewById(R.id.login_btn);
        login_btn.setText("Login");
        isLoading = false;

        // For firebase authentication
        mAuth = FirebaseAuth.getInstance();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoading) {

                    invalidDisplay.setText("");

                    login_btn.setText("Loading....");
                    isLoading = true;


                    TextInputLayout emailLayout = (TextInputLayout) findViewById(R.id.login_emailIdLayout);
                    TextInputLayout passwordLayout = (TextInputLayout) findViewById(R.id.login_passwordLayout);

                    emailLayout.setErrorEnabled(false);
                    passwordLayout.setErrorEnabled(false);

                    str_emailId = emailId.getText().toString().toLowerCase(Locale.ROOT).trim();
                    str_password = password.getText().toString().trim();

                    ////////////////////////////////////////////////////////////////////////////////////////

                    //////////////////////////////////////////////////////////////////////////////////////////////


                    boolean allFieldFilled = true;


                    if (str_emailId.isEmpty()) {
                        login_btn.setText("Login");
                        isLoading = false;
                        emailLayout.setErrorEnabled(true);
                        emailLayout.setError("* Email id field is empty");
                        allFieldFilled = false;
                    }
                    if (str_password.isEmpty()) {
                        login_btn.setText("Login");
                        isLoading = false;
                        passwordLayout.setErrorEnabled(true);
                        passwordLayout.setError("* Password field is empty");
                        allFieldFilled = false;
                    }
                    if (allFieldFilled) {

//                    ValidationOfInput validation = new ValidationOfInput();
//                    boolean validUsername = validation.validatedEmailId(str_emailId, emailLayout);
//                    boolean validPassword = validation.isValidPasswordForLogin(str_password, passwordLayout);

//                    if (validUsername && validPassword) {

                        mAuth.signInWithEmailAndPassword(str_emailId, str_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    emailId.setText("");
                                    password.setText("");

                                    SharedPreferences sharedPreferences_isLogin = getSharedPreferences("MyPrefsLogin", MODE_PRIVATE);
                                    SharedPreferences.Editor editor_isLogin = sharedPreferences_isLogin.edit();

                                    SharedPreferences sharedPreferences_loginDetails = getSharedPreferences("login_details", MODE_PRIVATE);
                                    SharedPreferences.Editor editor_loginDetails = sharedPreferences_loginDetails.edit();

                                    editor_isLogin.putBoolean("hasLoggedIn", true);
                                    editor_loginDetails.putString("email_id", str_emailId);
                                    editor_isLogin.commit();
                                    editor_loginDetails.commit();

                                    Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, UsertypeActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    login_btn.setText("Login");
                                    isLoading = false;
                                    int index = task.getException().toString().indexOf(":");
                                    String exception = task.getException().toString().toLowerCase(Locale.ROOT).replace(" ", "").trim().substring(index + 1);
                                    if (exception.contains("passwordisinvalid")) {
                                        invalidDisplay.setText("Invalid password!");
                                    } else if (exception.contains("nouserrecord")) {
                                        invalidDisplay.setText("User not exist!");
                                    } else if (exception.contains("badly")) {
                                        invalidDisplay.setText("Invalid input!");
                                    } else {
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
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogBoxResetPassword(MainActivity.this);
            }
        });
    }

    public void showDialogBoxResetPassword(Activity activity) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.forgot_password_dialogbox);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView textMsg = (TextView) dialog.findViewById(R.id.resetErrorMsg);
        EditText emailToResetPassword = (EditText) dialog.findViewById(R.id.resetEmail);

        Button submit_btn = (Button) dialog.findViewById(R.id.resetSubmitBtn);
        Button cancel_btn = (Button) dialog.findViewById(R.id.resetCancelBtn);

        dialog.show();

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textMsg.setText("");

                String strEmailId = emailToResetPassword.getText().toString().trim();

                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                FirebaseUser user = fAuth.getCurrentUser();

                if (strEmailId.isEmpty()) {
                    textMsg.setText("* Please fill email address");
                } else {
                    fAuth.sendPasswordResetEmail(strEmailId).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            invalidDisplay.setText("* Check your email box to reset password");
                            Toast.makeText(activity, "Reset email is sent to " + strEmailId, Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            textMsg.setText(e.getMessage());
                        }
                    });

                }
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    // fail code and do not used any where in project
//    private void isOldDeviceLoggedInFinder() {
//        final boolean[] isUserExist = {false};
//
//        root = db.getReference().child("admin_users");
//        root.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                EncoderDecoder encoderDecoder = new EncoderDecoder();
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    String model = dataSnapshot.getKey();
//
//                    if (model.equals(encoderDecoder.encodeUserEmail(str_emailId))) {
//                        checkStatusRoot = db.getReference().child("admin_users").child(encoderDecoder.encodeUserEmail(str_emailId)).child("status");
//                        checkStatusRoot.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                String strStatus = snapshot.getValue(String.class);
//                                if (strStatus.equals("active")) {
//                                    isOldDeviceLoggedIn = true;
//                                    invalidDisplay.setText("* You already logged in, you need to logout from old device");
//                                    return;
//                                } else {
//                                    return;
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }
}