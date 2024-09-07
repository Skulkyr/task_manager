package com.webapplication.task_management_system.services;

import com.webapplication.task_management_system.DTO.criteria.SearchDTO;
import org.springframework.data.jpa.domain.Specification;

/**
 * The interface Specification service.
 * This interface implements the mechanism for building a specification based on DTO
 * @see SearchDTO
 * @param <T> the type parameter
 */
public interface SpecificationService<T> {

    /**
     * Gets search specifications.
     *
     * @param searchDTO the search dto
     * @return the search specifications
     */
    Specification<T> getSearchSpecifications(SearchDTO searchDTO);
}
