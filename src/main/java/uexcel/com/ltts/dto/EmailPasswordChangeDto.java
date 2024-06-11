package uexcel.com.ltts.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailPasswordChangeDto {
    private String email;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
