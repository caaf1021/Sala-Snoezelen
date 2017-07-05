package salacima.ec.edu.ups.est.salamultisensorial.presentation;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.ArrayList;
import salacima.ec.edu.ups.est.salamultisensorial.R;
import salacima.ec.edu.ups.est.salamultisensorial.deal.Patient;

public class ListPatients extends AppCompatActivity {

    private ArrayList<Patient> ltsPatients;
    private RadioGroup rGlistPatients;
    private ImageButton btnselection;
    private String patientSelected ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_patients);

        ltsPatients = new ArrayList<Patient>();
        rGlistPatients =(RadioGroup) findViewById(R.id.listPac);
        btnselection = (ImageButton) findViewById(R.id.btnpatientSelected);
        btnselection.setEnabled(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///configuracion de luces
                Intent i = new Intent(getApplicationContext(),RoomLight.class);
                startActivity(i);
            }
        });

        btnselection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(patientSelected.equals("")){
                    Toast.makeText(getApplication(),R.string.msjbtnpatientSelected,Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(ListPatients.this, ListModules.class);
                    intent.putExtra("selected", patientSelected);
                    startActivity(intent);
                }
            }
        });
        new ObtenerPacientes().execute();
    }


    public class ObtenerPacientes extends AsyncTask<Void, Void, Boolean> {

        ObtenerPacientes() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            generarL();


            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            for(int i = 0; i< ltsPatients.size(); i++){
                RadioButton buttonn = new RadioButton(getApplication());
                buttonn.setId(i);
                buttonn.setText(ltsPatients.get(i).getName() + " " + ltsPatients.get(i).getLastname());
                buttonn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        patientSelected = ((RadioButton) v).getText().toString();
                        //Toast.makeText(getApplication(),"Selecciono---> " + patientSelected,Toast.LENGTH_SHORT).show();
                    }
                });

                buttonn.setTextColor(Color.BLACK);
                buttonn.setLinkTextColor(Color.BLACK);
                buttonn.setTextSize(24);
                rGlistPatients.addView(buttonn);
            }

            btnselection.setEnabled(true);

        }

        @Override
        protected void onCancelled() {

        }

        public void generarL(){

//object.getString("idComentario").toString(),//creamos los objetos segun sean los datos del json
                Patient item = new Patient("Brayant ","Andrade ");
                ltsPatients.add(item);

            item = new Patient("Daniel ","Quezada ");
            ltsPatients.add(item);

            item = new Patient("Andres ","Montaleza ");
            ltsPatients.add(item);

            item = new Patient("Ivan ","Espinoza ");
            ltsPatients.add(item);

            item = new Patient("Carlos ","Guaman ");
            ltsPatients.add(item);

            item = new Patient("Roberto ","Cabrera ");
            ltsPatients.add(item);

            item = new Patient("Cristian ","Pintado ");
            ltsPatients.add(item);

            item = new Patient("Alfredo ","Zumba ");
            ltsPatients.add(item);



        }
    }


}