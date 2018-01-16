package tison.com.outerspacemanagaer.outerspacemanager;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by atison on 16/01/2018.
 */

public interface Api {

    // Connection
    @POST("/api/v1/auth/login") Call<AuthResponse> Connection(@Body User user);

    // Create account
    @POST("/api/v1/auth/create") Call<AuthResponse> CreateAccount(@Body User user);
}
