package com.krzywe.Model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ResponseValueInternalStdTypeImpl  implements ICalculationResponseValueStrategy{

	protected ResponseValueInternalStdTypeImpl() {}
	
	@Override
	public BigDecimal getResponseCalculationValue(BigDecimal responseValue, BigDecimal internalStdResponseValue) {
		if (responseValue==null) return null;
		if (internalStdResponseValue==null||internalStdResponseValue.compareTo(BigDecimal.ZERO)==0)
			return BigDecimal.ZERO;		
		return responseValue.divide(internalStdResponseValue, new MathContext(32, RoundingMode.HALF_UP));
	}

	@Override
	public BigDecimal getTargetConcentrationValue(BigDecimal targetValueConcentration, BigDecimal internalStdConc) {
		if (targetValueConcentration==null) return null;
		if (internalStdConc==null||internalStdConc.compareTo(BigDecimal.ZERO)==0)
			return BigDecimal.ZERO;
		return targetValueConcentration.divide(internalStdConc, new MathContext(32, RoundingMode.HALF_UP));
	}
}
