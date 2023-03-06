package com.krzywe.Utils.Specifications;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Component;

import com.krzywe.DTO.SimpleCalibrationSetView;
import com.krzywe.Model.CalibrationSet;
import com.krzywe.Model.CalibrationSet_;
import com.krzywe.Model.MaterialType;

import jakarta.persistence.criteria.Predicate;

@Component
public class CalibrationSetSpec implements ICalibrationSetSpec {
	
	@Override
	public Specification<CalibrationSet> nameLikeSpecification(String name){
		  return (root, query, criteriaBuilder) 
			      -> name==null?criteriaBuilder.and(new Predicate[0]):
			    		  criteriaBuilder.like(root.get(CalibrationSet_.NAME), "%"+name+"%");
	}
	
	@Override
	public Specification<CalibrationSet> exactMaterialTypeSpecification(MaterialType materialType){
		  return (root, query, criteriaBuilder) 
			      -> materialType==null?criteriaBuilder.and(new Predicate[0]):
			    		  criteriaBuilder.equal(root.get(CalibrationSet_.MATERIAL_TYPE), materialType);
	}
	
	@Override
	public Specification<CalibrationSet> createdDateRangeSpecification(LocalDateTime from, LocalDateTime to){
		return (root, query, criteriaBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();  
			if (from!=null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(CalibrationSet_.CREATED_DATE), from));
			if (to!=null) predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(CalibrationSet_.CREATED_DATE), to));
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
	
	@Override
	public Specification<CalibrationSet> preprationDateRangeSpecification(LocalDateTime from, LocalDateTime to){
		return (root, query, criteriaBuilder) ->{
			List<Predicate> predicates = new ArrayList<>();  
			if (from!=null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(CalibrationSet_.PREPARATION_DATE), from));
			if (to!=null) predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(CalibrationSet_.PREPARATION_DATE), to));
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
	
	@Override
	public Function<FetchableFluentQuery<CalibrationSet>, FetchableFluentQuery<SimpleCalibrationSetView>>  getFluentQueryAsSimpleCalibrationSetView() {		
		return qq -> qq
				.as(SimpleCalibrationSetView.class);				
	}
}
