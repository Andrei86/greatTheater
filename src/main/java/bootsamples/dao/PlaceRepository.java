package bootsamples.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bootsamples.model.Place;

@Repository
public interface PlaceRepository extends CrudRepository<Place, Integer>{
	
	/*@Query("select p from Place as p join p.category_cost as c where "
			+ " p.seance_id = ?1")*/
	List<Place> findBySeanceId(Integer id);
	
	/*@Query("select p from Place as p join p.category_cost as c where "
			+ " p.customer_id = ?1")*/
	List<Place> findByCustomerId(Integer id); // add after union of place and customer
	
	/*@Query("select p from Place as p join category_cost as c where "
			+ "p.seance_id = ?1 AND p.customer_id = ?2")*/
	List<Place> findBySeanceAndCustomerId(Integer seanceId, Integer customerId); // для ограничения 
	//заказа юзером 6 мест на один сеанс

}
