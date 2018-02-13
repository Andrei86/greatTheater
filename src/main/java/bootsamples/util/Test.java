package bootsamples.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {

	public static void main(String args[]){
		
		BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
		
		System.out.println(enc.encode("user"));
	}
}
