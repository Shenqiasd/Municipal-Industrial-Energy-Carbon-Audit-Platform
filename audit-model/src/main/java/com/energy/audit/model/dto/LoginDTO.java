package com.energy.audit.model.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class LoginDTO implements Serializable {
    private String username;
    private String password;
    private String portal;
}
