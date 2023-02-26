package com.krzywe.Model;

import java.math.BigDecimal;

public class ResponseValueExternalStdTypeImpl implements ICalculationResponseValueStrategy {

	protected ResponseValueExternalStdTypeImpl() {
		
	}
	
	@Override
	public BigDecimal getResponseCalculationValue(BigDecimal responseValue, BigDecimal internalStdResponseValue) {
		return responseValue;
	}

	@Override
	public BigDecimal getTargetConcentrationValue(BigDecimal targetValueConcentration, BigDecimal internalStdConc) {
		return targetValueConcentration;
	}
	
	

}
