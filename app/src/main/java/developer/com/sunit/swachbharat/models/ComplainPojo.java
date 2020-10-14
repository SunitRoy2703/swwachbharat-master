package developer.com.sunit.swachbharat.models;

public class ComplainPojo {

    private String message,name,location,imageUrl,timeStamp,dpUrl,uid;



    public ComplainPojo(String uid,String message,String name,String location,String imageUrl,String dpUrl ,String timeStamp)
    {
        this.uid=uid;
        this.message=message;
        this.name=name;
        this.location=location;
        this.imageUrl=imageUrl;
        this.dpUrl=dpUrl;
        this.timeStamp=timeStamp;

    }
    public ComplainPojo(){}

    public String getLocation() {
        return location;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDpUrl() {
        return dpUrl;
    }

    public String getUid() {
        return uid;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDpUrl(String dpUrl) {
        this.dpUrl = dpUrl;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
