package br.com.gabrielferreira.moreaqui;

public class LocationEstate extends Estate {
    private static final long serialVersionUID = 1L;

    public final Double LATITUDE;
    public final Double LONGITUDE;

    public LocationEstate(String size, String status, String phone, String type, Double latitude, Double longitude) {
        super(size, status, phone, type);
        this.LATITUDE = latitude;
        this.LONGITUDE = longitude;
    }
}
