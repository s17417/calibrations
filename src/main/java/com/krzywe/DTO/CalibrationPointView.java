package com.krzywe.DTO;

import java.util.List;
import java.util.Set;

import com.krzywe.Model.TargetValue;

public interface CalibrationPointView extends AbstractPersistentObjectView {

	public String getPointId();
	
	public List<String> getAliases();
	
	public Set<TargetValueView> getTargetValues();
}
