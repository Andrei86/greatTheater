package bootsamples.adds;

import java.util.Calendar;
import java.util.Date;

import lombok.Data;


/**
 * @author Andrei Shalkevich
 * 
 * for set to 0 hours, minutes, seconds and milliseconds of date
 *
 */
@Data
public class ZeroedDateTestPerformer {
	
	private Date date;
	
	public Date zeroedDate() {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	public Date addOneDayToDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}
	
	public ZeroedDateTestPerformer(Date date) {
		super();
		this.date = date;
	}

}
