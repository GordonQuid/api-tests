package pages;

import annotations.Path;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CourseData;
import utils.DateParser;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/catalog/courses")
public class CoursesPage extends AbsBasePage<CoursesPage> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CoursesPage.class);
  private static final String COURSE_TITLES = ".sc-zzdkm7-0 h6";
  private static final String COURSE_CARD = ".sc-zzdkm7-0";
  private static final String COURSE_DATE = ".sc-hrqzy3-1.jEGzDf";
  private static final String LOAD_MORE_BUTTON = "//*[contains(text(), 'Показать еще')]";
  private static final String DIRECTIONS = "//*[contains(text(),'Направление')]/ancestor::div[contains(@class, 'sc-1w8jhjp-0')]//div[contains(@class, 'sc-1fry39v-0')]";
  private String expectedDirection;

  private List<CourseData> cachedCourses = null;

  public CoursesPage(WebDriver driver) {
    super(driver);
  }

  public CoursesPage(WebDriver driver, String direction) {
    super(driver);
    this.expectedDirection = direction;
  }

  public CoursePage openCourseByTitle(String title) {

    List<WebElement> elements = waitUntilStabilized(COURSE_TITLES);

    while (true) {
      Optional<WebElement> found = elements.stream()
          .filter(el -> title.equals(getTextSafely(el)))
          .findFirst();

      if (found.isPresent()) {
        scrollToElement(found.get());
        clickWithActions(found.get());
        return new CoursePage(driver);
      }

      if (!tryClickLoadMore()) {
        throw new NoSuchElementException("Course not found: " + title);
      }

      elements = waitUntilMoreThan(elements.size(), COURSE_TITLES);
    }
  }

  public void checkEarliestCourses() {
    checkCoursesWithExtremeDate(true);
  }

  public void checkLatestCourses() {
    checkCoursesWithExtremeDate(false);
  }

  public void verifyDirection() {

    List<WebElement> directions = $$(By.xpath(DIRECTIONS));
    List<WebElement> trueElements = new ArrayList<>();

    for (WebElement direction : directions) {
      String value = direction.getAttribute("value");
      if ("true".equalsIgnoreCase(value)) {
        trueElements.add(direction);
      }
    }

    Assertions.assertEquals(1, trueElements.size(),
        "Ожидался ровно 1 элемент с value='true', но найдено: " + trueElements.size());

    String actualDirection = trueElements.get(0).getText();

    scrollToElement(trueElements.get(0));
    highlight(trueElements.get(0));

    Assertions.assertEquals(expectedDirection, actualDirection,
        "Текст элемента с value='true' не совпадает с ожидаемым.");
  }


  private void checkCoursesWithExtremeDate(boolean earliest) {
    List<CourseData> all = loadAllCourses();

    Optional<LocalDate> extremeDate = all.stream()
        .map(CourseData::date)
        .reduce((a, b) -> earliest
            ? (a.isBefore(b) ? a : b)
            : (a.isAfter(b) ? a : b)
        );

    List<CourseData> extremeCourses = extremeDate
        .map(date -> all.stream()
            .filter(c -> c.date().equals(date))
            .toList()
        )
        .orElse(List.of());

    LOGGER.info(earliest ? "Самая ранняя дата: {}" : "Самая поздняя дата: {}", extremeDate.orElse(null));
    LOGGER.info("Курсов с этой датой: {}", extremeCourses.size());
    extremeCourses.forEach(c -> LOGGER.info("  - {} | {}", c.title(), c.date()));


    List<WebElement> cards = $$(By.cssSelector(COURSE_CARD));

    extremeCourses.forEach(course -> {
      WebElement matchingCard = cards.stream()
          .filter(card -> {
            try {
              String title = getTextSafely(card.findElement(By.cssSelector("h6")));
              return title.equals(course.title());
            } catch (NoSuchElementException e) {
              return false;
            }
          })
          .findFirst()
          .orElseThrow(() -> new NoSuchElementException(
              "Карточка не найдена в браузере: " + course.title()
          ));

      String titleInBrowser = getTextSafely(matchingCard.findElement(By.cssSelector("h6")));

      List<WebElement> dateElements = matchingCard.findElements(By.cssSelector(COURSE_DATE));
      String rawDateInBrowser = dateElements.stream()
          .map(this::getTextSafely)
          .filter(text -> !text.isEmpty())
          .reduce((a, b) -> b)
          .orElseThrow(() -> new NoSuchElementException(
              "Дата не найдена в браузере для курса: " + course.title()
          ));

      LocalDate dateInBrowser = DateParser.parse(rawDateInBrowser)
          .orElseThrow(() -> new NoSuchElementException(
              "Не удалось распарсить дату в браузере: " + rawDateInBrowser
          ));

      LOGGER.info("Проверка: {}", course.title());
      LOGGER.info("  jsoup title:   {}", course.title());
      LOGGER.info("  browser title: {}", titleInBrowser);
      LOGGER.info("  jsoup дата:    {}", course.date());
      LOGGER.info("  browser дата:  {}", dateInBrowser);

      Assertions.assertEquals(course.title(), titleInBrowser,
          "Название в браузере не совпадает с jsoup: ожидалось '"
              + course.title() + "', но было '" + titleInBrowser + "'"
      );

      Assertions.assertEquals(course.date(), dateInBrowser,
          "Дата в браузере не совпадает с jsoup для курса: " + course.title()
      );
    });
  }

  private List<CourseData> loadAllCourses() {
    if (cachedCourses != null) {
      return cachedCourses;
    }

    List<WebElement> elements = waitUntilStabilized(COURSE_TITLES);
    while (tryClickLoadMore()) {
      elements = waitUntilMoreThan(elements.size(), COURSE_TITLES);
    }

    Document doc = Jsoup.parse(driver.getPageSource());

    cachedCourses = doc.select(COURSE_CARD).stream()
        .map(card -> {
          String title = card.select("h6").text();
          String rawDate = card.select(COURSE_DATE).text();
          return DateParser.parse(rawDate)
              .map(date -> new CourseData(title, date));
        })
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toMap(
            CourseData::title,
            c -> c,
            (a, b) -> a
        ))
        .values()
        .stream()
        .toList();

    return cachedCourses;
  }

  private boolean tryClickLoadMore() {
    Optional<WebElement> button = webDriverWait.waitForResult(
        ExpectedConditions.elementToBeClickable(By.xpath(LOAD_MORE_BUTTON))
    );
    button.ifPresent(el -> {
      scrollToElement(el);
      clickWithActions(el);
    });
    return button.isPresent();
  }
}
