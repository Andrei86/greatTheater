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

import bootsamples.model.CinemaSchema;
import bootsamples.service.SchemaService;
import bootsamples.dto.IdDTO;
import bootsamples.dto.SchemaDTO;
import bootsamples.exceptions.IntegrityViolationException;

@RestController
@RequestMapping("/schemas")
public class SchemaController {
	
	@Autowired
	SchemaService schemaService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getSchemaBy(@PageableDefault Pageable pageable,
			@RequestParam(required=false) String name){

		SchemaDTO schemaDTO = null;
		
		List<SchemaDTO> schemasDTO = new ArrayList<>();
		
		if(name != null){
			schemaDTO = entity2dto(schemaService.findSchemaByName(name));
			
			return new ResponseEntity<SchemaDTO>(schemaDTO, HttpStatus.OK);
		}
		else{
		List<CinemaSchema> schemas = schemaService.findAllSchemas(pageable);
		
		for(CinemaSchema schema :	schemas){
			
			schemaDTO = entity2dto(schema);
			
			schemasDTO.add(schemaDTO);
		}
				
		return new ResponseEntity<List<SchemaDTO>>(schemasDTO, HttpStatus.OK);
		}
		
	}
	
	@RequestMapping(value = "/{id}",method = RequestMethod.GET)
	public ResponseEntity<?> getSchemaById(@PathVariable("id") Integer id){
		
		CinemaSchema schema = schemaService.findSchemaById(id);
		
		SchemaDTO schemaDTO = entity2dto(schema);
		
		return new ResponseEntity<SchemaDTO>(schemaDTO, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteSchemaById(@PathVariable("id") Integer id){
		
		try
		{
		schemaService.deleteSchemaById(id);
		}catch(DataIntegrityViolationException ex){
			throw new IntegrityViolationException(String.format("You can't delete schema with id = %s because links to it are still present in DB", id));
		}
		
		return new ResponseEntity<CinemaSchema>(HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAllSchemas(){
		try {
			schemaService.deleteAllSchemas();
		} catch (DataIntegrityViolationException ex) {
			throw new IntegrityViolationException("You can't delete schemas because links to them are still present in DB");
		}
		return new ResponseEntity<CinemaSchema>(HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createSchema(@Valid @RequestBody SchemaDTO schemaDTO){
		
		CinemaSchema schema = dto2entity(schemaDTO);
		
		schemaService.createSchema(schema);
		
		return new ResponseEntity<IdDTO>(new IdDTO(schema.getId()), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateSchema(@PathVariable("id") Integer id, /*@Valid*/ @RequestBody SchemaDTO schemaDTO){
		
		CinemaSchema schema = schemaService.findSchemaById(id);
		
		//отрефакторить!
		
		schema.setName(schemaDTO.getName() != null ? 
							schemaDTO.getName() : schema.getName());
		schema.setRowsNumber(schemaDTO.getRowsNumber() != null ? 
										schemaDTO.getRowsNumber() : schema.getRowsNumber());
		schema.setPlacesNumber(schemaDTO.getPlacesNumber() != null ? 
				schemaDTO.getPlacesNumber() : schema.getPlacesNumber());
		
		schemaService.updateSchema(schema);
		
		return new ResponseEntity<IdDTO>(HttpStatus.OK);
	}
	
	private CinemaSchema dto2entity(SchemaDTO schemaDTO)
	{
		CinemaSchema cinemaSchema = new CinemaSchema();
		
		if(schemaDTO.getId() != null)
			cinemaSchema.setId(schemaDTO.getId());
		
		cinemaSchema.setName(schemaDTO.getName());
		cinemaSchema.setPlacesNumber(schemaDTO.getPlacesNumber());
		cinemaSchema.setRowsNumber(schemaDTO.getRowsNumber());
		
		return cinemaSchema;
	}

	private SchemaDTO entity2dto (CinemaSchema cinemaSchema)
	{
		SchemaDTO schemaDto = new SchemaDTO();
		schemaDto.setName(cinemaSchema.getName());
		schemaDto.setId(cinemaSchema.getId());
		schemaDto.setRowsNumber(cinemaSchema.getRowsNumber());
		schemaDto.setPlacesNumber(cinemaSchema.getPlacesNumber());

		return schemaDto;
	}
}
