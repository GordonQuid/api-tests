package utils;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Optional;

public class Waiters {

  private final WebDriver driver;
  private final Duration timeout = Duration.ofSeconds(10);

  public Waiters(WebDriver driver) {
    this.driver = driver;
  }

  public boolean waitForCondition(ExpectedCondition<?> condition) {
    try {
      new WebDriverWait(driver, timeout).until(condition);
      return true;
    } catch (TimeoutException ignored) {
      return false;
    }
  }

  public <T> Optional<T> waitForResult(ExpectedCondition<T> condition) {
    try {
      T result = new WebDriverWait(driver, timeout).until(condition);
      return Optional.ofNullable(result);
    } catch (TimeoutException ignored) {
      return Optional.empty();
    }
  }
}
