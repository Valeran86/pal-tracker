package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class TimeEntryController {
    private final TimeEntryRepository repository;

    @Autowired
    public TimeEntryController(TimeEntryRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> list = repository.list();

        return ResponseEntity.ok(list);
    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntry = repository.create(timeEntryToCreate);

        URI uri = URI.create("/time-entries/" + timeEntry.getId());

        return ResponseEntity.created(uri).body(timeEntry);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") long timeEntryId) {
        TimeEntry timeEntry = repository.find(timeEntryId);
        if (timeEntry == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(timeEntry);
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity update(@PathVariable("id") long timeEntryId, @RequestBody TimeEntry timeEntry) {
        TimeEntry updatedTimeEntry = repository.update(timeEntryId, timeEntry);
        if (updatedTimeEntry == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedTimeEntry);
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity delete(@PathVariable("id") long timeEntryId) {
        repository.delete(timeEntryId);

        return ResponseEntity.noContent().build();
    }
}
