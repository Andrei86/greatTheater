package bootsamples.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bootsamples.additional.Status;
import bootsamples.additional.WarningMessage;
import bootsamples.dao.BookingRepository;
import bootsamples.exceptions.MyBuisnessException;
import bootsamples.model.Booking;
import bootsamples.model.Place;

//@Service
@Transactional
public class BookingService {

	/*private final BookingRepository bookingRepository;
	
	private final PlaceService placeService;
	
	@Autowired
	 public BookingService(BookingRepository bookingRepository, PlaceService placeService) {
		this.bookingRepository = bookingRepository;
		this.placeService = placeService;
	}*/

	/*public List<Booking> findBookingsByCustomerId(Integer id)
	{
		List<Booking> bookings = bookingRepository.findByCustomerId(id);
		
		return bookings; // все мои брони
	}
	
	public Booking createOrUpdateBooking(Booking booking) throws WarningMessage // нужно заказывать билеты на будущие сеансы
	{
		List<Place> places = booking.getPlaces();
		
		Date now = new Date();
		
		Date seanceDate;
		
		for(Place plac : places)
		{
			seanceDate = plac.getSeance().getDate();
			if(now.after(seanceDate))
				throw new WarningMessage("Booking error for expired seance date");
			else if(plac.getStatus().equals(Status.booked))
				throw new WarningMessage("You can not to book already booked places");
			
			placeService.setBookedStatus(plac.getId());

		}
		
		booking.setPlaces(places);
		booking.setBookingDate(new Timestamp(new Date().getTime()));
		return bookingRepository.save(booking);

	}
	
	public Booking findBookingById(Integer id) // нужно заказывать билеты на будущие сеансы
	{
		return bookingRepository.findOne(id);

	}
	
	public void deleteBookingById(Integer id)
	{
		bookingRepository.delete(id);
		
		Booking booking = bookingRepository.findOne(id);
		
		List<Place> places = booking.getPlaces();
		
		for(Place plac : places)
			placeService.setFreeStatus(plac.getId());

	}*/
}
