package com.webapplication.task_management_system.services.impl;

import com.webapplication.task_management_system.DTO.criteria.SearchCriteriaDTO;
import com.webapplication.task_management_system.DTO.criteria.SearchDTO;
import com.webapplication.task_management_system.DTO.criteria.exceptions.IncorrectCriteriaOperationException;
import com.webapplication.task_management_system.DTO.criteria.exceptions.OperationNotFoundException;
import com.webapplication.task_management_system.services.SpecificationService;
import jakarta.persistence.criteria.*;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.lang.annotation.Documented;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * The type Specification service.
 *
 * @param <T> the type parameter
 */
@Service
@Primary
public class SpecificationServiceImpl<T> implements SpecificationService<T> {

    public Specification<T> getSearchSpecifications(SearchDTO searchDTO) {

        return (root, _, criteriaBuilder) -> {

            if(searchDTO.getCriteria().isEmpty())
                return criteriaBuilder.conjunction();

            List<Predicate> predicates = new ArrayList<>();
            
            for (SearchCriteriaDTO criteria : searchDTO.getCriteria()) 
                applyCriteria(root, criteriaBuilder, predicates, criteria);
            
            if (searchDTO.getGlobalOperator() == SearchDTO.GlobalOperator.AND)
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            else return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
    /**
     * Add one criteria to list.
     * <p>
     * This method parses the path to the database column and selects the required filtering operation,
     * path parsing occurs through a dot and is built relative to the parent, designated by the generic.
     * <p>
     * Example: to search a comment by the author's name, use the 'author.firstName' construct
     *
     * @param criteria this is an object containing information about one filtering criterion.
     * @param criteriaBuilder is standard parameter of Criteria API.
     * @param root is standard parameter of Criteria API.
     * @param <X> is target class object for get database column.
     * @param predicates is list of all search criteria.
     */
    private <X> void applyCriteria(From<T, X> root,
                                  CriteriaBuilder criteriaBuilder,
                                  List<Predicate> predicates,
                                  SearchCriteriaDTO criteria) {

        From<T, X> from = root;

            if (criteria.getColumn().contains(".")) {
                String[] path = criteria.getColumn().split("\\.");
                criteria.setColumn(path[1]);
                from = root.join(path[0]);
                applyCriteria(from, criteriaBuilder, predicates, criteria);
                return;
            }

            switch (criteria.getOperation()) {
                case EQUALS -> executeOperationEquals(criteria, predicates, criteriaBuilder, from);
                case IN -> executeOperationIn(criteria, predicates, from);
                case LIKE -> executeOperationLike(criteria, predicates, criteriaBuilder, from);
                case BETWEEN -> executeOperationBetween(criteria, predicates, criteriaBuilder, from);
                case LESS_THAN -> executeOperationLessThan(criteria, predicates, criteriaBuilder, from);
                case GREATER_THAN -> executeOperationGreaterThan(criteria, predicates, criteriaBuilder, from);
                default -> throw new OperationNotFoundException(criteria.getOperation().name());
            }
    }

    /**
     * Adds a 'greater than' predicate to the list of predicates based on the specified search criteria.
     * This method constructs a predicate that checks if the attribute of the entity is greater than the specified value.
     *
     * @param <X> the type of the entity attribute being compared
     * @param criteria the search criteria containing the column name and the value to compare against
     * @param predicates the list of predicates where the new predicate will be added
     * @param builder used to construct the predicate
     * @param root the root from which the entity attributes are accessed
     */
    private <X> void executeOperationGreaterThan(SearchCriteriaDTO criteria, List<Predicate> predicates, CriteriaBuilder builder, From<T, X> root) {
        Predicate predicate = builder.greaterThan(root.get(criteria.getColumn()), criteria.getValue());
        predicates.add(predicate);
    }

    /**
     * Adds a 'less than' predicate to the list of predicates based on the specified search criteria.
     * This method constructs a predicate that checks if the attribute of the entity is less than the specified value.
     *
     * @param <X> the type of the entity attribute being compared
     * @param criteria the search criteria containing the column name and the value to compare against
     * @param predicates the list of predicates where the new predicate will be added
     * @param builder used to construct the predicate
     * @param root the root from which the entity attributes are accessed
     */
    private <X> void executeOperationLessThan(SearchCriteriaDTO criteria, List<Predicate> predicates, CriteriaBuilder builder, From<T, X> root) {
        Predicate predicate = builder.lessThan(root.get(criteria.getColumn()), criteria.getValue());
        predicates.add(predicate);
    }

    /**
     * Adds a 'LIKE' predicate to the list of predicates based on the specified search criteria.
     * This method constructs a predicate that checks if the attribute of the entity matches the specified pattern.
     *
     * @param <X> the type of the entity attribute being compared
     * @param criteria the search criteria containing the column name and the value to use in the pattern match
     * @param predicates the list of predicates where the new predicate will be added
     * @param builder used to construct the predicate
     * @param root the root from which the entity attributes are accessed
     */
    private <X> void executeOperationLike(SearchCriteriaDTO criteria, List<Predicate> predicates, CriteriaBuilder builder, From<T, X> root) {
        Predicate predicate = builder.like(root.get(criteria.getColumn()), STR."%\{criteria.getValue()}%");
        predicates.add(predicate);
    }

    /**
     * Adds an equality predicate to the list of predicates based on the specified search criteria.
     * This method constructs a predicate that checks if the attribute of the entity equals the specified value.
     *
     * @param <X> the type of the entity attribute being compared
     * @param criteria the search criteria containing the column name and the value to compare against
     * @param predicates the list of predicates where the new predicate will be added
     * @param builder used to construct the predicate
     * @param root the root from which the entity attributes are accessed
     */
    private <X> void executeOperationEquals(SearchCriteriaDTO criteria, List<Predicate> predicates, CriteriaBuilder builder, From<T, X> root) {
        var clazz = root.get(criteria.getColumn()).getJavaType();
        Predicate predicate = builder.equal(root.get(criteria.getColumn()), castValue(criteria.getValue(), clazz));
        predicates.add(predicate);
    }

    private <X> void executeOperationIn(SearchCriteriaDTO criteria, List<Predicate> predicates, From<T, X> root) {
        String[] parameters = criteria.getValue().split(",");
        var clazz = root.get(criteria.getColumn()).getJavaType();
        if (parameters.length < 1)
            throw new IncorrectCriteriaOperationException(STR.
                    "Incorrect string parameter: \{parameters} for \{parameters} column, it's must be like \"2,8,5,2......\"");

        Predicate predicate = root.get(criteria.getColumn()).in(Arrays.stream(parameters).map((s) -> castValue(s, clazz)).toList());
        predicates.add(predicate);
    }

    private <X> void executeOperationBetween(SearchCriteriaDTO criteria, List<Predicate> predicates, CriteriaBuilder builder, From<T, X> root) {
        String[] parameters = criteria.getValue().split(",");
        if (parameters.length != 2)
            throw new IncorrectCriteriaOperationException(STR.
                    "Incorrect string parameter: \{parameters} for \{criteria.getColumn()} column, it's must be like \"2,8\"");

        Predicate predicate = builder.between(root.get(criteria.getColumn()), parameters[0], parameters[1]);
        predicates.add(predicate);
    }
    /**
     * Converts a string value to the specified type. Supported
     * standard type wrappers, as well as Enum. If the type is not supported,
     * an exception will be thrown.
     *
     * @param value the string value to convert
     * @param type the type class to which the value should be converted
     * @param <Y> generic type indicating the target type of the conversion
     * @return a value of type {@code Y} obtained from a string
     * @throws IllegalArgumentException if the specified type is not supported
     * @throws UnsupportedOperationException if a conversion to {@link Date} is requested
     */
    private <Y> Y castValue(String value, Class<Y> type) {
        if (type.equals(String.class)) {
            return type.cast(value);
        } else if (type.equals(Integer.class)) {
            return type.cast(Integer.valueOf(value));
        } else if (type.equals(Long.class)) {
            return type.cast(Long.valueOf(value));
        } else if (type.equals(Float.class)) {
            return type.cast(Float.valueOf(value));
        } else if (type.equals(Double.class)) {
            return type.cast(Double.valueOf(value));
        } else if (type.equals(Boolean.class)) {
            return type.cast(Boolean.valueOf(value));
        } else if (type.equals(Date.class)) {
            throw new UnsupportedOperationException("Date conversion not supported yet");
        } else if (type.isEnum()) {
            @SuppressWarnings("all")
            Y enumValue = (Y) Enum.valueOf((Class<Enum>) type, value);
            return enumValue;
        } else {
            throw new IllegalArgumentException(STR."Unsupported type: \{type.getSimpleName()}");
        }
    }
}





