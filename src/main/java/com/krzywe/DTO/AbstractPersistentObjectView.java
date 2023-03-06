package com.krzywe.DTO;

import java.time.LocalDateTime;

public interface AbstractPersistentObjectView {
	
	public String getId();
	
	public LocalDateTime getCreatedDate();
	
	public LocalDateTime getModifiedDate();

}
