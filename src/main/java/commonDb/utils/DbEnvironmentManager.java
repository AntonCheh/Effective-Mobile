package commonDb.utils;

import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigCache;
import commonDb.properties.DbProperties;

/**
 * Менеджер для управления конфигурацией базы данных
 */
@Slf4j
public class DbEnvironmentManager {

    private static DbProperties dbConfig;

    public static DbProperties getConfig() {
        if (dbConfig == null) {
            dbConfig = ConfigCache.getOrCreate(DbProperties.class);
            printDbConfigInfo();
        }
        return dbConfig;
    }

    private static void printDbConfigInfo() {
        log.info("=== Database Configuration ===");
        log.info("DB URL: {}", dbConfig.dbUrl());
        log.info("DB User: {}", dbConfig.dbUser());
        log.info("DB Driver: {}", dbConfig.dbDriver());
        log.info("==============================");
    }
}