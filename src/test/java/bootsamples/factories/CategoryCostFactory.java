package bootsamples.factories;

import bootsamples.additional.Category;
import bootsamples.model.CategoryCost;

/**
 * @author Andrei Shalkevich
 *
 */
public class CategoryCostFactory {
	
	private Category category;
	private Double cost;

	public CategoryCostFactory() {

		category = Category.parterre;
		cost = 8.0;
	}
	
	public CategoryCost newInstance() {
		
		return new CategoryCost(category, cost);
	}
	
	public CategoryCostFactory setFields(Category category, Double cost){
		this.category = category;
		this.cost = cost;
		return this;
	}

}
