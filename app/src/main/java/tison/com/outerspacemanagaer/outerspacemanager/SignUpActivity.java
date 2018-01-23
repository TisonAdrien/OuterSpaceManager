package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnValider;
    private EditText inputUsername;
    private EditText inputPassword;
    private Button btnCreate;
    private String token;

    public static final String PREFS_NAME = "TOKEN_FILE";

    //SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    //private String token = settings.getString("token","");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnValider = (Button) findViewById(R.id.btnValider);
        btnCreate = (Button) findViewById(R.id.btnAjouter);
        inputUsername = (EditText) findViewById(R.id.inputUsername);
        inputPassword = (EditText) findViewById(R.id.inputPassword);

        btnCreate.setOnClickListener(this);
        btnValider.setOnClickListener(this);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");

        if(token != ""){
            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(myIntent);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnValider.getId())
        {
            Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
            Api service = retrofit.create(Api.class);
            Call<AuthResponse> request = service.Connection(new User(inputUsername.getText().toString(), inputPassword.getText().toString(), ""));
            request.enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if(response.code() != 200){
                        Toast.makeText(getApplicationContext(), "Identifiant ou mot de passe incorrecte", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Connection...", Toast.LENGTH_LONG).show();
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("token", response.body().getToken());
                        // Commit the edits!
                        editor.commit();

                        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(myIntent);
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            Intent myIntent = new Intent(getApplicationContext(), CreateAccountActivity.class);
            startActivity(myIntent);
        }


    }
}
