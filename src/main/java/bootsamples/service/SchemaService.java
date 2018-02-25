package bootsamples.service;

import java.sql.SQLException;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.slf4j.LoggerFactory;
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
import ch.qos.logback.classic.Logger;

/**
 * @author Andrei Shalkevich
 * 
 * Schema service class
 *
 */
@Service
public class SchemaService {
	
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(SchemaService.class);

	private final CinemaSchemaRepository cinemaSchemaRepository;

	public SchemaService(CinemaSchemaRepository cinemaRepository) {
		this.cinemaSchemaRepository = cinemaRepository;
	}

	public CinemaSchema findSchemaByName(String name) {
		
		LOGGER.info("Find schema by name = {} ", name);
		
		CinemaSchema schema = cinemaSchemaRepository.findByName(name);

		if (schema == null) {
			throw new MyResourceNotFoundException(String.format("schema with name= %s not found", name));
		}

		return schema;
	}
	
	public CinemaSchema findSchemaById(Integer id) {
		
		LOGGER.info("Find schema by id = {} ", id);
		
		CinemaSchema schema = cinemaSchemaRepository.findOne(id);

		if (schema == null) {
			throw new MyResourceNotFoundException(String.format("schema with such id= %s not found", id));
		}

		return schema;
	}

	public List<CinemaSchema> findAllSchemas(Pageable pageable) {
		
		LOGGER.info("Find all schemas");

		List<CinemaSchema> schemas = null;

		Page<CinemaSchema> page = cinemaSchemaRepository.findAll(pageable);

		schemas = page.getContent();

		return schemas;
	}
	
	@Transactional
	public void deleteSchemaById(Integer id) {
		
		LOGGER.info("Delete schema by id = {} ", id);
		
		findSchemaById(id);
		
		/*try{*/
		cinemaSchemaRepository.delete(id);
		/*} catch(DataIntegrityViolationException ex){ // здесь я привязываюсь к dao и при изменении dao слоя надо будет исправлять код?
			throw new IntegrityViolationException("You can't delete entitity because it's links are present in DB");
		}*/
	}
	
	@Transactional
	public CinemaSchema createSchema(CinemaSchema cinemaSchema) {
		
		LOGGER.info("Create schema with name = {} ", cinemaSchema.getName());

		try {
			findSchemaByName(cinemaSchema.getName());
		} catch (MyResourceNotFoundException e) {
			return cinemaSchemaRepository.save(cinemaSchema);
		}
		throw new DuplicateEntityException(
				String.format("There is already cinema schema object exist with name= %s", cinemaSchema.getName()));
	}
	
	@Transactional
	public CinemaSchema updateSchema(CinemaSchema cinemaSchema) {
		
		LOGGER.info("Update schema with id = {} ", cinemaSchema.getId());
		
		return cinemaSchemaRepository.save(cinemaSchema);
	}
	
	@Transactional
	public void deleteAllSchemas() {
		
		LOGGER.info("Delete all schemas");
		
		cinemaSchemaRepository.deleteAll();
	}

}
