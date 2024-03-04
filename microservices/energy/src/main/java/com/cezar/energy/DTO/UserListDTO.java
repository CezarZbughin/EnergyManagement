package com.cezar.energy.DTO;

import com.cezar.energy.model.EndUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserListDTO {
    private List<EndUser> users;
}
