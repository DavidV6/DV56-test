package com.adthena.testapi.api;

import static javax.ws.rs.client.Entity.json;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import com.adthena.testapi.db.dao.EventDao;
import com.adthena.testapi.db.entities.EventEntity;
import com.adthena.testapi.services.FilterDataService;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import java.time.LocalDateTime;
import javax.ws.rs.core.Response;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
class EventResourceTest {

  private static final EventDao DAO = mock(EventDao.class);
  private static final Jdbi JDBI = mock(Jdbi.class);
  private static final FilterDataService service = mock(FilterDataService.class);
  private static final ResourceExtension EXT = ResourceExtension.builder()
      .addResource(new EventResource(JDBI, service))
      .build();

  @BeforeEach
  void setup() {
    when(JDBI.onDemand(eq(EventDao.class))).thenReturn(DAO);
  }

  @AfterEach
  void tearDown() {
    reset(DAO);
    reset(JDBI);
  }

  @Test
  void getEventById() {
    final LocalDateTime startTime = LocalDateTime.now();
    final EventEntity expected = new EventEntity(1, 305, 8, 1851, "Gotterdammerung", startTime);
    when(DAO.getEventById(1)).thenReturn(expected);
    final Response response = EXT.target("/events/1").request().get();
    assertThat(response.readEntity(EventEntity.class)).isEqualTo(expected);
  }

  @Test
  void upsertCreate() {
    when(DAO.upsert(any())).thenCallRealMethod();
    when(DAO.nextId()).thenReturn(21);
    when(DAO.create(eq(21), any(), any(), any(), any(), any())).thenReturn(1);

    final LocalDateTime startTime = LocalDateTime.now();
    final EventEntity entity = new EventEntity(null, 321, 22, 43, "meetup", startTime);
    when(service.cleanup(eq(entity))).thenReturn(entity);
    when(service.save(eq(entity))).thenReturn(entity.withEventid(21));
    final Response response = EXT
        .target("/events/upsert")
        .request()
        .put(json(entity));

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.readEntity(EventEntity.class))
        .isEqualTo(new EventEntity(21, 321, 22, 43, "meetup", startTime));
  }

  @Test
  void upsertUpdate() {
    when(DAO.upsert(any())).thenCallRealMethod();
    when(DAO.update(eq(21), any(), any(), any(), any(), any())).thenReturn(1);

    final EventEntity entity = new EventEntity(21, 321, 22, 43, "meetup", LocalDateTime.now());
    when(service.cleanup(eq(entity))).thenReturn(entity);
    when(service.save(eq(entity))).thenReturn(entity);
    final Response response = EXT
        .target("/events/upsert")
        .request()
        .put(json(entity));

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.readEntity(EventEntity.class)).isEqualTo(entity);
  }
}
