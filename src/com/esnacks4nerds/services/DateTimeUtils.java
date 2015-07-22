package com.esnacks4nerds.services;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTimeUtils {

	public static long getCoolieExpiryTime(){
        Calendar calendar = getCalendarForNow();
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setTimeToEndofDay(calendar);
       // Date end = calendar.getTime();

		
		Calendar now = Calendar.getInstance();
		
		
		
		long diffTime = (calendar.getTimeInMillis() - now.getTimeInMillis())/1000;
		System.out.println("The difference  "+ diffTime);
		
		return diffTime;
	}
	
	private static void setTimeToEndofDay(Calendar calendar) {
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    calendar.set(Calendar.MILLISECOND, 999);
	}
	private static Calendar getCalendarForNow() {
	    Calendar calendar = GregorianCalendar.getInstance();
	    calendar.setTime(new Date());
	    return calendar;
	}

	
	public static void  main(String[] args){
		System.out.println(getCoolieExpiryTime());
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		try{
			date = dateFormat.parse("23/07/2015");
		
		long time = date.getTime();
		Timestamp ts1 = new Timestamp(time);
		java.util.Date now= new java.util.Date();
		Timestamp tsNow = new Timestamp(now.getTime());
		System.out.println(isValidDate(ts1));
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static boolean isValidDate(Timestamp ts){
		if ( ts == null ){
			return false;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis( ts.getTime() );
		System.out.println(cal.get(Calendar.MONTH) + " " + cal.get(Calendar.YEAR));

		Calendar today = Calendar.getInstance();
		System.out.println(today.get(Calendar.MONTH) + " " + today.get(Calendar.YEAR));

		return (cal.get(Calendar.MONTH) == today.get(Calendar.MONTH)) && (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR));
	}
	
}
