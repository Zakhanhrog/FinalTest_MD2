package com.codegym.contactmanagement.view;

import com.codegym.contactmanagement.model.Contact;
import com.codegym.contactmanagement.utils.ValidationUtils;
import java.util.List;
import java.util.Scanner;

public class ContactView {
    private Scanner scanner;

    private static final char TL_CORNER = '╔';
    private static final char TR_CORNER = '╗';
    private static final char BL_CORNER = '╚';
    private static final char BR_CORNER = '╝';
    private static final char H_LINE = '═';
    private static final char V_LINE = '║';
    private static final char T_INTERSECT = '╦';
    private static final char B_INTERSECT = '╩';
    private static final char L_INTERSECT = '╠';
    private static final char R_INTERSECT = '╣';
    private static final char CROSS_INTERSECT = '╬';
    private static final int BOX_WIDTH = 70;

    public ContactView() {
        this.scanner = new Scanner(System.in);
    }

    private String createLine(char left, char middle, char right, int width) {
        StringBuilder sb = new StringBuilder();
        sb.append(left);
        for (int i = 0; i < width - 2; i++) {
            sb.append(middle);
        }
        sb.append(right);
        return sb.toString();
    }

    private void printRow(String text) {
        printRow(text, false);
    }

    private void printRow(String text, boolean isMenuItem) {
        int textLength = text.length();
        int paddingTotal = BOX_WIDTH - 2 - textLength;
        int padLeft, padRight;

        if (isMenuItem) {
            padLeft = 2;
            padRight = BOX_WIDTH - 2 - padLeft - textLength;
        } else {
            padLeft = paddingTotal / 2;
            padRight = paddingTotal - padLeft;
        }

        if (padRight < 0) padRight = 0;

        System.out.print(V_LINE);
        for (int i = 0; i < padLeft; i++) System.out.print(" ");
        System.out.print(text.substring(0, Math.min(textLength, BOX_WIDTH - 2 - padLeft)));
        for (int i = 0; i < padRight; i++) System.out.print(" ");
        System.out.println(V_LINE);
    }

    private void printEmptyRow() {
        System.out.println(V_LINE + " ".repeat(BOX_WIDTH - 2) + V_LINE);
    }

    private void printTopBorder() {
        System.out.println(createLine(TL_CORNER, H_LINE, TR_CORNER, BOX_WIDTH));
    }

    private void printBottomBorder() {
        System.out.println(createLine(BL_CORNER, H_LINE, BR_CORNER, BOX_WIDTH));
    }

    private void printMidBorder() {
        System.out.println(createLine(L_INTERSECT, H_LINE, R_INTERSECT, BOX_WIDTH));
    }

    private void printTitle(String title) {
        printTopBorder();
        printRow(title.toUpperCase());
        printMidBorder();
    }

    private void printPrompt(String prompt) {
        System.out.print(V_LINE + "  " + prompt);
    }

    private void printError(String errorMessage) {
        int availableWidth = BOX_WIDTH - 6;
        String prefix = "Lỗi: ";
        String fullMessage = prefix + errorMessage;
        if (fullMessage.length() > availableWidth) {
            fullMessage = fullMessage.substring(0, availableWidth - 3) + "...";
        }
        System.out.println(V_LINE + "  " + String.format("%-" + (BOX_WIDTH-4) + "s", fullMessage) + V_LINE);
    }


    public int displayMenuAndGetChoice() {
        System.out.println();
        printTitle("CHƯƠNG TRÌNH QUẢN LÝ DANH BẠ");
        printRow("Chọn chức năng theo số:", true);
        printEmptyRow();
        printRow("1. Xem danh sách", true);
        printRow("2. Thêm mới", true);
        printRow("3. Cập nhật", true);
        printRow("4. Xoá", true);
        printRow("5. Tìm kiếm", true);
        printRow("6. Đọc từ file", true);
        printRow("7. Ghi vào file", true);
        printRow("8. Thoát", true);
        printMidBorder();
        int choice = -1;
        boolean validInput = false;
        while(!validInput) {
            printPrompt("Chọn chức năng (1-8): ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice >= 1 && choice <= 8) {
                    validInput = true;
                } else {
                    printError("Lựa chọn phải từ 1 đến 8.");
                }
            } else {
                printError("Vui lòng nhập một số.");
                scanner.next();
            }
            scanner.nextLine();
        }
        printBottomBorder();
        return choice;
    }

    public void displayContacts(List<Contact> contacts) {
        printTitle("DANH SÁCH DANH BẠ");
        if (contacts.isEmpty()) {
            printRow("Danh bạ trống.");
        } else {
            for (int i = 0; i < contacts.size(); i++) {
                String contactLine = (i + 1) + ". " + contacts.get(i).toShortString();
                if (contactLine.length() > BOX_WIDTH - 6) {
                    contactLine = contactLine.substring(0, BOX_WIDTH - 9) + "...";
                }
                printRow(contactLine, true);
                if ((i + 1) % 5 == 0 && i < contacts.size() - 1) {
                    printMidBorder();
                    printRow("Nhấn Enter để xem tiếp...");
                    printMidBorder();
                    scanner.nextLine();
                }
            }
        }
        printBottomBorder();
    }

    private String getInputWithValidation(String promptText, String fieldName, java.util.function.Predicate<String> validator, String errorMessage) {
        String input;
        boolean firstAttempt = true;
        do {
            if (!firstAttempt) {
                printError(errorMessage);
            }
            printPrompt(promptText);
            input = scanner.nextLine().trim();
            firstAttempt = false;
        } while (!validator.test(input));
        return input;
    }

    private String getRequiredInput(String promptText, String fieldName) {
        return getInputWithValidation(promptText, fieldName, s -> !ValidationUtils.isNullOrEmpty(s), fieldName + " không được để trống.");
    }

    public Contact getNewContactDetails() {
        printTitle("THÊM MỚI DANH BẠ");

        String phoneNumber = getInputWithValidation("Nhập số điện thoại: ", "Số điện thoại",
                ValidationUtils::isValidPhoneNumber,
                "SĐT không hợp lệ (10 số, bắt đầu bằng 0).");
        String group = getRequiredInput("Nhập nhóm: ", "Nhóm");
        String fullName = getRequiredInput("Nhập họ tên: ", "Họ tên");
        String gender = getRequiredInput("Nhập giới tính: ", "Giới tính");
        String address = getRequiredInput("Nhập địa chỉ: ", "Địa chỉ");
        String birthDate = getInputWithValidation("Nhập ngày sinh (dd/MM/yyyy): ", "Ngày sinh",
                ValidationUtils::isValidBirthDate,
                "Ngày sinh không hợp lệ (dd/MM/yyyy).");
        String email = getInputWithValidation("Nhập email: ", "Email",
                ValidationUtils::isValidEmail,
                "Email không hợp lệ.");
        printBottomBorder();
        return new Contact(phoneNumber, group, fullName, gender, address, birthDate, email);
    }

    public String getPhoneNumberInput(String promptMessage) {
        printPrompt(promptMessage);
        return scanner.nextLine().trim();
    }

    public Contact getUpdatedContactDetailsFromUser(Contact existingContact) {
        printTitle("CẬP NHẬT DANH BẠ SĐT: " + existingContact.getPhoneNumber());

        String group = getInputWithValidation("Nhóm mới (hiện tại: " + existingContact.getGroup() + "): ", "Nhóm",
                s -> !ValidationUtils.isNullOrEmpty(s), "Nhóm không được để trống.");

        String fullName = getInputWithValidation("Họ tên mới (hiện tại: " + existingContact.getFullName() + "): ", "Họ tên",
                s -> !ValidationUtils.isNullOrEmpty(s), "Họ tên không được để trống.");

        String gender = getInputWithValidation("Giới tính mới (hiện tại: " + existingContact.getGender() + "): ", "Giới tính",
                s -> !ValidationUtils.isNullOrEmpty(s), "Giới tính không được để trống.");

        String address = getInputWithValidation("Địa chỉ mới (hiện tại: " + existingContact.getAddress() + "): ", "Địa chỉ",
                s -> !ValidationUtils.isNullOrEmpty(s), "Địa chỉ không được để trống.");

        String birthDate = getInputWithValidation("Ngày sinh mới (dd/MM/yyyy) (hiện tại: " + existingContact.getBirthDate() + "): ", "Ngày sinh",
                ValidationUtils::isValidBirthDate, "Ngày sinh không hợp lệ (dd/MM/yyyy).");

        String email = getInputWithValidation("Email mới (hiện tại: " + existingContact.getEmail() + "): ", "Email",
                ValidationUtils::isValidEmail, "Email không hợp lệ.");

        printBottomBorder();
        return new Contact(existingContact.getPhoneNumber(), group, fullName, gender, address, birthDate, email);
    }

    public String getSearchTermInput() {
        printTitle("TÌM KIẾM DANH BẠ");
        printPrompt("Nhập số điện thoại hoặc họ tên cần tìm: ");
        String term = scanner.nextLine().trim();
        return term;
    }

    public boolean getConfirmation(String promptMessage) {
        System.out.println(createLine(L_INTERSECT, H_LINE, R_INTERSECT, BOX_WIDTH));
        printPrompt(promptMessage + " (Y/N): ");
        String input = scanner.nextLine().trim();
        System.out.println(createLine(BL_CORNER, H_LINE, BR_CORNER, BOX_WIDTH));
        return input.equalsIgnoreCase("Y");
    }

    public void displayMessage(String message) {
        printTitle("THÔNG BÁO");
        printRow(message);
        printBottomBorder();
    }

    public void displayOperationResult(String operationName, boolean success) {
        printTitle("KẾT QUẢ THAO TÁC");
        if (success) {
            printRow(operationName + " thành công!");
        } else {
            printRow("Lỗi: " + operationName + " thất bại.");
        }
        printBottomBorder();
    }

    public void displaySectionStart(String sectionName) {
    }
}