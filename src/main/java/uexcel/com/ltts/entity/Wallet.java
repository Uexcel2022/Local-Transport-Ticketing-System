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
    @Column(nullable = false)
    private String status;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "wallet_Number",referencedColumnName = "phone", nullable = false,updatable = false)
    private Client client;
}
