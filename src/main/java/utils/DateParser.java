package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateParser {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter
      .ofPattern("d MMMM, yyyy", new Locale("ru"));

  private static final Pattern DATE_PATTERN =
      Pattern.compile("(\\d{1,2}\\s+[а-яё]+,\\s+\\d{4})");

  public static Optional<LocalDate> parse(String rawText) {
    if (rawText == null || rawText.contains("объявлено")) {
      return Optional.empty();
    }
    try {
      Matcher matcher = DATE_PATTERN.matcher(rawText);
      if (!matcher.find()) {
        return Optional.empty();
      }
      return Optional.of(LocalDate.parse(matcher.group(1), FORMATTER));
    } catch (DateTimeParseException e) {
      return Optional.empty();
    }
  }
}
