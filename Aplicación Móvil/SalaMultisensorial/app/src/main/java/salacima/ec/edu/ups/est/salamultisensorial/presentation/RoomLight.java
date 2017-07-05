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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import salacima.ec.edu.ups.est.salamultisensorial.R;

public class RoomLight extends AppCompatActivity {

    private Button btnSendColor, btnDemonstration, btnFinalize;
    private SeekBar bar1,bar2,bar3;
    private ImageView colorSelected;
    private ImageButton lightRoom;
    private String txtColorSelected;
    public Socket socket;
    private ConectarSocket conexion;
    private static final String LOGTAG = "LogsAndroid";
    final Context context = this;
    final static int SERVERPORT = 12345;
    final static String SERVER_IP = "192.168.0.101";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_light);

        conexion = new ConectarSocket(context);
        conexion.execute((Void) null);

        btnSendColor =(Button)findViewById(R.id.btnRoomLightSend);
        btnDemonstration =(Button)findViewById(R.id.btnRoomLightDemonstration);
        btnFinalize =(Button)findViewById(R.id.btnRooomLightFinalize);
        lightRoom =(ImageButton)findViewById(R.id.btnRoomLight) ;

        lightRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colores();
            }
        });
        btnSendColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtColorSelected == null){
                    Toast.makeText(getApplicationContext(),"Seleccione un color",Toast.LENGTH_SHORT).show();
                }else{
                    String cad ="M06,1,"+ txtColorSelected;
                    dialogoEnvio(cad);
                }
            }
        });

        btnDemonstration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cad ="M06,2,"+ txtColorSelected;
                dialogoEnvio(cad);
            }
        });

        btnFinalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cad ="M06,3,"+ txtColorSelected;
                dialogoEnvio(cad);
            }
        });

    }

    public void colores(){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.paletacolores);
        dialog.setTitle("Seleccione un color");


        bar1=(SeekBar) dialog.findViewById(R.id.seekBar);
        bar2=(SeekBar) dialog.findViewById(R.id.seekBar2);
        bar3=(SeekBar) dialog.findViewById(R.id.seekBar3);
        colorSelected =(ImageView) dialog.findViewById(R.id.imageView);

        bar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                colorSelected.setBackgroundColor(Color.rgb(progress,bar2.getProgress(),bar3.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        bar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                colorSelected.setBackgroundColor(Color.rgb(bar1.getProgress(),progress,bar3.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        bar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                colorSelected.setBackgroundColor(Color.rgb(bar1.getProgress(),bar2.getProgress(),progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lightRoom.setBackgroundColor(Color.rgb(bar1.getProgress(),bar2.getProgress(),bar3.getProgress()));
                txtColorSelected =bar1.getProgress()+";"+bar2.getProgress()+";"+bar3.getProgress();
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    Dialog dialog;
    public void dialogoEnvio(final String mensaje){

        dialog = new Dialog(RoomLight.this,R.style.Theme_Dialog_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        TextView titulo = (TextView) dialog.findViewById(R.id.titulo);
        titulo.setText("Enviar ColorDice de luces");

        TextView contenido = (TextView) dialog.findViewById(R.id.contenido);
        contenido.setText("Desea enviar el color de luces");
        ((Button) dialog.findViewById(R.id.aceptar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                enviarComando(mensaje);
                Log.i("log app",""+mensaje);
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
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVERPORT);
                Log.i(LOGTAG,serverAddr +"  --  "+SERVERPORT);


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
                            "No se pudo conectar con:"+ SERVER_IP,
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
