package salacima.ec.edu.ups.est.salamultisensorial.presentation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import salacima.ec.edu.ups.est.salamultisensorial.R;

public class ModulesSelected extends AppCompatActivity {
    private String selection;
    private String[] selectionModules;
    private ImageButton btnTube, btnStairs, btnPiano, btnPictograph, btnDice,btnBack;
    private TextView titleTube,titleStairs,titlePiano,titlePictograph,titleDice;
    private Button room_lights;
    final static int SERVERPORT = 12345;
    final static String SERVER_IP = "192.168.0.101";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules_selected);

        selection=getIntent().getExtras().getString("modules");
        selectionModules = selection.split(",");

        room_lights =(Button) findViewById(R.id.btnRoomLights);
        btnTube = (ImageButton) findViewById(R.id.btnModuleTube);
        btnStairs = (ImageButton) findViewById(R.id.btnModuleStairs);
        btnPiano = (ImageButton) findViewById(R.id.btnModulePiano);
        btnPictograph = (ImageButton) findViewById(R.id.btnModulePictograph);
        btnDice = (ImageButton) findViewById(R.id.btnModuleDice);
        btnBack=(ImageButton) findViewById(R.id.btnListModuleBack);

        titleTube=(TextView)findViewById(R.id.txt_title_tube);
        titleStairs=(TextView)findViewById(R.id.txt_title_stairs);
        titlePiano=(TextView)findViewById(R.id.txt_title_piano);
        titlePictograph=(TextView)findViewById(R.id.txt_title_pictograph);
        titleDice=(TextView)findViewById(R.id.txt_title_dice);

        btnTube.setVisibility(View.GONE);
        btnStairs.setVisibility(View.GONE);
        btnPiano.setVisibility(View.GONE);
        btnPictograph.setVisibility(View.GONE);
        btnDice.setVisibility(View.GONE);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        room_lights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModulesSelected.this,RoomLight.class);
                startActivity(intent);
            }
        });
        for(int i=0 ;i<selectionModules.length;i++){
            if(selectionModules[i].equals("Tubo de burbujas")){
                titleTube.setText(R.string.tituloBurbujas);
                int imagen= R.drawable.ico_burbuja;
                btnTube.setImageResource(imagen);
                boton(btnTube);
                btnTube.setVisibility(View.VISIBLE);
                btnTube.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ModulesSelected.this,ModuleBubbleTube.class);
                        intent.putExtra("modulos", selection.substring(0,selection.length()-1));
                        startActivity(intent);
                    }
                });
            }
            if(selectionModules[i].equals("Escalera de luces")){

                int imagen= R.drawable.ico_escalera;
                btnStairs.setImageResource(imagen);
                boton(btnStairs);
                titleStairs.setText(selectionModules[i]);
                btnStairs.setVisibility(View.VISIBLE);
                btnStairs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ModulesSelected.this,ModuleStairwayLights.class);
                        startActivity(intent);
                    }
                });
            }

            if(selectionModules[i].equals("Piano")){
                titlePiano.setText(selectionModules[i]);

                int imagen= R.drawable.ico_piano;
                btnPiano.setImageResource(imagen);
                boton(btnPiano);
                btnPiano.setVisibility(View.VISIBLE);
                btnPiano.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ModulesSelected.this,ModulePiano.class);
                        startActivity(intent);
                    }
                });
            }
            if(selectionModules[i].equals("Pictogramas")){
                titlePictograph.setText(selectionModules[i]);

                int imagen= R.drawable.ico_pic;
                btnPictograph.setImageResource(imagen);
                boton(btnPictograph);
                btnPictograph.setVisibility(View.VISIBLE);
                btnPictograph.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ModulesSelected.this,ModulePictograms.class);
                        startActivity(intent);
                    }
                });
            }
            if(selectionModules[i].equals("Dado")) {
                titleDice.setText(selectionModules[i]);

                int imagen = R.drawable.ico_daice;
                btnDice.setImageResource(imagen);
                boton(btnDice);
                btnDice.setVisibility(View.VISIBLE);
                btnDice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ModulesSelected.this, ModuleDice.class);
                        startActivity(intent);
                    }
                });
            }
        }

    }

    public void boton(ImageButton button)
    {
        android.view.ViewGroup.LayoutParams params = button.getLayoutParams();
        params.height = 150;
        params.width = 150;
        button.setLayoutParams(params);


    }


}
