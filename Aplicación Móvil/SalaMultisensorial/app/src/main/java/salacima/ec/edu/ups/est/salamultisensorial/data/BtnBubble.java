package salacima.ec.edu.ups.est.salamultisensorial.data;

/**
 * Created by root on 18/04/17.
 */

public class BtnBubble {
    int id;
    String name;
    String r;
    String g;
    String b;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "BtnBubble{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", r='" + r + '\'' +
                ", g='" + g + '\'' +
                ", b='" + b + '\'' +
                '}';
    }

    public String rgb(){
        return r+";"+g+";"+b;
    }

}
