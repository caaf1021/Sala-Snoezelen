package salacima.ec.edu.ups.est.salamultisensorial.data;

/**
 * Created by root on 13/04/17.
 */

public class BtnPiano {
    int id;
    String nombreBoton;
    String r;
    String g;
    String b;
    String nomPic;
    String cdoPic;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getG() {
        return g;
    }

    public void setG(String g) {
        this.g = g;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getNomPic() {
        return nomPic;
    }

    public void setNomPic(String nomPic) {
        this.nomPic = nomPic;
    }

    public String getCdoPic() {
        return cdoPic;
    }

    public void setCdoPic(String cdoPic) {
        this.cdoPic = cdoPic;
    }

    public String getNombreBoton() {
        return nombreBoton;
    }

    public void setNombreBoton(String nombreBoton) {
        this.nombreBoton = nombreBoton;
    }

    @Override
    public String toString() {
        return "BtnPiano{" +
                "id=" + id +
                ", nombreBoton='" + nombreBoton + '\'' +
                ", r='" + r + '\'' +
                ", g='" + g + '\'' +
                ", b='" + b + '\'' +
                ", nomPic='" + nomPic + '\'' +
                ", cdoPic='" + cdoPic + '\'' +
                '}';
    }
}
