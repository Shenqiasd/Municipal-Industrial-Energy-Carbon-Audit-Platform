package com.energy.audit.model.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class ChangePasswordDTO implements Serializable {
    private String oldPassword;
    private String newPassword;
}
