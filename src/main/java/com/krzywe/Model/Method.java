package com.krzywe.Model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Class representing method belonging to a laboratory test describing a way to analyze one of analytes by analytical method and type of raw readings from instruments as response value units.
 * All fields ar optional.
 * @author tomek
 *
 */
@Entity
public class Method implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@EmbeddedId
	private MethodId id = new MethodId();
	
	@NotNull(message = "valid field can't be empty")
	@ManyToOne
	@MapsId("analyteId")
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_METHOD_ANALYTE_ID"))
	private Analyte analyte;
	
	@NotNull(message = "valid field can't be empty")
	@ManyToOne
	@MapsId("laboratoryTestId")
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_METHOD_LABORATORYTEST_ID"))
	private LaboratoryTest laboratoryTest;
	
	@Size(max = 100, message = "valid length - 1 to 100 chars")
	@Column(nullable = true, length = 100)
	private String responseValueUnits;
	
	
	@Size (min = 3, max = 100, message = "valid length - 3 to 100 chars")
	@Column (nullable = true, length = 100)
	private String analyticalMethod;

	public Method() {
		
	}

	public Method(Analyte analyte, LaboratoryTest laboratoryTest) {
		this.setAnalyte(analyte);
		this.setLaboratoryTest(laboratoryTest);
	}


	/**
	 * Returns Units for raw readings of analitical method
	 * @return {@link String}
	 */
	public String getResponseValueUnits() {
		return responseValueUnits;
	}

	
	public void setResponseValueUnits(String responseValueUnits) {
		this.responseValueUnits = responseValueUnits;
	}

	/**
	 * Returns analytical method type
	 * @return String
	 */
	public String getAnalyticalMethod() {
		return analyticalMethod;
	}


	public void setAnalyticalMethod(String analyticalMethod) {
		this.analyticalMethod = analyticalMethod;
	}


	public Analyte getAnalyte() {
		return analyte;
	}


	public void setAnalyte(Analyte analyte) {
		if (this.analyte==null) {
			if (analyte==null) return;
			analyte.getMethod().add(this);
			this.analyte=analyte;
			return;
		}
		if (this.analyte.getMethod().remove(this)) {
			this.analyte=analyte;
			if (this.analyte!=null)
				this.analyte.getMethod().add(this);
		}
	}


	public LaboratoryTest getLaboratoryTest() {
		return laboratoryTest;
	}


	public void setLaboratoryTest(LaboratoryTest laboratoryTest) {
		if (this.laboratoryTest == null ) {
			if (laboratoryTest == null) return;
			laboratoryTest.getMethod().add(this);
			this.laboratoryTest = laboratoryTest;
			return;
		}
		if (this.laboratoryTest.getMethod().remove(this)){
			this.laboratoryTest = laboratoryTest;
			if (this.laboratoryTest!=null)
				this.laboratoryTest.getMethod().add(this);
		}
		
		
		this.laboratoryTest = laboratoryTest;
	}

	public MethodId getId() {
		return id;
	}

	public void setId(MethodId id) {
		this.id = id;
	}
}
