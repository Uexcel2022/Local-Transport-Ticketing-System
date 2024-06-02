package uexcel.com.ltts.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uexcel.com.ltts.entity.Client;
import uexcel.com.ltts.exception.CustomException;

import java.time.LocalDate;

@Component
public class Validation {

    @Autowired
    private  Repos repos;

    public String validateName(String name){
        if(!name.matches("[a-zA-Z]+\s[A-Za-z]+\s?[a-zA-Z]*")){
            throw new CustomException("Name is unacceptable.","400");
        }

        return name;
    }

    public String validatePhone(String phone){
        if(!phone.matches("0[7-9][01][0-9]{8}")){
            throw new CustomException("Invalid Nigeria phone number.","400");
        }

        return phone;
    }

    public String validateEmail(String email){
        if(!email.matches("[a-zA-Z0-9_.+%~-]*@[a-zA-Z0-9.]+\\.[a-zA-Z]{2,}")){
            throw new CustomException("Invalid email address.","400");
        }

        return email;
    }

    public LocalDate validateAge(LocalDate dateOfBirt){
        if(LocalDate.now().getYear() - dateOfBirt.getYear() < 17) {
            throw new CustomException("You are not up to 18 years of age.", "400");
        }
        if(LocalDate.now().getYear() - dateOfBirt.getYear() == 17 &&
                dateOfBirt.getDayOfYear() > LocalDate.now().getDayOfYear()-1){
            throw new CustomException("You are not up to 18 years of age.","400");
        }

        return dateOfBirt;
    }

    public String passWord(String password, String confirmPassword){
        if(!password.equals(confirmPassword)){
            throw new CustomException("Passwords do not match.","400");
        }

        char[] chars = password.toCharArray();
        String uppCase ="[A-Z]";
        String specialChr = "[!$#&?~%*.^`|/=_+-]";
        String numbers = "[0-9]";
        boolean matchedUppCase = false;
        boolean matchedSpecialChr = false;
        boolean matchedNumbers = false;
        String msg = "Password must be >= 6 characters and must contain at least one uppercase," +
                "digit and special character and not more than 16 characters";


        for (char aChar : chars) {
            if (String.valueOf(aChar).matches(uppCase)) {
                matchedUppCase = true;
                break;
            }
        }
        if(!matchedUppCase){
            throw new CustomException(msg,"400");
        }

        for (char aChar : chars) {
            if (String.valueOf(aChar).matches(specialChr)) {
                matchedSpecialChr = true;
                break;
            }
        } if(!matchedSpecialChr){
            throw new CustomException(msg,"400");
        }

        for (char aChar : chars) {
            if (String.valueOf(aChar).matches(numbers)) {
                matchedNumbers = true;
                break;
            }
        }

        if(!matchedNumbers){
            throw new CustomException(msg,"400");
        }

        return password;
    }

    public void ifExist(String email, String phone){
        Client clientEmail = repos.getClientRepository().findByEmail(email);
        if(clientEmail != null){
            throw new CustomException("Email already exist.","400");
        }

        Client clientPhone =repos.getClientRepository().findByPhone(phone);
        if(clientPhone != null){
            throw new CustomException("Phone already exist.","400");
        }
    }




}
