package com.krzywe.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UK_INTERNAL_STANDARD_NAME", columnNames = {"name"})})
public class InternalStandard extends AbstractPersistentObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Size(min=1, max = 32, message = "{aliases.Size.message}")
	@ElementCollection(fetch = FetchType.LAZY)
	@UniqueElements
	@OrderColumn(name = "ALIAS_ORDER")
	@JoinColumn(foreignKey = @ForeignKey(name="FK_INTERNAL_STANDARD_ID"))
	@Column(name="alias",length = 32, unique = true)
	@CollectionTable(uniqueConstraints = @UniqueConstraint(name = "UK_INTERNAL_STANDARD_NAME",columnNames= {"alias"}))
	private List<@Size(min = 3,max = 32) String> aliases = new ArrayList<String>(); 
	
	

	/**
	 * Return list of aliases stored in upper case. List {@link Size} up to 32 and alias {@link Size} 3 - 32.
	 * 
	 * @return {@link List}<{@link String}>
	 */
	public List<String> getAliases() {
		return aliases;
	}

	public void setAliases(List<String> aliases) {
		this.aliases.clear();	
		this.aliases.addAll(
				aliases
				.stream()
				.map(String::toUpperCase)
				.collect(Collectors.toList())
				);
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
	public void removeAlias(String alias) {
		this.aliases.remove(alias.toUpperCase());
	}

}
