package com.adthena.testapi;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

import com.adthena.testapi.api.CategoryResource;
import com.adthena.testapi.api.DateResource;
import com.adthena.testapi.api.EventResource;
import com.adthena.testapi.api.VenueResource;
import com.adthena.testapi.services.FilterDataService;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.jdbi.v3.core.Jdbi;

public class TestApiApplication
    extends Application<TestApiConfiguration> {

  public static void main(String[] args) throws Exception {
    new TestApiApplication().run(args);
  }

  @Override
  public void initialize(final Bootstrap<TestApiConfiguration> bootstrap) {
    bootstrap.getObjectMapper().registerModule(new JavaTimeModule());
    bootstrap.getObjectMapper().disable(WRITE_DATES_AS_TIMESTAMPS);
  }

  @Override
  public void run(
      final TestApiConfiguration configuration,
      final Environment environment
  ) {
    final DataSourceFactory database = configuration.getDatabase();
    Flyway flyway = new Flyway(new FluentConfiguration()
        .dataSource(database.getUrl(), database.getUser(), database.getPassword()));
    flyway.migrate();
    final JdbiFactory factory = new JdbiFactory();
    final Jdbi jdbi = factory.build(environment, database, "postgresql");
    environment.jersey().register(new VenueResource(jdbi));
    final FilterDataService service = new FilterDataService(jdbi);
    environment.jersey().register(new CategoryResource(jdbi, service));
    environment.jersey().register(new DateResource(jdbi, service));
    environment.jersey().register(new EventResource(jdbi, service));
    final Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
    cors.setInitParameter("allowedOrigins", "*");
    cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
    cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
  }
}
