package com.krzywe.DTO;

import java.time.LocalDateTime;

public interface CalibrationSetView extends SimpleCalibrationSetView, AbstractPersistentObjectView {
	public LocalDateTime getModifiedDate();
}
