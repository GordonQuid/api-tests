package pages;

import annotations.Path;
import commons.AbsCommons;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import java.util.List;

public abstract class AbsBasePage<T> extends AbsCommons {

  public AbsBasePage(WebDriver driver) {
    super(driver);
  }

  @FindBy(css = "h1, h3")
  private WebElement header;

  private String baseUrl = System.getProperty("base.url");

  private String getPath() {
    Class clazz = getClass();

    if (clazz.isAnnotationPresent(Path.class)) {
      Path path = (Path) clazz.getDeclaredAnnotation(Path.class);
      return path.value();
    }

    throw new IllegalStateException(
        "Annotation @Path is missing for class: " + clazz.getName()
    );
  }

  public T open(String... data) {
    driver.get(baseUrl + getPath());

    return (T) this;
  }

  public T headerShouldBeSameAs(String header) {

    highlight(this.header);

    Assertions.assertEquals(header, this.header.getText());

    return (T) this;
  }

  protected void scrollToElement(WebElement element) {
    ((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({block: 'center'});",
        element
    );
  }

  protected String getTextSafely(WebElement element) {
    try {
      return element.getText().trim();
    } catch (StaleElementReferenceException e) {
      return "";
    }
  }

  protected List<WebElement> waitUntilStabilized(String cssSelector) {
    int[] last = {0};
    List<WebElement>[] result = new List[]{List.of()};

    webDriverWait.waitForCondition(driver -> {
      List<WebElement> elements = $$(By.cssSelector(cssSelector)).stream()
          .filter(el -> !getTextSafely(el).isEmpty())
          .toList();

      if (elements.size() > 0 && elements.size() == last[0]) {
        result[0] = elements;
        return true;
      }
      last[0] = elements.size();
      return false;
    });

    return result[0];
  }

  protected List<WebElement> waitUntilMoreThan(int previousCount, String cssSelector) {
    webDriverWait.waitForCondition(driver ->
        $$(By.cssSelector(cssSelector)).stream()
            .filter(el -> !getTextSafely(el).isEmpty())
            .count() > previousCount
    );
    return waitUntilStabilized(cssSelector);
  }

  protected void highlight(WebElement element) {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    String originalStyle = (String) js.executeScript(
        "return arguments[0].style.border", element
    );
    js.executeScript("arguments[0].style.border='3px solid red'", element);
    try {
      Thread.sleep(500);
      js.executeScript(
          "arguments[0].style.border='" + (originalStyle != null ? originalStyle : "") + "'",
          element
      );
      Thread.sleep(200);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  protected void clickWithActions(WebElement element) {
    highlight(element);
    new Actions(driver)
        .moveToElement(element)
        .click()
        .perform();
  }
}