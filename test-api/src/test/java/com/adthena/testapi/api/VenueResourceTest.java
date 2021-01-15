package com.adthena.testapi.api;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import com.adthena.testapi.db.dao.VenueDao;
import com.adthena.testapi.db.entities.VenueEntity;
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
class VenueResourceTest {

  private static final VenueDao DAO = mock(VenueDao.class);
  private static final Jdbi JDBI = mock(Jdbi.class);
  private static final ResourceExtension EXT = ResourceExtension.builder()
      .addResource(new VenueResource(JDBI))
      .build();

  @BeforeEach
  void setup() {
    when(JDBI.onDemand(eq(VenueDao.class))).thenReturn(DAO);
  }

  @AfterEach
  void tearDown() {
    reset(DAO);
    reset(JDBI);
  }

  @Test
  void list() {
    final List<VenueEntity> expected = asList(
        new VenueEntity(1, "Toyota Park", "Bridgeview", "IL", 0),
        new VenueEntity(2, "Columbus Crew Stadium", "Columbus", "OH", 0),
        new VenueEntity(3, "RFK Stadium", "Washington", "DC", 0)
    );
    when(DAO.list()).thenReturn(expected);
    final Response response = EXT.target("/venues").request().get();
    assertThat(response.readEntity(String.class)).isEqualTo(""
        + "["
        + "{\"venueid\":1,\"venuename\":\"Toyota Park\",\"venuecity\":\"Bridgeview\",\"venuestate\":\"IL\",\"venueseats\":0},"
        + "{\"venueid\":2,\"venuename\":\"Columbus Crew Stadium\",\"venuecity\":\"Columbus\",\"venuestate\":\"OH\",\"venueseats\":0},"
        + "{\"venueid\":3,\"venuename\":\"RFK Stadium\",\"venuecity\":\"Washington\",\"venuestate\":\"DC\",\"venueseats\":0}"
        + "]"
    );
  }
}
