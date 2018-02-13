package bootsamples.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bootsamples.dao.CinemaSchemaRepository;
import bootsamples.model.Cinema;
import bootsamples.service.CinemaService;

@RestController
public class SimpleRestController {
	
	@Autowired
	private CinemaService cinemaService;
	
	@Autowired
	private CinemaSchemaRepository cinSchemRep;
	
	//@GetMapping(consumes = {"text/plain", "application/*"}, value = "/cinemaschema/{id}")
	@GetMapping("/cinemaschema/{id}")  // 
	public String idCinemaSchema( @PathVariable(value="id") Integer id)
	{
				
		return cinSchemRep.findOne(id).toString();
	}
	
	
	/*@RequestMapping("/save")
	public String saveProcess(@RequestParam String name, String city, String address, Boolean isActive, 
			String schemaName){
		Cinema cinema = new Cinema(name, city, address, isActive, schemaName);
		return cinemaService.save(cinema);

		
	}
	
	@GetMapping(consumes = {"text/plain", "application/*"}, value = "/all-cinemas")
	public String allCinemas(){
		return cinemaService.findAll().toString();
	}
	
	@GetMapping("/city-cinemas")
	public String cityCinemas(@RequestParam String city){
		return cinemaService.findForCity(city).toString();
	}
	
	@GetMapping("/hello")
	public String hello()
	{
		return "Hello world!!";
	}
	
	@GetMapping("/cinema/{id}")  // 
	public String idCinema( @PathVariable(value="id") Integer id)
	{
				
		return cinemaService.findForId(id).toString();
	}

	@GetMapping("/save-cinema")
	public String saveCinema(@RequestParam String name, @RequestParam String city,
			@RequestParam String address, @RequestParam Boolean isActive, @RequestParam String schemaName)
	{
		Cinema cinema = new Cinema(name, city, address, isActive, schemaName);
		
		cinemaService.save(cinema);
		
		return "Cinema saved!";
	}
	
	@GetMapping("/save-cinema")
	public String saveCinema(@RequestParam String name, @RequestParam String city,
			@RequestParam String address, @RequestParam Boolean isActive, @RequestParam int schemaId)
	{
		Cinema cinema = new Cinema(name, city, address, isActive, schemaId);
		
		cinemaService.save(cinema);
		
		return "Cinema saved!";
	}
	
	@GetMapping("/delete-cinema")
	public String deleteCinema(@RequestParam int id)
	{
		cinemaService.delete(id);
		return "Deleted cinema with id = " + id;
	}
	
	@GetMapping("/cinema/findByCity/{city}")  // 
	public String byCity( @PathVariable(value="city") String city)
	{
		List<Cinema> list = cinemaService.findByCity(city);
				return list.toString();
	}*/
}
