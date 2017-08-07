package android.example.com.webservicemanager.managers;

import android.example.com.webservicemanager.retrofit.WeatherApiInterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Ashwani on 30-07-2017.
 */

public class WeatherServiceManager {
    private static Retrofit retrofit = null;
    private static WeatherServiceManager weatherServiceManager;
    public static final String BASE_URL = "http://api.wunderground.com/";
    private WeatherApiInterface weatherApiInterface = null;

    private WeatherServiceManager(){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        weatherApiInterface = retrofit.create(WeatherApiInterface.class);
    }

    public static WeatherServiceManager getInstance(){
        if(weatherServiceManager == null){
            weatherServiceManager = new WeatherServiceManager();
        }
        return weatherServiceManager;
    }

    public WeatherApiInterface getWeatherApiInterface(){
        return weatherApiInterface;
    }

    private static Map<String,String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        return headers;
    }

    private static OkHttpClient getHttpClient(final Map<String, String> headers){
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                okhttp3.Request original = chain.request();
                okhttp3.Request.Builder requestBuilder = original.newBuilder();
                for(Map.Entry<String, String> pairs : headers.entrySet()){
                        if(pairs.getValue() != null){
                            requestBuilder.header(pairs.getKey(), pairs.getValue());
                        }
                }
                requestBuilder.method(original.method(), original.body());
                okhttp3.Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        return httpClient.build();
    }
}
