package org.hibernate.bugs;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Article {

	@Id
	Long id;
	String name;
	@Column(name="price")
	BigDecimal price;
	@Column(name="price",updatable = false, insertable = false)
	BigDecimal prevPrice;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPrevPrice() {
		return prevPrice;
	}

	public void setPrevPrice(BigDecimal prevPrice) {
		this.prevPrice = prevPrice;
	}

}
