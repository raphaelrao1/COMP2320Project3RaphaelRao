package edu.westga.comp2320.babynamestatistics.model;

import java.io.*;

/**
 * Reads and writes NameRecord data in CSV format.
 * Each line: name,gender,year,frequency
 *
 * @author Raphael Rao
 */
public class CsvFileHandler {
    /**
     * Loads records from the given CSV file into the manager.
     * Replaces existing records.
     *
     * @param file    the CSV file to read from
     * @param manager the manager to populate
     * @throws IOException if the file cannot be read or is malformed
     */
    public static void load(File file, NameRecordManager manager) throws IOException {
        manager.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length != 4) {
                    throw new IOException("Malformed line: " + line);
                }
                String name = parts[0].trim();
                char gender = parts[1].trim().toUpperCase().charAt(0);
                int year = Integer.parseInt(parts[2].trim());
                int frequency = Integer.parseInt(parts[3].trim());
                manager.add(new NameRecord(name, gender, year, frequency));
            }
        } catch (NumberFormatException ex) {
            throw new IOException("Invalid number in file: " + ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            throw new IOException("Invalid record in file: " + ex.getMessage(), ex);
        }
    }

    /**
     * Saves all records from the manager to the given file.
     *
     * @param file    the destination file
     * @param manager the manager whose records to save
     * @throws IOException if the file cannot be written
     */
    public static void save(File file, NameRecordManager manager) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (NameRecord record : manager.getAllSorted()) {
                writer.printf("%s,%c,%d,%d%n",
                        record.getName(),
                        record.getGender(),
                        record.getYear(),
                        record.getFrequency());
            }
        }
    }
}
