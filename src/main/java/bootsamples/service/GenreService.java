package bootsamples.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bootsamples.dao.GenreRepository;
import bootsamples.exceptions.duplicate.DuplicateEntityException;
import bootsamples.exceptions.notFound.MyResourceNotFoundException;
import bootsamples.model.Genre;


/**
 * @author Andrei Shalkevich
 *
 */
@Service
@Transactional
public class GenreService {

	private final GenreRepository genreRepository;

	public GenreService(GenreRepository genreRepository) {
		this.genreRepository = genreRepository;
	}
	
	public Genre findGenreByName(String name){
		Genre genre = genreRepository.findByName(name);
		
		if (genre == null) {
			throw new MyResourceNotFoundException(String.format("genre with name %s not found", name));
		}

		return genre;
	}
	
	public Genre findGenreById(Integer id){
		
		Genre genre = genreRepository.findOne(id);
		
		if(genre == null){
		throw new MyResourceNotFoundException(String.format("genre with id= %s not found", id));
	}
		return genre;
	}

	public List<Genre> findAllGenres(Pageable pageable)
	{
		
		List<Genre> genres = null;
		
		Page<Genre> page = genreRepository.findAll(pageable);
		
		genres = page.getContent();
		
		
		return genres;
	}
	
	public void deleteGenreById(Integer id)
	{
		findGenreById(id);
		
		genreRepository.delete(id);
	}
	
	public void deleteAllGenres()
	{
		genreRepository.deleteAll();
	}
	
	public Genre createGenre(Genre genre)
	{
		try{
		findGenreByName(genre.getName());
		}catch(MyResourceNotFoundException e)
		{
			return genreRepository.save(genre);
		}
		throw new DuplicateEntityException("There is already such genre object exist");
		
	}
	
	public Genre updateGenre(Genre genre)
	{
	
		return genreRepository.save(genre);
		
	}

}
