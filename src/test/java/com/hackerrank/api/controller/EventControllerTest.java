package com.hackerrank.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.api.model.Event;
import com.hackerrank.api.repository.EventRepository;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hackerrank.api.service.EventService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
class EventControllerTest {
    ObjectMapper om = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EventRepository repository;
    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @Test
    public void testCreation() throws Exception {
        Event expectedRecord = Event.builder().name("Test Country").build();
        Event actualRecord = om.readValue(mockMvc.perform(post("/event")
                        .contentType("application/json")
                        .content(om.writeValueAsString(expectedRecord)))
                .andDo(print())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), Event.class);
        Assert.assertEquals(expectedRecord.getName(), actualRecord.getName());
    }

    @Test
    public void testGetAllEvent() throws Exception {
        List<Event> events = Arrays.asList(new Event(1, "Event 1", 100.0, 2),
                new Event(2, "Event 2", 200.0, 4));

        when(eventService.getAllEvent()).thenReturn(events);
        List<Event> result = eventController.getAllEvent();
        assertFalse(result.isEmpty());
        assertTrue(result.containsAll(events));
    }

    @Test
    public void testGetTotalByField() throws Exception {
        double expectedTotal = 300.0;
        when(eventService.getTotalByField("cost")).thenReturn(expectedTotal);
        ResponseEntity<Double> result = eventController.getTotalByField("cost");
        assertEquals(expectedTotal, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testGetTotalByFieldWithInvalidField() throws Exception {
        ResponseEntity<Double> result = eventController.getTotalByField("invalid_field");
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testGetEventById() throws Exception {

        long id = 1;
        Event expectedEvent = new Event((int) id, "Event 1", 100.0, 2);
        when(eventService.getEventById(id)).thenReturn(expectedEvent);
        ResponseEntity<Event> result = eventController.getEventById(id);
        assertEquals(expectedEvent, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testGetEventByIdWithInvalidId() throws Exception {
        long id = -1;
        ResponseEntity<Event> result = eventController.getEventById(id);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testTop3By() throws Exception {
        List<Event> expectedEvents = Arrays.asList(new Event(2, "Event 2", 200.0, 4),
                new Event(1, "Event 1", 100.0, 2));
        when(eventService.getTopThreeEvents("cost")).thenReturn(expectedEvents);
        ResponseEntity<List<Event>> result = eventController.top3By("cost");
        assertEquals(expectedEvents, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testTop3ByWithInvalidField() throws Exception {
        String sortBy = "invalid_field";
        ResponseEntity<List<Event>> result = eventController.top3By(sortBy);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
}
