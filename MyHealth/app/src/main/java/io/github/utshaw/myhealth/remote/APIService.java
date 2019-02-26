package io.github.utshaw.myhealth.remote;


import io.github.utshaw.myhealth.model.Login;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {

    @POST("/userlogin")
    @FormUrlEncoded
    Call<Login> saveLogin(@Query("mobile") String mobileNumber,
                         @Query("token") String token);


//    @POST("/userlogin")
//    @FormUrlEncoded
//    Call<Login> savePost(@Body Login login);
}