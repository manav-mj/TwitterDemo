package in.codingninjas.twitterdemo.network;

import in.codingninjas.twitterdemo.TwitterUtils;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by YourFather on 19-03-2017.
 */

public class ApiClient {
    private static Retrofit retrofit;
    private static ApiInterface apiInterface;
    public static ApiInterface getApiInterface(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.twitter.com/1.1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    // Twitter interceptor
                    .client(new OkHttpClient.Builder()
                            .addInterceptor(TwitterUtils.getInterceptor())
                            .build())
                    .build();

            apiInterface = retrofit.create(ApiInterface.class);
        }
        return apiInterface;
    }
}
