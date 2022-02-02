package Models;

public class Kullanicilar {
    private String dogumtarih,egitim,hakkımda,isim,resim;

    public Kullanicilar() {
    }

    public Kullanicilar(String dogumtarih, String egitim, String hakkımda, String isim, String resim) {
        this.dogumtarih = dogumtarih;
        this.egitim = egitim;
        this.hakkımda = hakkımda;
        this.isim = isim;
        this.resim = resim;
    }

    public String getDogumtarih() {
        return dogumtarih;
    }

    public void setDogumtarih(String dogumtarih) {
        this.dogumtarih = dogumtarih;
    }

    public String getEgitim() {
        return egitim;
    }

    public void setEgitim(String egitim) {
        this.egitim = egitim;
    }

    public String getHakkımda() {
        return hakkımda;
    }

    public void setHakkımda(String hakkımda) {
        this.hakkımda = hakkımda;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }
}

