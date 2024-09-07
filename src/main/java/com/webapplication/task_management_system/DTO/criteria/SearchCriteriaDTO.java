package com.webapplication.task_management_system.DTO.criteria;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Class for specifying search criteria")
public class SearchCriteriaDTO {

    @Schema(description = "Name of the search field", example = "id")
    private String column;
    @Schema(description = "Set value to search", example = "1")
    private String value;
    private Operation operation;

    @Schema(description = "Supported operations: ")
    public enum Operation {
        EQUALS, LIKE, IN, GREATER_THAN, LESS_THAN, BETWEEN
    }

}


