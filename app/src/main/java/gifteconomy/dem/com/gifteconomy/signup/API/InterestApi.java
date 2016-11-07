package gifteconomy.dem.com.gifteconomy.signup.API;

import gifteconomy.dem.com.gifteconomy.constant.Cons;
import gifteconomy.dem.com.gifteconomy.signup.model.InterestResponse;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by vaibhavirana on 09-03-2016.
 */
public interface InterestApi {
    @GET(Cons.INTEREST)
    Call<InterestResponse> loadInterest();
}



