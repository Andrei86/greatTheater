package bootsamples.controller;

import java.util.ArrayList;
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

import bootsamples.exceptions.MyException;
import bootsamples.exceptions.dataIntegrityViolation.MyConstraintException;
import bootsamples.exceptions.duplicate.DuplicateEntityException;
import bootsamples.model.Cinema;
import bootsamples.service.CinemaService;
import bootsamples.service.SchemaService;
import bootsamples.dto.CinemaDTO;
import bootsamples.dto.IdDTO;

@RestController
@RequestMapping("/cinemas")
public class CinemaController {
	
	@Autowired
	CinemaService cinemaService;

	@Autowired
	SchemaService schemaService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getCinemaBy(@PageableDefault Pageable pageable,  
			@RequestParam(required=false) String city, @RequestParam(required=false) String name)
	{
		
		List<CinemaDTO> cinemasDTO = new ArrayList<>();
		
		Cinema cinema1 = null;
		
		List<Cinema> cinemas = new ArrayList<Cinema>();
				
		if(city != null)
			cinemas = cinemaService.findCinemaByCity(city);
		else if(name != null)
		{
			cinema1 = cinemaService.findCinemaByName(name);
			
				cinemas.add(cinema1);

		}
		else
			cinemas = cinemaService.findAllCinemas(pageable);
		
		for(Cinema cinema :	cinemas){
			
			CinemaDTO cinemaDTO = entity2dto(cinema);
			
			cinemasDTO.add(cinemaDTO);
		}
		
		
		return new ResponseEntity<List<CinemaDTO>>(cinemasDTO, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCinemaById(@PathVariable("id") Integer id)
	{
		Cinema cinema = cinemaService.findCinemaById(id);
		
		CinemaDTO cinemaDTO = entity2dto(cinema);
		
		return new ResponseEntity<CinemaDTO>(cinemaDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCinemaById(@PathVariable("id") Integer id){
		
		cinemaService.deleteCinemaById(id);

		return new ResponseEntity<CinemaDTO>(HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAllCinemas(){
		
		cinemaService.deleteAllCinemas();
		
		return new ResponseEntity<CinemaDTO>(HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST) // GET method at /cinemas/add - display form for cinema creating
	public ResponseEntity<?> createCinema(@Valid @RequestBody CinemaDTO cinemaDTO)
	{
		Cinema cinema = dto2entity(cinemaDTO);
		
		cinemaService.createCinema(cinema);
		
		return new ResponseEntity<IdDTO>(new IdDTO(cinema.getId()), HttpStatus.CREATED);

	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateCinema(@PathVariable("id") Integer id,/*@Valid*/ @RequestBody CinemaDTO cinemaDTO)
	{
		// не надо гонять туда-сюда entity2dto и dto2entity ОТРЕФАКТОРИТЬ
		Cinema cinema = cinemaService.findCinemaById(id);
		
		//Cinema cinemaDB = dto2entity(cinemaDTO);
		
		cinema.setAddress(cinemaDTO.getAddress() != null ? cinemaDTO.getAddress() 
				: cinema.getAddress());
		cinema.setCinemaSchema(cinemaDTO.getSchemaName()!= null ? schemaService.findSchemaByName(cinemaDTO.getSchemaName()) 
				: cinema.getCinemaSchema());
		cinema.setCity(cinemaDTO.getCity() != null ? cinemaDTO.getCity() : cinema.getCity());
		cinema.setName(cinemaDTO.getName() != null ? cinemaDTO.getName() : cinema.getName());
		cinema.setIsActive(cinemaDTO.getIsActive() != null ? cinemaDTO.getIsActive() : cinema.getIsActive());
		
		cinemaService.updateCinema(cinema);
		
		return new ResponseEntity<IdDTO>(HttpStatus.OK);
	}
	
	private Cinema dto2entity(CinemaDTO cinemaDTO) {
		Cinema cinema = new Cinema();
		if(cinemaDTO.getId() != null)
        cinema.setId(cinemaDTO.getId());
		
        cinema.setName(cinemaDTO.getName());
        cinema.setCity(cinemaDTO.getCity());
        cinema.setAddress(cinemaDTO.getAddress());
        cinema.setIsActive(cinemaDTO.getIsActive());
        cinema.setCinemaSchema(schemaService.findSchemaByName(cinemaDTO.getSchemaName())); // schema service
        
        return cinema;
    }

    private CinemaDTO entity2dto(Cinema cinema) {
        CinemaDTO cinemaDTO = new CinemaDTO();
        
        cinemaDTO.setId(cinema.getId());
        cinemaDTO.setName(cinema.getName());
        cinemaDTO.setCity(cinema.getCity());
        cinemaDTO.setAddress(cinema.getAddress());
        cinemaDTO.setIsActive(cinema.getIsActive());
        cinemaDTO.setSchemaName(cinema.getCinemaSchema().getName());
        
        return cinemaDTO;
    }

}
