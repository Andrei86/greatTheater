package bootsamples.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bootsamples.additional.Category;
import bootsamples.dto.CategoryCostDTO;
import bootsamples.dto.IdDTO;
import bootsamples.exceptions.noEnum.NoEnumValueException;
import bootsamples.exceptions.notFound.MyResourceNotFoundException;
import bootsamples.model.CategoryCost;
import bootsamples.service.CategoryCostService;

/**
 * @author Andrei Shalkevich
 *
 */
@RestController
@RequestMapping("/categories")
public class CategoryCostCotroller {

	@Autowired
	CategoryCostService categoryCostService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getCategoryCostBy(@PageableDefault Pageable pageable,
			@RequestParam(required = false) String category) {

		CategoryCostDTO categoryCostDTO = null;
		CategoryCost categoryCost = null;
		List<CategoryCostDTO> categoryCostsDTO = new ArrayList<CategoryCostDTO>();
		List<CategoryCost> categoryCosts = null;

		if (category != null) {

			categoryCost = categoryCostService.findCategoryCostByCategory(Category.valueOf(category));

			if (categoryCost == null)
				throw new MyResourceNotFoundException(String.format("category %s not found", category));

			categoryCostDTO = entity2dto(categoryCost);

			return new ResponseEntity<CategoryCostDTO>(categoryCostDTO, HttpStatus.OK);
		} else {
			categoryCosts = categoryCostService.findAllCategoryCosts(pageable);

			for (CategoryCost categoryCost1 : categoryCosts) {
				categoryCostsDTO.add(entity2dto(categoryCost1));
			}

			return new ResponseEntity<List<CategoryCostDTO>>(categoryCostsDTO, HttpStatus.OK);
		}

	}
	
	@RequestMapping(value = "/{id}",method = RequestMethod.GET)
	public ResponseEntity<?> getCategoryCostById(@PathVariable("id") Integer id){
		
		CategoryCost categoryCost = categoryCostService.findCategoryCostById(id);
		
		CategoryCostDTO categoryCostDTO = entity2dto(categoryCost);
		
		return new ResponseEntity<CategoryCostDTO>(categoryCostDTO, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCategoryCostById(@PathVariable("id") Integer id){
		
		categoryCostService.deleteCategoryCostById(id);
		
		return new ResponseEntity<CategoryCost>(HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAllCategoryCosts(){
		
		categoryCostService.deleteAllCategoryCosts();
		
		return new ResponseEntity<CategoryCost>(HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createCategoryCost(@Valid @RequestBody CategoryCostDTO categoryCostDTO){
		
		CategoryCost categoryCost = dto2entity(categoryCostDTO);
		
		categoryCostService.createCategoryCost(categoryCost);
		
		return new ResponseEntity<IdDTO>(new IdDTO(categoryCost.getId()), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateCategoryCost(@PathVariable("id") Integer id, @RequestBody CategoryCostDTO categoryCostDTO){
		
		CategoryCost categoryCost = categoryCostService.findCategoryCostById(id);
	
		categoryCost.setCategory(categoryCostDTO.getCategory() != null ? 
		Category.valueOf(categoryCostDTO.getCategory()) : categoryCost.getCategory());
		
		categoryCost.setCost(categoryCostDTO.getCost() != null ? 
		categoryCostDTO.getCost() : categoryCost.getCost());
		
		categoryCostService.updateCategoryCost(categoryCost);
		
		return new ResponseEntity<CategoryCostDTO>(HttpStatus.OK);
	}
	  
	
	private CategoryCost dto2entity(CategoryCostDTO categoryCostDTO)
	{
		CategoryCost categoryCost = new CategoryCost();
		
		if(categoryCostDTO.getId() != null)
			categoryCost.setId(categoryCostDTO.getId());
		try{
		categoryCost.setCategory(Category.valueOf(categoryCostDTO.getCategory()));
		}catch(IllegalArgumentException e)
		{
			throw new NoEnumValueException(String.format("values of category must be one of %s", Arrays.toString(Category.values())));
		}
		
		categoryCost.setCost(categoryCostDTO.getCost());
		
		return categoryCost;
	}

	private CategoryCostDTO entity2dto (CategoryCost categoryCost)
	{
		CategoryCostDTO categoryCostDto = new CategoryCostDTO();
		
		categoryCostDto.setId(categoryCost.getId());
		categoryCostDto.setCategory(categoryCost.getCategory().name());
		categoryCostDto.setCost(categoryCost.getCost());

		return categoryCostDto;
	}

}
