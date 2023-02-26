package com.krzywe.Model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

public interface IWeight {
	
	public Map<ResponseValue,BigDecimal> calculateWeights(Collection<ResponseValue> responseValues);

}
