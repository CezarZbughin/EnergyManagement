package com.cezar.energymonitoring.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Consumption extends IdentityModel<Long> {
    @ManyToOne
    @JoinColumn(name = "device_id")
    @JsonManagedReference
    @JsonIdentityReference(alwaysAsId=true)
    private Device device;
    private Long timestamp;
    private float measure;
}
