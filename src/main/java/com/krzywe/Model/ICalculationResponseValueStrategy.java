package com.krzywe.Model;

import java.math.BigDecimal;

public interface ICalculationResponseValueStrategy {

	public BigDecimal getResponseCalculationValue(BigDecimal responseValue, BigDecimal internalStdResponseValue);
	
	public BigDecimal getTargetConcentrationValue(BigDecimal targetValueConcentration, BigDecimal internalStdConc);
}
