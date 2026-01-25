package config;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:test-config.properties"})
public interface TestConfig extends Config {

    @Key("browser")
    @DefaultValue("chrome")
    String browser();

    @Key("base.url")
    @DefaultValue("https://www.saucedemo.com")
    String baseUrl();

    @Key("timeout")
    @DefaultValue("10")
    int timeout();

    @Key("headless")
    @DefaultValue("false")
    boolean headless();
}
