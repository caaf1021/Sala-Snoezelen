
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
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
import salacima.ec.edu.ups.est.salamultisensorial.data.BtnBubble;

public class ModuleBubbleTube extends AppCompatActivity {

    private Button btnSelection,btnRemove;
    private ArrayAdapter<String> adaptadorButtons;
    private ListView ltsButtons;
    private ArrayList<String> ltssecuencia;
    private ImageButton btn1,btn2,btnYellow,btnBlue,btnRed,btnPurple,btnOrange,btnGreen,btnCyan,
            btnWhite,btnViolet,btnSelected,btnBack;
    final Context context = this;
    private ArrayList<BtnBubble>ltsbtn;
    private BtnBubble b1,b2;
    private String mode="1";

    public Socket socket;
    private ConectarSocket conexion;
    private static final String LOGTAG = "LogsAndroid";
    private RadioGroup selectedMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_bubble_tube);


        conexion = new ConectarSocket(context);
        conexion.execute((Void) null);

        btn1=(ImageButton) findViewById(R.id.btnBubbleColor1);
        btn2=(ImageButton) findViewById(R.id.btnBubbleColor2);

        btnRemove=(Button)findViewById(R.id.btnBubbleRemove) ;
        btnBack=(ImageButton) findViewById(R.id.btnTubeBubbleBack);
        btnSelection=(Button)findViewById(R.id.btnBubbleAccept);
        ltsButtons=(ListView)findViewById(R.id.ltsSecuenciaBubble);
        selectedMode=(RadioGroup) findViewById(R.id.rgBubbleMode);

        selectedMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                if (checkedId == R.id.rgBubblePersonalized){
                    //se desbloquea los botones del tubo
                    btn1.setEnabled(true);
                    btn2.setEnabled(true);
                    mode = "1";
                }else if (checkedId == R.id.rgBunbbleDemonstration){
                    //se bloqueda los botones del tubo y se pone en modo 2
                    btn1.setEnabled(false);
                    btn2.setEnabled(false);
                    mode = "2";
                }else if (checkedId == R.id.rgBubbleFinalize){
                    //se bloqueda los botones del tubo y se pone en modo 3
                    btn1.setEnabled(false);
                    btn2.setEnabled(false);
                    mode = "3";
                }else if (checkedId == R.id.rgBubbleActivateTubes){
                    //se desbloqueda los botones del tubo y se pone en modo 4
                    btn1.setEnabled(true);
                    btn2.setEnabled(true);
                    mode = "4";
                }else if (checkedId == R.id.rgBubbleActivateButtons){
                    //se desbloqueda los botones del tubo y se pone en modo 5
                    btn1.setEnabled(true);
                    btn2.setEnabled(true);
                    mode = "5";
                }
            }

        });

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
        b1= new BtnBubble();
        b1.setId(btn1.getId());
        b1.setName("1");

        b2= new BtnBubble();
        b2.setId(btn2.getId());
        b2.setName("2");

        ltsbtn = new ArrayList<>();
        ltsbtn.add(b1);
        ltsbtn.add(b2);

        ltssecuencia = new ArrayList<String>();

        adaptadorButtons= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, ltssecuencia);
        ltsButtons.setAdapter(adaptadorButtons);


        btnSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mode.equals("1")||mode.equals("4")||mode.equals("5")){
                    if(verificacion()){
                        String cad=crearCadena();
                        dialogoEnvio(cad);
                    }else{
                        Toast.makeText(getApplicationContext(),"Seleciones los colores",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    String cad=crearCadena();
                    dialogoEnvio(cad);
                }



            }
        });


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ltssecuencia.add("1");
                adaptadorButtons.notifyDataSetChanged();
            }
        });

        btn1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btnSelected=btn1;
                colores();
                return false;
            }


        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ltssecuencia.add("2");
                adaptadorButtons.notifyDataSetChanged();
            }
        });

        btn2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btnSelected=btn2;
                colores();
                return false;
            }


        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ltssecuencia.size()!=0){
                    ltssecuencia.remove(ltssecuencia.size()-1);
                    adaptadorButtons.notifyDataSetChanged();
                }
            }
        });

    }

    public boolean verificacion(){
        boolean verificacion=true;
        for(int i = 0;i<ltsbtn.size();i++){
            if (ltsbtn.get(i).getR()==null || ltsbtn.get(i).getG()==null||ltsbtn.get(i).getB()==null){
                verificacion=false;
            }
        }
        return verificacion;
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

    public String crearCadena(){
        String cadena="M04,"+mode+","+ltsbtn.get(0).rgb()+";"+ltsbtn.get(1).rgb()+",";

        for(int i = 0; i< ltssecuencia.size(); i++){
            cadena=cadena+ ltssecuencia.get(i)+";";
        }
        cadena= cadena.substring(0,cadena.length()-1);
        cadena=cadena+","+ ltssecuencia.size()+",ok";

        Log.i("log tube",""+cadena);

        return cadena;
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

        dialog = new Dialog(ModuleBubbleTube.this,R.style.Theme_Dialog_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        TextView titulo = (TextView) dialog.findViewById(R.id.titulo);
        titulo.setText("Enviar Datos");

        TextView contenido = (TextView) dialog.findViewById(R.id.contenido);
        contenido.setText("Desea enviar la secuancia al modulo");
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
