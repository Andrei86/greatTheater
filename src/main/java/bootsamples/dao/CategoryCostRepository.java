package bootsamples.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bootsamples.additional.Category;
import bootsamples.model.CategoryCost;

/**
 * @author Andrei Shalkevich
 *
 */
@Repository
public interface CategoryCostRepository extends JpaRepository<CategoryCost, Integer>{
	
	CategoryCost findByCategory(Category category);

}
