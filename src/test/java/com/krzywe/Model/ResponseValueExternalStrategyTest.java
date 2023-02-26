package com.krzywe.Model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class ResponseValueExternalStrategyTest {
	
	private ResponseValueExternalStdTypeImpl externalStdTypeImpl = new ResponseValueExternalStdTypeImpl();
	
	@Test
	public void ResponseValueExternalStdTypeImpl_getResponseCalculationValue() {
		assertThat(externalStdTypeImpl.getResponseCalculationValue(BigDecimal.TEN, null))
		.isEqualByComparingTo(BigDecimal.TEN);
	}
	
	@Test
	public void ResponseValueExternalStdTypeImplGetResponseCalculationValue_withInternalStd() {
		assertThat(externalStdTypeImpl.getResponseCalculationValue(BigDecimal.TEN, BigDecimal.TEN))
		.isEqualByComparingTo(BigDecimal.TEN);
	}
	
	@Test
	public void ResponseValueExternalStdTypeImplGetResponseCalculationValue_nullValue() {
		assertThat(externalStdTypeImpl.getResponseCalculationValue(null, null))
		.isNull();
	}
	
	@Test
	public void ResponseValueExternalStdTypeImpl_getTargetConcentrationValue() {
		assertThat(externalStdTypeImpl.getTargetConcentrationValue(BigDecimal.TEN, null))
		.isEqualByComparingTo(BigDecimal.TEN);
	}
	
	@Test
	public void ResponseValueExternalStdTypeImplGetTargetConcentrationValue_withInternalStd() {
		assertThat(externalStdTypeImpl.getTargetConcentrationValue(BigDecimal.TEN, BigDecimal.TEN))
		.isEqualByComparingTo(BigDecimal.TEN);
	}
	
	@Test
	public void ResponseValueExternalStdTypeImplGetTargetConcentrationValue_nullValue() {
		assertThat(externalStdTypeImpl.getTargetConcentrationValue(null, null))
		.isNull();
	}
}
