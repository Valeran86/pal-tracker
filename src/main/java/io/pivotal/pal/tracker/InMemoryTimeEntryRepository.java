package io.pivotal.pal.tracker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    private static final Map<Long, TimeEntry> store = new ConcurrentHashMap<>();
    private static AtomicLong sequence = new AtomicLong(0);

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        final long currentId = sequence.get();
        final TimeEntry created = TimeEntry.builder()
                .id(timeEntry.getId() < currentId ? sequence.incrementAndGet() : timeEntry.getId())
                .projectId(timeEntry.getProjectId())
                .userId(timeEntry.getUserId())
                .date(timeEntry.getDate() == null ? LocalDate.now() : timeEntry.getDate())
                .hours(timeEntry.getHours())
                .build();

        store.put(timeEntry.getId(), created);
        return created;
    }

    @Override
    public TimeEntry find(long id) {
        return store.get(toId(id));
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        store.put(toId(id), timeEntry);
        return null;
    }

    @Override
    public void delete(long id) {
        store.remove(toId(id));
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(store.values());
    }

    private static Long toId(long id) {
        return id;
    }
}
