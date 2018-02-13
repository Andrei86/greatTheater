package bootsamples.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

import bootsamples.model.Genre;
import bootsamples.service.GenreService;
import bootsamples.dto.GenreDTO;
import bootsamples.dto.IdDTO;
import bootsamples.exceptions.IntegrityViolationException;
import bootsamples.exceptions.notFound.MyResourceNotFoundException;

@RestController
@RequestMapping("/genres")
public class GenreController {

	@Autowired
	GenreService genreService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getGenreBy(@PageableDefault Pageable pageable, @RequestParam(required=false) String name){
		
		GenreDTO genreDTO = null;
		Genre genre = null;
		List<GenreDTO> genresDTO = new ArrayList<GenreDTO>();
		List<Genre> genres = null;
		
		if(name != null){
			
			genre = genreService.findGenreByName(name);
			
			if (genre == null)
				throw new MyResourceNotFoundException(String.format("genre with name %s not found", name));
			
			genreDTO = entity2dto(genre);
		
		return new ResponseEntity<GenreDTO>(genreDTO, HttpStatus.OK);
		}
		else{
			genres = genreService.findAllGenres(pageable);
		
		for(Genre genre1 : genres)
		{
			genresDTO.add(entity2dto(genre1));
		}
		
		return new ResponseEntity<List<GenreDTO>>(genresDTO, HttpStatus.OK);
		}
		
	}
	
	@RequestMapping(value = "/{id}",method = RequestMethod.GET)
	public ResponseEntity<?> getGenreById(@PathVariable("id") Integer id){
		
		Genre genre = genreService.findGenreById(id);
		
		GenreDTO genreDTO = entity2dto(genre);
		
		return new ResponseEntity<GenreDTO>(genreDTO, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteGenreById(@PathVariable("id") Integer id){
		try {
			genreService.deleteGenreById(id);
		} catch (DataIntegrityViolationException ex) {
			throw new IntegrityViolationException(String
					.format("You can't delete schema with id = %s because links to it are still present in DB", id));
		}
		return new ResponseEntity<Genre>(HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAllGenres(){
		try{
		genreService.deleteAllGenres();
	}catch(DataIntegrityViolationException ex){
		throw new IntegrityViolationException("You can't delete genres with because links to them are still present in DB");
	}
		return new ResponseEntity<Genre>(HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createGenre(@Valid @RequestBody GenreDTO genreDTO){
		
		Genre genre = dto2entity(genreDTO);
		
		genreService.createGenre(genre);
		
		return new ResponseEntity<IdDTO>(new IdDTO(genre.getId()), HttpStatus.CREATED);
	}
	
	/*@RequestMapping(value = "/{id}/update", method = RequestMethod.PUT)
	public ResponseEntity<?> updateGenre(@PathVariable("id") Integer id, @RequestBody GenreDTO genreDTO){
		
		Genre genre = genreService.findGenreById(id);
	
		genre.setName(genreDTO.getName() != null ? 
		genreDTO.getName() : genre.getName());
		
		genreService.createOrUpdateGenre(genre);
		
		return new ResponseEntity<GenreDTO>(HttpStatus.OK);
	}*/
	 // that is unnecessary to update genre
	private Genre dto2entity(GenreDTO genreDTO)
	{
		Genre genre = new Genre();
		
		if(genreDTO.getId() != null)
			genre.setId(genreDTO.getId());
		
		genre.setName(genreDTO.getName());
		
		return genre;
	}

	private GenreDTO entity2dto (Genre genre)
	{
		GenreDTO genreDto = new GenreDTO();
		
		genreDto.setId(genre.getId());
		genreDto.setName(genre.getName());

		return genreDto;
	}

}
