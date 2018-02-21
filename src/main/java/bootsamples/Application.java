package bootsamples;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.core.sym.Name;

import bootsamples.dao.CinemaRepository;
import bootsamples.dao.PlaceRepository;
import bootsamples.dao.SeanceRepository;
import bootsamples.model.Booking;
import bootsamples.model.Cinema;
import bootsamples.model.Customer;
import bootsamples.model.Movie;
import bootsamples.model.Place;
import bootsamples.model.Seance;
import bootsamples.security.UserDetailsServiceImpl;
import bootsamples.service.BookingService;
import bootsamples.service.CustomerService;
import bootsamples.service.MovieService;
import bootsamples.service.PlaceService;
import bootsamples.service.SchemaService;
import bootsamples.service.SeanceService;

@SpringBootApplication
@EnableCaching
public class Application implements CommandLineRunner {
	
	@Autowired
    private ApplicationContext appContext;
	
	@Bean
    CacheManager cacheManager() {
      return new ConcurrentMapCacheManager();
    }
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		
		SpringApplication.run(Application.class, args);
		
		
	}
	
	  @Override
	    public void run(String... args) throws Exception {

	        String[] beans = appContext.getBeanDefinitionNames();
	        Arrays.sort(beans);
	        for (String bean : beans) {
	            System.out.println(bean);
	        }
	        
	        /*List<Integer> pl = new ArrayList<>();
	        pl.add(8);
	        pl.add(9);*/
	        
	       /* Integer plac = 8;
	        
	        PlaceService placeService = (PlaceService) appContext.getBean("placeService");
	        
	        //placeService.busyPlaces(pl, 13);
	        
	        System.out.println(placeService.cancellingPlace(plac, 2));*/
	        
	        
	        /*UserDetailsServiceImpl service = (UserDetailsServiceImpl) appContext.getBean("userDetailsServiceImpl");
	        
	        User user =  (User) service.loadUserByUsername("Hero");*/
	        
	      /* DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	        Date dat1 = df.parse("10-12-2017");
	        Date dat2 = df.parse("11-12-2017");
	        
	        String name = "Saturn";
	        //SeanceRepository repo = (SeanceRepository) appContext.getBean("seanceRepository");*/
	        /*SchemaService repo = (SchemaService) appContext.getBean("schemaService");
	        //Seance seance = repo.findSeanceById(262);
	        
	        System.out.println(repo.findSchemaById(4));*/
	        
	        //final Pattern patt = Pattern.compile("\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}");
	        
	        
	        //System.out.println(patt.toString());
	        
	        /*PlaceService servP = (PlaceService) appContext.getBean("placeService");
	        
	        System.out.println(servP.findPlacesByCustomerId(13));*/
	        
	        /*List<Integer> l = new ArrayList<>();
	        l.add(2);
	        l.add(3);
	        
	        servP.cancellingPlaces(l, 1);*/
	        //servP.cancellingPlace(2);
	        //serv.setBookedStatus(2);
	        //System.out.println(place);
	        
	        //List<Place> places = serv.findPlacesBySeanceId(262);
	        
	        //System.out.println(place.getId() + " and movie " + place.getSeance().getMovie().getTitle());
	        
	        /*Booking booking = new Booking();
	        booking.setBookingDate(new Timestamp(new Date().getTime()));
	        
	        booking.setCustomer(((CustomerService) appContext.getBean("customerService")).findCustomerById(1));
	        
	        BookingService bServ = (BookingService) appContext.getBean("bookingService");*/
	        //Integer id = bServ.createOrUpdateBooking(booking).getId();
	        
	        //Booking settedBooking = bServ.findBookingById(id);
	     /*   List<Place> places = new ArrayList<>();
		       places.add(((PlaceService) appContext.getBean("placeService")).findPlaceById(2));
		       
		       booking.setPlaces(places);*/
		       
		       //System.out.println(booking);
		       
		        //settedBooking.setPlaces(places);
		        
		        //bServ.createOrUpdateBooking(booking);
	        
	        
	        //System.out.println(booking.getId() + " " + booking.getPlaces() + " " + booking.getCustomer());
	        
	        /*MovieService mServ = (MovieService) appContext.getBean("movieService");
	        Movie movie = mServ.findMovieById(343);
	        
	        
	        System.out.println(movie);*/
	        
	        /*CinemaRepository repo = (CinemaRepository) appContext.getBean("cinemaRepository");
	        Cinema cinema = repo.findOne(1);
	        
	        System.out.println(cinema.getName());*/
	  }
}
