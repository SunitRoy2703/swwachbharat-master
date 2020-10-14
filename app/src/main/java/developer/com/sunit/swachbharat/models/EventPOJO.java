package developer.com.sunit.swachbharat.models;

public class EventPOJO {
   private String name,dpUrl,location,description,timeStamp;
    public EventPOJO(){}
    public EventPOJO(String name,String dpUrl,String location,String description,String timeStamp)
    {
        this.name=name;
        this.dpUrl=dpUrl;
        this.location=location;
        this.description=description;
        this.timeStamp=timeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDpUrl() {
        return dpUrl;
    }

    public void setDpUrl(String dpUrl) {
        this.dpUrl = dpUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
