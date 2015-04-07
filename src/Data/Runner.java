package Data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class Runner implements Comparable<Runner> {

	private int id;
	private String CardID;
	private String Name;
	private String Email;
	private Date start;
	private Date end;
	private String time;
	
    public static class OrderByTime implements Comparator<Runner> {

        @Override
        public int compare(Runner o1, Runner o2) {
            return o1.time.compareTo(o2.time);
        }
    }
	
	public Runner() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCardID() {
		return CardID;
	}

	public void setCardID(String cardID) {
		CardID = cardID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(String start) {
		if(start!=null){
		DateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
		Date date;
		try {
			date = format.parse(start);
			this.start = date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		}
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(String end) {
		if(end!=null){
		DateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
		Date date;
		try {
			date = format.parse(end);
			this.end = date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		}
	}

	public String getTime() {
		SimpleDateFormat sdfDate = new SimpleDateFormat(
				"mm:ss.SSS");
		if(time==null && end != null && start != null){
			time = sdfDate.format(new Date(end.getTime() - start.getTime()));
		}
		return time;
	}
	

	public void setTime(String time) {
			this.time = time;
	}

	@Override
	public int compareTo(Runner o) {
		// TODO Auto-generated method stub
		return 0;
	}


	
}
