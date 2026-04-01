package com.energy.audit.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Pagination request DTO
 */
@Data
public class PageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Page number (1-based) */
    private Integer pageNum = 1;

    /** Page size */
    private Integer pageSize = 10;
}
