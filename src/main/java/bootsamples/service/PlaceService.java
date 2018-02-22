package bootsamples.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import bootsamples.additional.Status;
import bootsamples.additional.UserRole;
import bootsamples.dao.PlaceRepository;
import bootsamples.exceptions.InappropirateStatusException;
import bootsamples.exceptions.InsufficientRightsException;
import bootsamples.exceptions.LatenessCancelException;
import bootsamples.exceptions.notFound.MyResourceNotFoundException;
import bootsamples.model.Customer;
import bootsamples.model.Place;


/**
 * @author Andrei Shalkevich
 *
 */
@Service
public class PlaceService {
	
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(PlaceService.class);

	private final PlaceRepository placeRepository;
	
	private final CustomerService customerService;
	
	private final SeanceService seanceService;
	
	private static final long TIME_LIMIT = 3600000; // 1 hour

	public PlaceService(PlaceRepository placeRepository, CustomerService customerService, 
			SeanceService seanceService) {
		this.placeRepository = placeRepository;
		this.customerService =customerService;
		this.seanceService = seanceService;
		
	}
	
	public Place findPlaceById(Integer id)
	{
		LOGGER.info("Find place by id = {} ", id);
		
		Place place = placeRepository.findOne(id);
		
		if (place == null) {
			throw new MyResourceNotFoundException(String.format("place with id= %s not found", id));
		}
		
		return place;
	}
	
	//объединить поиск, попробовав @Query Т.К. МНОГО ПОВТОРЯЮЩЕГОСЯ КОДА!!!!! еще попробовать searchFilter
	public List<Place> findPlacesBySeanceId(Integer id){
		
		LOGGER.info("Find places by seance's id = {} ", id);
		
		List<Place> places = placeRepository.findBySeanceId(id);
		
		if(places.isEmpty())
			throw new MyResourceNotFoundException(String.format("no seance with id= %s", id));
		
		return places;
		
	}
	
	// переделаю под findByCustomerLogin, чтобы в url был не id, а login?
	public List<Place> findPlacesByCustomerId(Integer id){
		
		LOGGER.info("Find places by customer's id = {} ", id);
		
		customerService.findCustomerById(id);
		List<Place> places = placeRepository.findByCustomerId(id);
		
		if(places.isEmpty())
			throw new MyResourceNotFoundException(String.format("no booked places from the customer with id= %s", id));
		
		return places;
		
	}
	
	/*public List<Place> findPlacesBySeanceAndCustomerId(Integer seanceId, Integer customerId){
		
		List<Place> placesDB = placeRepository.findBySeanceAndCustomerId(seanceId, customerId);
		
		List<Place> places = new ArrayList<Place>();
		
		if (places.isEmpty())
			throw new MyResourceNotFoundException(String.format(
					"no booked places at seance with id=%s from the customer with id= %s", seanceId, customerId));
		else {
			for (Place place : placesDB) {
				places.add(place);
			}
		}

		return places;
	}*/
	
	
	/*
	 * В кинотеатре деньги за купленные билеты возвращаются, поэтому админ мож менять статус на free в люб время
	 * 
	 * Бронирование заканчивается за 1 час до начала сеанса для любого зарегистрированного пользователя
	 */
	@Transactional
	public Place changeStatusToBooked(Integer placeId)//, Integer customerId)
	{
		LOGGER.info("Change status to Booked to place with id = {} ", placeId);
		
		Place place = findPlaceById(placeId);
		
		Date now = new Date();
		
		Date seanceDate = place.getSeance().getDate();
		
		if(seanceDate.getTime() - now.getTime() < TIME_LIMIT)
			throw new LatenessCancelException("You already can't book place because less than one hour left before the seance");
		
		if(place.getStatus() == Status.free)
		place.setStatus(Status.booked); // если статус booked то 
		else
			throw new InappropirateStatusException(String.format("you can't book places with status= %s", place.getStatus().name()));
		
		//place.setCustomer(customerService.findCustomerById(customerId));
		
		return place;
	}
	
	/*
	 * Любой зарегистрированный пользователь с правами USER может отменять СВОЙ забронированный билет мин за 1 час до нач сеанса,
	 * а пользователь администратор может освобождать ЛЮБОЙ забронированный билет в любое время
	 */
	@Transactional
	public Place changeStatusToFree(Integer placeId, Integer customerId) // может админ отменять в любое время
	{
		LOGGER.info("Change status to Free to place with id = {} by customer with id = {}", placeId, customerId);
		
		Place place = findPlaceById(placeId);
		
		Customer loggedInCustomer = customerService.findCustomerById(customerId);
		
		Customer customerFromPlace = place.getCustomer(); // получаю кастомера из билета и он мож быть null
		
		
		if(loggedInCustomer.getUserRole().equals(UserRole.admin)){ // возврат или отмена брони билета
			
			place.setStatus(Status.free);
			place.setCustomer(null);
		}
		else if(loggedInCustomer.equals(customerFromPlace)){// || customer.getUserRole().equals(UserRole.admin.name())){ // это уже креденшиалс!!!!!!
			if (place.getStatus() != Status.busy) { // еще кассир не отменил бронь
				place.setStatus(Status.free);
				place.setCustomer(null);
			}
			else	
				throw new InsufficientRightsException("not enough rights for this action");
		}
		// тут можно выкинуть сообщение о невозможности отмены брони не своего места
		else	
			throw new InsufficientRightsException("you can't to set FREE status to foreign customer's place");
		
		return place;
	}
	
	/*
	 * Только админ (кассир) может продавать билеты и соотв менять статус на busy (c free or booked status) в кассе
	 */
	@Transactional
	 public Place changeStatusToBusy(Integer placeId, Integer customerId) // может админ отменять в любое время
	{
		LOGGER.info("Change status to Busy to place with id = {} by customer with id = {}", placeId, customerId);
		 
		Place place = findPlaceById(placeId);
		
		Status placeStatus = place.getStatus();
		 
		Customer customer = customerService.findCustomerById(customerId);
		
		if(customer.getUserRole().equals(UserRole.admin)){	
			if(placeStatus.equals(Status.free)){ // у места customer_id = null
			place.setStatus(Status.busy);
			place.setCustomer(customer);
			}else
				place.setStatus(Status.busy); // у места customer_id != null
			
		}
		else
			throw new InsufficientRightsException("not enough rights for this action");
		
		return place;
	} 
	
	@Transactional
	public void deletePlaceById(Integer id)
	{
		
		
		findPlaceById(id);
		placeRepository.delete(id);
	}
	
	@Transactional
	public Place createPlace(Place place)
	{
		seanceService.findSeanceById(place.getSeance().getId());
		return placeRepository.save(place);
	}
	
	@Transactional
	public Place updatePlace(Place place)
	{
		return placeRepository.save(place);
	}
	
	@Transactional
	public List<Place> updatePlaces(List<Place> places) // ordinary places updating
	{
		return (List<Place>) placeRepository.save(places);

	}
	
	@Transactional
	public Place bookingPlace(Integer placeId, Integer customerId) // single reservation
	{
		Customer customer = customerService.findCustomerById(customerId); // это типа аутентификация!!! будет
																			// проверяться в хедере
		Place place = changeStatusToBooked(placeId);// , customerId);
		place.setCustomer(customer);

		return updatePlace(place);

	}
	
	@Transactional
	public List<Place> bookingPlaces(List<Integer> placeIds, Integer customerId) // reservation
	{
		Customer customer = customerService.findCustomerById(customerId);

		List<Place> bookedPlaces = new ArrayList<>();
		Place place = null;
		for (Integer i : placeIds) {

			place = changeStatusToBooked(i);// , customerId);
			place.setCustomer(customer);
			bookedPlaces.add(place);

		}

		return updatePlaces(bookedPlaces);

	}
	
	@Transactional
	public Place cancellingPlace(Integer placeId, Integer customerId) {

		Place place = changeStatusToFree(placeId, customerId);

		return updatePlace(place);

	}
	
	@Transactional
	public List<Place> cancellingPlaces(List<Integer> placeIds, Integer customerId) // cancellations?
	{

		List<Place> cancelPlaces = new ArrayList<>();

		Place place = null;
		for (Integer i : placeIds) {

			place = changeStatusToFree(i, customerId);
			cancelPlaces.add(place);

		}

		return updatePlaces(cancelPlaces);

	}
	
	@Transactional
	public Place busyPlace(Integer placeId, Integer customerId) {

		Place place = changeStatusToBusy(placeId, customerId);

		return updatePlace(place);

	}
	 
	@Transactional 
	public List<Place> busyPlaces(List<Integer> placeIds, Integer customerId) {
		List<Place> busyPlaces = new ArrayList<>();

		Place place = null;
		for (Integer i : placeIds) {

			place = changeStatusToBusy(i, customerId);
			busyPlaces.add(place);

		}

		return updatePlaces(busyPlaces);

	}

}
