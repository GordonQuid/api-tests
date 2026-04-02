package modules;

import com.google.inject.AbstractModule;
import services.AuthorsApi;

public class ApiModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(AuthorsApi.class);
  }
}
