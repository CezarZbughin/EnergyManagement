package com.cezar.energymonitoring.repository;

import com.cezar.energymonitoring.model.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsumptionRepository extends JpaRepository<Consumption,Long> {
    List<Consumption> findByDevice_id(Long device_id);
    List<Consumption> findByDevice_idAndTimestampAfter(Long device_id, Long timestamp);

    List<Consumption> findByTimestampBetween(long timestamp, long timestamp2);
    List<Consumption> findByTimestampAfter(long timestamp);
}
