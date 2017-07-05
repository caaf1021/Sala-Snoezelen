
 // Librerias I2C para controlar el mpu6050
// la libreria MPU6050.h necesita I2Cdev.h, I2Cdev.h necesita Wire.h
#include "I2Cdev.h"
#include "MPU6050.h"
#include "Wire.h"

// La dirección del MPU6050 puede ser 0x68 o 0x69, dependiendo 
// del estado de AD0. Si no se especifica, 0x68 estará implicito
MPU6050 sensor;

// Valores RAW (sin procesar) del acelerometro  en los ejes x,y,z

int ax, ay, az;

// valores del giroscopio para plicar el filtro
int gx, gy, gz;

//Array to store the values of random number produced by the sensor 
   int  stable = 80; //valor de control de estabilizacion
   float valuesx[80];
   float valuesy[80];
   float valuesz[80];
    int valuescontrol=0;
    
//Array to store the values for tha validation of face

   int  validation = 350; //valor de control de estabilizacion
   float valuesxs[350];
   float valuesys[350];
   float valueszs[350];
   int valuestable=0;
//

// values of reference face
// Face 1
float face1[]={-25,25,-25,25,128,178}; // valor de referencia lado 1 mx 0 tx 1 my 2 ty 3 mz 4 tz 5

//Face 2
float face2[]={100,150,-25,25,0,50}; // valor de referencia lado 2 mx 0 tx 1 my 2 ty 3 mz 4 tz 5 

//Face 3
float face3[]={-25,25,-150,-100,0,50}; // valor de referencia lado 3 mx 0 tx 1 my 2 ty 3 mz 4 tz 5 

//Face 4
float face4[]={-25,25,100,150,0,50}; // valor de referencia lado 4 mx 0 tx 1 my 2 ty 3 mz 4 tz 5 

//Face 05
float face5[]={-150,-100,-25,25,0,50}; // valor de referencia lado 5 mx 0 tx 1 my 2 ty 3 mz 4 tz 5 

// Face 6
float face6[]={-25,25,-25,25,-125,-75}; // valor de referencia lado 5 mx 0 tx 1 my 2 ty 3 mz 4 tz 5 

// Iluminacion de las caras en RGB

int pinrgb[]={
              28,30,32,  // rgb lado1 0 1 2 
              22,24,26,  // rgb lado2 3 4 5
              49,51,53,  // rgb lado3 6 7 8
              46,44,42, // rgb lado4 9 10 11
              52,50,48, // rgb lado5 12 13 14
              43,45,47}; // rgb lado6 15 16 17


char *myStrings[5]; // alamcena la cadena recibida máximo 5 parámetros
char * coloresrgb[18];
int colores[18]; // alamacea los colores
bool encendido=false;
String ok=""; // valida que la cadena llegue completa
String nombremo=""; // alamacenara el ID de módulo
String metodoString=""; // identifica el metodo invocado
int metodo=0; // alamacenara el codigo que identifica la atividad a realizar
String timeString=""; // identifica el metodo invocado
int time; // alamacenara el codigo que identifica la atividad a realizar
bool ubicar;



// variables para perdida
int pr1=0;
int pg1=0;
int pb1=0;

String a="";//Recibe la informacion del serial

int ubicacion=7;  //alamacena la ultima ubicacion  

int errorA=0; //valida que solo ingrese a un error;


void setup() {
  Serial.begin(9600);    //Iniciando puerto serial
  Wire.begin();           //Iniciando I2C  
  sensor.initialize();    //Iniciando el sensor

    
  //valida que el sensor mpu6050 inicie correctamente
  if (sensor.testConnection()) Serial.println("M03 Dado iniciado correctamente");
  else Serial.println("M03 Error al iniciar dado");
}

void loop() {

 // String a="M03,1,255;255;255;255;255;255;255;255;255;255;255;255;255;255;255;255;255;255,delay,ok";
    //a="M03,1,255;0;0;0;255;0;0;0;255;255;255;255;255;0;255;255;255;0,1000,ok";
 ///  a="M03,3,0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0,0,ok";
    //String a="M03,1,0;255;0;0;255;0;0;255;0;0;255;0;0;255;0;0;255;0";
  // String a="M03,1,0;0;255;0;0;255;0;0;255;0;0;255;0;0;255;0;00;255";
  
      a=Serial.readString();

      
      if(a != ""){
      char sz[500];  
      a.toCharArray(sz, 500);
      char *p = sz;
      char *str;
      
      // divide la cadena recibida en base a ',' máximo llegara 4 parámetros
      for(int i=0;i<5;i++){
      str = strtok_r(p, ",", &p);
      myStrings[i]=str;
      }
      nombremo=myStrings[0];
      ok=myStrings[4];
       }
      else
      {
         nombremo = "Nocambio";
       }

       
  
      if(nombremo =="M03"){
     
      if(ok.equals(ok))
      {
      metodoString=myStrings[1];
      metodo = metodoString.toInt();
      timeString=myStrings[3];
      time = timeString.toInt();
      ubicarColores();
      }
      else
      {
       errorA=0;
       metodo=3;
       }
      
    }

      if(metodo==1)
      {
        asignarColores();
        valuescontrol=0;
        valuestable=0;
        errorA=0;
        ubicar=true;
        terapia(time); 
    
      }
      
      if(metodo==2)
      { 
        demostracion(2500);
        errorA=0;
      }
      
      if (metodo==3)
      {
       desactivarRGB();
       pr1=0;
       pg1=0;
       pb1=0;    
       valuescontrol=0;
       valuestable=0;
       ubicar=false;
       if (errorA==0)
       {
        error();
        errorA=1;
       }
        
      }
      if(metodo==0)
      {
        error();
        errorA=0;
      }
  
  
}
  
/**
 * demostracion
 * Enciende el dado en colores randomicos
 */
void demostracion(int time)
{


  randomSeed(millis());
  int demorandom = 0 ;

  for(int i = 0 ;i<18;i++){
    demorandom = random(255);
    colores[i] = demorandom;
  }
      pr1=colores[0];
      pg1=colores[1];
      pb1=colores[2];    

  asignarColores();
  delay(time);
  desactivarRGB();
  rgb(pinrgb[0],pinrgb[1],pinrgb[2],pr1,pg1,pb1);
  delay(time);
  desactivarRGB();
  rgb(pinrgb[3],pinrgb[4],pinrgb[5],colores[3],colores[4],colores[5]);
  delay(time);
  desactivarRGB();
  rgb(pinrgb[6],pinrgb[7],pinrgb[8],colores[6],colores[7],colores[8]);
  delay(time);
  desactivarRGB();
  rgb(pinrgb[9],pinrgb[10],pinrgb[11],colores[9],colores[10],colores[11]);
  delay(time);
  desactivarRGB();
  rgb(pinrgb[12],pinrgb[13],pinrgb[14],colores[12],colores[13],colores[14]);
  delay(time);
  desactivarRGB();
  rgb(pinrgb[15],pinrgb[16],pinrgb[17],colores[15],colores[16],colores[17]);
  delay(time);
  desactivarRGB();

      pr1=0;
      pg1=0;
      pb1=0;    
  
}
/**
 * ubicarColores()
 * lee los colores recibidos y los ubica en una matriz para su posterior utilización.
 */
void ubicarColores()
{
  
       
      String rgb= myStrings[2];
      char sz[100];  
      rgb.toCharArray(sz, 100);
      char *p = sz;
      char *str;
      for(int i=0;i<18;i++){
        str = strtok_r(p, ";", &p);
        coloresrgb[i]=str;
       }

      for(int i=0;i<18;i++){
        String coloresrgbs =coloresrgb[i];
        colores[i] = coloresrgbs.toInt();
      }

      pr1=colores[0];
      pg1=colores[1];
      pb1=colores[2];      
    
}

/**
 * Activa el error cuando el mensaje no llega completo
 */
void error()
{
  activarRGB(255,0,0);
  delay(500);
  desactivarRGB();
  delay(500);
  activarRGB(255,0,0);
  delay(500);
  desactivarRGB();
 
  
}

/*
 *terapia() 
 *Buscara la ubicacion del dado en cuanto este activado.
 */
void terapia(int time)
{
  while (ubicar==true)
   { 
    
  // Leer las aceleraciones del sensor
  sensor.getAcceleration(&ax, &ay, &az);

/**
 * Caluclo de los angulos obtenidos del acelerometro basados en formula.
 */
  float angx=ax/131;
  float angy=ay/131;
  float angz=az/131;

 /*
  Serial.println(angx);
  Serial.println(angy);
  Serial.println(angz);
  delay(1000);
 */ 
  // carga en el vector los valores de referencia 
  
  if (valuescontrol<=stable)
  {
     valuesx[valuescontrol] = angx;
     valuesy[valuescontrol] = angy;
     valuesz[valuescontrol] = angz;
     valuescontrol++;
    
  }
    else
  {
   
      validationarrays();
  }
  
  if(valuescontrol>stable and valuestable<=validation)
  {
     valuesxs[valuestable] = angx;
     valuesys[valuestable] = angy;
     valueszs[valuestable] = angz;
     valuestable++;
    // Serial.println(valuestable);
  }

  if (valuescontrol>stable and valuestable>validation)
  {
       int locate=locatedfaced();
       if (locate==0)
       {
        
        valuescontrol=0;
        valuestable=0;
          
       }
       else
       {  
          if(ubicacion==locate)
          {

          ubicar=false; 
          }
          else
          {
          if(locatedfaced()==1)
          {
          desactivarRGB();
          rgb(pinrgb[0],pinrgb[1],pinrgb[2],pr1,pg1,pb1);
          Serial.println("M06,1,"+String(pr1)+";"+String(pg1)+";"+String(pb1));
          delay(time); 
          ubicacion=1;  
          ubicar=false;
          }
          if(locatedfaced()==2)
          {
          desactivarRGB();
          rgb(pinrgb[3],pinrgb[4],pinrgb[5],colores[3],colores[4],colores[5]);
          Serial.println("M06,1,"+String(colores[3])+";"+String(colores[4])+";"+String(colores[5]));
          delay(time);
          ubicacion=2;  
          ubicar=false;
          
          }
          if(locatedfaced()==3)
          {
          desactivarRGB();
          rgb(pinrgb[6],pinrgb[7],pinrgb[8],colores[6],colores[7],colores[8]);
          Serial.println("M06,1,"+String(colores[6])+";"+String(colores[7])+";"+String(colores[8]));
          delay(1000);
          ubicacion=3;  
          ubicar=false;
          }
          if(locatedfaced()==4)
          {
          desactivarRGB();
          rgb(pinrgb[9],pinrgb[10],pinrgb[11],colores[9],colores[10],colores[11]);
          Serial.println("M06,1,"+String(colores[9])+";"+String(colores[10])+";"+String(colores[11]));
          delay(time);
          ubicacion=4;  
          ubicar=false;
          }
          if(locatedfaced()==5)
          {
          desactivarRGB();
          rgb(pinrgb[12],pinrgb[13],pinrgb[14],colores[12],colores[13],colores[14]);
          Serial.println("M06,1,"+String(colores[12])+";"+String(colores[13])+";"+String(colores[14]));
          delay(time);
          ubicacion=5;  
          ubicar=false;
          }
          if(locatedfaced()==6)
          {
          desactivarRGB();
          rgb(pinrgb[15],pinrgb[16],pinrgb[17],colores[15],colores[16],colores[17]);
          Serial.println("M06,1,"+String(colores[15])+";"+String(colores[16])+";"+String(colores[17]));
          delay(time);
          ubicacion=6;  
          ubicar=false;
          }
          }
          
         
       }
  }

  }
}




/**
 * locatedfaced
 * valida los valores de angulo para obtener la pocicion del dado
 */


int locatedfaced()
{
  int res=0;

  float sumx=0;
  float sumy=0;
  float sumz=0;
  
   for (int i=0;i<validation;i++)
  {
    sumx=sumx+valuesxs[i];
    sumy=sumy+valuesys[i];
    sumz=sumz+valueszs[i];
  }

  // se obtiene los promedios de los valores 
  float meanx=sumx/validation;
  float meany=sumy/validation;
  float meanz=sumz/validation;

   // validation face 1 
  if ((meanx>face1[0] and meanx<face1[1]) and (meany>face1[2] and meany<face1[3]) and (meanz>face1[4] and meanz<face1[5]))
  {     res=1;}

  // validation face 2 

  if ((meanx>face2[0] and meanx<face2[1]) and (meany>face2[2] and meany<face2[3]) and (meanz>face2[4] and meanz<face2[5]))
  {     res=2;}
  

  // validation face 3

  if ((meanx>face3[0] and meanx<face3[1]) and (meany>face3[2] and meany<face3[3]) and (meanz>face3[4] and meanz<face3[5]))
  {      res=3; }


  // validation face 4 

  if ((meanx>face4[0] and meanx<face4[1]) and (meany>face4[2] and meany<face4[3]) and (meanz>face4[41] and meanz<face4[5]))
  {     res=4;}

  // validation face 5 

  if ((meanx>face5[0] and meanx<face5[1]) and (meany>face5[2] and meany<face5[3]) and (meanz>face5[4] and meanz<face5[5]))
  {      res=5; }

  // validation face 6 

  if ((meanx>face6[0] and meanx<face6[1]) and (meany>face6[2] and meany<face6[3]) and (meanz>face6[4] and meanz<face6[5]))
  {
      res=6;
   
  }

  return res;
}

/**
 * valida los valores en los array de x y y para verificar que se estabilise el sensor
 */
boolean validationarrays()
{
  boolean res = false;
  float sumx=0;
  float sumy=0;
  float sumz=0;  

  for (int i=0;i<stable;i++)
  {
    sumx=sumx+valuesx[i];
    sumy=sumy+valuesy[i];
    sumz=sumz+valuesz[i];
  }

  float meanx=sumx/stable;
  float meany=sumy/stable;
  float meanz=sumz/stable;


   

  //resta los promedios con un valor en absuto la resta debe ser minina (0 o 1) para afirmar que el valor esta estable  
  float value = abs(meanx-valuesx[stable-67])+abs(meany-valuesy[stable-49])+abs(meanz-valuesz[stable-53]);

   if (value<=3)
   {
    res=true;
   }
   else
   {
    valuescontrol=0;
    res=false;
    
   }

  return res;
}

/**
 * activarRGB
 * Activa de un color unico todas las caras del lado
 */

void activarRGB(int r,int g,int b)
{
  rgb(pinrgb[0],pinrgb[1],pinrgb[2],r,g,b);
  rgb(pinrgb[3],pinrgb[4],pinrgb[5],r,g,b);
  rgb(pinrgb[6],pinrgb[7],pinrgb[8],r,g,b);
  rgb(pinrgb[9],pinrgb[10],pinrgb[11],r,g,b);
  rgb(pinrgb[12],pinrgb[13],pinrgb[14],r,g,b);
  rgb(pinrgb[15],pinrgb[16],pinrgb[17],r,g,b);
}

/**
 * desactivarRGB
 * manda a apagar tadas las caras del dado
 */

void desactivarRGB()
{
  rgb(pinrgb[0],pinrgb[1],pinrgb[2],0,0,0);
  rgb(pinrgb[3],pinrgb[4],pinrgb[5],0,0,0);
  rgb(pinrgb[6],pinrgb[7],pinrgb[8],0,0,0);
  rgb(pinrgb[9],pinrgb[10],pinrgb[11],0,0,0);
  rgb(pinrgb[12],pinrgb[13],pinrgb[14],0,0,0);
  rgb(pinrgb[15],pinrgb[16],pinrgb[17],0,0,0);
}

/**
 * asignarColores
 * enciende todas las caras del dado en base a colores establecidos en el vector colores
 */

void asignarColores(){
  rgb(pinrgb[0],pinrgb[1],pinrgb[2],pr1,pg1,pb1);
  rgb(pinrgb[3],pinrgb[4],pinrgb[5],colores[3],colores[4],colores[5]);
  rgb(pinrgb[6],pinrgb[7],pinrgb[8],colores[6],colores[7],colores[8]);
  rgb(pinrgb[9],pinrgb[10],pinrgb[11],colores[9],colores[10],colores[11]);
  rgb(pinrgb[12],pinrgb[13],pinrgb[14],colores[12],colores[13],colores[14]);
  rgb(pinrgb[15],pinrgb[16],pinrgb[17],colores[15],colores[16],colores[17]);
  
  
}

/**
 * rgb
 * Metodo unico que permite activar o desactivar una a una las caras del dado
 */
void rgb(int pr, int pg, int pb, int r, int  g, int b)
{
  analogWrite(pr,r); 
  analogWrite(pg,g); 
  analogWrite(pb,b); 
}  
