package bootsamples.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bootsamples.model.Cinema;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Integer> {
	
	//for user
	/*@Query("select * from cinema where is_active = true and city = ?1")
	List<Cinema> findByCity(Date startDate, Date endDate);*/
	
	List<Cinema> findByCity(String city);
	
	Cinema findByName(String name);
	
	//Page<Cinema> allCinemaList(Pageable pageable);
	

}
