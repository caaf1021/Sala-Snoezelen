package salacima.ec.edu.ups.est.salamultisensorial.deal;

/**
 * Created by paul on 28/08/16.
 */
public class Patient {
    String name;
    String lastname;

    public Patient(){

    }
    public Patient(String nombre, String apellido) {
        this.name = nombre;
        lastname = apellido;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
