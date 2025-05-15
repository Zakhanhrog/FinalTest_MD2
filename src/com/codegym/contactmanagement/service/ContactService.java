package com.codegym.contactmanagement.service;

import com.codegym.contactmanagement.model.Contact;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContactService {
    private List<Contact> contactList;
    private static final String DATA_FILE_PATH = "./data/contacts.csv";

    public ContactService() {
        this.contactList = new ArrayList<>();
    }

    public List<Contact> getAllContacts() {
        return new ArrayList<>(contactList);
    }

    public void addContact(Contact contact) {
        contactList.add(contact);
    }

    public Contact findContactByPhoneNumber(String phoneNumber) {
        for (Contact contact : contactList) {
            if (contact.getPhoneNumber().equals(phoneNumber)) {
                return contact;
            }
        }
        return null;
    }

    public boolean updateContact(String phoneNumber, String group, String fullName, String gender, String address, String birthDate, String email) {
        Contact contactToUpdate = findContactByPhoneNumber(phoneNumber);
        if (contactToUpdate != null) {
            contactToUpdate.setGroup(group);
            contactToUpdate.setFullName(fullName);
            contactToUpdate.setGender(gender);
            contactToUpdate.setAddress(address);
            contactToUpdate.setBirthDate(birthDate);
            contactToUpdate.setEmail(email);
            return true;
        }
        return false;
    }

    public boolean deleteContact(String phoneNumber) {
        Contact contactToRemove = findContactByPhoneNumber(phoneNumber);
        if (contactToRemove != null) {
            contactList.remove(contactToRemove);
            return true;
        }
        return false;
    }

    public List<Contact> searchContacts(String searchTerm) {
        String lowerCaseSearchTerm = searchTerm.toLowerCase();
        return contactList.stream()
                .filter(contact -> contact.getPhoneNumber().contains(lowerCaseSearchTerm) ||
                        contact.getFullName().toLowerCase().contains(lowerCaseSearchTerm))
                .collect(Collectors.toList());
    }

    public void loadContactsFromFile() throws IOException {
        List<Contact> contactsFromFile = CSVFile.readContactsFromFile(DATA_FILE_PATH);
        this.contactList.clear();
        this.contactList.addAll(contactsFromFile);
    }

    public void saveContactsToFile() throws IOException {
        CSVFile.writeContactsToFile(DATA_FILE_PATH, this.contactList);
    }
}