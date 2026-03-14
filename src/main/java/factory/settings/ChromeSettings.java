package factory.settings;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;

public class ChromeSettings implements IBrowserOptions {

  @Override
  public AbstractDriverOptions settings() {
    ChromeOptions chromeOptions = new ChromeOptions();

    return chromeOptions;
  }
}
