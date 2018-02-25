package bootsamples.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import bootsamples.dao.SeanceRepository;
import bootsamples.exceptions.notFound.MyResourceNotFoundException;
import bootsamples.model.Seance;
import ch.qos.logback.classic.Logger;


/**
 * @author Andrei Shalkevich
 *
 */
@Service
public class SeanceService {
	
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(SeanceService.class);
	
	private final SeanceRepository seanceRepository;
	
	private final MovieService movieService;
	
	private final CinemaService cinemaService;
	
	private Date endDate = null; // next day after searching date- я не уверен, что для 
	//каждого пользователя будет создаваться своя копия переменной endDte!! И если так, то 
	// перенести ее в Controller

	public SeanceService(SeanceRepository seanceRepository, MovieService movieService,
			CinemaService cinemaService) {
		
		this.seanceRepository = seanceRepository;
		this.movieService = movieService;
		this.cinemaService = cinemaService;
	}
	
	//@Async - для метода, который поддерживает многопоточность, а @EnableAsync - для класса, котрый использует многопоточные методы
	public List<Seance> findSeanceByMovieDate(String movieTitle, Date date)// need Guava
	{
		
		LOGGER.info("Find seance by movie title = {}  and date = {}", movieTitle,  date);
		
		endDate = new Date(date.getTime() + 86400000);// можно просто +1 - сутки

		List<Seance> seances = new ArrayList<Seance>();
		List<Seance> seancesFound = seanceRepository.findByMovieDate(movieTitle, date, endDate);
		if(seancesFound.isEmpty())
			throw new MyResourceNotFoundException(String.format("seances satisfying the search criteria not found"));
		
		for(Seance seance : seancesFound)
		{
			seances.add(seance);
		}
		
		return seances;
	}
	
	public List<Seance> findSeanceByCinemaDate(String cinemaName, Date date)// need Guava
	{
		LOGGER.info("Find seance by cinema name = {}  and date = {}", cinemaName,  date);
		
		endDate = new Date(date.getTime() + 86400000);// можно просто +1

		List<Seance> seances = new ArrayList<Seance>();
		List<Seance> seancesFound = seanceRepository.findByCinemaDate(cinemaName, date, endDate);
		if(seancesFound.isEmpty())
			throw new MyResourceNotFoundException(String.format("seances satisfying the search criteria not found"));
		
		for(Seance seance : seancesFound)
		{
			seances.add(seance);
		}
		
		return seances;
	}
	
	public List<Seance> findSeanceByDate(Date date)// need Guava
	{
		LOGGER.info("Find seance by date = {}", date);
		
		endDate = new Date(date.getTime() + 86400000);// можно просто +1

		List<Seance> seances = new ArrayList<Seance>();
		List<Seance> seancesFound = seanceRepository.findByDate(date, endDate);
		if(seancesFound.isEmpty())
			throw new MyResourceNotFoundException(String.format("seances satisfying the search criteria not found"));
		
		for(Seance seance : seancesFound)
		{
			seances.add(seance);
		}
		
		return seances;
	}
	
	public Seance findSeanceById(Integer id)
	{
		LOGGER.info("Find seance by id = {}", id);
		
		Seance seance = seanceRepository.findOne(id);
		
		if (seance == null) {
			throw new MyResourceNotFoundException(String.format("seance with id= %s not found", id));
		}
		
		return seance;
	}
	
	@Transactional
	public void deleteSeanceById(Integer id)
	{
		LOGGER.info("Delete seance by id = {}", id);
		
		findSeanceById(id);
		seanceRepository.delete(id);
	}
	
	@Transactional
	public Seance updateSeance(Seance seance)
	{
		LOGGER.info("Update seance with id = {}", seance.getId());
		
		return seanceRepository.save(seance);

	}
	
	@Transactional
	public Seance createSeance(Seance seance)
	{
		LOGGER.info("Create seance on for movie title = {} in cinema = {} at date = {}", seance.getMovie().getTitle(),
			   seance.getCinema().getName(), seance.getDate());
		
		cinemaService.findCinemaByName(seance.getCinema().getName());
		movieService.findMovieByTitle(seance.getMovie().getTitle());
		return seanceRepository.save(seance);

	}

}
