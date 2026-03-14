package pages;

import annotations.Path;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Path("/")
public class MainPage extends AbsBasePage<MainPage> {

  public MainPage(WebDriver driver) {
    super(driver);
  }

  private static final  String BURGER_MENU_BUTTON = ".sc-5n5sda-0.exrzoV";
  private static final String DIRECTIONS_TITLES = "//*[contains(@class,'sc-ig0m9y-0')]//a";
  private static final Random RANDOM = new Random();
  private String savedElementText;


  public CoursesPage clickRandomDirection() {
    Optional<WebElement> button = webDriverWait.waitForResult(
        ExpectedConditions.elementToBeClickable(By.cssSelector(BURGER_MENU_BUTTON))
    );

    button.ifPresent(el -> {
      scrollToElement(el);
      clickWithActions(el);
    });

    List<WebElement> directionTitles = $$(By.xpath(DIRECTIONS_TITLES));

    int randomIndex = RANDOM.nextInt(directionTitles.size());
    WebElement randomElement = directionTitles.get(randomIndex);

    savedElementText = normalizeText(randomElement.getText());
    scrollToElement(randomElement);
    clickWithActions(randomElement);;

    return new CoursesPage(driver, savedElementText);
  }

  private String normalizeText(String text) {
    return text.replaceAll("\\s*\\(.*\\)", "").trim();
  }
}
