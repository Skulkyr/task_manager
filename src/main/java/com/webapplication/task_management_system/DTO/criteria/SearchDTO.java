package com.webapplication.task_management_system.DTO.criteria;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
public class SearchDTO {

    private GlobalOperator globalOperator;
    private Integer pageNumber;
    private Integer pageSize;
    private Sort.Direction direction;
    private List<String> sortedBy;
    private List<SearchCriteriaDTO> criteria;

    public enum GlobalOperator {AND, OR}

    public Pageable getPageable() {
        if (pageNumber == null) pageNumber = 0;
        if (pageSize == null) pageSize = 10;
        if (direction == null) direction = Sort.Direction.ASC;
        if (sortedBy == null) sortedBy = List.of("id");

        return PageRequest.of(pageNumber, pageSize, direction, sortedBy.toArray(new String[0]));
    }
}
