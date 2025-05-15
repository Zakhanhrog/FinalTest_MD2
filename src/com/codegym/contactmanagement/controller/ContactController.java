package com.codegym.contactmanagement.controller;

import com.codegym.contactmanagement.model.Contact;
import com.codegym.contactmanagement.service.ContactService;
import com.codegym.contactmanagement.view.ContactView;
import com.codegym.contactmanagement.utils.ValidationUtils;
import java.io.IOException;
import java.util.List;

public class ContactController {
    private ContactService contactService;
    private ContactView contactView;

    public ContactController() {
        this.contactService = new ContactService();
        this.contactView = new ContactView();
    }

    public void run() {
        boolean running = true;
        while (running) {
            int choice = contactView.displayMenuAndGetChoice();
            switch (choice) {
                case 1:
                    viewContacts();
                    break;
                case 2:
                    addNewContact();
                    break;
                case 3:
                    updateContact();
                    break;
                case 4:
                    deleteContact();
                    break;
                case 5:
                    searchContacts();
                    break;
                case 6:
                    loadContactsFromFile();
                    break;
                case 7:
                    saveContactsToFile();
                    break;
                case 8:
                    running = false;
                    contactView.displayMessage("Đã thoát chương trình.");
                    break;
                default:
                    contactView.displayMessage("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }
        }
    }

    private void viewContacts() {
        List<Contact> contacts = contactService.getAllContacts();
        contactView.displayContacts(contacts);
    }

    private void addNewContact() {
        Contact newContact = contactView.getNewContactDetails();
        if (contactService.findContactByPhoneNumber(newContact.getPhoneNumber()) != null) {
            contactView.displayMessage("Lỗi: Số điện thoại đã tồn tại trong danh bạ.");
            return;
        }
        contactService.addContact(newContact);
        contactView.displayOperationResult("Thêm mới danh bạ", true);
    }

    private void updateContact() {
        contactView.displaySectionStart("CẬP NHẬT DANH BẠ");
        String phoneNumber = contactView.getPhoneNumberInput("Nhập số điện thoại của danh bạ cần cập nhật (nhấn Enter để bỏ qua): ");
        if (ValidationUtils.isNullOrEmpty(phoneNumber)) {
            return;
        }

        Contact existingContact = contactService.findContactByPhoneNumber(phoneNumber);
        if (existingContact == null) {
            contactView.displayMessage("Không tìm được danh bạ với số điện thoại trên.");
            return;
        }

        Contact updatedDetails = contactView.getUpdatedContactDetailsFromUser(existingContact);
        boolean success = contactService.updateContact(
                existingContact.getPhoneNumber(),
                updatedDetails.getGroup(),
                updatedDetails.getFullName(),
                updatedDetails.getGender(),
                updatedDetails.getAddress(),
                updatedDetails.getBirthDate(),
                updatedDetails.getEmail()
        );
        contactView.displayOperationResult("Cập nhật danh bạ", success);
    }

    private void deleteContact() {
        contactView.displaySectionStart("XOÁ DANH BẠ");
        String phoneNumber = contactView.getPhoneNumberInput("Nhập số điện thoại của danh bạ cần xoá (nhấn Enter để bỏ qua): ");
        if (ValidationUtils.isNullOrEmpty(phoneNumber)) {
            return;
        }

        Contact contactToDelete = contactService.findContactByPhoneNumber(phoneNumber);
        if (contactToDelete == null) {
            contactView.displayMessage("Không tìm được danh bạ với số điện thoại trên.");
            return;
        }

        boolean confirmed = contactView.getConfirmation("Bạn có chắc muốn xoá thông tin danh bạ này?");
        if (confirmed) {
            boolean success = contactService.deleteContact(phoneNumber);
            contactView.displayOperationResult("Xoá danh bạ", success);
        } else {
            contactView.displayMessage("Hủy bỏ thao tác xoá.");
        }
    }

    private void searchContacts() {
        String searchTerm = contactView.getSearchTermInput();
        if (ValidationUtils.isNullOrEmpty(searchTerm)) {
            contactView.displayMessage("Nội dung tìm kiếm không được để trống.");
            return;
        }
        List<Contact> results = contactService.searchContacts(searchTerm);
        if (results.isEmpty()) {
            contactView.displayMessage("Không tìm thấy kết quả nào phù hợp.");
        } else {
            contactView.displayContacts(results);
        }
    }

    private void loadContactsFromFile() {
        contactView.displaySectionStart("ĐỌC TỪ FILE");
        boolean confirmed = contactView.getConfirmation("Bạn có chắc muốn đọc dữ liệu từ file? Mọi dữ liệu hiện tại trong bộ nhớ sẽ bị xoá.");
        if (confirmed) {
            try {
                contactService.loadContactsFromFile();
                contactView.displayOperationResult("Đọc dữ liệu từ file", true);
            } catch (IOException e) {
                contactView.displayMessage("Lỗi: Không thể đọc dữ liệu từ file. " + e.getMessage());
                contactView.displayOperationResult("Đọc dữ liệu từ file", false);
            }
        } else {
            contactView.displayMessage("Hủy bỏ thao tác đọc từ file.");
        }
    }

    private void saveContactsToFile() {
        contactView.displaySectionStart("GHI VÀO FILE");
        boolean confirmed = contactView.getConfirmation("Bạn có chắc muốn ghi dữ liệu hiện tại vào file? Dữ liệu trong file sẽ bị ghi đè.");
        if (confirmed) {
            try {
                contactService.saveContactsToFile();
                contactView.displayOperationResult("Ghi dữ liệu vào file", true);
            } catch (IOException e) {
                contactView.displayMessage("Lỗi: Không thể ghi dữ liệu vào file. " + e.getMessage());
                contactView.displayOperationResult("Ghi dữ liệu vào file", false);
            }
        } else {
            contactView.displayMessage("Hủy bỏ thao tác ghi vào file.");
        }
    }
}