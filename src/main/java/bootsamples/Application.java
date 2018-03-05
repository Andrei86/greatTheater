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
	
	/*@Bean
	CacheManager cacheManager() {
	   return new RedisCacheManager(redisTemplate());*/
	
	/*@Bean
    CacheManager cacheManager() {
      return new ConcurrentMapCacheManager();
    }*/
	
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
	        
	  }
}
