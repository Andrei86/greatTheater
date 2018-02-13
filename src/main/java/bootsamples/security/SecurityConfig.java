package bootsamples.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import bootsamples.additional.UserRole;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private AppAuthenticationEntryPoint appAuthenticationEntryPoint;
	
	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception{
		
		auth.inMemoryAuthentication().withUser("Hero").password("admin").roles("admin");
		auth.inMemoryAuthentication().withUser("Rest1134").password("user").roles("user");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests()
		
		.antMatchers("/schemas").hasRole(UserRole.admin.name())
		.antMatchers("/genres").hasRole(UserRole.admin.name()) // "/schemas**/**" is not working sometimes!!!!

		.antMatchers(HttpMethod.POST, "/cinemas").hasRole(UserRole.admin.name())
		.antMatchers(HttpMethod.PUT, "/cinemas").hasRole(UserRole.admin.name())
		.antMatchers(HttpMethod.DELETE, "/cinemas").hasRole(UserRole.admin.name())
		
		.antMatchers(HttpMethod.POST, "/seances").hasRole(UserRole.admin.name())
		.antMatchers(HttpMethod.PUT, "/seances").hasRole(UserRole.admin.name())
		.antMatchers(HttpMethod.DELETE, "/seances").hasRole(UserRole.admin.name())
		
		.antMatchers(HttpMethod.POST, "/movies").hasRole(UserRole.admin.name())
		.antMatchers(HttpMethod.PUT, "/movies").hasRole(UserRole.admin.name())
		.antMatchers(HttpMethod.DELETE, "/movies").hasRole(UserRole.admin.name())
		
		.antMatchers(HttpMethod.POST, "/places").hasRole(UserRole.admin.name())
		.antMatchers(HttpMethod.PUT, "/places").hasRole(UserRole.admin.name())
		.antMatchers(HttpMethod.DELETE, "/places").hasRole(UserRole.admin.name())
		
		.antMatchers(HttpMethod.POST, "/seances").hasRole(UserRole.admin.name())
		.antMatchers(HttpMethod.PUT, "/seances").hasRole(UserRole.admin.name())
		.antMatchers(HttpMethod.DELETE, "/seances").hasRole(UserRole.admin.name())
		
		.antMatchers(HttpMethod.PUT, "/customers").hasAnyRole(UserRole.admin.name(), UserRole.user.name())
		.antMatchers(HttpMethod.DELETE, "/customers").hasRole(UserRole.admin.name())
		.antMatchers(HttpMethod.GET, "/customers").hasRole(UserRole.admin.name())
		.antMatchers(HttpMethod.GET, "/customers/**").hasAnyRole(UserRole.admin.name(), UserRole.user.name())
		
		.antMatchers(HttpMethod.POST, "/customers").permitAll()
		//.antMatchers(HttpMethod.GET, "/**").permitAll()
		.anyRequest().authenticated()
		.and().httpBasic().realmName("My APP REALM")
		.authenticationEntryPoint(appAuthenticationEntryPoint)
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	}
	
	@Bean
	public AppAuthenticationEntryPoint getBasicAuthEntryPoint(){
		return new AppAuthenticationEntryPoint();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder);
	}
}
