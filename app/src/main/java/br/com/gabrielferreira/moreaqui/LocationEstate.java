package br.com.gabrielferreira.moreaqui;

public class LocationEstate extends Estate {

	private static final long serialVersionUID = 1L;

	public final Double LATITUDE;

	public final Double LONGITUDE;

	public LocationEstate(String type, String size, String phone, String inConstruction, double latitude,
			double longitude) {
		super(type, size, phone, inConstruction);
		this.LATITUDE = latitude;
		this.LONGITUDE = longitude;
	}

	@Override
	public final String toString() {
		String ans = "Imovel: " + TYPE + ", Tamanho: " + SIZE + ", Contato: " + this.PHONE + ", (" + this.STATUS + ") " + "Latitude: " + this.LATITUDE + " Longitude: " + this.LONGITUDE;
		return ans;
	}


}
