import java.text.SimpleDateFormat;


public class TXTEvent {
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
	private String ServiceID="";
	private String Name="";
	private String EngName="";
	private String StartDate="";
	private String EndDate="";
	public String getEndDate() {
		return EndDate;
	}
	public void setEndDate(String endDate) {
		EndDate = endDate;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		name=name.replaceAll("\n", " ").replaceAll("\r", " ");

		Name = name;
	}
	public String getStartDate() {
		return StartDate;
	}
	public void setStartDate(String startDate) {
		StartDate = startDate;
	}
	public String getServiceID() {
		return ServiceID;
	}
	public void setServiceID(String serviceID) {
		ServiceID = serviceID;
	}
	public String getEngName() {
		return EngName;
	}
	public void setEngName(String engName) {
		EngName = engName;
	}
}
