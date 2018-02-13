package bootsamples.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import bootsamples.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer>{
	
	Movie findByTitle(String title);
	
	@Query("select m from Movie m where start_rental_date <= ?1 and end_rental_date >= ?1")
	List<Movie> findByDate(Date date);

}
