package salacima.ec.edu.ups.est.salamultisensorial.presentation;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import salacima.ec.edu.ups.est.salamultisensorial.R;

public class ListModules extends AppCompatActivity {

    private TextView txtselection;
    private ImageButton btnModules,btnLogout,btnBack;
    private CheckBox chModule1, chModule2, chModule3, chModule4, chModule5;
    private String patientSelected ="";
    private static String menssage ="Por favor seleccione un modulo";
    private String modulesSelected="";
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_modules);

        patientSelected =getIntent().getExtras().getString("selected");
        txtselection =(TextView)findViewById(R.id.lbSeleccion);
        txtselection.setText(patientSelected);
        btnModules =(ImageButton) findViewById(R.id.btnModuloSelected);
        chModule1 = (CheckBox)findViewById(R.id.chbMod1);
        chModule2 = (CheckBox)findViewById(R.id.chbMod2);
        chModule3 = (CheckBox)findViewById(R.id.chbMod3);
        chModule4 = (CheckBox)findViewById(R.id.chbMod4);
        chModule5 = (CheckBox)findViewById(R.id.chbMod5);
        btnLogout= (ImageButton) findViewById(R.id.btnLogout);
        btnBack=(ImageButton) findViewById(R.id.btnListModuleBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        btnModules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modulesSelected="";

                if(chModule1.isChecked()){
                    modulesSelected=modulesSelected+getResources().getString(R.string.chb_mod1)+",";
                }
                if(chModule2.isChecked()){
                    modulesSelected=modulesSelected+getResources().getString(R.string.chb_mod2)+",";
                }
                if(chModule3.isChecked()){
                    modulesSelected=modulesSelected+getResources().getString(R.string.chb_mod3)+",";
                }
                if(chModule4.isChecked()){
                    modulesSelected=modulesSelected+getResources().getString(R.string.chb_mod4)+",";
                }
                if(chModule5.isChecked()){
                    modulesSelected=modulesSelected+getResources().getString(R.string.chb_mod5)+",";
                }

                if(modulesSelected.equals("")){
                    Toast.makeText(getApplication(), menssage,Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(ListModules.this,ModulesSelected.class);
                    intent.putExtra("modules", modulesSelected.substring(0,modulesSelected.length()-1));
                    startActivity(intent);
                }
            }
        });

    }

    public void logout(){

        dialog = new Dialog(ListModules.this,R.style.Theme_Dialog_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        TextView titulo = (TextView) dialog.findViewById(R.id.titulo);
        titulo.setText("Salir");

        TextView contenido = (TextView) dialog.findViewById(R.id.contenido);
        contenido.setText("Desea Cerrar su secion?");
        /////
        ((Button) dialog.findViewById(R.id.aceptar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getApplicationContext(),Login.class);
                startActivity(i);
                finish();
                dialog.dismiss();


            }
        });

        ((Button) dialog.findViewById(R.id.cancelar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                dialog.dismiss();


            }
        });

        dialog.show();


    }

}
