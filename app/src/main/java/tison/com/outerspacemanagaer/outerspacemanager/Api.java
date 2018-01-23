package tison.com.outerspacemanagaer.outerspacemanager;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by atison on 16/01/2018.
 */

public interface Api {

    // Connection
    @POST("/api/v1/auth/login")
    Call<AuthResponse> Connection(@Body User user);

    // Create account
    @POST("/api/v1/auth/create")
    Call<AuthResponse> CreateAccount(@Body User user);

    // GetUserInfo
    @GET("/api/v1/users/get")
    Call<UserResponse> GetUserInfo(@Header("x-access-token") String token);

    // GetUsers
    @GET("/api/v1/users/0/20")
    Call<UserTable> GetUsers(@Header("x-access-token") String token);

    // GetSearchesForUser
    @GET("/api/v1/searches/list")
    Call<Searches> GetSearchesForUser(@Header("x-access-token") String token);

    // StartSearchForUser
    @GET("/api/v1/searches/create/{searchId}")
    Call<CodeResponse> StartSearchesForUser(@Header("x-access-token") String token, @Path("searchId") String searchId);

    // GetShips
    @GET("/api/v1/ships")
    Call<Ships> GetShips(@Header("x-access-token") String token);

    // GetShip
    @GET("/api/v1/ships/{shipId}")
    Call<Ship> GetShips(@Header("x-access-token") String token, @Path("shipId") String shipId);

    // CreateShips
    @POST("/api/v1/ships/create/{shipId}")
    Call<CodeResponse> CreateShips(@Header("x-access-token") String token, @Body Ship ship);

    // AttackUser
    @POST("/api/v1/fleet/attack/{userId}")
    Call<CodeResponse> AttackUser(@Header("x-access-token") String token, @Body Ships ships, @Path("userId") String userId);

    // GetBuildings
    @GET("/api/v1/buildings/list")
    Call<Buildings> GetBuildings(@Header("x-access-token") String token);

    // CreateBuilding
    @POST("/api/v1/buildings/create/{buildingId}")
    Call<CodeResponse> CreateBuilding(@Header("x-access-token") String token, @Path("buildingId") String buildingId);
}
