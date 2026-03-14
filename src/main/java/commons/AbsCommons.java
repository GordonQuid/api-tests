package commons;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import utils.Waiters;
import java.util.List;

public abstract class AbsCommons {

  protected WebDriver driver;
  protected Waiters webDriverWait;

  public AbsCommons(WebDriver driver) {
    this.driver = driver;
    this.webDriverWait = new Waiters(driver);

    PageFactory.initElements(driver, this);
  }

  public WebElement $(By locator) {
    return driver.findElement(locator);
  }

  public List<WebElement> $$(By locator) {
    return driver.findElements(locator);
  }
}
