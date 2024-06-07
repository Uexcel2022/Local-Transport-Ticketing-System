package uexcel.com.ltts.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
;

@Entity
@Data
public class Checkin {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private LocalDate date;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Bus bus;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Booking booking;
}
