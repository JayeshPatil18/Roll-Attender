package com.apt.attendancecall;

import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationOfInput {

    char[] specificChar_withNum = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '`', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+', '{', '}', '[', ']', ';', ':', '|', '"', '<', ',', '>', '.', '?', '/', '\\', '\''};
    char[] specificChar_withoutNum = {'`', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+', '{', '}', '[', ']', ';', ':', '|', '"', '<', ',', '>', '.', '?', '/', '\\', '\''};
    char[] specificChar_forDate = {'`', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+', '{', '}', '[', ']', ';', ':', '|', '"', '<', ',', '>', '.', '?', '\\', '\''}; // This array not contain character '/'
    char[] specificChar_forEmailId = {'`', '~', '!', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+', '{', '}', '[', ']', ';', ':', '|', '"', '<', ',', '>', '?', '/', '\\', '\''}; // This array not contain characters '@', '.'

    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint) || isEmoji(String.valueOf(codePoint))) {
                return true;
            }/*from  w w  w .  j  a va 2 s. com*/
        }
        return false;
    }

    public static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    private static boolean isEmoji(String message) {
        return message.matches("(?:[\uD83C\uDF00-\uD83D\uDDFF]|[\uD83E\uDD00-\uD83E\uDDFF]|" +
                "[\uD83D\uDE00-\uD83D\uDE4F]|[\uD83D\uDE80-\uD83D\uDEFF]|" +
                "[\u2600-\u26FF]\uFE0F?|[\u2700-\u27BF]\uFE0F?|\u24C2\uFE0F?|" +
                "[\uD83C\uDDE6-\uD83C\uDDFF]{1,2}|" +
                "[\uD83C\uDD70\uD83C\uDD71\uD83C\uDD7E\uD83C\uDD7F\uD83C\uDD8E\uD83C\uDD91-\uD83C\uDD9A]\uFE0F?|" +
                "[\u0023\u002A\u0030-\u0039]\uFE0F?\u20E3|[\u2194-\u2199\u21A9-\u21AA]\uFE0F?|[\u2B05-\u2B07\u2B1B\u2B1C\u2B50\u2B55]\uFE0F?|" +
                "[\u2934\u2935]\uFE0F?|[\u3030\u303D]\uFE0F?|[\u3297\u3299]\uFE0F?|" +
                "[\uD83C\uDE01\uD83C\uDE02\uD83C\uDE1A\uD83C\uDE2F\uD83C\uDE32-\uD83C\uDE3A\uD83C\uDE50\uD83C\uDE51]\uFE0F?|" +
                "[\u203C\u2049]\uFE0F?|[\u25AA\u25AB\u25B6\u25C0\u25FB-\u25FE]\uFE0F?|" +
                "[\u00A9\u00AE]\uFE0F?|[\u2122\u2139]\uFE0F?|\uD83C\uDC04\uFE0F?|\uD83C\uDCCF\uFE0F?|" +
                "[\u231A\u231B\u2328\u23CF\u23E9-\u23F3\u23F8-\u23FA]\uFE0F?)+");
    }

    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }


    public boolean isValidPasswordForLogin(String password, TextInputLayout invalidDisplay) {
        // to check emoji
        if (containsEmoji(password)) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Password should not contain any emoji");
            return false;
        }

        // to check space
        if (password.contains(" ")) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Password should not contain any space");
            return false;
        }

        // for checking if password length
        // is between 8 and 15
        if (!((password.length() >= 8)
                && (password.length() <= 15))) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Password length should be between 8 to 15 characters");
            return false;
        }
        return true;
    }

    // whether a password is valid or not
    public boolean isValidCreation(String password, TextInputLayout invalidDisplay) {
        // to check emoji  // code by me
        if (containsEmoji(password)) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Password should not contain any emoji");
            return false;
        }
        // to check space
        if (password.contains(" ")) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Password should not contain any space");
            return false;
        }

        // for checking if password length
        // is between 8 and 15
        if (!((password.length() >= 8)
                && (password.length() <= 15))) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Password length should be between 8 to 15 characters");
            return false;
        }

        if (true) {
            int count = 0;

            // check digits from 0 to 9
            for (int i = 0; i <= 9; i++) {

                // to convert int to string
                String str1 = Integer.toString(i);

                if (password.contains(str1)) {
                    count = 1;
                }
            }
            if (count == 0) {
                invalidDisplay.setErrorEnabled(true);
                invalidDisplay.setError("* Password should contain at least one digit(0-9)");
                return false;
            }
        }

        // for special characters
        if (!(password.contains("@") || password.contains("#")
                || password.contains("!") || password.contains("~")
                || password.contains("$") || password.contains("%")
                || password.contains("^") || password.contains("&")
                || password.contains("*") || password.contains("(")
                || password.contains(")") || password.contains("-")
                || password.contains("+") || password.contains("/")
                || password.contains(":") || password.contains(".")
                || password.contains(", ") || password.contains("<")
                || password.contains(">") || password.contains("?")
                || password.contains("|"))) {

            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Password should contain at least one special character ( @, #, %, &, !, $, etc…)");
            return false;
        }

        if (true) {
            int count = 0;

            // checking capital letters
            for (int i = 65; i <= 90; i++) {

                // type casting
                char c = (char) i;

                String str1 = Character.toString(c);
                if (password.contains(str1)) {
                    count = 1;
                }
            }
            if (count == 0) {
                invalidDisplay.setErrorEnabled(true);
                invalidDisplay.setError("* Password should contain at least one uppercase letter(A-Z)");
                return false;
            }
        }

        if (true) {
            int count = 0;

            // checking small letters
            for (int i = 97; i <= 122; i++) {

                // type casting
                char c = (char) i;
                String str1 = Character.toString(c);

                if (password.contains(str1)) {
                    count = 1;
                }
            }
            if (count == 0) {
                invalidDisplay.setErrorEnabled(true);
                invalidDisplay.setError("* Password should contain at least one lowercase letter(a-z)");
                return false;
            }
        }

        // if all conditions fails
        return true;
    }

    public boolean validatedPassword(String str_password, TextInputLayout invalidDisplay) {
        if (containsEmoji(str_password)) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Password should not contain emoji");
            return false;
        } else if (str_password.contains(" ")) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Password should not contain space");
            return false;
        } else if (!isValidPassword(str_password)) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Try another password");
//            invalidDisplay.setError("* Try another password\n* Password should contain at least one digit(0-9).\n* Password must be at least 8 characters.\n* Password should contain at least one lowercase letter(a-z).\n* Password should contain at least one uppercase letter(A-Z).\n* Password should contain at least one special character ( @, #, %, &, !, $, etc…).");
            return false;
        }
        return true;
    }

    public boolean validatedPhoneNo(String str_phoneNo, TextInputLayout invalidDisplay) {

        boolean isValid = true;
        for (int i = 0; i < str_phoneNo.length(); i++) {
            char ch = str_phoneNo.charAt(i);
            if (ch == ' ') {
                continue;
            } else if (!Character.isDigit(ch)) {
                isValid = false;
            }
        }

        if (!isValid) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Phone no is invalid");
            return false;
        }
        if (str_phoneNo.contains(" ")) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Remove all space from phone no");
            return false;
        } else if (str_phoneNo.length() > 10) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Do not add country code with phone no");
            return false;
        } else if (str_phoneNo.length() < 10) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Phone no must contain 10 digit");
            return false;
        }

        return true;
    }

    public boolean validatedEmailId(String str_emailId, TextInputLayout invalidDisplay) {
        boolean specificChar = false;
        for (int i = 0; i < str_emailId.length(); i++) {
            char ch = str_emailId.charAt(i);
            for (int j = 0; j < specificChar_forEmailId.length; j++) {
                if (specificChar_forEmailId[j] == ch) {
                    specificChar = true;
                    break;
                }
            }
        }

        if (specificChar) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Email id should not contain symbols except '@' , '.'");
            return false;
        } else if (containsEmoji(str_emailId)) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Email id should not contain emoji");
            return false;
        } else if (str_emailId.contains(" ")) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Remove all space from email id");
            return false;
        } else if (str_emailId.length() < 3) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Email id must be at least 3 characters");
            return false;
        }
        return true;
    }

    public boolean validatedName(String str_name, TextInputLayout invalidDisplay) {
        boolean specificChar = false;
        for (int i = 0; i < str_name.length(); i++) {
            char ch = str_name.charAt(i);
            for (int j = 0; j < specificChar_withNum.length; j++) {
                if (specificChar_withNum[j] == ch) {
                    specificChar = true;
                    break;
                }
            }
        }

        if (specificChar) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Name should not contain digits or symbols");
            return false;
        } else if (containsEmoji(str_name)) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Name should not contain emoji");
            return false;
        }
        return true;
    }


    public boolean validatedEnrollmentNo(String student_enrollment_no, TextInputLayout invalidDisplay) {

        boolean isValid = true;
        for (int i = 0; i < student_enrollment_no.length(); i++) {
            char ch = student_enrollment_no.charAt(i);
            if (ch == ' ') {
                continue;
            } else if (!Character.isDigit(ch)) {
                isValid = false;
            }
        }

        if (!isValid) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Enrollment no is invalid");
            return false;
        } else if (student_enrollment_no.contains(" ")) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Remove all space from enrollment no");
            return false;
        }

        return true;
    }

    public boolean validatedBranch(String student_branch, TextInputLayout invalidDisplay) {
        boolean specificChar = false;
        for (int i = 0; i < student_branch.length(); i++) {
            char ch = student_branch.charAt(i);
            for (int j = 0; j < specificChar_withoutNum.length; j++) {
                if (specificChar_withoutNum[j] == ch) {
                    specificChar = true;
                    break;
                }
            }
        }

        if (specificChar) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Branch name should not contain symbols");
            return false;
        } else if (containsEmoji(student_branch)) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Branch name should not contain emoji");
            return false;
        }
        return true;
    }

    public boolean validatedSubject(String subject, TextView invalidDisplay) {
        boolean specificChar = false;
        for (int i = 0; i < subject.length(); i++) {
            char ch = subject.charAt(i);
            for (int j = 0; j < specificChar_withoutNum.length; j++) {
                if (specificChar_withoutNum[j] == ch) {
                    specificChar = true;
                    break;
                }
            }
        }

        if (specificChar) {
            invalidDisplay.setText("* Subject name should not contain symbols");
            return false;
        } else if (containsEmoji(subject)) {
            invalidDisplay.setText("* Subject name should not contain emoji");
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean validatedDate(String dateToAdd, TextView invalidDisplay) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime localDate = LocalDateTime.now();
        String egDate = dtf.format(localDate);

        boolean isInvalid = false;
        String str = dateToAdd.replace(" ", "").replace("/", "");

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (!Character.isDigit(ch)) {
                isInvalid = true;
            }
        }

        if (isInvalid) {
            invalidDisplay.setText("* Date must contain only digit except '/'");
            return false;
        } else if (containsEmoji(dateToAdd)) {
            invalidDisplay.setText("* Date should not contain emoji");
            return false;
        } else if (dateToAdd.contains(" ")) {
            invalidDisplay.setText("* Remove all space from date");
            return false;
        } else if (!(slashCount(dateToAdd) == 2)) {
            invalidDisplay.setText("* Correct your format eg. " + egDate);
            return false;
        }

        ArrayList<Integer> arrayList = dateDigitCount(dateToAdd);

        if (arrayList.get(0) != 4) {
            invalidDisplay.setText("* Year in date must contain 4 digit");
            return false;
        } else if (arrayList.get(1) != 2) {
            invalidDisplay.setText("* Month in date must contain 2 digit");
            return false;
        } else if (arrayList.get(2) != 2) {
            invalidDisplay.setText("* Day in date must contain 2 digit");
            return false;
        }

        return true;
    }

    private static int slashCount(String dateToAdd) {
        int count = 0;
        for (int i = 0; i < dateToAdd.length(); i++) {
            char ch = dateToAdd.charAt(i);
            if (ch == '/') {
                count++;
            }
        }
        return count;
    }

    private static ArrayList<Integer> dateDigitCount(String dateToAdd) {
        int yearDigitCount, monthDigitCount, dayDigitCount;
        yearDigitCount = monthDigitCount = dayDigitCount = 0;

        int slashCountNum = 0;

        ArrayList<Integer> arrayList = new ArrayList<>();

        for (int i = 0; i < dateToAdd.length(); i++) {
            char ch = dateToAdd.charAt(i);
            if (slashCountNum <= 2) {
                if (ch == '/') {
                    slashCountNum++;
                } else if (slashCountNum == 0) {
                    yearDigitCount++;
                } else if (slashCountNum == 1) {
                    monthDigitCount++;
                } else if (slashCountNum == 2) {
                    dayDigitCount++;
                }
            }
        }
        arrayList.add(0, yearDigitCount);
        arrayList.add(1, monthDigitCount);
        arrayList.add(2, dayDigitCount);

        return arrayList;
    }

    public boolean validatedEmailIdForRequest(String str_emailId, TextView invalidDisplay) {
        boolean specificChar = false;
        for (int i = 0; i < str_emailId.length(); i++) {
            char ch = str_emailId.charAt(i);
            for (int j = 0; j < specificChar_forEmailId.length; j++) {
                if (specificChar_forEmailId[j] == ch) {
                    specificChar = true;
                    break;
                }
            }
        }

        if (specificChar) {
            invalidDisplay.setText("* Email id should not contain symbols except '@' , '.'");
            return false;
        } else if (containsEmoji(str_emailId)) {
            invalidDisplay.setText("* Email id should not contain emoji");
            return false;
        } else if (str_emailId.contains(" ")) {
            invalidDisplay.setText("* Remove all space from email id");
            return false;
        } else if (str_emailId.length() < 3) {
            invalidDisplay.setText("* Email id must be at least 3 characters");
            return false;
        }
        return true;
    }

    public boolean isPasswordMatched(String str_password, String str_rEnterPassword, TextInputLayout invalidDisplay) {
        if (!str_password.equals(str_rEnterPassword)) {
            invalidDisplay.setErrorEnabled(true);
            invalidDisplay.setError("* Password do not match with above password");
            return false;
        }
        return true;
    }

    public boolean validatedRollNo(String student_enrollment_no, TextView invalidDisplay) {

        boolean isValid = true;
        for (int i = 0; i < student_enrollment_no.length(); i++) {
            char ch = student_enrollment_no.charAt(i);
            if (ch == ' ' || ch == ',') {
                continue;
            } else if (!Character.isDigit(ch)) {
                isValid = false;
            }
        }

        if (!isValid) {
            invalidDisplay.setText("* Roll no. is invalid");
            return false;
        } else if (student_enrollment_no.contains(" ")) {
            invalidDisplay.setText("* Remove all space from Roll no.");
            return false;
        }

        return true;
    }

    public ArrayList<String> findRollFromString(String strRoll_no) {
        ArrayList<String> arr = new ArrayList<>();
        String stub = "";

        if (strRoll_no.length() >= 2) {
            if (strRoll_no.charAt(0) == ',') {
                strRoll_no = strRoll_no.substring(1);//////////////////
            }
            if (strRoll_no.charAt(strRoll_no.length() - 1) == ',') {
                strRoll_no = strRoll_no.substring(0, strRoll_no.length() - 1);////////////////
            }
        }

        for (int i = 0; i < strRoll_no.length(); i++) {
            if (i != strRoll_no.length() - 1) {
                if (strRoll_no.charAt(i) == ',' && strRoll_no.charAt(i + 1) == ',') {
                    continue;//////////////
                }
            }

            if (strRoll_no.charAt(i) == ',') {
                arr.add(stub);
                stub = "";
            } else {
                stub = stub + strRoll_no.charAt(i);
            }
        }
        arr.add(stub);
        return arr;
    }

    public boolean isRollNoFormatValid(String strRoll_no, TextView invalidDisplay) {
        ArrayList<String> arr = new ArrayList<>();

        for (int i = 0; i < strRoll_no.length(); i++) {
            if (i != strRoll_no.length() - 1) {
                if (strRoll_no.charAt(i) == ',' && strRoll_no.charAt(i + 1) == ',') {
                    invalidDisplay.setText("* Remove extra ','");
                    return false;
                }
            }
        }
        return true;
    }
}
