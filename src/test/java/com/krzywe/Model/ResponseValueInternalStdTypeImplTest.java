package com.krzywe.Model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class ResponseValueInternalStdTypeImplTest {

	ResponseValueInternalStdTypeImpl internalStdTypeImpl = new ResponseValueInternalStdTypeImpl();

	@Test
	public void checkResponseValueInternalStdTypeImpl_getResponseCalculationValue() {
		assertThat(internalStdTypeImpl.getResponseCalculationValue(BigDecimal.TEN, BigDecimal.TEN))
		.isEqualByComparingTo(BigDecimal.ONE);	
	}
	
	@Test
	public void checkResponseValueInternalStdTypeImplGetResponseCalculationValue_NullInternalStandard() {
		assertThat(internalStdTypeImpl.getResponseCalculationValue(BigDecimal.TEN, null))
		.isEqualByComparingTo(BigDecimal.ZERO);	
	}
	
	@Test
	public void checkResponseValueInternalStdTypeImplGetResponseCalculationValue_NullInternalStandardAndResponse() {
		assertThat(internalStdTypeImpl.getResponseCalculationValue(null, null))
		.isNull();	
	}
	
	@Test
	public void checkResponseValueInternalStdTypeImplGetResponseCalculationValue_NullResponse() {
		assertThat(internalStdTypeImpl.getResponseCalculationValue(null, BigDecimal.ONE))
		.isNull();	
	}
	
	@Test
	public void checkResponseValueInternalStdTypeImpl_getTargetConcentrationValue() {
		assertThat(internalStdTypeImpl.getTargetConcentrationValue(BigDecimal.TEN, BigDecimal.TEN))
		.isEqualByComparingTo(BigDecimal.ONE);
	}
	
	@Test
	public void checkResponseValueInternalStdTypeImplGetTargetConcentrationValue_InternalTargetNull() {
		assertThat(internalStdTypeImpl.getTargetConcentrationValue(BigDecimal.TEN, null))
		.isEqualByComparingTo(BigDecimal.ZERO);
	}
	
	@Test
	public void checkResponseValueInternalStdTypeImplGetTargetConcentrationValue_InternalTargetNullAndResponse() {
		assertThat(internalStdTypeImpl.getTargetConcentrationValue(null, null))
		.isNull();
	}

}
