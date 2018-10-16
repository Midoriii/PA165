package cz.fi.muni.pa165.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "ESHOP_PRODUCTS")
public class Product {
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@Column(nullable = false, unique = true)
	private String name;
	
	@Column
	private Color color;
	
	@Temporal(TemporalType.DATE)
	Date addedDate;
	
	public Product(){}

	public void setName(String given_name) {
		this.name = given_name;	
	}

	public void setColor(Color given_color) {
		this.color = given_color;
	}

	public void setAddedDate(Date Date) {
		this.addedDate = Date;
	}

	public Object getName() {
		return this.name;
	}

	public Object getAddedDate() {
		return this.addedDate;
	}

	public Object getColor() {
		return this.color;
	}

	public void setId(long given_id) {
		this.id = given_id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Product))
			return false;
		Product other = (Product) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.getName()))
			return false;
		return true;
	}
}
