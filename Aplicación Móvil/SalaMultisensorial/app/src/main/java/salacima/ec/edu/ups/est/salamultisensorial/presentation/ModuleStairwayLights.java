package salacima.ec.edu.ups.est.salamultisensorial.presentation;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import salacima.ec.edu.ups.est.salamultisensorial.R;
import salacima.ec.edu.ups.est.salamultisensorial.data.BtnStairway;
import salacima.ec.edu.ups.est.salamultisensorial.data.ColorDice;

public class ModuleStairwayLights extends AppCompatActivity {
    Button btnSend, btnRamdonColor;
    ImageButton btnYellow,btnBlue,btnRed,btnPurple,btnOrange,btnGreen,btnCyan,btnWhite,btnViolet,btnSelected,
            btnColor1, btnColor2, btnColor3, btnColor4, btnColor5, btnColor6, btnColor7, btnColor8,btnBack;
    BtnStairway bcolor1,bcolor2,bcolor3,bcolor4,bcolor5,bcolor6,bcolor7,bcolor8;
    ArrayList<BtnStairway>ltsbtn;
    final Context context = this;
    public Socket socket;
    private ConectarSocket conexion;
    String nivel="2";
    private RadioGroup selectedMode;
    String mode="1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_stairway_lights);

        //Conexion con el Socket usndo una tarea sincrona
        conexion = new ConectarSocket(context);
        conexion.execute((Void) null);

        btnColor1 = (ImageButton)findViewById(R.id.btnStairwayColor1);
        btnColor2 = (ImageButton)findViewById(R.id.btnStairwayColor2);
        btnColor3 = (ImageButton)findViewById(R.id.btnStairwayColor3);
        btnColor4 = (ImageButton)findViewById(R.id.btnStairwayColor4);
        btnColor5 = (ImageButton)findViewById(R.id.btnStairwayColor5);
        btnColor6 = (ImageButton)findViewById(R.id.btnStairwayColor6);
        btnColor7 = (ImageButton)findViewById(R.id.btnStairwayColor7);
        btnColor8 = (ImageButton)findViewById(R.id.btnStairwayColor8);
        btnBack= (ImageButton) findViewById(R.id.btnStairwayBack);

        btnSend = (Button)findViewById(R.id.btnStairwaySend);
        btnRamdonColor = (Button)findViewById(R.id.btnStairwayRamdonColor);
        selectedMode =(RadioGroup) findViewById(R.id.rgStairwayMode);

        ltsbtn = new ArrayList<>();
        bcolor1 = new BtnStairway();
        bcolor1.setId(btnColor1.getId());

        bcolor2 = new BtnStairway();
        bcolor2.setId(btnColor2.getId());

        bcolor3 = new BtnStairway();
        bcolor3.setId(btnColor3.getId());

        bcolor4 = new BtnStairway();
        bcolor4.setId(btnColor4.getId());

        bcolor5 = new BtnStairway();
        bcolor5.setId(btnColor5.getId());

        bcolor6 = new BtnStairway();
        bcolor6.setId(btnColor6.getId());

        bcolor7 = new BtnStairway();
        bcolor7.setId(btnColor7.getId());

        bcolor8 = new BtnStairway();
        bcolor8.setId(btnColor8.getId());

        ltsbtn.add(bcolor1);
        ltsbtn.add(bcolor2);
        ltsbtn.add(bcolor3);
        ltsbtn.add(bcolor4);
        ltsbtn.add(bcolor5);
        ltsbtn.add(bcolor6);
        ltsbtn.add(bcolor7);
        ltsbtn.add(bcolor8);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    enviarComando("cerrar");
                    onBackPressed();
                    if(!socket.isConnected()){
                        socket.close();
                        socket=null;
                    }
                }catch (Exception e){
                    onBackPressed();
                    e.printStackTrace();
                }

            }
        });

        btnRamdonColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coloresAleatorios();
            }
        });

        selectedMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                if (checkedId == R.id.rgStairway1){
                    //se desbloquea los botones de la escalera
                    btnColor1.setEnabled(true);
                    btnColor2.setEnabled(true);
                    btnColor3.setEnabled(true);
                    btnColor4.setEnabled(true);
                    btnColor5.setEnabled(true);
                    btnColor6.setEnabled(true);
                    btnColor7.setEnabled(true);
                    btnColor8.setEnabled(true);
                    mode = "1";
                }else if (checkedId == R.id.rgStairway2){
                    //se bloqueda los botones de la escalera y se pone en modo 2
                    btnColor1.setEnabled(false);
                    btnColor2.setEnabled(false);
                    btnColor3.setEnabled(false);
                    btnColor4.setEnabled(false);
                    btnColor5.setEnabled(false);
                    btnColor6.setEnabled(false);
                    btnColor7.setEnabled(false);
                    btnColor8.setEnabled(false);
                    mode = "2";
                }else if (checkedId == R.id.rgStairway3){
                    //se bloqueda los botones de la escalera y se pone en modo 3
                    btnColor1.setEnabled(false);
                    btnColor2.setEnabled(false);
                    btnColor3.setEnabled(false);
                    btnColor4.setEnabled(false);
                    btnColor5.setEnabled(false);
                    btnColor6.setEnabled(false);
                    btnColor7.setEnabled(false);
                    btnColor8.setEnabled(false);
                    mode = "3";
                }


            }

        });



        btnColor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSelected= btnColor1;
                colores();
            }

        });

        btnColor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSelected= btnColor2;
                colores();
            }

        });

        btnColor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSelected= btnColor3;
                colores();
            }

        });

        ////
        btnColor4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSelected= btnColor4;
                colores();
            }

        });
        btnColor5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSelected= btnColor5;
                colores();
            }

        });
        btnColor6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSelected= btnColor6;
                colores();
            }

        });
        btnColor7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSelected= btnColor7;
                colores();
            }

        });
        btnColor8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSelected= btnColor8;
                colores();
            }

        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mode.equals("1")){
                    //hacer validacion para q ningun color vaya vacio
                    if(verficarColores()==true) {
                        String mensaje = crearCadena();
                        dialogoEnvio(mensaje);
                    }else{
                        Toast.makeText(getApplicationContext(),R.string.msjerrorColorSelected,Toast.LENGTH_SHORT).show();
                    }
                }else{
                    String mensaje = crearCadena();
                    dialogoEnvio(mensaje);
                }

            }
        });


    }

    
    public boolean verficarColores(){
        boolean verificacion=true;
        for(int i=0;i<ltsbtn.size();i++){
            if(ltsbtn.get(i).getR()==null || ltsbtn.get(i).getG()==null || ltsbtn.get(i).getB()==null){
                verificacion=false;
            }
        }

        return verificacion;
    }
    public String crearCadena(){
        String cadena="M05,"+mode+","+nivel+",";
        for(int i=0;i<ltsbtn.size();i++){
            cadena=cadena+ltsbtn.get(i).getR()+","+ltsbtn.get(i).getG()+","+ltsbtn.get(i).getB()+",";
        }
        cadena=cadena.substring(0,cadena.length()-1);
        cadena=cadena+",ok";
        Log.i("app log",""+cadena);
        return cadena;
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    public void coloresAleatorios(){
        ColorDice yellow,blue,red,purple,orange,green,cyan,white,violet;
        yellow = new ColorDice("yellow",255,255,0);
        blue= new ColorDice("blue",0,0,255);
        red= new ColorDice("red",255,0,0);
        purple= new ColorDice("purple",255,119,187);
        orange= new ColorDice("orange",255,127,0);
        green= new ColorDice("green",0,255,0);
        cyan= new ColorDice("cyan",0,188,212);
        white= new ColorDice("white",255,255,255);
        violet= new ColorDice("violet",255,0,255);

        ArrayList<ColorDice> ltsColors= new ArrayList<>();
        ltsColors.add(yellow);
        ltsColors.add(blue);
        ltsColors.add(red);
        ltsColors.add(purple);
        ltsColors.add(orange);
        ltsColors.add(green);
        ltsColors.add(cyan);
        ltsColors.add(white);
        ltsColors.add(violet);

        for(int i = 1 ;i<=8;i++){
            int colorRamdon=colorAleatorio();
            if(i==1){
                guardarColor(ltsColors.get(colorRamdon).getR(),ltsColors.get(colorRamdon).getG(),
                        ltsColors.get(colorRamdon).getB(),btnColor1);
            }

            if(i==2){
                guardarColor(ltsColors.get(colorRamdon).getR(),ltsColors.get(colorRamdon).getG(),
                        ltsColors.get(colorRamdon).getB(),btnColor2);
            }

            if(i==3){
                guardarColor(ltsColors.get(colorRamdon).getR(),ltsColors.get(colorRamdon).getG(),
                        ltsColors.get(colorRamdon).getB(),btnColor3);
            }

            if(i==4){
                guardarColor(ltsColors.get(colorRamdon).getR(),ltsColors.get(colorRamdon).getG(),
                        ltsColors.get(colorRamdon).getB(),btnColor4);
            }

            if(i==5){
                guardarColor(ltsColors.get(colorRamdon).getR(),ltsColors.get(colorRamdon).getG(),
                        ltsColors.get(colorRamdon).getB(),btnColor5);
            }

            if(i==6){
                guardarColor(ltsColors.get(colorRamdon).getR(),ltsColors.get(colorRamdon).getG(),
                        ltsColors.get(colorRamdon).getB(),btnColor6);
            }

            if(i==7){
                guardarColor(ltsColors.get(colorRamdon).getR(),ltsColors.get(colorRamdon).getG(),
                        ltsColors.get(colorRamdon).getB(),btnColor7);
            }

            if(i==8){
                guardarColor(ltsColors.get(colorRamdon).getR(),ltsColors.get(colorRamdon).getG(),
                        ltsColors.get(colorRamdon).getB(),btnColor8);
            }
        }
    }
    public int colorAleatorio(){
        return  (int) (Math.random()*8+1);
    }


    public void colores(){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.panelcolores);
        dialog.setTitle("Seleccione un color");

        btnYellow= (ImageButton) dialog.findViewById(R.id.btnYellow);
        btnRed= (ImageButton) dialog.findViewById(R.id.btnRed);
        btnBlue = (ImageButton) dialog.findViewById(R.id.btnBlue);
        btnViolet= (ImageButton) dialog.findViewById(R.id.btnViolet);
        btnOrange = (ImageButton) dialog.findViewById(R.id.btnOrange);
        btnGreen = (ImageButton) dialog.findViewById(R.id.btnGreen);
        btnCyan = (ImageButton) dialog.findViewById(R.id.btnCyan);
        btnWhite = (ImageButton) dialog.findViewById(R.id.btnWhite);
        btnPurple= (ImageButton) dialog.findViewById(R.id.btnPurple);

        String cad;
        btnYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarColor(255,255,0,btnSelected);
                dialog.dismiss();
            }
        });

        btnRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarColor(255,0,0,btnSelected);
                dialog.dismiss();
            }
        });

        btnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarColor(0,0,255,btnSelected);
                dialog.dismiss();
            }
        });

        btnViolet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarColor(255,0,255,btnSelected);
                dialog.dismiss();
            }
        });

        btnOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarColor(255,127,0,btnSelected);
                dialog.dismiss();
            }
        });

        btnGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarColor(0,255,0,btnSelected);
                dialog.dismiss();
            }
        });

        btnCyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarColor(0,188,212,btnSelected);
                dialog.dismiss();
            }
        });

        btnPurple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarColor(255,119,187,btnSelected);
                dialog.dismiss();
            }
        });

        btnWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarColor(255,255,255,btnSelected);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void guardarColor(int r, int g, int b, ImageButton btn){
        for(int i = 0; i <ltsbtn.size();i++){
            if(btn.getId()==ltsbtn.get(i).getId()){
                ltsbtn.get(i).setR(r+"");
                ltsbtn.get(i).setG(g+"");
                ltsbtn.get(i).setB(b+"");
            }
        }
        btn.setBackgroundColor(Color.rgb(r,g,b));
    }

    Dialog dialog;
    public void dialogoEnvio(final String mensaje){

        dialog = new Dialog(ModuleStairwayLights.this,R.style.Theme_Dialog_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        TextView titulo = (TextView) dialog.findViewById(R.id.titulo);
        titulo.setText("Enviar Datos");

        TextView contenido = (TextView) dialog.findViewById(R.id.contenido);
        contenido.setText("Desea enviar la secuancia al modulo");
        /////
        ((Button) dialog.findViewById(R.id.aceptar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                enviarComando(mensaje);
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

    public void enviarComando(String comando){
        try {
            if(socket==null){
                conexion= new ConectarSocket(context);
                conexion.execute((Void) null);
            }
            if(!socket.isConnected()){
                socket.close();
                socket=null;
                Toast.makeText(
                        this,
                        "No conectado. Tratanto de reconectar!",
                        Toast.LENGTH_SHORT).show();
                conexion= new ConectarSocket(context);
                conexion.execute((Void) null);

            }
            if(socket==null){
                Toast.makeText(
                        this,
                        "Error de conexion, al enviar el comando",
                        Toast.LENGTH_SHORT).show();
            }
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println(comando);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String LOGTAG = "LogsAndroid";

    public class ConectarSocket extends AsyncTask<Void, Void, Boolean> {
        Context context;

        public ConectarSocket(Context context) {
            this.context = context;
        }

        /**
         *
         * @param params
         * @return
         */
        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                if(socket!=null){
                    if(socket.isConnected()) {
                        socket.close();
                    }
                    socket=null;
                }
                InetAddress serverAddr = InetAddress.getByName(ModulesSelected.SERVER_IP);
                socket = new Socket(serverAddr, ModulesSelected.SERVERPORT);
                Log.i(LOGTAG,serverAddr +"  --  "+ModulesSelected.SERVERPORT);


            } catch (UnknownHostException e1) {
                e1.printStackTrace();
                return false;
            } catch (IOException e1) {
                e1.printStackTrace();
                return false;
            }

            return true;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            conexion = null;
            try{


                if(socket==null){
                    Toast.makeText(
                            context,
                            "Imposible conectar",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(socket.isConnected()){
                    Toast.makeText(
                            context,
                            "Conectado!",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(
                            context,
                            "No se pudo conectar con:"+ ModulesSelected.SERVER_IP,
                            Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Toast.makeText(
                        context,
                        "Exeption Thread" + e.toString(),
                        Toast.LENGTH_LONG).show();
            }



        }

        @Override
        protected void onCancelled() {
            conexion = null;

        }
    }




}
