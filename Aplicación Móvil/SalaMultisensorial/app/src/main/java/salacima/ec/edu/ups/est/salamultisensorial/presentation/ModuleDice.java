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
import salacima.ec.edu.ups.est.salamultisensorial.data.BtnDice;
import salacima.ec.edu.ups.est.salamultisensorial.data.ColorDice;


public class ModuleDice extends AppCompatActivity {

    private Button btnSend,btnRamdonColor;
    public Socket socket;
    private ConectarSocket conexion;
    private static final String LOGTAG = "LogsAndroid";
    final Context context = this;
    private BtnDice b1,b2,b3,b4,b5,b6;
    private ArrayList<BtnDice> ltsbtnDice;
    private ImageButton btn1,btn2,btn3,btn4,btn5,btn6,btnSelected,btnYellow,btnBlue,btnRed,btnPurple,
            btnOrange,btnGreen,btnCyan,btnWhite,btnViolet,btnBack;
    private Dialog dialog;
    private RadioGroup selectedMode;
    private String mode = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_dice);

        btn1 =(ImageButton) findViewById(R.id.btnDiceOne);
        btn2 =(ImageButton) findViewById(R.id.btnDiceTwo);
        btn3 =(ImageButton) findViewById(R.id.btnDiceThree);
        btn4 =(ImageButton) findViewById(R.id.btnDiceFour);
        btn5 =(ImageButton) findViewById(R.id.btnDiceFive);
        btn6 =(ImageButton) findViewById(R.id.btnDiceSix);
        btnBack=(ImageButton) findViewById(R.id.btnDiceBack);
        btnSend = (Button) findViewById(R.id.btnDiceSend);
        selectedMode =(RadioGroup) findViewById(R.id.rgMode);
        btnRamdonColor = (Button) findViewById(R.id.btnDiceRamdonColors);

        selectedMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                if (checkedId == R.id.rgDice1){
                    //se desbloquea los botones del dado
                    btn1.setEnabled(true);
                    btn2.setEnabled(true);
                    btn3.setEnabled(true);
                    btn4.setEnabled(true);
                    btn5.setEnabled(true);
                    btn6.setEnabled(true);
                    mode = "1";
                }else if (checkedId == R.id.rgDice2){
                    //se bloqueda los botones del dado y se pone en modo 2
                    btn1.setEnabled(false);
                    btn2.setEnabled(false);
                    btn3.setEnabled(false);
                    btn4.setEnabled(false);
                    btn5.setEnabled(false);
                    btn6.setEnabled(false);
                    mode = "2";
                }else if (checkedId == R.id.rgDice3){
                    //se bloqueda los botones del dado y se pone en modo 3
                    btn1.setEnabled(false);
                    btn2.setEnabled(false);
                    btn3.setEnabled(false);
                    btn4.setEnabled(false);
                    btn5.setEnabled(false);
                    btn6.setEnabled(false);
                    mode = "3";
                }
            }

        });

        conexion = new ConectarSocket(context);
        conexion.execute((Void) null);

        btnRamdonColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coloresAleatorios();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarComando("cerrar");
                try {
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

        b1=new BtnDice();
        b1.setLado(1);
        b1.setId(btn1.getId());

        b2=new BtnDice();
        b2.setLado(2);
        b2.setId(btn2.getId());

        b3=new BtnDice();
        b3.setLado(3);
        b3.setId(btn3.getId());

        b4=new BtnDice();
        b4.setLado(4);
        b4.setId(btn4.getId());

        b5=new BtnDice();
        b5.setLado(5);
        b5.setId(btn5.getId());

        b6=new BtnDice();
        b6.setLado(6);
        b6.setId(btn6.getId());

        ltsbtnDice = new ArrayList<>();
        ltsbtnDice.add(b1);
        ltsbtnDice.add(b2);
        ltsbtnDice.add(b3);
        ltsbtnDice.add(b4);
        ltsbtnDice.add(b5);
        ltsbtnDice.add(b6);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mode.equals("1")){
                    boolean ver =validar();
                    if(ver){
                        String mensaje =crearCadena();
                        dialogoEnvio(mensaje);
                    }else{
                        Toast.makeText(getApplicationContext(),R.string.msjerrorColorSelected,Toast.LENGTH_SHORT).show();
                    }
                }else{
                    String mensaje =crearCadena();
                    dialogoEnvio(mensaje);

                }
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSelected=btn1;
                colores();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSelected=btn2;
                colores();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSelected=btn3;
                colores();

            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSelected=btn4;
                colores();

            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSelected=btn5;
                colores();

            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSelected=btn6;
                colores();

            }
        });
    }

    public String crearCadena(){
        String cadena="M03,"+mode+",";
        for(int i =0;i<ltsbtnDice.size();i++){
         cadena =cadena+ltsbtnDice.get(i).getR()+";"+ltsbtnDice.get(i).getG()+";"+ltsbtnDice.get(i).getB()+";";
        }
        cadena = cadena.substring(0,cadena.length()-1);
        cadena=cadena+",1000,ok";
        Log.i("log dado"," "+cadena);
        return cadena;
    }


    public boolean validar(){
        boolean verificacion = true;

        for(int i =0;i<ltsbtnDice.size();i++){
            if(ltsbtnDice.get(i).getR()==null){
                verificacion=false;
                break;
            }
        }

        return verificacion;
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

        for(int i = 1 ;i<=6;i++){
            int colorRamdon=colorAleatorio();
            if(i==1){
                guardarColor(ltsColors.get(colorRamdon).getR(),ltsColors.get(colorRamdon).getG(),
                        ltsColors.get(colorRamdon).getB(),btn1);
            }

            if(i==2){
                guardarColor(ltsColors.get(colorRamdon).getR(),ltsColors.get(colorRamdon).getG(),
                        ltsColors.get(colorRamdon).getB(),btn2);
            }

            if(i==3){
                guardarColor(ltsColors.get(colorRamdon).getR(),ltsColors.get(colorRamdon).getG(),
                        ltsColors.get(colorRamdon).getB(),btn3);
            }

            if(i==4){
                guardarColor(ltsColors.get(colorRamdon).getR(),ltsColors.get(colorRamdon).getG(),
                        ltsColors.get(colorRamdon).getB(),btn4);
            }

            if(i==5){
                guardarColor(ltsColors.get(colorRamdon).getR(),ltsColors.get(colorRamdon).getG(),
                        ltsColors.get(colorRamdon).getB(),btn5);
            }

            if(i==6){
                guardarColor(ltsColors.get(colorRamdon).getR(),ltsColors.get(colorRamdon).getG(),
                        ltsColors.get(colorRamdon).getB(),btn6);
            }

        }
    }

    public int colorAleatorio(){
        return  (int) (Math.random()*8+1);
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
        Log.i("log dado","entro al metodo guardar");
        for(int i = 0; i <ltsbtnDice.size();i++){
            if(btn.getId()==ltsbtnDice.get(i).getId()){
                ltsbtnDice.get(i).setR(r+"");
                ltsbtnDice.get(i).setG(g+"");
                ltsbtnDice.get(i).setB(b+"");

                break;
            }
        }
        btn.setBackgroundColor(Color.rgb(r,g,b));
    }

    public void dialogoEnvio(final String mensaje){

        // con este tema personalizado evitamos los bordes por defecto
        dialog = new Dialog(ModuleDice.this,R.style.Theme_Dialog_Translucent);
        //deshabilitamos el título por defecto
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //obligamos al usuario a pulsar los botones para cerrarlo
        dialog.setCancelable(false);
        //establecemos el contenido de nuestro dialog
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
