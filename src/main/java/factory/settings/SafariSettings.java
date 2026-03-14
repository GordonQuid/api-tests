package factory.settings;

import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.safari.SafariOptions;

public class SafariSettings implements IBrowserOptions {

  @Override
  public AbstractDriverOptions settings() {
    SafariOptions safariOptions = new SafariOptions();

    return safariOptions;
  }
}
