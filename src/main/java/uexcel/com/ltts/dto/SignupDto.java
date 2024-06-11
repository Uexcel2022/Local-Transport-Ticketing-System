package uexcel.com.ltts.dto;

import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.lang.NonNull;
import uexcel.com.ltts.entity.Role;

import java.time.LocalDate;

@Data
public class SignupDto {
     private String fullName;
     private String phone;
     private String gender;
     private LocalDate dateOfBirth;
     private String email;
     private String password;
     private String confirmPassword;
     private String nFullName;
     private String nPhone;
}
