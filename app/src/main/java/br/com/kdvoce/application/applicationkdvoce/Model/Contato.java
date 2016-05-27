package br.com.kdvoce.application.applicationkdvoce.Model;

public class Contato {

    private String id_telefone;
    private Double latitude;
    private Double longitude;

    public String getId_telefone() {
        return id_telefone;
    }

    public void setId_telefone(String id_telefone) {
        this.id_telefone = id_telefone;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
