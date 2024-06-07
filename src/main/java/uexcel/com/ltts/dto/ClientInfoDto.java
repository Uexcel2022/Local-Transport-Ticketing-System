package uexcel.com.ltts.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class ClientInfoDto {
    private String Id;
    private String fullName;
    private String phone;
    private LocalDate dateOfBirth;
    private String email;
    private double balance;
    private String nFullName;
    private String nPhone;
}
