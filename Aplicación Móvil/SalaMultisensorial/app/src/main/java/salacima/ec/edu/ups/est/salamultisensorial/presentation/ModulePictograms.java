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
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import salacima.ec.edu.ups.est.salamultisensorial.data.BtnPictogram;

public class ModulePictograms extends AppCompatActivity {

    private ImageButton btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10,btn11,btn12,btnselected,btnBack;
    private BtnPictogram b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12;
    private static final int ABRIRFICHERO_RESULT_CODE = 1;
    final Context context = this;
    private String colorSeleccionado="";
    public ArrayList<BtnPictogram> ltsBPictogram;
    private ArrayList<BtnPictogram> ltsSelected;
    private Button btnSend,btnRamdonColors;
    private String codModule="M01";
    private String modePlay="1";
    private String mode="1";
    public Socket socket;
    private ConectarSocket conexion;
    private static final String LOGTAG = "LogsAndroid";
    private RadioGroup selectedMode, selectedPlay;
    private ArrayList<ImageButton> buttonsScream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_pictograms);

        conexion = new ConectarSocket(context);
        conexion.execute((Void) null);

        btn1 = (ImageButton) findViewById(R.id.btnPic1);
        btn2 = (ImageButton) findViewById(R.id.btnPic2);
        btn3 = (ImageButton) findViewById(R.id.btnPic3);
        btn4 = (ImageButton) findViewById(R.id.btnPic4);
        btn5 = (ImageButton) findViewById(R.id.btnPic5);
        btn6 = (ImageButton) findViewById(R.id.btnPic6);
        btn7 = (ImageButton) findViewById(R.id.btnPic7);
        btn8 = (ImageButton) findViewById(R.id.btnPic8);
        btn9 = (ImageButton) findViewById(R.id.btnPic9);
        btn10 = (ImageButton) findViewById(R.id.btnPic10);
        btn11 = (ImageButton) findViewById(R.id.btnPic11);
        btn12 = (ImageButton) findViewById(R.id.btnPic12);
        btnSend = (Button) findViewById(R.id.btnPicSend);
        selectedMode = (RadioGroup) findViewById(R.id.rgPicMode);
        btnRamdonColors= (Button) findViewById(R.id.btnPicRamdonColor);
        btnBack = (ImageButton) findViewById(R.id.btnPicBack);

        btnRamdonColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i =0 ; i<ltsBPictogram.size();i++){
                    ltsBPictogram.get(i).setR(colorAleatorio());
                    ltsBPictogram.get(i).setG(colorAleatorio());
                    ltsBPictogram.get(i).setB(colorAleatorio());
                    buttonsScream.get(i).setBackgroundColor(Color.rgb(Integer.parseInt(ltsBPictogram.get(i).getR()),
                            Integer.parseInt(ltsBPictogram.get(i).getG()),Integer.parseInt(ltsBPictogram.get(i).getB())));

                }

            }
        });

        selectedMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                if (checkedId == R.id.rgPicMode1){
                    //se desbloquea los botones de los pictogramas
                    btn1.setEnabled(true);
                    btn2.setEnabled(true);
                    btn3.setEnabled(true);
                    btn4.setEnabled(true);
                    btn5.setEnabled(true);
                    btn6.setEnabled(true);
                    btn7.setEnabled(true);
                    btn8.setEnabled(true);
                    btn9.setEnabled(true);
                    btn10.setEnabled(true);
                    btn11.setEnabled(true);
                    btn12.setEnabled(true);
                    mode = "1";
                }else if (checkedId == R.id.rgPicMode2){
                    //se bloqueda los botones de los pictogramas y se pone en modo 2
                    btn1.setEnabled(false);
                    btn2.setEnabled(false);
                    btn3.setEnabled(false);
                    btn4.setEnabled(false);
                    btn5.setEnabled(false);
                    btn6.setEnabled(false);
                    btn7.setEnabled(false);
                    btn8.setEnabled(false);
                    btn9.setEnabled(false);
                    btn10.setEnabled(false);
                    btn11.setEnabled(false);
                    btn12.setEnabled(false);
                    mode = "2";
                }else if (checkedId == R.id.rgPicMode3){
                    //se bloqueda los botones de los pictogramas y se pone en modo 3
                    btn1.setEnabled(false);
                    btn2.setEnabled(false);
                    btn3.setEnabled(false);
                    btn4.setEnabled(false);
                    btn5.setEnabled(false);
                    btn6.setEnabled(false);
                    btn7.setEnabled(false);
                    btn8.setEnabled(false);
                    btn9.setEnabled(false);
                    btn10.setEnabled(false);
                    btn11.setEnabled(false);
                    btn12.setEnabled(false);
                    mode = "3";
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
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode.equals("1")){
                    if(modePlay.equals("1")){
                        boolean verificacion = verificacionModo1();
                        if(verificacion){
                            String cadena=crearCadena();
                            dialogoEnvio(cadena);
                        }else{
                            Toast.makeText(getApplicationContext(),R.string.msjErrorPictograma1,Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        boolean verificacion = verificacionModo2Pictogramas();
                        if(verificacion){
                            boolean verificacion2 = verificacionModo2Color();
                            if(verificacion2){
                                String cadena=crearCadena();
                                dialogoEnvio(cadena);
                            }else{
                                Toast.makeText(getApplicationContext(),R.string.msjErrorPictograma2,Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),R.string.msjErrorPictograma1,Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    String cadena=crearCadena();
                    dialogoEnvio(cadena);
                }
            }
        });
        /*
        btndemostracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cadena=crearCadena("2");;
                dialogoEnvio(cadena);
            }
        });
*/

        b1=new BtnPictogram();
        b1.setId(btn1.getId());
        b1.setNombreBoton("1");

        b2=new BtnPictogram();
        b2.setId(btn2.getId());
        b2.setNombreBoton("2");

        b3=new BtnPictogram();
        b3.setId(btn3.getId());
        b3.setNombreBoton("3");

        b4=new BtnPictogram();
        b4.setId(btn4.getId());
        b4.setNombreBoton("4");

        b5=new BtnPictogram();
        b5.setId(btn5.getId());
        b5.setNombreBoton("5");

        b6=new BtnPictogram();
        b6.setId(btn6.getId());
        b6.setNombreBoton("6");

        b7=new BtnPictogram();
        b7.setId(btn7.getId());
        b7.setNombreBoton("7");

        b8=new BtnPictogram();
        b8.setId(btn8.getId());
        b8.setNombreBoton("8");

        b9=new BtnPictogram();
        b9.setId(btn9.getId());
        b9.setNombreBoton("9");

        b10=new BtnPictogram();
        b10.setId(btn10.getId());
        b10.setNombreBoton("10");

        b11=new BtnPictogram();
        b11.setId(btn11.getId());
        b11.setNombreBoton("11");

        b12=new BtnPictogram();
        b12.setId(btn12.getId());
        b12.setNombreBoton("12");

        ltsBPictogram= new ArrayList<>();
        ltsSelected = new ArrayList<>();

        ltsBPictogram.add(b1);
        ltsBPictogram.add(b2);
        ltsBPictogram.add(b3);
        ltsBPictogram.add(b4);
        ltsBPictogram.add(b5);
        ltsBPictogram.add(b6);
        ltsBPictogram.add(b7);
        ltsBPictogram.add(b8);
        ltsBPictogram.add(b9);
        ltsBPictogram.add(b10);
        ltsBPictogram.add(b11);
        ltsBPictogram.add(b12);

        buttonsScream = new ArrayList<>();
        buttonsScream.add(btn1);
        buttonsScream.add(btn2);
        buttonsScream.add(btn3);
        buttonsScream.add(btn4);
        buttonsScream.add(btn5);
        buttonsScream.add(btn6);
        buttonsScream.add(btn7);
        buttonsScream.add(btn8);
        buttonsScream.add(btn9);
        buttonsScream.add(btn10);
        buttonsScream.add(btn11);
        buttonsScream.add(btn12);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarColor(btn1);
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
                seleccionarColor(btn2);
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
                seleccionarColor(btn3);
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
        ////

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarColor(btn4);
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
                seleccionarColor(btn5);
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
                seleccionarColor(btn6);
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
                seleccionarColor(btn7);
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
                seleccionarColor(btn8);
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

        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarColor(btn9);
            }
        });

        btn9.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btnselected=btn9;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, ABRIRFICHERO_RESULT_CODE);
                return false;
            }
        });

        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarColor(btn10);
            }
        });

        btn10.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btnselected=btn10;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, ABRIRFICHERO_RESULT_CODE);
                return false;
            }
        });

        btn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarColor(btn11);
            }
        });

        btn11.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btnselected=btn11;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, ABRIRFICHERO_RESULT_CODE);
                return false;
            }
        });

        btn12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarColor(btn12);
            }
        });

        btn12.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                btnselected=btn12;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, ABRIRFICHERO_RESULT_CODE);
                return false;
            }
        });




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
                colorSeleccionado=barra1.getProgress()+","+barra2.getProgress()+","+barra3.getProgress();

                if(modePlay.equals("1")){
                    guardarColor(colorSeleccionado,btn);
                }else{
                    btn1.setBackgroundColor(Color.rgb(barra1.getProgress(),barra2.getProgress(),barra3.getProgress()));
                    btn2.setBackgroundColor(Color.rgb(barra1.getProgress(),barra2.getProgress(),barra3.getProgress()));
                    btn3.setBackgroundColor(Color.rgb(barra1.getProgress(),barra2.getProgress(),barra3.getProgress()));
                    btn4.setBackgroundColor(Color.rgb(barra1.getProgress(),barra2.getProgress(),barra3.getProgress()));
                    btn5.setBackgroundColor(Color.rgb(barra1.getProgress(),barra2.getProgress(),barra3.getProgress()));
                    btn6.setBackgroundColor(Color.rgb(barra1.getProgress(),barra2.getProgress(),barra3.getProgress()));
                    btn7.setBackgroundColor(Color.rgb(barra1.getProgress(),barra2.getProgress(),barra3.getProgress()));
                    btn8.setBackgroundColor(Color.rgb(barra1.getProgress(),barra2.getProgress(),barra3.getProgress()));
                    btn9.setBackgroundColor(Color.rgb(barra1.getProgress(),barra2.getProgress(),barra3.getProgress()));
                    btn10.setBackgroundColor(Color.rgb(barra1.getProgress(),barra2.getProgress(),barra3.getProgress()));
                    btn11.setBackgroundColor(Color.rgb(barra1.getProgress(),barra2.getProgress(),barra3.getProgress()));
                    btn12.setBackgroundColor(Color.rgb(barra1.getProgress(),barra2.getProgress(),barra3.getProgress()));
                    guardarColorTodos(colorSeleccionado);
                }
                dialog.dismiss();
            }
        });

        Button dialogButtonQuitar = (Button) dialog.findViewById(R.id.dialogButtonQuitar);
        dialogButtonQuitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setBackgroundColor(getResources().getColor(R.color.default_piano));
                btn.setImageBitmap(null);
                colorSeleccionado="238,238,238";
                guardarColor(colorSeleccionado,btn);
                quitarPic(btn);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void quitarPic(ImageButton btn){
        for(int i = 0; i<ltsSelected.size();i++){
            if(ltsSelected.get(i).getId()==btn.getId()){
                ltsSelected.remove(i);
            }
        }
    }

    public void guardarColor(String colores, ImageButton btn){
        for(int i = 0; i<ltsBPictogram.size();i++){
            if(ltsBPictogram.get(i).getId()==btn.getId()){
                String lista[]=colores.split(",");
                ltsBPictogram.get(i).setR(lista[0]);
                ltsBPictogram.get(i).setG(lista[1]);
                ltsBPictogram.get(i).setB(lista[2]);

                if(ltsSelected.contains(ltsBPictogram.get(i))){
                    //solo reemplazar
                    for(int j = 0; j<ltsSelected.size();j++){
                        if(ltsSelected.get(j).getId()==btn.getId()){
                            ltsSelected.get(j).setR(lista[0]);
                            ltsSelected.get(j).setG(lista[1]);
                            ltsSelected.get(j).setB(lista[2]);
                        }
                    }
                }else{
                    //agrego
                    ltsSelected.add(ltsBPictogram.get(i));
                }
            }
        }
    }

    public void guardarColorTodos(String colores){

        for(int i = 0; i<ltsBPictogram.size();i++){
                String lista[]=colores.split(",");
                ltsBPictogram.get(i).setR(lista[0]);
                ltsBPictogram.get(i).setG(lista[1]);
                ltsBPictogram.get(i).setB(lista[2]);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

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
        for(int i=0;i<ltsBPictogram.size();i++){
            if(btn.getId()==ltsBPictogram.get(i).getId()){
                ltsBPictogram.get(i).setCdoPic(idpic);
                ltsBPictogram.get(i).setNomPic(nompic);
            }
        }
    }

    public boolean verificacionModo1(){
        boolean verificacion=true;

        return verificacion;
    }

    public boolean verificacionModo2Pictogramas(){
        boolean verificacion=true;
        for(int i=0;i<ltsBPictogram.size();i++){
            if(ltsBPictogram.get(i).getCdoPic()==null||
                    ltsBPictogram.get(i).getCdoPic().equals("")){
                verificacion=false;
            }
        }
        return verificacion;
    }
    public boolean verificacionModo2Color(){
        boolean verificacion=true;

            if(ltsBPictogram.get(0).getR()==null||
                    ltsBPictogram.get(0).getR().equals("")){
                verificacion=false;
            }

        return verificacion;
    }

    public String crearCadena(){
        String cad ="";
        if(modePlay.equals("1")){
            cad = cad+codModule+","+mode+","+ltsSelected.size()+",";
            for(int i = 0;i<ltsSelected.size();i++){
                cad=cad+ltsSelected.get(i).getR()+";"+ltsSelected.get(i).getG()+";"+ltsSelected.get(i).getB()+";";
            }

            cad= cad.substring(0,cad.length()-1);
            cad = cad+",";
            for(int i = 0;i<ltsSelected.size();i++){
                cad=cad+ltsSelected.get(i).getNombreBoton()+";";
            }

            cad= cad.substring(0,cad.length()-1);
            cad = cad+",";
            for(int i = 0;i<ltsSelected.size();i++){
                cad=cad+ltsSelected.get(i).getNomPic()+";";
            }

            cad= cad.substring(0,cad.length()-1);
            cad = cad+",";
            for(int i = 0;i<ltsSelected.size();i++){
                cad=cad+ltsSelected.get(i).getCdoPic()+".mp3;";
            }

            cad= cad.substring(0,cad.length()-1);
            cad = cad+","+modePlay;/// es asi cad = cad+",1";
        }else{
            cad = cad+codModule+","+mode+","+ltsBPictogram.size()+",";
            for(int i = 0;i<ltsBPictogram.size();i++){
                cad=cad+ltsBPictogram.get(i).getR()+";"+ltsBPictogram.get(i).getG()+";"+ltsBPictogram.get(i).getB()+";";
            }

            cad= cad.substring(0,cad.length()-1);
            cad = cad+",";
            for(int i = 0;i<ltsBPictogram.size();i++){
                cad=cad+ltsBPictogram.get(i).getNombreBoton()+";";
            }

            cad= cad.substring(0,cad.length()-1);
            cad = cad+",";
            for(int i = 0;i<ltsBPictogram.size();i++){
                cad=cad+ltsBPictogram.get(i).getNomPic()+";";
            }

            cad= cad.substring(0,cad.length()-1);
            cad = cad+",";
            for(int i = 0;i<ltsBPictogram.size();i++){
                cad=cad+ltsBPictogram.get(i).getCdoPic()+".mp3;";
            }

            cad= cad.substring(0,cad.length()-1);
            cad = cad+","+modePlay;/// es asi cad = cad+",1";
        }
        Log.i("log picto"," "+cad);
        return  cad;
    }

    Dialog dialog;
    public void dialogoEnvio(final String mensaje){


        dialog = new Dialog(ModulePictograms.this,R.style.Theme_Dialog_Translucent);
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

    public String colorAleatorio(){
        return  ""+(int) (Math.random()*254+1);
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
