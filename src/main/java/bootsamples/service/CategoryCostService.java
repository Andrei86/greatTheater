package bootsamples.service;

import org.springframework.cache.annotation.Cacheable;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bootsamples.additional.Category;
import bootsamples.dao.CategoryCostRepository;
import bootsamples.exceptions.duplicate.DuplicateEntityException;
import bootsamples.exceptions.notFound.MyResourceNotFoundException;
import bootsamples.model.CategoryCost;
import ch.qos.logback.classic.Logger;

/**
 * @author Andrei Shalkevich
 *
 */
@Service
@Transactional
public class CategoryCostService {
	
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(CategoryCostService.class);
	
	private final CategoryCostRepository categoryRepo;
	
	public CategoryCostService(CategoryCostRepository categoryRepo) {
		
		this.categoryRepo = categoryRepo;
	}
	
	@Cacheable("categories")
	public CategoryCost findCategoryCostByCategory(Category category) {
		
		LOGGER.info("Find CategoryCost by category = {} ", category.name());
		
		CategoryCost categoryCost = categoryRepo.findByCategory(category);

		if (categoryCost == null) {
			throw new MyResourceNotFoundException(String.format("category cost with category %s not found", category));
		}

		return categoryCost;
	}
	
	//@Cacheable("categories")
	public CategoryCost findCategoryCostById(Integer id) {
		
		LOGGER.info("Find CategoryCost by id = {} ", id);
		
		CategoryCost categoryCost = categoryRepo.findOne(id);

		if (categoryCost == null) {
			throw new MyResourceNotFoundException(String.format("category cost with id= %s not found", id));
		}
		return categoryCost;
	}

	public List<CategoryCost> findAllCategoryCosts(Pageable pageable) {

		List<CategoryCost> categoryCosts = null;

		Page<CategoryCost> page = categoryRepo.findAll(pageable);

		categoryCosts = page.getContent();

		return categoryCosts;
	}

	public void deleteCategoryCostById(Integer id) {
		findCategoryCostById(id);

		categoryRepo.delete(id);
	}

	public void deleteAllCategoryCosts() {
		categoryRepo.deleteAll();
	}

	public CategoryCost createCategoryCost(CategoryCost categoryCost) {
		try {
			findCategoryCostByCategory(categoryCost.getCategory());
		} catch (MyResourceNotFoundException e) {
			return categoryRepo.save(categoryCost);
		}
		throw new DuplicateEntityException("There is already such category cost object exist");

	}

	public CategoryCost updateCategoryCost(CategoryCost categoryCost) {

		return categoryRepo.save(categoryCost);

	}

}
