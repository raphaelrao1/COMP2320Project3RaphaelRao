package edu.westga.comp2320.babynamestatistics.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages a collection of NameRecord objects.
 * Enforces uniqueness on (name, gender, year).
 *
 * @author Your Name
 */
public class NameRecordManager {

    private final List<NameRecord> records;

    /**
     * Creates an empty manager.
     */
    public NameRecordManager() {
        this.records = new ArrayList<>();
    }

    /**
     * Adds a record if no duplicate (by name, gender, year) exists.
     *
     * @param record the record to add (non-null)
     * @return true if added, false if a duplicate already exists
     * @throws IllegalArgumentException if record is null
     */
    public boolean add(NameRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("Record cannot be null.");
        }
        if (this.records.contains(record)) {
            return false;
        }
        return this.records.add(record);
    }

    /**
     * Removes the given record.
     *
     * @param record the record to remove
     * @return true if removed
     */
    public boolean remove(NameRecord record) {
        return this.records.remove(record);
    }

    /**
     * Removes all records.
     */
    public void clear() {
        this.records.clear();
    }

    /**
     * @return the number of records
     */
    public int size() {
        return this.records.size();
    }

    /**
     * @return true if there are no records
     */
    public boolean isEmpty() {
        return this.records.isEmpty();
    }

    /**
     * Returns an unmodifiable view of all records.
     *
     * @return all records
     */
    public List<NameRecord> getAll() {
        return Collections.unmodifiableList(this.records);
    }
}