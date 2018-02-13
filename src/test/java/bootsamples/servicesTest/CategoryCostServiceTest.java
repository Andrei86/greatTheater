package bootsamples.servicesTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import bootsamples.additional.Category;
import bootsamples.exceptions.notFound.MyResourceNotFoundException;
import bootsamples.factories.CategoryCostFactory;
import bootsamples.model.CategoryCost;
import bootsamples.service.CategoryCostService;

/**
 * @author Andrei Shalkevich
 *
 */
public class CategoryCostServiceTest extends AbstractServiceTest{
	
	@Autowired
	private EntityManager testEntityManager;
	
	@Autowired
	private CategoryCostService categoryCostService;

	private CategoryCostFactory categoryCostFactory = new CategoryCostFactory();
	private CategoryCost categoryCost1 = categoryCostFactory.newInstance();
	private CategoryCost categoryCost2 = categoryCostFactory.setFields(Category.balcony, 9.99).newInstance();
	
	
	@Before
	public void before() {
		testEntityManager.persist(categoryCost1);
		testEntityManager.persist(categoryCost2);
	}
	
	@Test
	public void findCategoryCostByCategoryTest(){

		CategoryCost categoryCostFromDB = categoryCostService.findCategoryCostByCategory(categoryCost1.getCategory());
		
		assertThat(categoryCostFromDB).isEqualTo(categoryCost1);
	}

	@Test
	public void findCategoryCostByIdTest(){

		CategoryCost categoryCostFromDB = categoryCostService.findCategoryCostById(categoryCost2.getId());
		
		assertThat(categoryCostFromDB).isEqualTo(categoryCost2);
	}
	
	@Test
	public void findAllCategoryCostTest(){
		
		Pageable foundPage = new PageRequest(0, 3);
		List<CategoryCost> categoryCostListFromDB = categoryCostService.findAllCategoryCosts(foundPage);
		
		assertThat(categoryCostListFromDB).hasSize(2);
	}	
	
	@Test(expected=MyResourceNotFoundException.class)
	public void deleteCategoryCostByIdTest(){
		
		Integer categoryCostId = categoryCost1.getId();
		categoryCostService.deleteCategoryCostById(categoryCostId);
		
		categoryCostService.findCategoryCostById(categoryCostId);
	}
	
	@Test
	public void deleteAllCategoryCostTest()
	{
		categoryCostService.deleteAllCategoryCosts();
		
		Pageable foundPage = new PageRequest(0, 3);
		List<CategoryCost> categoryCostListFromDB = categoryCostService.findAllCategoryCosts(foundPage);
		
		assertThat(categoryCostListFromDB).hasSize(0);
	}
	
	@Test
	public void createCategoryCostTest(){

		CategoryCost newCategoryCost = categoryCostFactory.setFields(Category.vip, 12.0).newInstance();

		CategoryCost createdCategoryCost = categoryCostService.createCategoryCost(newCategoryCost);
		
		assertThat(createdCategoryCost.getCost()).isEqualTo(12.0);
	}
	
	@Test
	public void updateCategoryCostTest(){
		
		CategoryCost categoryCostFromDB = categoryCostService.findCategoryCostById(categoryCost1.getId());
		categoryCostFromDB.setCost(100500.0);
		
		CategoryCost updatedCategoryCost = categoryCostService.updateCategoryCost(categoryCostFromDB);
		
		assertThat(updatedCategoryCost.getCost()).isEqualTo(100500.0);
	} 
}
