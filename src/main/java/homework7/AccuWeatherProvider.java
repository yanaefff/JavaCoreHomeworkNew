package homework7;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import homework7.enums.Periods;
import homework7.homework8.DataBaseConnection;
import homework7.homework8.WeatherData;
import homework7.weather1d.WeatherResponse1day;
import homework7.weather5d.WeatherResponse;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class AccuWeatherProvider implements WeatherProvider {

    private static final String BASE_HOST = "dataservice.accuweather.com";
    private static final String FORECAST_ENDPOINT = "forecasts";
    private static final String CURRENT_CONDITIONS_ENDPOINT = "currentconditions";
    private static final String API_VERSION = "v1";
    private static final String API_KEY = ApplicationGlobalState.getInstance().getApiKey();

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void getWeather(Periods periods) throws IOException {
        String cityKey = detectCityKey();
        if (periods.equals(Periods.NOW)) {
            HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(BASE_HOST)
                .addPathSegment(CURRENT_CONDITIONS_ENDPOINT)
                .addPathSegment(API_VERSION)
                .addPathSegment(cityKey)
                .addQueryParameter("apikey", API_KEY)
                    .addQueryParameter("language", "RU")
                .build();

            Request request = new Request.Builder()
                .addHeader("accept", "application/json")
                .url(url)
                .build();

            Response response = client.newCall(request).execute();


            ObjectMapper objectMapper = new ObjectMapper();

            String json = response.body().string();
            json = json.substring(1, json.length() - 1);


            try {
                WeatherResponse1day weatherList1day = objectMapper.readValue(json, WeatherResponse1day.class);
                System.out.println("Текущая погода: " + weatherList1day.getWeatherText()
                        + ". Температура: " + weatherList1day.getTemperature().getMetric().getValue() + "С");
            }
            catch (JsonProcessingException ex) {
               ex.printStackTrace();
            }


        } else if(periods.equals(Periods.FIVE_DAYS)){

            HttpUrl url = new HttpUrl.Builder()
                    .scheme("http")
                    .host(BASE_HOST)
                    .addPathSegment(FORECAST_ENDPOINT)
                    .addPathSegment(API_VERSION)
                    .addPathSegment("daily")
                    .addPathSegment("5day")
                    .addPathSegment(cityKey)
                    .addQueryParameter("apikey", API_KEY)
                    .addQueryParameter("language", "RU")
                    .addQueryParameter("metric", "true")

                    .build();

            Request request = new Request.Builder()
                    .addHeader("accept", "application/json")
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();


            ObjectMapper objectMapper = new ObjectMapper();

            String json = response.body().string();


            try {
                WeatherResponse weatherList = objectMapper.readValue(json, WeatherResponse.class);
                System.out.println("Прогноз погоды на следующие 5 дней: "
                        + weatherList.getHeadline().getText() + ". Температура: " +
                        weatherList.getDailyForecasts().get(0).getTemperature().getMaximum().getValue() + " C, " +
                        weatherList.getDailyForecasts().get(1).getTemperature().getMinimum().getValue() + " C, " +
                        weatherList.getDailyForecasts().get(2).getTemperature().getMinimum().getValue() + " C, " +
                        weatherList.getDailyForecasts().get(3).getTemperature().getMinimum().getValue() + " C, " +
                        weatherList.getDailyForecasts().get(4).getTemperature().getMinimum().getValue() + " C.");

                WeatherData weatherData = new WeatherData();
                weatherData.setLocalDate(weatherList.getDailyForecasts().get(0).getDate());
                weatherData.setText(weatherList.getHeadline().getText());
                weatherData.setCity(weatherList.getHeadline().getCategory());
                weatherData.setTemperature(weatherList.getDailyForecasts().get(0).getTemperature().getMaximum().getValue());

                DataBaseConnection dataBaseConnection = new DataBaseConnection();
                dataBaseConnection.createTableIfNotExists();
                dataBaseConnection.saveWeatherData(weatherData);



            }
            catch (JsonProcessingException ex) {
                ex.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public String detectCityKey() throws IOException {
        String selectedCity = ApplicationGlobalState.getInstance().getSelectedCity();

        HttpUrl detectLocationURL = new HttpUrl.Builder()
            .scheme("http")
            .host(BASE_HOST)
            .addPathSegment("locations")
            .addPathSegment(API_VERSION)
            .addPathSegment("cities")
            .addPathSegment("autocomplete")
            .addQueryParameter("apikey", API_KEY)
            .addQueryParameter("q", selectedCity)
            .build();

        Request request = new Request.Builder()
            .addHeader("accept", "application/json")
            .url(detectLocationURL)
            .build();

        Response response = client.newCall(request).execute();
        System.out.println(response);
        if (!response.isSuccessful()) {
            throw new IOException("Невозможно прочесть информацию о городе. " +
                "Код ответа сервера = " + response.code() + " тело ответа = " + response.body().string());
        }
        String jsonResponse = response.body().string();
        System.out.println("Произвожу поиск города " + selectedCity);

        if (objectMapper.readTree(jsonResponse).size() > 0) {
            String cityName = objectMapper.readTree(jsonResponse).get(0).at("/LocalizedName").asText();
            String countryName = objectMapper.readTree(jsonResponse).get(0).at("/Country/LocalizedName").asText();
            System.out.println("Найден город " + cityName + " в стране " + countryName);
        } else throw new IOException("Server returns 0 cities");

        return objectMapper.readTree(jsonResponse).get(0).at("/Key").asText();
    }
}
