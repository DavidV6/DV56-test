package com.adthena.testapi.api;

import static java.util.Arrays.asList;
import static javax.ws.rs.client.Entity.json;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import com.adthena.testapi.db.dao.CategoryDao;
import com.adthena.testapi.db.entities.CategoryEntity;
import com.adthena.testapi.services.FilterDataService;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import java.util.List;
import javax.ws.rs.core.Response;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
class CategoryResourceTest {

  private static final CategoryDao DAO = mock(CategoryDao.class);
  private static final Jdbi JDBI = mock(Jdbi.class);
  private static final FilterDataService service = mock(FilterDataService.class);
  private static final ResourceExtension EXT = ResourceExtension.builder()
      .addResource(new CategoryResource(JDBI, service))
      .build();

  @BeforeEach
  void setup() {
    when(JDBI.onDemand(eq(CategoryDao.class))).thenReturn(DAO);
  }

  @AfterEach
  void tearDown() {
    reset(DAO);
    reset(JDBI);
  }

  @Test
  void list() {
    final List<CategoryEntity> expected = asList(
        new CategoryEntity(1, "Sports", "MLB", "Major League Baseball"),
        new CategoryEntity(2, "Sports", "NFL", "National Football League"),
        new CategoryEntity(3, "Shows", "Plays", "All non-musical theatre")
    );
    when(DAO.list()).thenReturn(expected);
    final Response response = EXT.target("/categories").request().get();
    assertThat(response.readEntity(String.class)).isEqualTo(""
        + "["
        + "{\"catid\":1,\"catgroup\":\"Sports\",\"catname\":\"MLB\",\"catdesc\":\"Major League Baseball\"},"
        + "{\"catid\":2,\"catgroup\":\"Sports\",\"catname\":\"NFL\",\"catdesc\":\"National Football League\"},"
        + "{\"catid\":3,\"catgroup\":\"Shows\",\"catname\":\"Plays\",\"catdesc\":\"All non-musical theatre\"}"
        + "]"
    );
  }

  @Test
  void getCategoryById() {
    final CategoryEntity expected =
        new CategoryEntity(2, "Sports", "NFL", "National Football League");

    when(DAO.getCategoryById(2)).thenReturn(expected);
    final Response response = EXT.target("/categories/2").request().get();
    assertThat(response.readEntity(String.class)).isEqualTo(
        "{\"catid\":2,\"catgroup\":\"Sports\",\"catname\":\"NFL\",\"catdesc\":\"National Football League\"}"
    );
  }

  @Test
  void upsertCreate() {
    when(DAO.upsert(any())).thenCallRealMethod();
    when(DAO.nextId()).thenReturn(42);
    when(DAO.create(eq(42), any(), any(), any())).thenReturn(1);

    final CategoryEntity entity = new CategoryEntity(null, "catgroup", "eric", "catdesc");
    when(service.cleanup(eq(entity))).thenReturn(entity);
    when(service.save(eq(entity))).thenReturn(entity.withCatid(42));
    final Response response = EXT
        .target("/categories/upsert")
        .request()
        .put(json(entity));

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.readEntity(String.class)).isEqualTo(""
        + "{\"catid\":42,\"catgroup\":\"catgroup\",\"catname\":\"eric\",\"catdesc\":\"catdesc\"}"
    );
  }

  @Test
  void upsertUpdate() {
    when(DAO.upsert(any())).thenCallRealMethod();
    when(DAO.update(eq(42), any(), any(), any())).thenReturn(1);

    final CategoryEntity entity = new CategoryEntity(42, "catgroup", "eric", "catdesc");
    when(service.cleanup(eq(entity))).thenReturn(entity);
    when(service.save(eq(entity))).thenReturn(entity);
    final Response response = EXT
        .target("/categories/upsert")
        .request()
        .put(json(entity));

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.readEntity(String.class)).isEqualTo(""
        + "{\"catid\":42,\"catgroup\":\"catgroup\",\"catname\":\"eric\",\"catdesc\":\"catdesc\"}"
    );
  }
}
