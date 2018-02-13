package bootsamples.factories;

import bootsamples.additional.Status;
import bootsamples.model.CategoryCost;
import bootsamples.model.Customer;
import bootsamples.model.Place;
import bootsamples.model.Seance;

/**
 * @author Andrei Shalkevich
 *
 */
public class PlaceFactory {

	private Seance seance;
	private Customer customer;
	private CategoryCost categoryCost;
	
	private Integer row;
	private Integer place;
	private Status status;
	
	public PlaceFactory() {
		
		seance = new SeanceFactory().newInstance();
		customer = null;
		categoryCost = new CategoryCostFactory().newInstance();
		row = 4;
		place = 7;
		status = Status.free;
		
	}
	
	public Place newInstance() {
		
		return new Place(seance, customer, categoryCost, row, place, status);
	}
	
	public PlaceFactory setFields(Seance seance, Customer customer, CategoryCost categoryCost,
			Integer row, Integer place, Status status) {
		this.seance = seance;
		this.customer = customer;
		this.categoryCost = categoryCost;
		this.row = row;
		this.place = place;
		this.status = status;
		
		return this;
	}

}
