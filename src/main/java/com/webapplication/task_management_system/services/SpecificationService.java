package com.webapplication.task_management_system.services;

import com.webapplication.task_management_system.DTO.criteria.SearchDTO;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationService<T> {
    Specification<T> getSearchSpecifications(SearchDTO searchDTO);
}
