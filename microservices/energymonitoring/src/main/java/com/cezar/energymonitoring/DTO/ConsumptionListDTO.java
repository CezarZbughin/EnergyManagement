package com.cezar.energymonitoring.DTO;

import com.cezar.energymonitoring.model.Consumption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ConsumptionListDTO {
    List<Consumption> consumptions;
}
