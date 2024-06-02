package uexcel.com.ltts.dto;

import lombok.Data;

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
}
