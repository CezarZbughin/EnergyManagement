package com.cezar.energymonitoring.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class NotificationDTO {
    long deviceId;
    long userId;
    String message;
}
