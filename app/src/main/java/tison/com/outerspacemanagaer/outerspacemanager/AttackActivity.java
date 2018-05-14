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
import android.widget.TextView;
import android.widget.Toast;

import com.dynamitechetan.flowinggradient.FlowingGradientClass;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AttackActivity extends AppCompatActivity implements View.OnClickListener{

    public String user_to_attack;

    public TextView textViewUsername;

    public EditText number0;
    public EditText number1;
    public EditText number2;
    public EditText number3;
    public EditText number4;

    public ImageButton attackButton;

    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;

    private BackgroundView backgroundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attack);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        backgroundView = (BackgroundView) findViewById(R.id.backgroundViewAttack);
        backgroundView.animate(this, size.x, size.y);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");


        String json = getIntent().getStringExtra("USER_TO_ATTACK");
        Gson gson = new Gson();
        user_to_attack = gson.fromJson(json, String.class);

        textViewUsername = (TextView) findViewById(R.id.nameUser);
        textViewUsername.setText(user_to_attack);

        number0 = (EditText) findViewById(R.id.number0);
        number1 = (EditText) findViewById(R.id.number1);
        number2 = (EditText) findViewById(R.id.number2);
        number3 = (EditText) findViewById(R.id.number3);
        number4 = (EditText) findViewById(R.id.number4);

        attackButton = (ImageButton) findViewById(R.id.btnAttack);
        attackButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Ships ships = new Ships();
        Ship[] shipList = new Ship[5];
        String num0 = number0.getText().toString();
        shipList[0] = new Ship("0", num0);
        String num1 = number1.getText().toString();
        shipList[1] = new Ship("1", num1);
        String num2 = number2.getText().toString();
        shipList[2] = new Ship("2", num2);
        String num3 = number3.getText().toString();
        shipList[3] = new Ship("3", num3);
        String num4 = number4.getText().toString();
        shipList[4] = new Ship("4", num4);
        ships.setShips(shipList);


        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Call<CodeResponse> request = service.AttackUser(token, ships, user_to_attack);

        request.enqueue(new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                if(response.code() != 200){

                    switch (response.code()){
                        case 401:
                            //Pas assez de ressources
                            Toast.makeText(getApplicationContext(), "Vous n'avez pas assez de ressources", Toast.LENGTH_LONG).show();
                            break;
                        case 403 :
                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                            settings.edit().remove("token").apply();
                            Toast.makeText(getApplicationContext(), "Veuillez vous réauthentifier s'il vous plait", Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                            startActivity(myIntent);
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
                    Toast.makeText(getApplicationContext(), "L'attaque à été lancée !", Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(myIntent);
                }
            }

            @Override
            public void onFailure(Call<CodeResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Aucune réponse, vérifiez votre connection internet", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backgroundView.pause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        backgroundView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        backgroundView.resume();
    }
}
