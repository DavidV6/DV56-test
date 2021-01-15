package com.adthena.testapi.api;

import static java.time.LocalDate.parse;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static javax.ws.rs.client.Entity.json;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import com.adthena.testapi.db.dao.DateDao;
import com.adthena.testapi.db.entities.DateEntity;
import com.adthena.testapi.services.FilterDataService;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import javax.ws.rs.core.Response;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
class DateResourceTest {

  private static final DateDao DAO = mock(DateDao.class);
  private static final Jdbi JDBI = mock(Jdbi.class);
  private static final FilterDataService service = mock(FilterDataService.class);
  private static final ResourceExtension EXT = ResourceExtension.builder()
      .addResource(new DateResource(JDBI, service))
      .build();

  @BeforeEach
  void setup() {
    when(JDBI.onDemand(eq(DateDao.class))).thenReturn(DAO);
  }

  @AfterEach
  void tearDown() {
    reset(DAO);
    reset(JDBI);
  }


  @Test
  void getCategoryById() {
    final DateEntity expected =
        new DateEntity(2, parse("2008-01-01", ISO_DATE), "WE", 1, "JAN", "1", 2008, true);

    when(DAO.getDateById(2)).thenReturn(expected);
    final Response response = EXT.target("/dates/2").request().get();
    assertThat(response.readEntity(String.class)).isEqualTo(""
        + "{\"dateid\":2,\"caldate\":[2008,1,1],\"day\":\"WE\",\"week\":1,\"month\":\"JAN\",\"qtr\":\"1\",\"year\":2008,\"holiday\":true}"
    );
  }

  @Test
  void upsertCreate() {
    when(DAO.upsert(any())).thenCallRealMethod();
    when(DAO.nextId()).thenReturn(42);
    when(DAO.create(eq(42), any(), any(), any(), any(), any(), any(), any())).thenReturn(1);

    final DateEntity entity =
        new DateEntity(null, parse("2008-01-01", ISO_DATE), "WE", 1, "JAN", "1", 2008, true);
    when(service.cleanup(eq(entity))).thenReturn(entity);
    when(service.save(eq(entity))).thenReturn(entity.withDateid(42));
    final Response response = EXT
        .target("/dates/upsert")
        .request()
        .put(json(entity));

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.readEntity(String.class)).isEqualTo(""
        + "{\"dateid\":42,\"caldate\":[2008,1,1],\"day\":\"WE\",\"week\":1,\"month\":\"JAN\",\"qtr\":\"1\",\"year\":2008,\"holiday\":true}"
    );
  }

  @Test
  void upsertUpdate() {
    when(DAO.upsert(any())).thenCallRealMethod();
    when(DAO.update(eq(42), any(), any(), any(), any(), any(), any(), any())).thenReturn(1);

    final DateEntity entity =
        new DateEntity(42, parse("2008-01-01", ISO_DATE), "WE", 1, "JAN", "1", 2008, true);
    when(service.cleanup(eq(entity))).thenReturn(entity);
    when(service.save(eq(entity))).thenReturn(entity);
    final Response response = EXT
        .target("/dates/upsert")
        .request()
        .put(json(entity));

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.readEntity(String.class)).isEqualTo(""
        + "{\"dateid\":42,\"caldate\":[2008,1,1],\"day\":\"WE\",\"week\":1,\"month\":\"JAN\",\"qtr\":\"1\",\"year\":2008,\"holiday\":true}"
    );
  }
}
