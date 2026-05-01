package edu.westga.comp2320.babynamestatistics.model;

import java.util.Objects;

/**
 * Represents a single baby name record with name, gender, year, and frequency.
 * Two records are considered equal if they share name, gender, and year.
 *
 * @author Raphael Rao
 */
public class NameRecord {

    private final String name;
    private final char gender;
    private final int year;
    private int frequency;

    /**
     * Creates a new NameRecord.
     *
     * @param name      the baby name (non-null, non-blank)
     * @param gender    the gender ('M' or 'F')
     * @param year      the year (non-negative)
     * @param frequency the frequency (non-negative)
     * @throws IllegalArgumentException if any argument is invalid
     */
    public NameRecord(String name, char gender, int year, int frequency) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank.");
        }
        if (gender != 'M' && gender != 'F') {
            throw new IllegalArgumentException("Gender must be 'M' or 'F'.");
        }
        if (year < 0) {
            throw new IllegalArgumentException("Year cannot be negative.");
        }
        if (frequency < 0) {
            throw new IllegalArgumentException("Frequency cannot be negative.");
        }
        this.name = name;
        this.gender = gender;
        this.year = year;
        this.frequency = frequency;
    }

    /**
     * @return the baby name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the gender
     */
    public char getGender() {
        return this.gender;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return this.year;
    }

    /**
     * @return the frequency
     */
    public int getFrequency() {
        return this.frequency;
    }

    /**
     * Updates the frequency.
     *
     * @param frequency the new frequency (non-negative)
     * @throws IllegalArgumentException if frequency is negative
     */
    public void setFrequency(int frequency) {
        if (frequency < 0) {
            throw new IllegalArgumentException("Frequency cannot be negative.");
        }
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof NameRecord that)) {
            return false;
        }
        return this.year == that.year
                && this.gender == that.gender
                && this.name.equalsIgnoreCase(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name.toLowerCase(), this.gender, this.year);
    }

    @Override
    public String toString() {
        return String.format("%s (%c, %d): %d",
                this.name, this.gender, this.year, this.frequency);
    }
}