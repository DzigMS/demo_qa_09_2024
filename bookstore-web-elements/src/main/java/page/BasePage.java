package page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {
    private final WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    protected WebDriver getDriver() {
        return driver;
    }

    public abstract void openPage();

    protected void openPage(String path) {
        driver.get(path);
    }
}
