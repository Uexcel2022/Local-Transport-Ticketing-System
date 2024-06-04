package uexcel.com.ltts.ropsitory;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import uexcel.com.ltts.entity.Client;

import java.util.List;

@Repository
public interface ClientRepository extends CrudRepository<Client, String> {
    Client findByEmail(String email);

    Client findByPhone(String phone);

}
