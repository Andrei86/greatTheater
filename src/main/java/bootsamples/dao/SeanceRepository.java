package bootsamples.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import bootsamples.model.Seance;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Integer>{
	
	@Query("select s from Seance as s join s.movie as m where "
			+ " m.title = ?1 and s.date >= ?2 and s.date < ?3")
	List<Seance> findByMovieDate(String movieTitle, Date date1, Date date2); // i want to go for a certain movie in a specific date
	
	@Query("select s from Seance as s where s.date >= ?1 and s.date < ?2") // i want to go to the certain cinema in a specific date
	List<Seance> findByDate(Date date1, Date date2);
	
	@Query("select s from Seance as s join s.cinema as c where c.name = ?1 and s.date >= ?2 and s.date < ?3")
	List<Seance> findByCinemaDate(String cinemaName, Date date1, Date date2); // i want to go to the certain cinema in a specific date

}
