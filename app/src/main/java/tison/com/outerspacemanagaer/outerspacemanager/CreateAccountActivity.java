package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dynamitechetan.flowinggradient.FlowingGradientClass;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnValider;
    private EditText inputEmail;
    private EditText inputUsername;
    private EditText inputPassword;
    private ImageButton btnConnect;

    public static final String PREFS_NAME = "TOKEN_FILE";
    private BackgroundView backgroundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        btnConnect = (ImageButton) findViewById(R.id.btnConnect);
        btnValider = (ImageButton) findViewById(R.id.btnValider);
        inputUsername = (EditText) findViewById(R.id.inputUsername);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        inputEmail = (EditText) findViewById(R.id.inputEmail);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        backgroundView = (BackgroundView) findViewById(R.id.backgroundViewCreate);
        backgroundView.animate(this, size.x, size.y);

        btnValider.setOnClickListener(this);
        btnConnect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnValider.getId())
        {
            Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
            Api service = retrofit.create(Api.class);
            Call<AuthResponse> request = service.CreateAccount(new User(inputUsername.getText().toString(), inputPassword.getText().toString(), inputEmail.getText().toString()));
            request.enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if(response.code() != 200){
                        switch (response.code()){
                            case 401:
                                //Pas assez de ressources
                                Toast.makeText(getApplicationContext(), "Vous n'avez pas assez de ressources", Toast.LENGTH_LONG).show();
                                break;
                            case 400 :
                                Toast.makeText(getApplicationContext(), "Nom de compte ou mail déjà utilisé", Toast.LENGTH_LONG).show();
                                break;
                            case 500:
                                //Erreur serveur
                                Toast.makeText(getApplicationContext(), "Une erreur interne s'est produite, réessayez plus tard...", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                //Stop crack
                                Toast.makeText(getApplicationContext(), "Une erreur s'est produite, elle vient de vous. Vous êtes une erreur.", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Votre compte a bien été créé !", Toast.LENGTH_LONG).show();

                        Retrofit retrofit2 = new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
                        Api service2 = retrofit2.create(Api.class);
                        Call<AuthResponse> request2 = service2.Connection(new User(inputUsername.getText().toString(), inputPassword.getText().toString(), ""));
                        request2.enqueue(new Callback<AuthResponse>() {
                            @Override
                            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                                if(response.code() != 200){
                                    Toast.makeText(getApplicationContext(), "Identifiant ou mot de passe incorrect", Toast.LENGTH_LONG).show();
                                }else{
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
                                Toast.makeText(getApplicationContext(), "Aucune réponse, vérifiez votre connection internet", Toast.LENGTH_LONG).show();
                            }
                        });


                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Aucune réponse, vérifiez votre connection internet", Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            Intent myIntent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(myIntent);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        backgroundView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backgroundView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        backgroundView.resume();
    }
}
