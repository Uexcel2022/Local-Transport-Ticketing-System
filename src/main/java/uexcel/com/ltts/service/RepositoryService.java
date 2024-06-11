package uexcel.com.ltts.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uexcel.com.ltts.ropsitory.*;

@Service
@Getter
public class RepositoryService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private WalletTransRepository walletTransRepository;
    @Autowired
    private BusRepository busRepository;
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CheckingRepository checkingRepository;
    @Autowired
    VerificationTokenRepository verificationTokenRepository;

}

