package salacima.ec.edu.ups.est.salamultisensorial.presentation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import salacima.ec.edu.ups.est.salamultisensorial.R;
import salacima.ec.edu.ups.est.salamultisensorial.data.BtnPiano;

public class ModulePiano extends AppCompatActivity {

    private ImageButton btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btnselected,btnBack;
    private ArrayList<String> buttons;
    private ArrayAdapter<String> adaptadorButtons;
    private ArrayList<ImageButton> buttonsScream;
    private ListView ltsButtons;
    private boolean sequence;
    private Button btnCreateSequence, btnRemoveLast, btnSend,  btnselectedColor;
    private final Context context = this;
    private BtnPiano b1,b2,b3,b4,b5,b6,b7,b8;
    private String colorSeleccionado="";
    public Socket socket;
    private ConectarSocket conexion;
    private static final int ABRIRFICHERO_RESULT_CODE = 1;
    private Button btnrandomColors;
    private ArrayList<BtnPiano> ltsBPiano;
    private String codModule="M02";
    private String mode ="1";
    private String modePlay ="2";
    private RadioGroup levelStrength,selectedMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_piano);

        btn1 = (ImageButton) findViewById(R.id.btnPiano1);
        btn2 = (ImageButton) findViewById(R.id.btnPiano2);
        btn3 = (ImageButton) findViewById(R.id.btnPiano3);
        btn4 = (ImageButton) findViewById(R.id.btnPiano4);
        btn5 = (ImageButton) findViewById(R.id.btnPiano5);
        btn6 = (ImageButton) findViewById(R.id.btnPiano6);
        btn7 = (ImageButton) findViewById(R.id.btnPiano7);
        btn8 = (ImageButton) findViewById(R.id.btnPiano8);
        btnBack=(ImageButton) findViewById(R.id.btnPianoBack);
        levelStrength = (RadioGroup)findViewById(R.id.nFuerza);
        btnCreateSequence = (Button) findViewById(R.id.btnPianoCreateSequence);
        btnRemoveLast = (Button) findViewById(R.id.btnPianoRemoveLast);
        btnSend = (Button) findViewById(R.id.btnPianoSend);
        btnselectedColor = (Button) findViewById(R.id.btnPianoSelectedColor);
        ltsButtons=(ListView)findViewById(R.id.ltsPianoSequence);
        btnrandomColors = (Button) findViewById(R.id.btnPianoRandomColors);
        selectedMode= (RadioGroup)findViewById(R.id.rgPianoMode);
        selectedMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                if (checkedId == R.id.rgPianoPersonalized){
                    //se desbloquea los botones del dado

                    mode = "1";
                }else if (checkedId == R.id.rgPianoDemonstration){
                    //se bloqueda los botones del dado y se pone en modo 2

                    mode = "2";
                }else if (checkedId == R.id.rgPianoFinalize){
                    //se bloqueda los botones del dado y se pone en modo 3

                    mode = "3";
                }
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

        buttonsScream = new ArrayList<>();
        buttonsScream.add(btn1);
        buttonsScream.add(btn2);
        buttonsScream.add(btn3);
        buttonsScream.add(btn4);
        buttonsScream.add(btn5);
        buttonsScream.add(btn6);
        buttonsScream.add(btn7);
        buttonsScream.add(btn8);

        conexion = new ConectarSocket(context);
        conexion.execute((Void) null);
        sequence = false;


        levelStrength.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == R.id.rBajo){
                    modePlay ="0";

                }else if (checkedId == R.id.rMedio){
                    modePlay ="1";
                }else if (checkedId == R.id.rAlto){
                    modePlay ="2";
                }

                Log.i("log app",""+ mode);

            }

        });
        b1=new BtnPiano();
        b1.setId(btn1.getId());
        b1.setNombreBoton("1");

        b2=new BtnPiano();
        b2.setId(btn2.getId());
        b2.setNombreBoton("2");

        b3=new BtnPiano();
        b3.setId(btn3.getId());
        b3.setNombreBoton("3");

        b4=new BtnPiano();
        b4.setId(btn4.getId());
        b4.setNombreBoton("4");

        b5=new BtnPiano();
        b5.setId(btn5.getId());
        b5.setNombreBoton("5");

        b6=new BtnPiano();
        b6.setId(btn6.getId());
        b6.setNombreBoton("6");

        b7=new BtnPiano();
        b7.setId(btn7.getId());
        b7.setNombreBoton("7");

        b8=new BtnPiano();
        b8.setId(btn8.getId());
        b8.setNombreBoton("8");

        ltsBPiano= new ArrayList<>();
        ltsBPiano.add(b1);
        ltsBPiano.add(b2);
        ltsBPiano.add(b3);
        ltsBPiano.add(b4);
        ltsBPiano.add(b5);
        ltsBPiano.add(b6);
        ltsBPiano.add(b7);
        ltsBPiano.add(b8);

        buttons = new ArrayList<String>();
        adaptadorButtons= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,buttons);
        ltsButtons.setAdapter(adaptadorButtons);
        btnrandomColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i =0 ; i<ltsBPiano.size();i++){
                    ltsBPiano.get(i).setR(colorAleatorio());
                    ltsBPiano.get(i).setG(colorAleatorio());
                    ltsBPiano.get(i).setB(colorAleatorio());
                    buttonsScream.get(i).setBackgroundColor(Color.rgb(Integer.parseInt(ltsBPiano.get(i).getR()),
                            Integer.parseInt(ltsBPiano.get(i).getG()),Integer.parseInt(ltsBPiano.get(i).getB())));

                }

            }
        });

        btnselectedColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sequence =false;
            }
        });
        btnCreateSequence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sequence =true;
            }
        });

        btnRemoveLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttons.size()!=0){
                    buttons.remove(buttons.size()-1);
                    adaptadorButtons.notifyDataSetChanged();
                }
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mode.equals("1")){
                    if(verficarColores()==true) {
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
                if (sequence){
                    buttons.add("1");
                    adaptadorButtons.notifyDataSetChanged();
                }else{
                    seleccionarColor(btn1);
                }
            }
        });

        btn1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btnselected=btn1;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, ABRIRFICHERO_RESULT_CODE);
                return false;
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sequence){
                    buttons.add("2");
                    adaptadorButtons.notifyDataSetChanged();
                }else{
                    seleccionarColor(btn2);
                    }
            }
        });

        btn2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btnselected=btn2;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, ABRIRFICHERO_RESULT_CODE);
                return false;
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sequence){
                    buttons.add("3");
                    adaptadorButtons.notifyDataSetChanged();
                }else{
                    seleccionarColor(btn3);
                }
            }
        });

        btn3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btnselected=btn3;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, ABRIRFICHERO_RESULT_CODE);

                return false;
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sequence){
                    buttons.add("4");
                    adaptadorButtons.notifyDataSetChanged();
                }else{
                    seleccionarColor(btn4);
                }
            }
        });

        btn4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btnselected=btn4;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, ABRIRFICHERO_RESULT_CODE);

                return false;
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sequence){
                    buttons.add("5");
                    adaptadorButtons.notifyDataSetChanged();
                }else{
                    seleccionarColor(btn5);
                }
            }
        });

        btn5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btnselected=btn5;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, ABRIRFICHERO_RESULT_CODE);

                return false;
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sequence){
                    buttons.add("6");
                    adaptadorButtons.notifyDataSetChanged();
                }else{
                    seleccionarColor(btn6);
                }
            }
        });

        btn6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btnselected=btn6;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, ABRIRFICHERO_RESULT_CODE);

                return false;
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sequence){
                    buttons.add("7");
                    adaptadorButtons.notifyDataSetChanged();
                }else{
                    seleccionarColor(btn7);
                }
            }
        });

        btn7.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btnselected=btn7;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, ABRIRFICHERO_RESULT_CODE);

                return false;
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sequence){
                    buttons.add("8");
                    adaptadorButtons.notifyDataSetChanged();
                }else{
                    seleccionarColor(btn8);
                }
            }
        });

        btn8.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btnselected=btn8;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, ABRIRFICHERO_RESULT_CODE);

                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode != RESULT_CANCELED) if (requestCode == ABRIRFICHERO_RESULT_CODE) {
            Uri rut = data.getData();
            String ruta = getPathFromURI(rut);

            String lista[] = ruta.split("/");
            String idpic =lista[lista.length-1];
            idpic = idpic.substring(0,idpic.length()-4);
            String nombpic=buscarNombre(idpic);
            if (nombpic.equals("")) {
                    Toast.makeText(getApplicationContext(),"Imagen no valida",Toast.LENGTH_SHORT).show();
            }else{
                try{
                    guardarDatos(idpic,nombpic,btnselected);
                    Bitmap bmp = BitmapFactory.decodeFile(ruta);
                    btnselected.setImageBitmap(bmp);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public String buscarNombre(String id){
        String nompic="";
        try
        {
            InputStream file =getResources().openRawResource(R.raw.codigosnombre);

            BufferedReader read = new BufferedReader(new InputStreamReader(file));

            String linea;
            while((linea=read.readLine())!=null){
                String lista[] = linea.split(",");
                if(lista[0].equals(id)){
                    nompic=lista[1];
                    break;
                }
            }
            file.close();

        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde recurso raw");
        }

        return nompic;
    }
    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public void guardarDatos(String idpic,String nompic,ImageButton btn){
        for(int i=0;i<ltsBPiano.size();i++){
            if(btn.getId()==ltsBPiano.get(i).getId()){
                ltsBPiano.get(i).setCdoPic(idpic);
                ltsBPiano.get(i).setNomPic(nompic);
            }
        }
    }

    Dialog dialog;
    public void dialogoEnvio(final String msj){

        // con este tema personalizado evitamos los bordes por defecto
        dialog = new Dialog(ModulePiano.this,R.style.Theme_Dialog_Translucent);
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

                enviarComando(msj);///cambiar x 1
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


    public boolean verficarColores(){
        boolean verificacion=true;
        for(int i=0;i<ltsBPiano.size();i++){
            if(ltsBPiano.get(i).getR()==null || ltsBPiano.get(i).getG()==null || ltsBPiano.get(i).getB()==null){
                verificacion=false;
            }
        }

        return verificacion;
    }
    /*Metodo para construir la cadena a ser enviada
    **
    **
    */

    public String crearCadena(){

        String cadena = "";
        cadena = cadena+codModule+","+mode+",";
        for(int i = 0;i<ltsBPiano.size();i++){
            cadena=cadena+ltsBPiano.get(i).getR()+";"+ltsBPiano.get(i).getG()+";"+ltsBPiano.get(i).getB()+";";
        }
        cadena= cadena.substring(0,cadena.length()-1);
        cadena = cadena+",";

        for(int i = 0;i<ltsBPiano.size();i++){
            cadena=cadena+ltsBPiano.get(i).getNomPic()+";";
        }
        cadena= cadena.substring(0,cadena.length()-1);
        cadena = cadena+",";

        for(int i = 0;i<ltsBPiano.size();i++){
            cadena=cadena+ltsBPiano.get(i).getCdoPic()+".mp3;";
        }
        cadena= cadena.substring(0,cadena.length()-1);
        cadena = cadena+","+buttons.size()+",";



        for(int i = 0;i<buttons.size();i++){
            cadena=cadena+buttons.get(i)+";";
        }
        cadena= cadena.substring(0,cadena.length()-1);
        cadena = cadena+","+ modePlay;

        Log.i("Log piano"," "+cadena);
        return cadena;
    }

    public void seleccionarColor(final ImageButton btn){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.paletacolores);
        dialog.setTitle("Seleccione su color");


        final SeekBar barra1=(SeekBar) dialog.findViewById(R.id.seekBar);
        final SeekBar barra2=(SeekBar) dialog.findViewById(R.id.seekBar2);
        final SeekBar barra3=(SeekBar) dialog.findViewById(R.id.seekBar3);
        final ImageView color_sel=(ImageView) dialog.findViewById(R.id.imageView);

        barra1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                color_sel.setBackgroundColor(Color.rgb(progress,barra2.getProgress(),barra3.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        barra2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                color_sel.setBackgroundColor(Color.rgb(barra1.getProgress(),progress,barra3.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        barra3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                color_sel.setBackgroundColor(Color.rgb(barra1.getProgress(),barra2.getProgress(),progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    btn.setBackgroundColor(Color.rgb(barra1.getProgress(),barra2.getProgress(),barra3.getProgress()));

                    //cl1=convertirRGB(color);
                colorSeleccionado=barra1.getProgress()+","+barra2.getProgress()+","+barra3.getProgress();
                guardarColor(colorSeleccionado,btn);

                dialog.dismiss();
            }
        });

        Button dialogButtonQuitar = (Button) dialog.findViewById(R.id.dialogButtonQuitar);
        // if button is clicked, close the custom dialog
        dialogButtonQuitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                btn.setBackgroundColor(getResources().getColor(R.color.default_piano));
                colorSeleccionado="238,238,238";
                guardarColor(colorSeleccionado,btn);

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void guardarColor(String colores, ImageButton btn){
        for(int i = 0; i<ltsBPiano.size();i++){
            if(ltsBPiano.get(i).getId()==btn.getId()){
                String lista[]=colores.split(",");
                ltsBPiano.get(i).setR(lista[0]);
                ltsBPiano.get(i).setG(lista[1]);
                ltsBPiano.get(i).setB(lista[2]);
            }
        }
    }

    private static final String LOGTAG = "LogsAndroid";

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

    public String colorAleatorio(){
        return  ""+(int) (Math.random()*254+1);
    }


}
