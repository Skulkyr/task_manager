package com.webapplication.task_management_system.DTO.criteria;

import lombok.Data;

@Data
public class SearchCriteriaDTO {

    private String column;
    private String value;
    private Operation operation;
    

    public enum Operation {
        EQUALS, LIKE, IN, GREATER_THAN, LESS_THAN, BETWEEN
    }
    
}


