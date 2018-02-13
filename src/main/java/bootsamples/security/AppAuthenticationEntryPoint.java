package bootsamples.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AppAuthenticationEntryPoint extends BasicAuthenticationEntryPoint{

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		/*response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
		super.commence(request, response, authException);*/
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "");
		
		PrintWriter writer = response.getWriter();
		writer.print("HTTP Status 401 : " + authException.getMessage());
		
	}
		@Override
		public void afterPropertiesSet() throws Exception {
			setRealmName("My APP REALM");
		}
}
