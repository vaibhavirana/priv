package gifteconomy.dem.com.gifteconomy.signup.API;

import gifteconomy.dem.com.gifteconomy.constant.Cons;
import gifteconomy.dem.com.gifteconomy.signup.model.SignUpRequest;
import gifteconomy.dem.com.gifteconomy.signup.model.SignUpResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by vaibhavirana on 09-03-2016.
 */
public interface SignUpApi {
    @POST(Cons.SIGNUP)
    Call<SignUpResponse> signup(@Body SignUpRequest signupRequest);

}



