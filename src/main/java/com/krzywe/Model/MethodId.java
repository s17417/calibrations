package com.krzywe.Model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MethodId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String laboratoryTestId;
	
	private String analyteId;

	
	public MethodId() {
		
	}

	public MethodId(String laboratoryTestId, String analyteId) {
		this.laboratoryTestId = laboratoryTestId;
		this.analyteId = analyteId;
	}
	
	


	public String getLaboratoryTestId() {
		return laboratoryTestId;
	}

	public void setLaboratoryTestId(String laboratoryTestId) {
		this.laboratoryTestId = laboratoryTestId;
	}

	public String getAnalyteId() {
		return analyteId;
	}

	public void setAnalyteId(String analyteId) {
		this.analyteId = analyteId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(analyteId, laboratoryTestId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MethodId other = (MethodId) obj;
		return Objects.equals(analyteId, other.analyteId) && Objects.equals(laboratoryTestId, other.laboratoryTestId);
	}
	
	

}
