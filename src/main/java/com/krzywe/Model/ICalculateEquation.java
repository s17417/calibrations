package com.krzywe.Model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ICalculateEquation {
	
	public List<BigDecimal> calculateParameters(Collection<ResponseValue> responseValues);
	
	public Optional<BigDecimal> calculateCoefficientOfDetermination(Collection<ResponseValue> responseValues, Collection<BigDecimal> parameters);
	
	public Optional<String> getFormula(List<BigDecimal> parameters);
	
	public Integer getMaximalParametersSize();
	
	public List<BigDecimal> setParameters(List<BigDecimal> parameters);

}
