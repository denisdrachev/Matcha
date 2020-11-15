package matcha.event.service;

import lombok.AllArgsConstructor;
import matcha.event.manipulation.EventManipulator;
import matcha.event.model.Event;
import matcha.reactive.EventUnicastService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EventService {

    private EventUnicastService eventUnicastService;
    private EventManipulator eventManipulator;

    public void saveEvent(Event event) {
        if (event == null)
            return;
        eventManipulator.insertEvent(event);
        eventUnicastService.onNext(event);
    }

    public List<Event> getAllEvents() {
        return eventManipulator.getAllEvents();
    }
}
