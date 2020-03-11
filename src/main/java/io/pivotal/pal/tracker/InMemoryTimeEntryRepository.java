package io.pivotal.pal.tracker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    private final Map<Long, TimeEntry> store = new LinkedHashMap<>();
    private AtomicLong sequence = new AtomicLong(0);

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        long currentId = timeEntry.getId() == 0 ? sequence.incrementAndGet() : timeEntry.getId();
        final TimeEntry created = TimeEntry.builder()
                .id(currentId)
                .projectId(timeEntry.getProjectId())
                .userId(timeEntry.getUserId())
                .date(timeEntry.getDate() == null ? LocalDate.now() : timeEntry.getDate())
                .hours(timeEntry.getHours())
                .build();

        store.put(created.getId(), created);
        return created;
    }

    @Override
    public TimeEntry find(long id) {
        return store.get(id);
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry foundedTimeEntry = find(id);
        if (foundedTimeEntry == null) {
            return null;
        }

        store.remove(foundedTimeEntry);
        timeEntry.setId(id);
        return create(timeEntry);
    }

    @Override
    public void delete(long id) {
        store.remove(id);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(store.values());
    }
}
