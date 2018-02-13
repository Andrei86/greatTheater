package bootsamples.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import bootsamples.model.Booking;

//@Repository
public interface BookingRepository { //extends CrudRepository<Booking, Integer> {
	
	//List<Booking> findByCustomerId(Integer id);

}
