package uexcel.com.ltts.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uexcel.com.ltts.dto.SignupDto;
import uexcel.com.ltts.entity.Client;
import uexcel.com.ltts.entity.Wallet;
import uexcel.com.ltts.util.Repos;
import uexcel.com.ltts.util.Validation;


@Service
public class ClientServiceImp implements ClientService {

    private final Validation validation;

    public ClientServiceImp(Repos repos, Validation validation) {
        this.validation = validation;
    }

    @Override
    @Transactional
    public String processSignup(SignupDto signupDto) {

        validation.ifExist(signupDto.getEmail(),signupDto.getPhone());

        Client client = new Client();
        client.setFullName(validation.validateName(signupDto.getFullName()));
        client.setPhone(validation.validatePhone(signupDto.getPhone()));
        client.setEmail(validation.validateEmail(signupDto.getEmail()));
        client.setDateOfBirth(validation.validateAge(signupDto.getDateOfBirth()));
        client.setPassword(validation.passWord(signupDto.getPassword(), signupDto.getConfirmPassword()));
        client.setGender(signupDto.getGender());

        Wallet wallet = new Wallet();
        wallet.setClient(client);
//        repos.getWalletRepository().save(wallet);
        return "Signup Successful";
    }
}
