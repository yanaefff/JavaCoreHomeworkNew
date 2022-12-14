package homework7;

import homework7.homework8.DataBaseConnection;

import java.io.IOException;
import java.util.Scanner;

public class UserInterface {

    private final Controller controller = new Controller();

    public void runApplication() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите название города на английском языке");
            String city = scanner.nextLine();

            setGlobalCity(city);

            System.out.println("Введите ответ: 1 - Получить текущую погоду, " +
                "2 - Получить погоду на следующие 5 дней, " +
                "3 - Выход и завершить работу, " + "4 - Получить данные из БД.");
            String result = scanner.nextLine();

            checkIsExit(result);
            checkIsGetDataBase(result);

            try {
                validateUserInput(result);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            try {
                notifyController(result);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void checkIsExit(String result) {
        if (result.equals("3")) {
            System.out.println("Завершаю работу");
            System.exit(0);
        }
    }

    private void checkIsGetDataBase(String result) {
        if (result.equals("4")) {
            System.out.println("Получаю результаты из базы данных");
            DataBaseConnection dataBaseConnection = new DataBaseConnection();
            dataBaseConnection.getTable();
        }
    }

    private void setGlobalCity(String city) {
        ApplicationGlobalState.getInstance().setSelectedCity(city);
    }


    private void validateUserInput(String userInput) throws IOException {
        if (userInput == null || userInput.length() != 1) {
            throw new IOException("Incorrect user input: expected one digit as answer, but actually get " + userInput);
        }
        int answer = 0;
        try {
            answer = Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            throw new IOException("Incorrect user input: character is not numeric!");
        }
    }

    private void notifyController(String input) throws IOException {
        controller.onUserInput(input);
    }

}
