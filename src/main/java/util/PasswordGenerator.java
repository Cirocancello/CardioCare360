package util;
//classe per generare l'Hash della password, in questo caso admin123

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin123"; // la password in chiaro
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Hash generato: " + encodedPassword);
    }
}
