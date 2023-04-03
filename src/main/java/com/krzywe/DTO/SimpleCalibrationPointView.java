package com.krzywe.DTO;

import java.util.List;

public interface SimpleCalibrationPointView  extends AbstractPersistentObjectView  {
	
	public String getPointId();
	
	public List<String> getAliases();
	
	
}
