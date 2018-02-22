package bootsamples.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bootsamples.dao.CinemaRepository;
import bootsamples.exceptions.duplicate.DuplicateEntityException;
import bootsamples.exceptions.notFound.MyResourceNotFoundException;
import bootsamples.model.Cinema;

/**
 * @author Andrei Shalkevich
 *
 */
@Service
public class CinemaService {
	
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(CinemaService.class);
	
	private final CinemaRepository cinemaRepository;
	private final SchemaService schemaService;
	
	public CinemaService(CinemaRepository cinemaRepository, SchemaService schemaService) {
		this.cinemaRepository = cinemaRepository;
		this.schemaService = schemaService;
	}
	
	public List<Cinema> findCinemaByCity(String city) {
		
		LOGGER.info("Find cinema by city = {}", city);
		
		List<Cinema> cinemas = new ArrayList<>();
		List<Cinema> cinemasDB = cinemaRepository.findByCity(city);

		if (cinemasDB.isEmpty())
			throw new MyResourceNotFoundException("cinemas not found in such city");
		else {
			for (Cinema cinema : cinemasDB) {
				cinemas.add(cinema);
			}

			return cinemas;
		}
	}
	
	public Cinema findCinemaByName(String name) {
		
		LOGGER.info("Find cinema by name = {}", name);
		
		Cinema cinema = cinemaRepository.findByName(name);

		if (cinema == null) {
			throw new MyResourceNotFoundException("cinema with such name not found");
		}
		return cinema;
	}
	
	public Cinema findCinemaById(Integer id) {

		LOGGER.info("Find cinema by id = {}", id);
		
		Cinema cinema = cinemaRepository.findOne(id);

		if (cinema == null) {
			throw new MyResourceNotFoundException(String.format("cinema with id=%s not found", id));
		}
		return cinema;
	}

	public List<Cinema> findAllCinemas(Pageable pageable) {
		
		LOGGER.info("Find all cinemas");
		
		List<Cinema> cinemas = null;

		Page<Cinema> page = cinemaRepository.findAll(pageable);

		cinemas = page.getContent();

		return cinemas;
	}
	
	@Transactional
	public void deleteCinemaById(Integer id) {
		
		LOGGER.info("Delete cinema by id = {}", id);
		
		findCinemaById(id);

		cinemaRepository.delete(id);
	}
	
	@Transactional
	public Cinema createCinema(Cinema cinema) {
		
		LOGGER.info("Create cinema with name = {}", cinema.getName());
		
		String schemaName = cinema.getCinemaSchema().getName();

		try {
			findCinemaByName(cinema.getName());
		} catch (MyResourceNotFoundException e) {

			schemaService.findSchemaByName(schemaName); // Это правильно
			return cinemaRepository.save(cinema);

		}
		throw new DuplicateEntityException("There is already such cinema object exist");
	}
	
	@Transactional
	public Cinema updateCinema(Cinema cinema) {
		
		LOGGER.info("Update cinema with id = {}", cinema.getId());
		
		return cinemaRepository.save(cinema);
	}
	
	@Transactional
	public void deleteAllCinemas() {
		
		LOGGER.info("Delete all cinemas");
		
		cinemaRepository.deleteAll();
	}
	
}
