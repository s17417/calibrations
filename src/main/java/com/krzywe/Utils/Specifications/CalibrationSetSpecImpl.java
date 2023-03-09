package com.krzywe.Utils.Specifications;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.krzywe.Model.CalibrationSet;
import com.krzywe.Model.CalibrationSet_;
import com.krzywe.Model.MaterialType;

import jakarta.persistence.criteria.Predicate;

@Component
public class CalibrationSetSpecImpl implements ICalibrationSetSpec {
	
	private Supplier<Optional<Specification<CalibrationSet>>> emptySpec = () -> Optional.of(Specification.<CalibrationSet>where(null));
	
	public Optional<Specification<CalibrationSet>> nameLikeSpecification(String name){
		Specification<CalibrationSet> spec = (root, q, cb) ->
			cb.like(cb.lower(root.get(CalibrationSet_.NAME)), "%"+name.toLowerCase()+"%");
		
		return Optional
				.ofNullable(spec)
				.filter(obj -> name!=null)
				.or(emptySpec);
	}
	
	public Optional<Specification<CalibrationSet>> exactMaterialTypeSpecification(MaterialType materialType){
		Specification<CalibrationSet> spec = (root, q, cb) ->
			cb.equal(root.get(CalibrationSet_.MATERIAL_TYPE), materialType);
		
		return Optional
				.ofNullable(spec)
				.filter(obj -> materialType!=null)
				.or(emptySpec);		      
	}
	
	public Optional<Specification<CalibrationSet>> createdDateRangeSpecification(LocalDateTime from, LocalDateTime to){
		Specification<CalibrationSet> spec = (root, q, cb) ->{
			List<Predicate> predicates = new ArrayList<>();  
			
			if (from!=null) predicates.add(
					cb.greaterThanOrEqualTo(root.get(CalibrationSet_.CREATED_DATE), from)
					);			
			if (to!=null) predicates.add(
					cb.lessThanOrEqualTo(root.get(CalibrationSet_.CREATED_DATE), to)
					);
			
			return cb.and(predicates.toArray(new Predicate[0]));
		};
		
		return Optional
				.ofNullable(spec)
				.filter(obj -> from!=null||to!=null)
				.or(emptySpec);
	}
	
	public Optional<Specification<CalibrationSet>> preprationDateRangeSpecification(LocalDate from, LocalDate to){
		Specification<CalibrationSet> spec = (root, q, cb) ->{
			List<Predicate> predicates = new ArrayList<>();  
			
			if (from!=null) predicates.add(
					cb.greaterThanOrEqualTo(root.get(CalibrationSet_.PREPARATION_DATE), from)
					);
			if (to!=null) predicates.add(
					cb.lessThanOrEqualTo(root.get(CalibrationSet_.PREPARATION_DATE), to)
					);
			
			return cb.and(predicates.toArray(new Predicate[0]));
		};
		
		return Optional
				.ofNullable(spec)
				.filter(obj -> from!=null||to!=null)
				.or(emptySpec);
	}
	
	@Override
	public Specification<CalibrationSet> getAllSpecificationsChain(
			String name,
			MaterialType materialType,
			LocalDateTime createdDateFrom,
			LocalDateTime createdDateTo,
			LocalDate preparationDateFrom,
			LocalDate preparationDateTo) {
		
		return Specification.allOf(
				nameLikeSpecification(name).get(),
				exactMaterialTypeSpecification(materialType).get(),
				createdDateRangeSpecification(createdDateFrom, createdDateTo).get(),
				preprationDateRangeSpecification(preparationDateFrom, preparationDateTo).get()
				);
	}
	
	@Override
	public Specification<CalibrationSet> getAll(){
		return getAllSpecificationsChain(null, null, null, null, null, null);
	}
	
}
