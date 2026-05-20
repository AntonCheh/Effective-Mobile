package commonDb.utils;


import commonDb.properties.DbProperties;
import lombok.extern.slf4j.Slf4j;
import restApi.utils.EnvironmentManager;

import java.sql.*;

/**
 * Менеджер для работы с базой данных через JDBC
 */
@Slf4j
public class DatabaseManager {

    private static Connection connection;
    private static DbProperties config;

    static {
        config = DbEnvironmentManager.getConfig();
    }

    /**
     * Получить соединение с БД
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Загружаем драйвер
                Class.forName(config.dbDriver());

                // Создаем соединение
                connection = DriverManager.getConnection(
                        config.dbUrl(),
                        config.dbUser(),
                        config.dbPassword()
                );

                log.info("✅ Database connected successfully!");

                // Создаем таблицы, если их нет
                initDatabase();

            } catch (ClassNotFoundException e) {
                log.error("❌ Database driver not found: {}", config.dbDriver(), e);
                throw new RuntimeException("Database driver not found", e);
            } catch (SQLException e) {
                log.error("❌ Failed to connect to database", e);
                throw new RuntimeException("Database connection failed", e);
            }
        }
        return connection;
    }

    /**
     * Инициализация базы данных (создание таблиц)
     */
    private static void initDatabase() throws SQLException {
        // Таблица для бронирований
        String createBookingsTable = """
            CREATE TABLE IF NOT EXISTS bookings (
                id INT AUTO_INCREMENT PRIMARY KEY,
                booking_id INT NOT NULL,
                firstname VARCHAR(100) NOT NULL,
                lastname VARCHAR(100) NOT NULL,
                totalprice INT,
                depositpaid BOOLEAN,
                checkin_date DATE,
                checkout_date DATE,
                additionalneeds VARCHAR(255),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

        // Таблица для пользователей
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                user_id INT NOT NULL,
                email VARCHAR(255),
                firstname VARCHAR(100),
                lastname VARCHAR(100),
                avatar VARCHAR(500),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createBookingsTable);
            stmt.execute(createUsersTable);
            log.info("✅ Database tables initialized");
        }
    }

    /**
     * Выполнить SQL запрос (SELECT)
     */
    public static ResultSet executeQuery(String sql, Object... params) throws SQLException {
        PreparedStatement pstmt = getConnection().prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        return pstmt.executeQuery();
    }

    /**
     * Выполнить SQL обновление (INSERT, UPDATE, DELETE)
     */
    public static int executeUpdate(String sql, Object... params) throws SQLException {
        PreparedStatement pstmt = getConnection().prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        return pstmt.executeUpdate();
    }

    /**
     * Вставить данные и получить сгенерированный ID
     */
    public static int insertAndGetId(String sql, Object... params) throws SQLException {
        PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        pstmt.executeUpdate();

        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        }
        return -1;
    }

    /**
     * Очистить таблицу (для тестов)
     */
    public static void truncateTable(String tableName) throws SQLException {
        executeUpdate("DELETE FROM " + tableName);
        log.info("Truncated table: {}", tableName);
    }

    /**
     * Закрыть соединение с БД
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                log.info("🔌 Database connection closed");
            } catch (SQLException e) {
                log.error("Error closing database connection", e);
            }
        }
    }
}