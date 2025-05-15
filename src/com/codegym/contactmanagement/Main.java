package com.codegym.contactmanagement;

import com.codegym.contactmanagement.controller.ContactController;

public class Main {
    public static void main(String[] args) {
        ContactController contactController = new ContactController();
        contactController.run();
    }
}