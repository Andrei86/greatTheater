package bootsamples.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import bootsamples.additional.Category;
import lombok.Data;


/**
 * @author Andrei Shalkevich
 *
 */
@Entity
@Data
@Table(name="category_cost") // сделать layers for that entity(repo, service, controller, dto)
public class CategoryCost implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public CategoryCost() {
		super();
	}

	public CategoryCost(Category category, Double cost) {
		super();
		this.category = category;
		this.cost = cost;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "category")
	private Category category;
	
	private Double cost;

}
