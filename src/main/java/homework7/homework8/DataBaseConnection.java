package homework7.homework8;

import java.sql.*;

public class DataBaseConnection {


    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    String createTable = "CREATE TABLE IF NOT EXISTS weather \n" +
            "            (\"id\" INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "            \"category\" TEXT NOT NULL,\n" +
            "            \"date_time\" TEXT NOT NULL,\n" +
            "            \"weather_text\" TEXT NOT NULL,\n" +
            "            \"temperature\" REAL NOT NULL)";

    String insertWeather = "INSERT INTO weather (category, date_time, weather_text, temperature) VALUES (?,?,?,?)";

    String getWeather = "SELECT * FROM weather";

    String dropWeather = "DROP TABLE IF EXISTS weather";

    private Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:homework8.db");
        return connection;
    }


    public void createTableIfNotExists() {
        try (Connection connection = getConnection()) {
            connection.createStatement().execute(dropWeather);
            connection.createStatement().execute(createTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void getTable() {
        try (Connection connection = getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(getWeather);
            System.out.println("Category: " + rs.getString(2));
            System.out.println("Date_time: " + rs.getString(3));
            System.out.println("Weather_text: " + rs.getString(4));
            System.out.println("Temperature: " + rs.getString(5));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean saveWeatherData(WeatherData weatherData) throws SQLException {
        try (Connection connection = getConnection();

             PreparedStatement saveWeather = connection.prepareStatement(insertWeather)) {
            saveWeather.setString(1, weatherData.getCity());
            saveWeather.setString(2, weatherData.getLocalDate());
            saveWeather.setString(3, weatherData.getText());
            saveWeather.setDouble(4, weatherData.getTemperature());
            return saveWeather.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        throw new SQLException("Failure on saving weather object");
    }
}


