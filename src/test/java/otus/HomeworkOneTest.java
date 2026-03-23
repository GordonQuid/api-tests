package otus;

import com.google.inject.Inject;
import extensions.UIExtensions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.CoursesPage;
import pages.MainPage;

@ExtendWith(UIExtensions.class)
public class HomeworkOneTest {

  @Inject
  private CoursesPage coursesPage;

  @Inject
  private MainPage mainPage;

  @ParameterizedTest
  @ValueSource(strings = {"Архитектура и шаблоны проектирования", "Менеджер Agile-проектов"})
  public void findCourseByTitleTest(String courseName) {
    coursesPage.open()
        .openCourseByTitle(courseName)
        .headerShouldBeSameAs(courseName);
  }

  @Test
  public void checkEarliestAndLatestCoursesTest() {
    coursesPage.open()
        .checkEarliestCourses();
    coursesPage.checkLatestCourses();
  }

  @Test
  public void checkDirectionTest() {
    mainPage.open()
        .clickRandomDirection()
        .verifyDirection();
  }
}