package parkingsystem.felipeschwartz.com.github.model.entities;

import jakarta.persistence.*;
import parkingsystem.felipeschwartz.com.github.model.enums.PaymentMethod;
import parkingsystem.felipeschwartz.com.github.model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Payment {
    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "parking_session_id")
    private ParkingSession parkingSession;

    private BigDecimal amount;

    private LocalDateTime paymentDate;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private String reference;



    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

}
