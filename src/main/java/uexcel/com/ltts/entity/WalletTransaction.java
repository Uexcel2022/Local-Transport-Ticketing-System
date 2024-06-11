package uexcel.com.ltts.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
public class WalletTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private String sourceAccount;
    @Column(nullable = false)
    private String transactionType;
    @Column(nullable = false)
    private String Bank;
    @Column(nullable = false)
    private String  sourceName;
    private String cCnumber;
    private String cCtype;
    private double amount;
    @Column(nullable = false)
    private LocalDate transactionDate=LocalDate.now();

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Wallet wallet;

}
