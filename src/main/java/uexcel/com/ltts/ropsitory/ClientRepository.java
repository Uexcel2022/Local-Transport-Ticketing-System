package uexcel.com.ltts.ropsitory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uexcel.com.ltts.entity.Client;

@Repository
public interface ClientRepository extends CrudRepository<Client, String> {
    boolean existsClientByEmail(String email);

    Client findByPhone(String phone);

    boolean existsClientByPhone(String phone);
}
