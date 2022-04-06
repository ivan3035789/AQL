package ru.netology.web.data;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.util.Random;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    @Value
    public static class VerificationCode {
        String code;
    }

    public static String invalidCode(int min, int max) {
        max -= min;
        return String.valueOf(Math.random() * ++max + min);
    }

    public static AuthInfo authInfo() {
        String login;
        String password;
        String[] validLogin = {"vasya", "petya"};
        String[] validPassword = {"qwerty123", "123qwerty"};
        Random random = new Random();
        login = validLogin[random.nextInt(validLogin.length)];
        password = (login.equals("vasya") ? validPassword[0] : validPassword[1]);
        return new AuthInfo(login, password);
    }

    public static AuthInfo authInfoOnlyVasya() {
        String[] validLogin = {"vasya", "petya"};
        String[] validPassword = {"qwerty123", "123qwerty"};
        String login = validLogin[0];
        String password = validPassword[0];
        return new AuthInfo(login, password);
    }

    public static AuthInfo invalidPasswordAuthInfo() {
        String[] validLogin = {"vasya", "petya"};
        Faker faker = new Faker();
        return new AuthInfo(validLogin[0], faker.internet().password());
    }


    @SneakyThrows
    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        String[] codeSQL = {"SELECT ac.code FROM auth_codes ac , users u where u.login ='vasya' AND u.id =ac.user_id ORDER by ac.created DESC LIMIT 1;",
                "SELECT ac.code FROM auth_codes ac , users u WHERE u.login ='petya' AND u.id =ac.user_id " +
                        " ORDER BY ac.created DESC LIMIT 1;"
        };
        String sqlQuery = authInfo.login.equals("vasya") ? codeSQL[0] : codeSQL[1];
        val runner = new QueryRunner();
        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app?allowPublicKeyRetrieval=true&useSSL=false", "app", "pass"
                );
        ) {
            val codeObject = runner.query(conn, sqlQuery, new ScalarHandler<>());
            val codeString = codeObject.toString();
            return new VerificationCode(codeString);
        }

    }

    @SneakyThrows
    public static String userStatus() {
        String status;
        val selectSQL = "SELECT status FROM users WHERE login = ?;";
        val runner = new QueryRunner();

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app?allowPublicKeyRetrieval=true&useSSL=false", "app", "pass"
                );
        ) {
            status = runner.query(conn, selectSQL, new ScalarHandler<>(), authInfo().login);
        }
        return status;
    }

    @SneakyThrows
    public static void truncateTables() {
        val dataSQLAuth = "DELETE FROM auth_codes;";
        val dataSQLUsers = "DELETE FROM users;";
        val dataSQLCards = "DELETE FROM cards;";
        val dataSQLTransactions = "DELETE FROM card_transactions;";
        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app?allowPublicKeyRetrieval=true&useSSL=false", "app", "pass"
                );
                val dataStmtCards = conn.prepareStatement(dataSQLCards);
                val dataStmtransactions = conn.prepareStatement(dataSQLTransactions);
                val dataStmtAuth = conn.prepareStatement(dataSQLAuth);
                val dataStmtUsers = conn.prepareStatement(dataSQLUsers);
        ) {
            dataStmtCards.executeUpdate();
            dataStmtransactions.executeUpdate();
            dataStmtAuth.executeUpdate();
            dataStmtUsers.executeUpdate();
        }
    }

    @SneakyThrows
    public static void auth_codes() {
        val dataSQLAuth = "DELETE FROM auth_codes;";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app?allowPublicKeyRetrieval=true&useSSL=false", "app", "pass"
                );
                var dataStmtAuth = conn.prepareStatement(dataSQLAuth);
        ) {
            dataStmtAuth.executeUpdate();
        }
    }
}
