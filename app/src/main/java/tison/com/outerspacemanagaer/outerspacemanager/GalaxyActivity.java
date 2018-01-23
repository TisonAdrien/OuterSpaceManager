package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GalaxyActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private ArrayList<UserResponse> listUsers;
    private ListView viewUsers;

    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galaxy);
        viewUsers = (ListView) findViewById(R.id.listViewGalaxy);
        viewUsers.setOnItemClickListener(this);



        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");

        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Call<UserTable> request = service.GetUsers(token);

        request.enqueue(new Callback<UserTable>() {
            @Override
            public void onResponse(Call<UserTable> call, Response<UserTable> response) {
                if(response.code() != 200){
                    //Toast.makeText(getApplicationContext(), "Une erreur est survenue !", Toast.LENGTH_LONG).show();

                    try {
                        Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    //Toast.makeText(getApplicationContext(), "Connection...", Toast.LENGTH_LONG).show();
                    listUsers = response.body().getUsers();
                    viewUsers.setAdapter(new ArrayAdapter(getApplicationContext(),  android.R.layout.simple_list_item_1, listUsers));
                }
            }

            @Override
            public void onFailure(Call<UserTable> call, Throwable t) {
                Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
