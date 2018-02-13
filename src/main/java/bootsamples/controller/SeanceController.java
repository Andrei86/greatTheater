package bootsamples.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bootsamples.model.Seance;
import bootsamples.service.CinemaService;
import bootsamples.service.MovieService;
import bootsamples.service.SeanceService;
import bootsamples.dto.SeanceDTO;
import bootsamples.exceptions.dateException.MyDateFormatException;
import bootsamples.exceptions.notFound.MyResourceNotFoundException;

@RestController
@RequestMapping("/seances")
public class SeanceController {

	@Autowired
	SeanceService seanceService;
	
	@Autowired
	CinemaService cinemaService;
	
	@Autowired
	MovieService movieService;
	
	private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	private DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	private Pattern patt = Pattern.compile("\\d{2}-\\d{2}-\\d{4}");
	private Pattern patt2 = Pattern.compile("\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}");
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getSeanceBy(@RequestParam(required=false) String movieTitle, 
			@RequestParam(required=false) String cinemaName, @RequestParam(required=false) String date){ // переименовать метод
		
		// все-таки можно сделать seanceFilter, но надо разобраться как это сделать в spring boot
		
		System.out.println(date);
		
		Date utilDate = null; // date for query
		
		List<Seance> seances = null;
		
		List<SeanceDTO> seanceDTO = new ArrayList<SeanceDTO>();
		
		if(date == null)
			utilDate = new Date();
		else{
			try
			{
				if(!(patt.matcher(date).matches()))
					throw new MyDateFormatException("Please, insert date in format dd-mm-yyyy");
				
			System.out.println(date);	
			utilDate = df.parse(date);

			}
			catch(Exception e){}

		}
		
		if(movieTitle != null)
			seances = seanceService.findSeanceByMovieDate(movieTitle, utilDate);
		// можно сдесь написать сообщение о неправильном названии фильма?
		else if(cinemaName != null)
			seances = seanceService.findSeanceByCinemaDate(cinemaName, utilDate);
		// можно сдесь написать сообщение о неправильном названии кинотеатра?
		else
			seances = seanceService.findSeanceByDate(utilDate);
		
		if (seances.isEmpty()) {
			throw new MyResourceNotFoundException("seances not found");
		}
		
		for(Seance seance : seances)
		{
			seanceDTO.add(entity2dto(seance));
		}
		
		return new ResponseEntity<List<SeanceDTO>>(seanceDTO, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getSeanceById(@PathVariable("id") Integer id)
	{
		Seance seance = seanceService.findSeanceById(id);
		
		SeanceDTO seanceDTO = entity2dto(seance);
		
		return new ResponseEntity<SeanceDTO>(seanceDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteSeanceById(@PathVariable("id") Integer id){
		
		
		seanceService.deleteSeanceById(id);
		
		return new ResponseEntity<Seance>(HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createSeance(@Valid @RequestBody SeanceDTO seanceDTO){
		
		if (!patt2.matcher(seanceDTO.getDate()).matches())
			throw new MyDateFormatException("Please, insert seance date in format \"dd-mm-yyyy hh:mm\"");

		Seance seance = dto2entity(seanceDTO);
		
		seanceService.createSeance(seance);
		
		return new ResponseEntity<Integer>(seance.getId(), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateSeance(@PathVariable("id") Integer id, @RequestBody SeanceDTO seanceDTO){
		
		// плохо гонять туда-сюда entity2 dto and dto2entity и ОТРЕФАКТОРИТЬ
		
		Seance seanceDB = seanceService.findSeanceById(id);
		SeanceDTO seanceDBDTO = entity2dto(seanceDB);
		
		Boolean boolDate = false;
		
		if(seanceDTO.getDate() != null && patt2.matcher(seanceDTO.getDate()).matches())
			boolDate = true;//patt2.matcher(seanceDTO.getDate()).matches();
		else
			throw new MyDateFormatException("Please, insert seance date in format \"dd-mm-yyyy hh:mm\"");
		
		seanceDBDTO.setCinemaName(
				seanceDTO.getCinemaName() != null ? seanceDTO.getCinemaName() : seanceDBDTO.getCinemaName());
		
		seanceDBDTO.setMovieTitle(
				seanceDTO.getMovieTitle() != null ? seanceDTO.getMovieTitle() : seanceDBDTO.getMovieTitle());
		
		seanceDBDTO.setDate(boolDate ? seanceDTO.getDate() : seanceDBDTO.getDate());
		
		Seance seanceNew = dto2entity(seanceDBDTO);
		
		seanceService.updateSeance(seanceNew);
		
		return new ResponseEntity<SeanceDTO>(seanceDBDTO, HttpStatus.OK);
	}
	
	
	private Seance dto2entity(SeanceDTO seanceDTO) {
		String dateString = null;
		Seance seance = new Seance();
		
		if (seanceDTO.getId() != null)
		seance.setId(seanceDTO.getId());

		seance.setCinema(cinemaService.findCinemaByName(seanceDTO.getCinemaName())); // findByName cinema add method
		
		seance.setMovie(movieService.findMovieByTitle(seanceDTO.getMovieTitle()));
		try {
			dateString = seanceDTO.getDate();
			seance.setDate(df2.parse(dateString));
		} catch (Exception e) {
		}

		return seance;
	}

	private SeanceDTO entity2dto(Seance seance) {

		String dateString = null;
		SeanceDTO seanceDTO = new SeanceDTO();

		seanceDTO.setId(seance.getId());
		seanceDTO.setCinemaName(seance.getCinema().getName());
		seanceDTO.setMovieTitle(seance.getMovie().getTitle());

		dateString = df2/*df*/.format(seance.getDate());
		seanceDTO.setDate(dateString);
		
		//seanceDTO.setTime(dateString.substring(11));

		return seanceDTO;
	}

}
