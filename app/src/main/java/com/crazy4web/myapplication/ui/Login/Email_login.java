package com.crazy4web.myapplication.ui.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crazy4web.myapplication.MainActivity;
import com.crazy4web.myapplication.R;
import com.crazy4web.myapplication.ui.signup.SignUpOptions;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Any;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Email_login extends AppCompatActivity {

    TextView general_signup;
    Button button_login;
//    private FirebaseAuth mAuth;
    TextInputLayout textInputLayout,textInputLayout2;
    private static final String TAG = "Email_login";
    FirebaseFirestore database;
    private String prefFile = "com.crazy4web.myapplication.userdata";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
//        Log.d(TAG, ""+isLoggedIn);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent2);
        }

        database = FirebaseFirestore.getInstance();
        textInputLayout = findViewById(R.id.textInputLayout);
        textInputLayout2 = findViewById(R.id.textInputLayout2);


        textInputLayout2.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        general_signup = findViewById(R.id.general_signup);
        general_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), SignUpOptions.class);
                startActivity(i);
            }
        });

        button_login = findViewById(R.id.button_login);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email = textInputLayout.getEditText().getText().toString();
                String password = textInputLayout2.getEditText().getText().toString();

//                  Log.d(TAG, "onClick: "+email+password);
                SharedPreferences sp = getSharedPreferences("prefFile", Context.MODE_PRIVATE);
                ArrayList<String> arr = new ArrayList<>();
                database.collection("users").whereEqualTo("email",email).whereEqualTo("password",password).get()
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful() && task.getResult().size() > 0) {

//                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                                startActivity(i);
                                Log.d(TAG, " " + task.getResult().getDocuments());
//
//                                QuerySnapshot xyz = task.getResult();
//
//                                xyz.getDocuments().forEach(abcd ->{
//                                    Log.d(TAG, "onClick: "+abcd);
//                                });


                                for (QueryDocumentSnapshot document : task.getResult()) {   // LOOP
                                    Log.d(TAG,""+document.getData());

                                    Map abcd = new HashMap();
                                    abcd = document.getData();

                                    abcd.forEach((key,value) ->{
                                        Log.d(TAG, "key -> "+key+" value -> "+ value);
                                        arr.add(value.toString());
                                    });
                                }
//                                Log.d(TAG, "onClick: "+arr.get(0)+arr.get(1));
//                                Log.d(TAG, "emailId: "+email);
                                sp.edit().putString("emailId", email).apply();
                                Log.d(TAG, "emailId: "+sp.getString("emailId",""));
                                sp.edit().putString("emailName", arr.get(0)+" "+arr.get(1)).apply();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                            }else{
                                Log.d(TAG, "onClick: Nothing found");
                            }
                        });
            }
        });

    }
    @Override
    public void onBackPressed() {}
}
