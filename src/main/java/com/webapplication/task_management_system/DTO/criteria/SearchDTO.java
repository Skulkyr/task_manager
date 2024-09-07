package com.webapplication.task_management_system.DTO.criteria;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SearchDTO {

    private GlobalOperator globalOperator;

    @Schema(description = "Indicates the page number requested",
            defaultValue = "0")
    private Integer pageNumber;

    @Schema(description = "Specifies the number of elements to receive",
            defaultValue = "10")
    private Integer pageSize;

    @Schema(description = "Specifies the sorting direction, descending or ascending",
            defaultValue = "ASC")
    private Sort.Direction direction;

    @ArraySchema(arraySchema = @Schema(
                    description = "Determining which field the results will be sorted by, supports sorting by multiple fields in descending order of priority",
                    example = "[\"id\", \"name\"]"),
                schema = @Schema(description = "Sort field"))
    private List<String> sortedBy;
    private List<SearchCriteriaDTO> criteria;

    @Schema(description = "Global comparison operator, determines whether the result must match all conditions or any")
    public enum GlobalOperator {AND, OR}

    public SearchDTO() {
        this.globalOperator = GlobalOperator.AND;
        this.pageNumber = 0;
        this.pageSize = 10;
        this.direction = Sort.Direction.ASC;
        this.sortedBy = List.of("id");
        this.criteria = new ArrayList<>();
    }

    @JsonIgnore
    public Pageable getPageable() {
        return PageRequest.of(pageNumber, pageSize, direction, sortedBy.toArray(new String[0]));
    }
}
