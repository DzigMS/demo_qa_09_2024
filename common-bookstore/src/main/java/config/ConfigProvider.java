package config;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

@Slf4j
public class ConfigProvider {
    private static final Properties properties = initProperties();

    private ConfigProvider() {
    }

    private static Properties initProperties() {
        Properties prop = new Properties();
        String configPath = System.getenv("CONFIG_PATH") == null ? System.getProperty("CONFIG_PATH") : System.getenv("CONFIG_PATH");
        if (configPath == null) {
            log.warn("config path is null. Set up default path=src/main/resources/config.properties");
            configPath = "src/main/resources/config.properties";
        }

        try (FileInputStream file = new FileInputStream(configPath)) {
            prop.load(file);
        } catch (IOException ex) {
            log.warn("Could not find property file");
        }

        return prop;
    }

    public static WebDriver getDriver() {
        String browserName = properties.getProperty("selenium.driver", "chrome");
        WebDriver browser = switch (browserName) {
            case "chrome" -> new ChromeDriver();
            case "safari" -> new SafariDriver();
            case "mozilla" -> new FirefoxDriver();
            default -> {
                log.error("Incorrect browser name");
                throw new IllegalArgumentException("Incorrect browser name");
            }
        };

        int timeout = Integer.valueOf(properties.getProperty("driver.implicitly.wait", "5"), 10);
        browser.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));

        return browser;
    }

    public static String getSuitsXml() {
        return properties.getProperty("test.suit.path");
    }
}