package bootsamples.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

//@Entity
@Data
@Table(name="booking")
public class Booking {
	
	/*@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch=FetchType.EAGER)
	private Customer customer;
	
	@OneToMany(mappedBy = "booking",fetch = FetchType.EAGER)
	@JoinTable(name="booking_place", joinColumns=@JoinColumn(name="booking_id"), 
	inverseJoinColumns=@JoinColumn(name="place_id"))
	private List<Place> places;
	
	private Timestamp bookingDate; // можно сделать стрингу т.к. исп только для инфо???
*/

}
