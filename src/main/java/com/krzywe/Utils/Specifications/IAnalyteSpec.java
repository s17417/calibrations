package com.krzywe.Utils.Specifications;

import org.springframework.data.jpa.domain.Specification;

import com.krzywe.Model.Analyte;

public interface IAnalyteSpec {

	Specification<Analyte> getAllSpecificationsChain(String name, String alias);

	Specification<Analyte> getAll();

}