package com.krzywe.Model;

import java.io.Serializable;

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
 * All fields ar optional besides laboratory test and analyte attributes.
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
	
	@NotNull
	@ManyToOne
	@MapsId("analyteId")
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_METHOD_ANALYTE_ID"))
	private Analyte analyte;
	
	@NotNull
	@ManyToOne
	@MapsId("laboratoryTestId")
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_METHOD_LABORATORYTEST_ID"))
	private LaboratoryTest laboratoryTest;
	
	@Size(max = 100)
	@Column(nullable = true, length = 100)
	private String responseValueUnits;
	
	
	@Size(min = 3, max = 100)
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
	 * @return {@link String}
	 */
	public String getAnalyticalMethod() {
		return analyticalMethod;
	}


	public void setAnalyticalMethod(String analyticalMethod) {
		this.analyticalMethod = analyticalMethod;
	}

	/**
	 * Returns associated to this method analyte. Attribute {@link NotNull} and is part of primary key.
	 * @return {@link Analyte}
	 */
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

	/**
	 * Returns an laboratory test containing this method. Attribute {@link NotNull} and is part of primary key.
	 * @return {@link LaboratoryTest}
	 */
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
