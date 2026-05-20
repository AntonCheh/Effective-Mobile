package dbTests;

import commonDb.utils.DatabaseManager;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;

import java.sql.SQLException;

/**
 * Базовый класс для тестов, которые работают с базой данных
 */
@Slf4j
public abstract class BaseDatabaseTest {

    @BeforeMethod
    public void cleanDatabase() {
        try {
            // Очищаем таблицы перед каждым тестом для изоляции
            DatabaseManager.truncateTable("bookings");
            DatabaseManager.truncateTable("users");
            log.info("🧹 Database cleaned before test");
        } catch (SQLException e) {
            log.error("Failed to clean database", e);
        }
    }

    @AfterSuite
    public void closeDatabase() {
        DatabaseManager.closeConnection();
    }
}