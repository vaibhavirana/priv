package gifteconomy.dem.com.gifteconomy.login.API;

import gifteconomy.dem.com.gifteconomy.constant.Cons;
import gifteconomy.dem.com.gifteconomy.login.model.LoginRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by vaibhavirana on 09-03-2016.
 */
public interface LoginApi {
    @POST(Cons.LOGIN)
    Call<LoginRequest> login(@Body LoginRequest loginRequest);
    //Call<LoginResponse> login(@Body LoginRequest loginRequest);
   // Call<LoginResponse> login(@Path("username") String emailid, @Path("password") String password);
}



