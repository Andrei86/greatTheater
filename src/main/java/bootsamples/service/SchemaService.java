package bootsamples.service;

import java.sql.SQLException;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bootsamples.dao.CinemaSchemaRepository;
import bootsamples.exceptions.IntegrityViolationException;
import bootsamples.exceptions.duplicate.DuplicateEntityException;
import bootsamples.exceptions.notFound.MyResourceNotFoundException;
import bootsamples.model.CinemaSchema;

/**
 * @author Andrei Shalkevich
 * 
 * Schema service class
 *
 */
@Service
@Transactional
public class SchemaService {

	private final CinemaSchemaRepository cinemaSchemaRepository;

	public SchemaService(CinemaSchemaRepository cinemaRepository) {
		this.cinemaSchemaRepository = cinemaRepository;
	}

	public CinemaSchema findSchemaByName(String name) {
		CinemaSchema schema = cinemaSchemaRepository.findByName(name);

		if (schema == null) {
			throw new MyResourceNotFoundException(String.format("schema with name= %s not found", name));
		}

		return schema;
	}
	
	public CinemaSchema findSchemaById(Integer id) {
		CinemaSchema schema = cinemaSchemaRepository.findOne(id);

		if (schema == null) {
			throw new MyResourceNotFoundException(String.format("schema with such id= %s not found", id));
		}

		return schema;
	}

	public List<CinemaSchema> findAllSchemas(Pageable pageable) {

		List<CinemaSchema> schemas = null;

		Page<CinemaSchema> page = cinemaSchemaRepository.findAll(pageable);

		schemas = page.getContent();

		return schemas;
	}

	public void deleteSchemaById(Integer id) {
		findSchemaById(id);
		
		/*try{*/
		cinemaSchemaRepository.delete(id);
		/*} catch(DataIntegrityViolationException ex){ // здесь я привязываюсь к dao и при изменении dao слоя надо будет исправлять код?
			throw new IntegrityViolationException("You can't delete entitity because it's links are present in DB");
		}*/
	}
	
	public CinemaSchema createSchema(CinemaSchema cinemaSchema) {

		try {
			findSchemaByName(cinemaSchema.getName());
		} catch (MyResourceNotFoundException e) {
			return cinemaSchemaRepository.save(cinemaSchema);
		}
		throw new DuplicateEntityException(
				String.format("There is already cinema schema object exist with name= %s", cinemaSchema.getName()));
	}
	
	public CinemaSchema updateSchema(CinemaSchema cinemaSchema) {
		return cinemaSchemaRepository.save(cinemaSchema);
	}

	public void deleteAllSchemas() {
		cinemaSchemaRepository.deleteAll();
	}

}
