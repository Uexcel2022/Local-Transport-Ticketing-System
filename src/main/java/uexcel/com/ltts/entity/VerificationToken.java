package uexcel.com.ltts.entity;


import jakarta.persistence.*;
import lombok.Data;


import java.util.Date;
import java.util.UUID;

@Entity
@Data
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true, nullable = false)
    private String token = UUID.randomUUID().toString();
    private Date date = new Date(System.currentTimeMillis() + 15*60*60*1000);
    @OneToOne(cascade = CascadeType.PERSIST)
    private Client client;
}
