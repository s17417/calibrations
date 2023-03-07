package com.krzywe.Utils.Specifications;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.krzywe.Model.CalibrationSet;
import com.krzywe.Model.CalibrationSet_;
import com.krzywe.Model.MaterialType;

import jakarta.persistence.criteria.Predicate;

@Component
public class CalibrationSetSpec implements ICalibrationSetSpec {
	
	public Specification<CalibrationSet> nameLikeSpecification(String name){
		  return (root, query, criteriaBuilder) 
			      -> name==null?criteriaBuilder.and(new Predicate[0]):
			    		  criteriaBuilder.like(root.get(CalibrationSet_.NAME), "%"+name+"%");
	}
	
	public Specification<CalibrationSet> exactMaterialTypeSpecification(MaterialType materialType){
		  return (root, query, criteriaBuilder) 
			      -> materialType==null?criteriaBuilder.and(new Predicate[0]):
			    		  criteriaBuilder.equal(root.get(CalibrationSet_.MATERIAL_TYPE), materialType);
	}
	
	public Specification<CalibrationSet> createdDateRangeSpecification(LocalDateTime from, LocalDateTime to){
		return (root, query, criteriaBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();  
			if (from!=null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(CalibrationSet_.CREATED_DATE), from));
			if (to!=null) predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(CalibrationSet_.CREATED_DATE), to));
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
	
	public Specification<CalibrationSet> preprationDateRangeSpecification(LocalDate from, LocalDate to){
		return (root, query, criteriaBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();  
			if (from!=null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(CalibrationSet_.PREPARATION_DATE), from));
			if (to!=null) predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(CalibrationSet_.PREPARATION_DATE), to));
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
	
	@Override
	public Specification<CalibrationSet> getAllSpecificationsChain(
			String name,
			MaterialType materialType,
			LocalDateTime createdDateFrom,
			LocalDateTime createdDateTo,
			LocalDate preparationDateFrom,
			LocalDate preparationDateTo) {
		
		return nameLikeSpecification(name)
				.and(exactMaterialTypeSpecification(materialType))
				.and(createdDateRangeSpecification(createdDateFrom, createdDateTo))
				.and(preprationDateRangeSpecification(preparationDateFrom, preparationDateTo));
	}
	
	@Override
	public Specification<CalibrationSet> getAll(){
		return getAllSpecificationsChain(null, null, null, null, null, null);
	}
	
}
