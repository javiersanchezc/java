package com.hackerrank.api.service.impl;

import com.hackerrank.api.exception.BadRequestException;
import com.hackerrank.api.model.Event;
import com.hackerrank.api.repository.EventRepository;
import com.hackerrank.api.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DefaultEventService implements EventService {
  private final EventRepository eventRepository;

  @Autowired
  DefaultEventService(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  @Override
  public List<Event> getAllEvent() {
    return eventRepository.findAll();
  }


  @Override
  public Event createNewEvent(Event event) {
    if (event.getId() != null) {
      throw new BadRequestException("The ID must not be provided when creating a new Event");
    }

    return eventRepository.save(event);
  }

  @Override
  public Event getEventById(Long id) {
    return eventRepository.findById(id).orElse(null);
  }

  @Override
  public List<Event> top3By(String by) {
    List<Event> events = eventRepository.findAll();
    if (by.equals("cost")) {
        events.sort(Comparator.comparingDouble(Event::getCost));
    } else if (by.equals("duration")) {
        events.sort(Comparator.comparingLong(Event::getDuration));
    }
    return events.subList(0, Math.min(3, events.size()));
  }

  @Override
  public Integer totalBy(String by) {
    List<Event> events = eventRepository.findAll();
    double total = 0;
    if (by.equals("cost")) {
        for (Event event : events) {
            total += event.getCost();
        }
    } else if (by.equals("duration")) {
        for (Event event : events) {
            total += event.getDuration();
        }
    }
    return (int) total;
  }

  @Override
  public List<Event> getTopThreeEvents(String sortBy) {
    List<Event> events = eventRepository.findAll();
    if (sortBy.equals("cost")) {
        events.sort(Comparator.comparingDouble(Event::getCost));
    } else {
        events.sort(Comparator.comparingLong(Event::getDuration));
    }
    return events.subList(0, Math.min(3, events.size()));
}

  @Override
  public double getTotalByField(String field) {
    List<Event> events = eventRepository.findAll();
    double total = 0;
    if (field.equals("cost")) {
        for (Event event : events) {
            total += event.getCost();
        }
    } else if (field.equals("duration")) {
        for (Event event : events) {
            total += event.getDuration();
        }
    }
    return total;
}


}
