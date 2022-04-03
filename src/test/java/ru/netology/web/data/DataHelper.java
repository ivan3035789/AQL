package ru.netology.web.data;

import lombok.*;
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

    @SneakyThrows
    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        String[] codeSQL = {"SELECT ac.code FROM auth_codes ac , users u where u.login ='vasya' AND u.id =ac.user_id ORDER by ac.created DESC LIMIT 1;",
                "SELECT ac.code FROM auth_codes ac , users u WHERE u.login ='petya' AND u.id =ac.user_id " +
                        " ORDER BY ac.created DESC LIMIT 1;"
        };
        String sqlQuery = authInfo.login.equals("vasya") ? codeSQL[0] : codeSQL[1];
        var runner = new QueryRunner();
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app?allowPublicKeyRetrieval=true&useSSL=false", "app", "pass"
                );
        ) {
            var codeObject = runner.query(conn, sqlQuery, new ScalarHandler<>());
            var codeString = codeObject.toString();
            return new VerificationCode(codeString);
        }

    }

    @SneakyThrows
    public static void truncateTables() {
        var dataSQLAuth = "DELETE FROM auth_codes;";
        var dataSQLUsers = "DELETE FROM users;";
        var dataSQLCards = "DELETE FROM cards;";
        var dataSQLTransactions = "DELETE FROM card_transactions;";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app?allowPublicKeyRetrieval=true&useSSL=false", "app", "pass"
                );
                var dataStmtCards = conn.prepareStatement(dataSQLCards);
                var dataStmtransactions = conn.prepareStatement(dataSQLTransactions);
                var dataStmtAuth = conn.prepareStatement(dataSQLAuth);
                var dataStmtUsers = conn.prepareStatement(dataSQLUsers);
        ) {
            dataStmtCards.executeUpdate();
            dataStmtransactions.executeUpdate();
            dataStmtAuth.executeUpdate();
            dataStmtUsers.executeUpdate();
        }
    }

    @SneakyThrows
    public static void deleteCodes() {
        var dataSQLAuth = "DELETE FROM auth_codes;";
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
