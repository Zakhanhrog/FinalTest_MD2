package com.codegym.contactmanagement.service;

import com.codegym.contactmanagement.model.Contact;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSVFile {
    private static final String CSV_HEADER = "PhoneNumber,Group,FullName,Gender,Address,BirthDate,Email";

    public static void writeContactsToFile(String filePath, List<Contact> contacts) throws IOException {
        Files.createDirectories(Paths.get(filePath).getParent());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(CSV_HEADER);
            writer.newLine();
            for (Contact contact : contacts) {
                writer.write(contact.toCsvString());
                writer.newLine();
            }
        }
    }

    public static List<Contact> readContactsFromFile(String filePath) throws IOException {
        List<Contact> contacts = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return contacts;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            if (line == null || !line.trim().equals(CSV_HEADER)) {
                if (line == null) return contacts;
            }
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] values = line.split(",", -1);
                if (values.length == 7) {
                    contacts.add(new Contact(values[0], values[1], values[2], values[3], values[4], values[5], values[6]));
                }
            }
        }
        return contacts;
    }
}