package factory;

import exceptions.BrowserNotSupportedException;
import factory.settings.ChromeSettings;
import factory.settings.SafariSettings;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public class WebDriverFactory {

  private String browser = System.getProperty("browser");

  public WebDriver create() {
    return switch (browser) {
      case "chrome" -> {
        WebDriverManager.chromedriver().setup();
        yield new ChromeDriver((ChromeOptions) new ChromeSettings().settings());
      }
      case "safari" -> {
        WebDriverManager.safaridriver().setup();
        yield new SafariDriver((SafariOptions) new SafariSettings().settings());
      }
      default -> throw new BrowserNotSupportedException(browser);
    };

  }
}
