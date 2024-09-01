package com.webapplication.task_management_system.services.impl;

import com.webapplication.task_management_system.DTO.criteria.SearchCriteriaDTO;
import com.webapplication.task_management_system.DTO.criteria.SearchDTO;
import com.webapplication.task_management_system.DTO.criteria.exceptions.IncorrectCriteriaOperationException;
import com.webapplication.task_management_system.DTO.criteria.exceptions.OperationNotFoundException;
import com.webapplication.task_management_system.services.SpecificationService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@Primary
public class SpecificationServiceImpl<T> implements SpecificationService<T> {


    public Specification<T> getSearchSpecifications(SearchDTO searchDTO) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            for (SearchCriteriaDTO criteria : searchDTO.getCriteria()) {
                switch (criteria.getOperation()) {
                    case EQUALS -> executeOperationEquals(criteria, predicates, criteriaBuilder, root);
                    case IN -> executeOperationIn(criteria, predicates, criteriaBuilder, root);
                    case LIKE -> executeOperationLike(criteria, predicates, criteriaBuilder, root);
                    case BETWEEN -> executeOperationBetween(criteria, predicates, criteriaBuilder, root);
                    case LESS_THAT -> executeOperationLessThat(criteria, predicates, criteriaBuilder, root);
                    case GREATER_THAT -> executeOperationGreaterThat(criteria, predicates, criteriaBuilder, root);
                    default -> throw new OperationNotFoundException(criteria.getOperation().name());
                }
            }

            if (searchDTO.getGlobalOperator() == SearchDTO.GlobalOperator.AND)
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            else return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

    private void executeOperationGreaterThat(SearchCriteriaDTO criteria, List<Predicate> predicates, CriteriaBuilder builder, Root<T> root) {
        var searchValue = extractType(criteria, root);
        Predicate predicate = builder.greaterThan(root.get(criteria.getColumn()), criteria.getValue());
        predicates.add(predicate);
    }

    private void executeOperationLessThat(SearchCriteriaDTO criteria, List<Predicate> predicates, CriteriaBuilder builder, Root<T> root) {
        Predicate predicate = builder.lessThan(root.get(criteria.getColumn()), criteria.getValue());
        predicates.add(predicate);
    }

    private void executeOperationLike(SearchCriteriaDTO criteria, List<Predicate> predicates, CriteriaBuilder builder, Root<T> root) {
        Predicate predicate = builder.like(root.get(criteria.getColumn()), STR."%\{criteria.getValue()}%");
        predicates.add(predicate);
    }

    private void executeOperationEquals(SearchCriteriaDTO criteria, List<Predicate> predicates, CriteriaBuilder builder, Root<T> root) {
        var value = extractType(criteria, root);
        Predicate predicate = builder.equal(root.get(criteria.getColumn()), value);
        predicates.add(predicate);
    }

    private void executeOperationIn(SearchCriteriaDTO criteria, List<Predicate> predicates, CriteriaBuilder builder, Root<T> root) {
        String[] parameters = criteria.getValue().split(",");
        if (parameters.length < 1)
            throw new IncorrectCriteriaOperationException(STR.
                    "Incorrect string parameter: \{parameters} for \{criteria.getColumn()} column, it's must be like \"2,8,5,2......\"");

        Predicate predicate = root.get(criteria.getColumn()).in(Arrays.asList(parameters));
        predicates.add(predicate);
    }

    private void executeOperationBetween(SearchCriteriaDTO criteria, List<Predicate> predicates, CriteriaBuilder builder, Root<T> root) {
        String[] parameters = criteria.getValue().split(",");
        if (parameters.length != 2)
            throw new IncorrectCriteriaOperationException(STR.
                    "Incorrect string parameter: \{parameters} for \{criteria.getColumn()} column, it's must be like \"2,8\"");

        Predicate predicate = builder.between(root.get(criteria.getColumn()), parameters[0], parameters[1]);
        predicates.add(predicate);
    }

    private Object extractType(SearchCriteriaDTO criteria, Root<T> root) {
        if (root.get(criteria.getColumn()).getJavaType().getSimpleName().equals("String"))
            return criteria.getValue();
        else return Long.parseLong(criteria.getValue());
    }
}





