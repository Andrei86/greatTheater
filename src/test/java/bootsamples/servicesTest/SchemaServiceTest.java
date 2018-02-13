package bootsamples.servicesTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import bootsamples.exceptions.notFound.MyResourceNotFoundException;
import bootsamples.factories.SchemaFactory;
import bootsamples.model.CinemaSchema;
import bootsamples.service.SchemaService;


/**
 * @author Andrei Shalkevich
 *
 */
public class SchemaServiceTest extends AbstractServiceTest{
	
	@Autowired
	private EntityManager testEntityManager;
	
	@Autowired
	private SchemaService schemaService;
	
	public SchemaFactory schemaFactory = new SchemaFactory();
	public CinemaSchema schema1 = schemaFactory.newInstance();
	public CinemaSchema schema2 = schemaFactory.setName("testSchemaName2").newInstance();
	
	@Before
	public void before(){
		
		testEntityManager.persist(schema1);
		testEntityManager.persist(schema2);
		
	}
	
	@Test
	public void findSchemaByIdTest(){

		CinemaSchema schemaFromDB = schemaService.findSchemaById(schema1.getId());
		
		assertThat(schemaFromDB).isEqualTo(schema1);
	}
	
	@Test
	public void findSchemaByNameTest(){

		CinemaSchema schemaFromDB = schemaService.findSchemaByName(schema2.getName());

		assertThat(schemaFromDB).isEqualTo(schema2);
	}
	
	@Test
	public void findAllSchemasTest(){
		
		Pageable foundPage = new PageRequest(0, 3);
		List<CinemaSchema> schemaListFromDB = schemaService.findAllSchemas(foundPage);
		
		assertThat(schemaListFromDB).hasSize(2);
	}	
	
	@Test(expected=MyResourceNotFoundException.class)
	public void deleteSchemaByIdTest(){
		
		Integer schemaId = schema1.getId();
		schemaService.deleteSchemaById(schemaId);
		
		schemaService.findSchemaById(schemaId);
	}
	
	
	@Test
	public void createSchemaTest(){
		
		CinemaSchema newSchema = schemaFactory.setName("newTestSchema").newInstance();

		CinemaSchema createdSchema = schemaService.createSchema(newSchema);
		
		assertThat(createdSchema).isNotNull();
	}
	
	@Test
	public void updateSchemaTest(){
		
		CinemaSchema schemaFromDB = schemaService.findSchemaByName(schema1.getName());
		schemaFromDB.setPlacesNumber(1);
		
		CinemaSchema updatedSchema = schemaService.updateSchema(schemaFromDB);
		
		assertThat(updatedSchema.getPlacesNumber()).isEqualTo(1);
	} 
	 
}
