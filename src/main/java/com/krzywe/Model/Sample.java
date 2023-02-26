package com.krzywe.Model;

import java.math.BigDecimal;
import java.util.Objects;

public class Sample {
	
	private String Id;
	
	private BigDecimal AnalRespVal;
	
	private BigDecimal intStdRespVal;

	private BigDecimal calcConcentration;

	protected Sample(String id, BigDecimal analRespVal, BigDecimal intStdRespVal) {
		Id = id;
		AnalRespVal = analRespVal;
		this.intStdRespVal = intStdRespVal;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public BigDecimal getAnalRespVal() {
		return AnalRespVal;
	}

	public void setAnalRespVal(BigDecimal analRespVal) {
		AnalRespVal = analRespVal;
	}

	public BigDecimal getIntStdRespVal() {
		return intStdRespVal;
	}

	public void setIntStdRespVal(BigDecimal intStdRespVal) {
		this.intStdRespVal = intStdRespVal;
	}

	public BigDecimal getCalcConcentration() {
		return calcConcentration;
	}

	public void setCalcConcentration(BigDecimal calcConcentration) {
		this.calcConcentration = calcConcentration;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sample other = (Sample) obj;
		return Objects.equals(Id, other.Id);
	}
	
	
	
}
