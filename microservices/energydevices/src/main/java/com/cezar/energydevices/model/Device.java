package com.cezar.energydevices.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
// (defined by ID,
//description, address, maximum hourly energy consumption)
public class Device extends  IdentityModel<Long>{
    private String description;
    private String address;
    private Float maxHourlyConsumption;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    @JsonIdentityReference(alwaysAsId=true)
    private EndUser owner;
}
