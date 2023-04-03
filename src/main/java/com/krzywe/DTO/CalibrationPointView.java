package com.krzywe.DTO;

import java.util.Set;

public interface CalibrationPointView extends SimpleCalibrationPointView {
	
	public Set<TargetValueView> getTargetValues();
}
