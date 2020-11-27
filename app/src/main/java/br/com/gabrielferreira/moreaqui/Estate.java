package br.com.gabrielferreira.moreaqui;

public class Estate {
    String TYPE, SIZE, STATUS, PHONE;


    public Estate(String size, String status, String phone, String type) {
        this.TYPE = type;
        this.SIZE = size;
        this.STATUS = status;
        this.PHONE = phone;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getSIZE() {
        return SIZE;
    }

    public void setSIZE(String SIZE) {
        this.SIZE = SIZE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }
}
