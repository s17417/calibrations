package com.krzywe.DTO;

import java.math.BigDecimal;

import com.krzywe.Model.Analyte;

public interface TargetValueView extends AbstractPersistentObjectView {
	
	public BigDecimal getTargetValue();
	
	public String getUnits();
	
	public AnalyteView getAnalyte();
}
