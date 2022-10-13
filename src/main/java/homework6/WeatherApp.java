package homework6;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;

public class WeatherApp {

    public static void main(String[] args) throws IOException {


        OkHttpClient clientHttp = new OkHttpClient();

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("api.weather.yandex.ru")
                .addPathSegment("v2")
                .addPathSegment("forecast")
                .addQueryParameter("lat", "59.9386")
                .addQueryParameter("lon", "30.3141")
                .addQueryParameter("lang", "ru_RU")
                .addQueryParameter("limit", "5")
                .addQueryParameter("hours", "false")
                .addQueryParameter("extra", "false")
                .build();

        System.out.println(url);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-Yandex-API-Key", "c51dd617-a3da-49de-b112-2a5d96a998b2")
                .build();

        String response = clientHttp.newCall(request).execute().body().string();
        System.out.println(response);
    }

}
