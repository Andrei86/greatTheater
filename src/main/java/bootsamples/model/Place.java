package bootsamples.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import bootsamples.additional.Category;
import bootsamples.additional.Status;
import lombok.Data;


/**
 * @author Andrei Shalkevich
 *
 */
@Entity
@Data
@Table(name="place")
public class Place {
	
	public Place() {
		super();
	}

	public Place(Seance seance, Customer customer, CategoryCost categoryCost, Integer row, Integer place,
			Status status) {
		super();
		this.seance = seance;
		this.customer = customer;
		this.categoryCost = categoryCost;
		this.row = row;
		this.place = place;
		this.status = status;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="seance_id")
	private Seance seance;
	
	@ManyToOne
	@JoinColumn(name="customer_id") // т.к. у билета есть владелец
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name="category_cost_id")
	private CategoryCost categoryCost;
	
	private Integer row;
	private Integer place;
	
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "status")
	private Status status;
	
}
