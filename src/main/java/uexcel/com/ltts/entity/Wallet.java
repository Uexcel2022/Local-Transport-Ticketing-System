package uexcel.com.ltts.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique=true, nullable=false)
    private double balance;
    private String status = "active";

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "walletNumber",referencedColumnName = "phone", nullable = false,updatable = false)
    private Client client;
}
