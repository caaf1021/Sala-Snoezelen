package salacima.ec.edu.ups.est.salamultisensorial.data;

/**
 * Created by root on 20/04/17.
 */

public class BtnDice {
    int id;
    int lado;
    String r;
    String g;
    String b;

    public int getLado() {
        return lado;
    }

    public void setLado(int lado) {
        this.lado = lado;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BtnDice{" +
                "id=" + id +
                ", lado=" + lado +
                ", r='" + r + '\'' +
                ", g='" + g + '\'' +
                ", b='" + b + '\'' +
                '}';
    }
}
