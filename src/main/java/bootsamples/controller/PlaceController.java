package bootsamples.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import bootsamples.additional.Category;
import bootsamples.additional.Status;
import bootsamples.dto.IdDTO;
import bootsamples.dto.PlaceDTO;
import bootsamples.exceptions.notFound.MyResourceNotFoundException;
import bootsamples.model.Place;
import bootsamples.model.Seance;
import bootsamples.service.CategoryCostService;
import bootsamples.service.PlaceService;
import bootsamples.service.SeanceService;

@RestController
@RequestMapping("/places")
public class PlaceController {
	
	@Autowired
	SeanceService seanceService;

	@Autowired
	PlaceService placeService;
	
	@Autowired
	CategoryCostService categoryCostService;
	
	//getPlacesBySeanceId надо PageAble применять и если не указано параметров в поиске надо пытаться 
	//из хедера получать юзера, хотя можно вроде обойтись
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getPlacesBy(@PageableDefault Pageable pageable, @RequestParam(required=false) Integer seanceId, 
			@RequestParam(required=false) Integer customerId)
	{
		List<Place> places = null;
		
		/*if(seanceId != null && customerId != null)
			places = placeService.findPlacesBySeanceAndCustomerId(seanceId, customerId);
		else*/ if(seanceId != null)
			places = placeService.findPlacesBySeanceId(seanceId);
		/*else if(customerId != null)
			places = placeService.findPlacesByCustomerId(customerId);*/
		else
			return new ResponseEntity<String>("You must specify param seanceId", HttpStatus.BAD_REQUEST);
		
		List<PlaceDTO> placesDTO = new ArrayList<>();
		
		if (places.isEmpty()) {
			throw new MyResourceNotFoundException("places not found");
		}
		
		for(Place place : places)
		placesDTO.add(entity2dto(place));
		
		// !! Для аутентификации/авторизации модифицируем код
		// For headers data 
		/*Map<String, String> map = new HashMap<String, String>();
		
		Enumeration headerNames = request.getHeaderNames();
		
		while (headerNames.hasMoreElements())
		{
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		
		System.out.println(map.get("login") + " " + map.get("password"));*/
		
		return new ResponseEntity<List<PlaceDTO>>(placesDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/customer/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getPlacesByCustomerId(@PathVariable("id") Integer id){
		
		List<Place> places = null;
		places = placeService.findPlacesByCustomerId(id);
		List<PlaceDTO> placesDTO = new ArrayList<>();
		
		for(Place place : places)
			placesDTO.add(entity2dto(place));
		
		return new ResponseEntity<List<PlaceDTO>>(placesDTO, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getPlaceById(@PathVariable("id") Integer id) // combine with getPlaceBy()
	{
		Place place = placeService.findPlaceById(id);
		
		PlaceDTO placeDTO = entity2dto(place);
		
		return new ResponseEntity<PlaceDTO>(placeDTO, HttpStatus.OK);
	}

	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createPlace(@Valid @RequestBody PlaceDTO placeDTO)
	{
		seanceService.findSeanceById(placeDTO.getSeanceId());
		
		Place place = dto2entity(placeDTO);
		
		placeService.createPlace(place);
		
		return new ResponseEntity<IdDTO>(new IdDTO(place.getId()), HttpStatus.CREATED);
		
	}

	
	
	/*@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT) // not necessary for place
	public ResponseEntity<?> placeBooking(@PathVariable("id") Integer placeId, 
	@RequestParam(required = true) Integer customerId) // вытягиваем кастомера из боди, а пока так
	{
		Place place = placeService.bookingPlace(placeId, customerId);
		PlaceDTO placeDTO = entity2dto(place);
		
		return new ResponseEntity<PlaceDTO>(placeDTO, HttpStatus.OK);
	}*/
	
	
	// эти методы использоваться не будут в контроллере, т.к. все будет под капотом !!!!!!!!
	
	
	@RequestMapping(value = "/booking/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> placeBooking(@PathVariable("id") Integer placeId, 
			@RequestParam(required=true) Integer customerId)  // вытягиваем кастомера из боди, а пока так
	{
		Place place = placeService.bookingPlace(placeId, customerId);
		
		PlaceDTO placeDTO = entity2dto(place);
		
		return new ResponseEntity<PlaceDTO>(placeDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/booking", method = RequestMethod.PUT)
	public ResponseEntity<?> placesBooking(@RequestParam(required=true) List<Integer> placeIds, 
			@RequestParam(required=true) Integer customerId)  // вытягиваем кастомера из боди, а пока так
	{
		List<Place> places = placeService.bookingPlaces(placeIds, customerId);
		
		List<PlaceDTO> placesDTO = new ArrayList<PlaceDTO>();
		
		for(Place place : places)
		placesDTO.add(entity2dto(place));
		
		return new ResponseEntity<List<PlaceDTO>>(placesDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> cancelPlaceBooking(@PathVariable("id") Integer placeId, 
			@RequestParam(required=true) Integer customerId)
	{
		Place place = placeService.cancellingPlace(placeId, customerId);
		
		PlaceDTO placeDTO = entity2dto(place);
		
		return new ResponseEntity<PlaceDTO>(placeDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/cancel", method = RequestMethod.PUT)
	public ResponseEntity<?> cancelPlacesBooking(@RequestParam(required=true) List<Integer> placeIds, 
			@RequestParam(required=true) Integer customerId)
	{
		List<Place> places = placeService.cancellingPlaces(placeIds, customerId);;
		
		List<PlaceDTO> placesDTO = new ArrayList<PlaceDTO>();
		
		for(Place place : places)
		placesDTO.add(entity2dto(place));
		
		return new ResponseEntity<List<PlaceDTO>>(placesDTO, HttpStatus.OK);
	}
	
	/*@RequestMapping(value = "/booking/cancel/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> placeCancelling(@PathVariable("id") Integer placeId, 
	@RequestParam(required = true) Integer customerId) // вытягиваем кастомера из боди, а пока так
	{
		placeService.cancellingPlace(placeId, customerId);
		//PlaceDTO placeDTO = entity2dto(place);
		
		return new ResponseEntity<PlaceDTO>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/bookings/cancel", method = RequestMethod.PUT)
	public ResponseEntity<?> placesCanselling(@RequestBody BPlaceDTO bPlaceDTO)  // вытягиваем кастомера из боди, а пока так
	{
		
		List<Integer> placeIds = bPlaceDTO.getPlacesId();
		Integer customerId = bPlaceDTO.getCustomerId();
		// если в хедере правильные данные отмену разрешаем, нет в противном случае
		placeService.cancellingPlaces(placeIds, customerId);
		
		return new ResponseEntity<List<PlaceDTO>>(HttpStatus.OK);
	}*/
	
	
	@RequestMapping(value = "/busy/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> busyPlaceSet(@PathVariable("id") Integer id, 
			@RequestParam(required=true) Integer customerId)
	{
		Place place = placeService.busyPlace(id, customerId);
		
		PlaceDTO placeDTO = entity2dto(place);
		
		return new ResponseEntity<PlaceDTO>(placeDTO, HttpStatus.OK);
	}
	 
	@RequestMapping(value = "/busy", method = RequestMethod.PUT)
	public ResponseEntity<?> busyPlacesSet(@RequestParam(required=true) List<Integer> id, 
			@RequestParam(required=true) Integer customerId){
		
		List<Place> places = placeService.busyPlaces(id, customerId);
		
		List<PlaceDTO> placesDTO = new ArrayList<PlaceDTO>();
		
		for(Place place : places)
		placesDTO.add(entity2dto(place));
		
		return new ResponseEntity<List<PlaceDTO>>(placesDTO, HttpStatus.OK);
	} 
	
	@RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
	public ResponseEntity<?> deletePlaceById(@PathVariable("id") Integer id){
			
		placeService.deletePlaceById(id);
		
		return new ResponseEntity<Place>(HttpStatus.OK);
	}
	
	
	
	private Place dto2entity(PlaceDTO placeDTO) {
		
		Seance seance = seanceService.findSeanceById(placeDTO.getSeanceId());
		Place place = new Place();
		if (placeDTO.getId() != null)
			place.setId(placeDTO.getId());
		
		place.setSeance(seance);
		place.setRow(placeDTO.getRow());
		place.setPlace(placeDTO.getPlace());
		place.setStatus(Status.valueOf(placeDTO.getStatus()));
		place.setCategoryCost(categoryCostService.
				findCategoryCostByCategory(Category.valueOf(placeDTO.getCategory())));

		return place;
	}	

	private PlaceDTO entity2dto(Place place) {
		
		Seance seance = seanceService.findSeanceById(place.getSeance().getId());
		
		PlaceDTO placeDTO = new PlaceDTO();
		
		placeDTO.setId(place.getId());
		placeDTO.setSeanceId(seance.getId());
		placeDTO.setCinema(seance.getCinema().getName());
		placeDTO.setMovie(seance.getMovie().getTitle());
		placeDTO.setSeanceDate(seance.getDate().toString().substring(0, 16)); // проверить как будет выглядеть эта дата
		placeDTO.setRow(place.getRow());
		placeDTO.setPlace(place.getPlace());
		placeDTO.setStatus(place.getStatus().name());
		placeDTO.setCategory(place.getCategoryCost().getCategory().name());
		placeDTO.setCost(place.getCategoryCost().getCost());
		
		return placeDTO;
	}

}
