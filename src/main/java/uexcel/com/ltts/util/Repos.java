package uexcel.com.ltts.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uexcel.com.ltts.ropsitory.*;

@Component
@Getter
public class Repos {
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

}
