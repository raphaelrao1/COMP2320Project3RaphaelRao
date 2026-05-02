package edu.westga.comp2320.babynamestatistics.model;

import java.util.ArrayList;
import java.util.Comparator;
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
     */
    public void remove(NameRecord record) {
        this.records.remove(record);
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
     * Returns all records sorted by year (descending), gender
     * (F before M), frequency (descending), then name (ascending,
     * case-insensitive).
     *
     * @return a new sorted list
     */
    public List<NameRecord> getAllSorted() {
        List<NameRecord> sorted = new ArrayList<>(this.records);
        sorted.sort(getDefaultComparator());
        return sorted;
    }

    /**
     * Sorts the given list in place using the default ordering.
     *
     * @param records the list to sort
     */
    public static void sortRecords(List<NameRecord> records) {
        records.sort(getDefaultComparator());
    }

    private static Comparator<NameRecord> getDefaultComparator() {
        return Comparator
                .comparingInt(NameRecord::getYear).reversed()
                .thenComparingInt(record -> record.getGender() == 'F' ? 0 : 1)
                .thenComparing(Comparator.comparingInt(NameRecord::getFrequency).reversed())
                .thenComparing(NameRecord::getName, String.CASE_INSENSITIVE_ORDER);
    }

    /**
     * Searches for records matching the provided filters.
     * A null or blank name is treated as a wildcard, as are
     * null gender, year, and frequency parameters.
     *
     * @param name      optional name filter (case-insensitive exact match)
     * @param gender    optional gender filter ('M', 'F', or null)
     * @param year      optional year filter
     * @param frequency optional frequency filter
     * @return matching records
     */
    public List<NameRecord> search(String name, Character gender,
                                   Integer year, Integer frequency) {
        List<NameRecord> result = new ArrayList<>();
        for (NameRecord record : this.records) {
            if (name != null && !name.isBlank()
                    && !record.getName().equalsIgnoreCase(name)) {
                continue;
            }
            if (gender != null && record.getGender() != gender) {
                continue;
            }
            if (year != null && record.getYear() != year) {
                continue;
            }
            if (frequency != null && record.getFrequency() != frequency) {
                continue;
            }
            result.add(record);
        }
        return result;
    }
}