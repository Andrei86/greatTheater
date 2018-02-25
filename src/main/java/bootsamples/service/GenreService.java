package bootsamples.service;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bootsamples.dao.GenreRepository;
import bootsamples.exceptions.duplicate.DuplicateEntityException;
import bootsamples.exceptions.notFound.MyResourceNotFoundException;
import bootsamples.model.Genre;
import ch.qos.logback.classic.Logger;


/**
 * @author Andrei Shalkevich
 *
 */
@Service
public class GenreService {
	
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(GenreService.class);

	private final GenreRepository genreRepository;

	public GenreService(GenreRepository genreRepository) {
		this.genreRepository = genreRepository;
	}
	
	public Genre findGenreByName(String name){
		
		LOGGER.info("Find genre by name = {} ", name);
		
		Genre genre = genreRepository.findByName(name);
		
		if (genre == null) {
			throw new MyResourceNotFoundException(String.format("genre with name %s not found", name));
		}

		return genre;
	}
	
	public Genre findGenreById(Integer id){
		
		LOGGER.info("Find genre by id = {} ", id);
		
		Genre genre = genreRepository.findOne(id);
		
		if(genre == null){
		throw new MyResourceNotFoundException(String.format("genre with id= %s not found", id));
	}
		return genre;
	}

	public List<Genre> findAllGenres(Pageable pageable)
	{
		
		LOGGER.info("Find all genres");
		
		List<Genre> genres = null;
		
		Page<Genre> page = genreRepository.findAll(pageable);
		
		genres = page.getContent();
		
		
		return genres;
	}
	
	@Transactional
	public void deleteGenreById(Integer id)
	{
		
		LOGGER.info("Delete genre by id = {} ", id);
		
		findGenreById(id);
		
		genreRepository.delete(id);
	}
	
	@Transactional
	public void deleteAllGenres()
	{
		LOGGER.info("Delete all genres");
		
		genreRepository.deleteAll();
	}
	
	@Transactional
	public Genre createGenre(Genre genre)
	{
		LOGGER.info("Create genre with name = {} ", genre.getName());
		
		try{
		findGenreByName(genre.getName());
		}catch(MyResourceNotFoundException e)
		{
			return genreRepository.save(genre);
		}
		throw new DuplicateEntityException("There is already such genre object exist");
		
	}
	
	@Transactional
	public Genre updateGenre(Genre genre)
	{
		LOGGER.info("Update genre with id = {} ", genre.getId());
		
		return genreRepository.save(genre);
		
	}

}
