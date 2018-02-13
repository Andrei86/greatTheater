package bootsamples.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bootsamples.model.CinemaSchema;

@Repository
public interface CinemaSchemaRepository extends JpaRepository<CinemaSchema, Integer>{
	
	CinemaSchema findByName(String name);

}
