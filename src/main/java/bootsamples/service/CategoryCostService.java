package bootsamples.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.LoggerFactory;
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
public class CategoryCostService {
	
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(CategoryCostService.class);
	
	private final CategoryCostRepository categoryRepo;
	
	public CategoryCostService(CategoryCostRepository categoryRepo) {
		
		this.categoryRepo = categoryRepo;
	}
	
	@Cacheable(value = "category", key = "#category")
	public CategoryCost findCategoryCostByCategory(Category category) {
		
		LOGGER.info("Find CategoryCost by category = {} ", category.name());
		
		CategoryCost categoryCost = categoryRepo.findByCategory(category);

		if (categoryCost == null) {
			throw new MyResourceNotFoundException(String.format("category cost with category %s not found", category));
		}

		return categoryCost;
	}
	
	@Cacheable(value = "category", key = "#id")
	public CategoryCost findCategoryCostById(Integer id) {
		
		LOGGER.info("Find CategoryCost by id = {} ", id);
		
		CategoryCost categoryCost = categoryRepo.findOne(id);

		if (categoryCost == null) {
			throw new MyResourceNotFoundException(String.format("category cost with id= %s not found", id));
		}
		return categoryCost;
	}

	@Cacheable(value = "category")
	public List<CategoryCost> findAllCategoryCosts(Pageable pageable) {
		
		LOGGER.info("Find all CategoryCosts");
		
		List<CategoryCost> categoryCosts = null;

		Page<CategoryCost> page = categoryRepo.findAll(pageable);

		categoryCosts = page.getContent();

		return categoryCosts;
	}
	
	@Transactional
	@CacheEvict(value = "category", key = "#id")
	public void deleteCategoryCostById(Integer id) {
		
		LOGGER.info("Delete CategoryCost by id = {} ", id);
		
		findCategoryCostById(id);

		categoryRepo.delete(id);
	}
	
	@Transactional
	@CacheEvict(value = "category",  allEntries = true)
	public void deleteAllCategoryCosts() {
		
		LOGGER.info("Delete all CategoryCosts");
		
		categoryRepo.deleteAll();
	}
	
	@Transactional
	@CachePut(value = "categories")
	public CategoryCost createCategoryCost(CategoryCost categoryCost) {
		
		LOGGER.info("Create CategoryCost with category = {} and cost = {}", categoryCost.getCategory(), categoryCost.getCost());
		
		try {
			findCategoryCostByCategory(categoryCost.getCategory());
		} catch (MyResourceNotFoundException e) {
			return categoryRepo.save(categoryCost);
		}
		throw new DuplicateEntityException("There is already such categoryCost object exist");

	}
	
	@Transactional
	@Caching( put = {
        @CachePut(value = "category", key = "#id"),
        @CachePut(value = "category", key = "#category")
	
	})
	public CategoryCost updateCategoryCost(CategoryCost categoryCost) {
		
		LOGGER.info("Update CategoryCost with id = {}", categoryCost.getId());

		return categoryRepo.save(categoryCost);

	}

}
