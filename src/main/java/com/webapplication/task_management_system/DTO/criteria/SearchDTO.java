package com.webapplication.task_management_system.DTO.criteria;

import lombok.Data;

import java.util.List;

@Data
public class SearchDTO {

    private GlobalOperator globalOperator;
    private List<SearchCriteriaDTO> criteria;

    public enum GlobalOperator {AND, OR}
}
