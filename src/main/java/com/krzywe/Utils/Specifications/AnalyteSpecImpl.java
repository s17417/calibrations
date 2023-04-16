package com.krzywe.Utils.Specifications;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.data.jpa.domain.Specification;

import com.krzywe.Model.Analyte;
import com.krzywe.Model.Analyte_;

public class AnalyteSpecImpl implements IAnalyteSpec {
	
	private Supplier<Optional<Specification<Analyte>>> emptySpec = 
			() -> Optional.of(Specification.<Analyte>where(null));
	
	public Optional<Specification<Analyte>> nameLikeSpecification(String name){
		Specification<Analyte> spec = (root, q, cb) ->
		cb.like(cb.lower(root.get(Analyte_.NAME)), "%"+name.toLowerCase()+"%");
		
		return Optional
				.ofNullable(spec)
				.filter(obj -> name!=null)
				.or(emptySpec);				
	}
	
	public Optional<Specification<Analyte>> aliasLikeSpecification(String alias){
		Specification<Analyte> spec = (root, q, cb) ->
		cb.in(root.join(Analyte_.ALIASES).get(Analyte_.ALIASES)).value(alias);
		
		return Optional
				.ofNullable(spec)
				.filter(obj -> alias!=null)
				.or(emptySpec);	
	}
	
	@Override
	public Specification<Analyte> getAllSpecificationsChain(
			String name,
			String alias){
		return Specification.allOf(
				nameLikeSpecification(name).get(),
				aliasLikeSpecification(alias).get()
				);
	}
	
	@Override
	public Specification<Analyte> getAll(){
		return getAllSpecificationsChain(null, null);
	}

}
