package bootsamples.exceptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestDates {

	public TestDates() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception
	{

	DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:SS");
	
	DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");

	Date d1 = df2.parse("12-12-2017");
	
	Date d2 = df2.parse("12-12-2017");
	
	System.out.println(d1.equals(d2));
	}

}
