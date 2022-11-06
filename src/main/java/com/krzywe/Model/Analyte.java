package com.krzywe.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.UniqueElements;

/**
 * Entity being equivalent to analyte.
 * Stores name and aliases of analyte and is in relation to CalibrationPoint and LaboratoryTest by association class Method.
 * Obligatory fields: name
 * @author tomek
 *
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UK_ANALYTE_NAME",columnNames = { "name" })})
public class Analyte extends AbstractPersistentObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotBlank (message = "valid field can't be empty")
	@Size(min = 3, max = 100, message = "valid length - 3 to 100 chars")
	@Column(nullable = false, length = 100)
	private String name;
	
	@Size(max = 32, message = "valid collection size - up to 32 aliases")
	@ElementCollection(fetch = FetchType.LAZY)
	@UniqueElements(message = "must contain only unique elements")
	@OrderColumn(name = "ALIAS_ORDER")
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_ANALYTE_ID"))
	@Column(length = 32)
	private List<@Size(min = 3, max = 32, message = "valid length - 3 to 32 chars") String> aliases = new ArrayList<>();

	@OneToMany(mappedBy = "analyte", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Method> method = new HashSet<>();
	
	/**
	 *  Returns name of unique name of Analyte. Attribute {@link Size} 3 - 100, {@link NotBlank} and unique. 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return list of aliases stored in upper case. List {@link Size} up to 32 and alias {@link Size} 3 - 32.
	 * @return
	 */
	public List<String> getAliases() {
		return aliases;
	}

	public void setAliases(Collection<String> aliases) {
		this.aliases.clear();
		this.aliases.addAll(aliases.stream().map(String::toUpperCase).collect(Collectors.toList()));
	}
	
	/**
	 * Adds alias to aliases list. Alias is transformed to upper case. List {@link Size} up to 32 and alias {@link Size} 3 - 32.
	 * @param {@link String} alias
	 */
	public void addAlias(String alias) {
			this.aliases.add(alias.toUpperCase());
	}
	
	/**
	 * Removes alias to aliases list. Alias is transformed to upper case. List {@link Size} up to 32 and alias {@link Size} 3 - 32.
	 * @param {@link String} alias
	 */
	public void removeAlias (String alias) {
			this.aliases.remove(alias.toUpperCase());
	}

	public Set<Method> getMethod() {
		return method;
	}

	public <V extends Collection<Method>> void setMethod(V method) {
		this.method.clear();
		this.method.addAll(method);
	}
	
	public void addMethod(Method method) {
		method.setAnalyte(this);
	}
	
	public void removeMethod(Method method) {
		if (this.method.contains(method)) {
			method.setAnalyte(null);
		}
	}
	

}
