package matcha.event.manipulation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matcha.event.db.EventDB;
import matcha.event.model.Event;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventManipulator {

    private final EventDB eventDB;

    public void insertEvent(Event event) {
        eventDB.insertEvent(event);
    }

    public List<Event> getAllEvents() {
        return eventDB.getAllEvents();
    }
}
