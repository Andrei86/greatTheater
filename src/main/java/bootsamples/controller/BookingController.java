package bootsamples.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bootsamples.additional.WarningMessage;
import bootsamples.dto.CinemaDTO;
import bootsamples.dto.GetBookingDTO;
import bootsamples.dto.IdDTO;
import bootsamples.dto.PlaceDTO;
import bootsamples.dto.PostBookingDTO;
import bootsamples.exceptions.MyException;
import bootsamples.model.Booking;
import bootsamples.model.Cinema;
import bootsamples.model.Place;
import bootsamples.model.Seance;
import bootsamples.service.BookingService;
import bootsamples.service.CustomerService;
import bootsamples.service.PlaceService;
import bootsamples.service.SeanceService;

//@RestController
//@RequestMapping("/bookings")
public class BookingController {
	
	/*@Autowired
	BookingService bookingService;*/
	
	@Autowired
	SeanceService seanceService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	PlaceService placeService;
	
	public BookingController() {
		// TODO Auto-generated constructor stub
	}
	
	/*@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getBookingById(@PathVariable("id") Integer id) {
		Booking booking = bookingService.findBookingById(id);

		GetBookingDTO getBookingDTO = entity2dto(booking);

		return new ResponseEntity<GetBookingDTO>(getBookingDTO, HttpStatus.OK);
	}
	 
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getBookingsByCustomerId(@RequestParam(required = true) Integer customerId) {
		List<GetBookingDTO> getBookingsDTO = new ArrayList<>();

		List<Booking> bookings = bookingService.findBookingsByCustomerId(customerId);

		for (Booking booking : bookings)
			getBookingsDTO.add(entity2dto(booking));

		return new ResponseEntity<List<GetBookingDTO>>(getBookingsDTO, HttpStatus.OK);
	}
	 
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteBookingById(@PathVariable("id") Integer id) {

			bookingService.deleteBookingById(id);

		return new ResponseEntity<GetBookingDTO>(HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createBooking(@RequestBody PostBookingDTO postBookingDTO) {

		Booking booking = dto2entity(postBookingDTO);

		try {
			bookingService.createOrUpdateBooking(booking);
		} catch (WarningMessage e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
			
		}

		return new ResponseEntity<IdDTO>(new IdDTO(booking.getId()), HttpStatus.CREATED);

	}
	
	private Booking dto2entity(PostBookingDTO postBookingDTO) {
		List<Place> places = new ArrayList<>();

		Booking booking = new Booking();

		booking.setCustomer(customerService.findCustomerById(postBookingDTO.getCustomerId()));

		for (Integer placeId : postBookingDTO.getPlaceIds())
			places.add(placeService.findPlaceById(placeId));
		
		booking.setPlaces(places);

		return booking;
	}
	
	private PlaceDTO entity2dto(Place place) {

		Seance seance = seanceService.findSeanceById(place.getSeance().getId());

		PlaceDTO placeDTO = new PlaceDTO();

		placeDTO.setId(place.getId());
		placeDTO.setSeanceId(seance.getId());
		placeDTO.setCinema(seance.getCinema().getName());
		placeDTO.setMovie(seance.getMovie().getTitle());
		placeDTO.setSeanceDate(seance.getDate().toString().substring(0, 16)); // проверить
																				// как
																				// будет
																				// выглядеть
																				// эта
																				// дата
		placeDTO.setRow(place.getRow());
		placeDTO.setPlace(place.getPlace());
		placeDTO.setStatus(place.getStatus().name());
		placeDTO.setCategory(place.getCategory().name());
		placeDTO.setCost(place.getCost());

		return placeDTO;
	}

	private GetBookingDTO entity2dto(Booking booking) {
		List<PlaceDTO> placesDTO = new ArrayList<>();

		GetBookingDTO getBookingDTO = new GetBookingDTO();

		getBookingDTO.setId(booking.getId());
		getBookingDTO.setCustomerLogin(booking.getCustomer().getLogin());
		for (Place place : booking.getPlaces())
			placesDTO.add(entity2dto(place));

		getBookingDTO.setPlaceDTO(placesDTO);

		getBookingDTO.setBookingDate(booking.getBookingDate().toString());

		return getBookingDTO;
	}
    */
    

}
