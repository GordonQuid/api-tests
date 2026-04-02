package extensions;

import com.google.inject.Guice;
import com.google.inject.Injector;
import modules.ApiModule;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class GuiceExtension implements BeforeEachCallback {
  private static final Injector INJECTOR = Guice.createInjector(new ApiModule());

  @Override
  public void beforeEach(ExtensionContext context) {
    context.getTestInstance().ifPresent(INJECTOR::injectMembers);
  }
}
