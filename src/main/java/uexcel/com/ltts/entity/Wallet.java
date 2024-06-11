package uexcel.com.ltts.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private double balance;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "wallet_Number",referencedColumnName = "phone", nullable = false,updatable = true)
    private Client client;
}
