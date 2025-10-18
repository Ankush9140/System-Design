import java.time.LocalDateTime;
import java.util.*;

class User {
    private final String name;
    private final String userId;
    private UserCalendar calendar;

    public User(String name, String userId) {
        this.name = name;
        this.userId = userId;
        this.calendar = new UserCalendar();
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public UserCalendar getCalendar() { return calendar; }
}

class Event {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final String eventId;
    private final String title;

    public Event(LocalDateTime start, LocalDateTime end, String eventId, String title) {
        this.start = start;
        this.end = end;
        this.eventId = eventId;
        this.title = title;
    }

    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }
    public String getTitle() { return title; }
    public String getEventId() { return eventId; }
}

class UserCalendar {
    private List<Event> events;

    public UserCalendar() {
        this.events = new ArrayList<>();
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public void removeEvent(Event event) {
        events.removeIf(e -> e.getEventId().equals(event.getEventId()));
    }

    public List<Event> getEvents() {
        return events;
    }
}

class BookingService {
    public void bookEvent(User user, Event eventToAdd) {
        UserCalendar calendar = user.getCalendar();
        List<Event> events = calendar.getEvents();
        for (Event event : events) {
            if (eventsOverlap(event, eventToAdd)) {
                System.out.println("The event is colliding with another event in the calendar.");
                return;
            }
        }
        calendar.addEvent(eventToAdd);
        System.out.println("Event added successfully");
    }

    public void removeEvent(User user, Event eventToRemove) {
        UserCalendar calendar = user.getCalendar();
        calendar.removeEvent(eventToRemove);
    }

    public boolean eventsOverlap(Event eventA, Event eventB) {
        return eventA.getStart().isBefore(eventB.getEnd()) && eventB.getStart().isBefore(eventA.getEnd());
    }
}

public class CalendarBookingSystem {
    public static void main(String[] args) {
        User user = new User("Ankush", "U00001");
        BookingService bookingService = new BookingService();

        System.out.println("Welcome, " + user.getName() + "! Your Calendar Booking System is ready.\n");

        LocalDateTime start1 = LocalDateTime.of(2024, 6, 14, 10, 0);
        LocalDateTime end1 = LocalDateTime.of(2024, 6, 14, 11, 0);
        Event event1 = new Event(start1, end1, "E00001", "Team Meeting");
        bookingService.bookEvent(user, event1);

        LocalDateTime start2 = LocalDateTime.of(2024, 6, 14, 11, 30);
        LocalDateTime end2 = LocalDateTime.of(2024, 6, 14, 12, 30);
        Event event2 = new Event(start2, end2, "E00002", "Project Discussion");
        bookingService.bookEvent(user, event2);

        LocalDateTime start3 = LocalDateTime.of(2024, 6, 14, 10, 30);
        LocalDateTime end3 = LocalDateTime.of(2024, 6, 14, 11, 30);
        Event event3 = new Event(start3, end3, "E00003", "Client Call");
        bookingService.bookEvent(user, event3);

        System.out.println("\nRemoving 'Project Discussion' event...");
        bookingService.removeEvent(user, event2);

        System.out.println("\nCurrent Events in " + user.getName() + "'s Calendar:");
        for (Event event : user.getCalendar().getEvents()) {
            System.out.println("- " + event.getTitle() + " from " + event.getStart() + " to " + event.getEnd());
        }
    }
}
