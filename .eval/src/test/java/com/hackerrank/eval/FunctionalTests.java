package com.hackerrank.eval;

import com.hackerrank.eval.extensions.RESTExtension;
import com.hackerrank.eval.model.Event;
import static io.restassured.RestAssured.get;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Arrays;
import java.util.List;

@ExtendWith({RESTExtension.class})
class FunctionalTests {

  @Test
  @DisplayName("test top 3")
  void testTop3() {
    List<Event> actual = Arrays.asList(get("/event/top3?by=duration")
            .then()
            .statusCode(SC_OK)
            .extract()
            .response()
            .as(Event[].class));

    assertThat(
            actual.stream().map(Event::getName).toArray(),
            arrayContaining(new String[]{"event7", "event6", "event5"}));
  }

  @Test
  @DisplayName("total")
  void testTotal() {
    Integer actual = get("/event/total?by=cost")
            .then()
            .statusCode(SC_OK)
            .extract()
            .response()
            .as(Integer.class);

    Assertions.assertEquals(28, actual);
  }
}
