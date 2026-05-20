package commonDb.properties;

import org.aeonbits.owner.Config;

/**
 * Интерфейс конфигурации для подключения БД.
 * Использует библиотеку Owner для загрузки свойств из различных источников.
 *
 * Источники данных (в порядке приоритета):
 * 1. Системные свойства (System.properties)
 * 2. Переменные окружения (Environment variables)
 * 3. Файл config.properties в ресурсах
 */

@Config.Sources({"file:src/main/resources/db/db.properties",
        "system:properties",
        "system:env"})
public interface DbProperties extends Config {

    @DefaultValue("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL")
    @Key("db.url")
    String dbUrl();

    @DefaultValue("chikanov")
    @Key("db.user")
    String dbUser();

    @DefaultValue("")
    @Key("db.password")
    String dbPassword();

    @DefaultValue("org.h2.Driver")
    @Key("db.driver")
    String dbDriver();

}
