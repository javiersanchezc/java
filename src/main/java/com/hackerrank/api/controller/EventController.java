package com.hackerrank.api.controller;

import com.hackerrank.api.model.Event;
import com.hackerrank.api.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Event> getAllEvent() {
        return eventService.getAllEvent();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@RequestBody Event event) {
        return eventService.createNewEvent(event);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable("id") long id) {
        Event event = eventService.getEventById(id);
        if (event == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @GetMapping("/top3")
    public ResponseEntity<List<Event>> top3By(@RequestParam("by") String sortBy) {
        if (!sortBy.equals("cost") && !sortBy.equals("duration")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Event> topThreeEvents = eventService.getTopThreeEvents(sortBy);
        return new ResponseEntity<>(topThreeEvents, HttpStatus.OK);
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getTotalByField(@RequestParam("by") String field) {
        if (!field.equals("cost") && !field.equals("duration")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        double total = eventService.getTotalByField(field);
        return new ResponseEntity<>(total, HttpStatus.OK);
    }

}
