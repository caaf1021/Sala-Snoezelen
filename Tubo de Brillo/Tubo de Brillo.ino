 
/**
 * tubo de brillo
 */


 /**
  * variables tube 1
  */
int pinColores[]={
        13,12,11, // led motor 1 red green blue 
        10,9,8 // led motor 2 red green blue 
          };


int bot1=2;  // señal de pulsante 1
int bot2=3; // señal de pulsante 2

int mot1=6;  // varibale de activacion motor 1
int mot2=7; // variable par activar motor 2

int p1=0; // recibe la señal del pulsante 1
int p2=0; // recibe la señal del pulsante 2


char *myStrings[6]; // alamcena la cadena recibida máximo 6 parámetros

char * coloresrgb[6]; // alamaceno los colores enviados maximo 6
int colores[6]; // alamacea los colores
bool encendido=false; 
String nombremo=""; // alamacenara el ID de módulo
String ok=""; // valida que la cadena llegue completa

String metodoString=""; // identifica el metodo invocado
int metodo=0; // alamacenara el codigo que identifica la atividad a realizar

String tamString=""; // identifica el metodo invocado
int tam=0; // alamacenara el codigo que identifica la atividad a realizar

String a=""; // cadena que lee del serial.
String b=""; // cadena que lee del serial para proceso en el modulo

int errorA=0; //valida que solo ingrese a un error;


void setup() {
  
  
pinMode(bot1,INPUT);
pinMode(bot2,INPUT);
pinMode(mot2,OUTPUT);
pinMode(mot1,OUTPUT);

// Iniciar serial monitoreo
Serial.begin(9600);


}


void loop() {
    
   
     procesoSerial();
     
     if(metodo==1)
     {
     
     tamString=myStrings[4];
     tam = tamString.toInt();
     terapia();
     metodo=3;
     }
      
      if(metodo==2)
      {
      demostracion();  
      errorA=0;
      }
   
      if(metodo==3)
      {
        
       desactivarRGB(0,1,2);
       desactivarMotor(mot1);
       
       desactivarRGB(3,4,5);
       desactivarMotor(mot2);
    
        if (errorA==0)
         {
           error();
           errorA=1;
          }
  
      }
  
      if(metodo==4)
      {
      errorA=0;
      asignarColores();    
      iluminacion();
       }

      if(metodo==5)
      {
      errorA=0;
      asignarColores();    
      activar();
      }

      if(metodo==0)
      {
        error();
        
      }


}



void terapia()
{
      char * sec[tam]; // alamaceno los valores de secuencia enviados maximo 6
      int secuencia[tam];   // alamaceno los valores de secuencia maximo 6
      int total=1; // valida que cumpla el el total de la secuencia
      
            
      String s= myStrings[3]; //obtiene la secuencia
      char sz[100];   // 
      s.toCharArray(sz, 100);
      char *p = sz;
      char *str;
    
      // Extrae los valores de la secuencia
      for(int i=0;i<tam;i++){
        str = strtok_r(p, ";", &p);
        sec[i]=str;
       }

       //pasa a entero los valores
      for(int i=0;i<tam;i++){
        String sect =sec[i];
        secuencia[i] = sect.toInt();
       }

      while(total<(tam+1))
      {
          
          int validar[total];   // alamaceno los colores enviados maximo 6
          int contador=0;  // para validar la secuencia
    
          for (int j=0;j<total;j++)
          {

          
            if(secuencia[j]==1)
             {
             activarRGB(0,1,2,colores[0],colores[1],colores[2]); 
             activarMotor(mot1);
             delay(4000); 
             desactivarRGB(0,1,2);
             desactivarMotor(mot1);
             delay(500);
             
             }
             if(secuencia[j]==2)
             {
             activarRGB(3,4,5,colores[3],colores[4],colores[5]);
             activarMotor(mot2);
             delay(4000);
             desactivarRGB(3,4,5);
             desactivarMotor(mot2);
             delay(500);
             }
          }


            
            int c=0; // valida que cumpla que toda la secuencia enviada
            while(c<total)
             {       
              
              p1=digitalRead(bot1);
              p2=digitalRead(bot2);
              
              if (p1 == 1)  // valida que se active el pulsante del motor 1
              {  
              
               activarRGB(0,1,2,colores[0],colores[1],colores[2]); 
               activarMotor(mot1);
               delay(3000);
               desactivarRGB(0,1,2);
               desactivarMotor(mot1);
               validar[c]=1;
               c++;
               delay(500);
               
              }
             
            
               if (p2 == 1) // valida que se active el pulsante del motor 2
               {  
               
                activarRGB(3,4,5,colores[3],colores[4],colores[5]);
                activarMotor(mot2);
                delay(3000);
                desactivarMotor(mot2);
                desactivarRGB(3,4,5); 
                validar[c]=2;
                c++;
                delay(500);
                
                }
                     
             }
            
            for (int k=0;k<total;k++)
             {
              if(secuencia[k]==validar[k])
              {
                contador++;
              }
             }

          if(contador==total)
           {
            total++;
           }
           else
            {
              error();
              if(total>1)
              {
                total--;
              }
              
            }

           bool flag=procesoSerial();
                     
            if(flag==true)
            {
   
              total=tam+tam;
              errorA=0;
   
            } 
           
      }

      if(total==(tam+1))
      {
       activarRGB(0,1,2,255,255,255); 
       activarRGB(3,4,5,255,255,255);
       delay(1000);
       desactivarRGB(0,1,2);
       desactivarRGB(3,4,5);
       errorA=1;
      }
      
      
      
}

//activa el metodo 4
void iluminacion()
{
  int c=1;

             while(c==1)
             {
                 activarRGB(3,4,5,colores[3],colores[4],colores[5]);
                 activarMotor(mot2);
                 activarRGB(0,1,2,colores[0],colores[1],colores[2]); 
                 activarMotor(mot1);

                 bool flag=procesoSerial();
                  if(flag==true)
                  { c=c+2;     
                     desactivarRGB(0,1,2);
                     desactivarMotor(mot1);
                     desactivarRGB(3,4,5);
                     desactivarMotor(mot2);
                      
                  } 
            
             }
    

    
  
}

bool procesoSerial()
{
    bool res=false;
    
    a=Serial.readString();

   // Serial.println(a);
    if(a != ""){
    char sz[500];  
    a.toCharArray(sz, 500);
    char *p = sz;
    char *str;
    
    // divide la cadena recibida en base a ',' máximo llegara 4 parámetros
    for(int i=0;i<6;i++){
    str = strtok_r(p, ",", &p);
    myStrings[i]=str;
    }
    nombremo = myStrings[0];
    ok=myStrings[5];
    }else{
       nombremo = "Nocambio";
    }

     
      if(nombremo =="M04")
      {
        b=a;
        char sz[500];  
        b.toCharArray(sz, 500);
        char *p = sz;
        char *str;
    
        // divide la cadena recibida en base a ',' máximo llegara 4 parámetros
        for(int i=0;i<6;i++){
        str = strtok_r(p, ",", &p);
        myStrings[i]=str;
        }

        
        if(ok.equals(ok))
        {
        res=true;  
        metodoString=myStrings[1];
        metodo = metodoString.toInt();
        asignarColores();  
        }
        else
        {
        errorA=0;
        metodo=3;
        }
    
      }
  return res;
}



/**
 * Activa todo el mecanimos con pulsantes metodo 5
 */

void activar()
{

             int c=1;

             while(c==1)
             {
               
              bool act=true;
              while(act)
            
              {
              p1=digitalRead(bot1);
              p2=digitalRead(bot2);
              
              if (p1 == 1)  // valida que se active el pulsante del motor 1
              {                  
               activarRGB(0,1,2,colores[0],colores[1],colores[2]); 
               activarMotor(mot1);
               act=false;
               
              }
              else
              {
              desactivarRGB(0,1,2);
              desactivarMotor(mot1);  
              }
             
            
               if (p2 == 1) // valida que se active el pulsante del motor 2
               {  
               activarRGB(3,4,5,colores[3],colores[4],colores[5]);
               activarMotor(mot2);
               act=false;
               }
               else
               {
                desactivarRGB(3,4,5);
                desactivarMotor(mot2);
               }
              }

              bool flag=procesoSerial();
               if(flag==true)
                { c=c+2;     
                } 
              
             }
              
}

/**
 * lee la informacion enviada y carga los valores RGB asignados al modulo
 */
void asignarColores()
{

      String coloresrgbs;
    
      String rgb= myStrings[2];
      char sz[100];  
      rgb.toCharArray(sz, 100);
      char *p = sz;
      char *str;
      for(int i=0;i<6;i++){
        str = strtok_r(p, ";", &p);
        coloresrgb[i]=str;
       }


       for(int i=0;i<6;i++){
        String coloresrgbs =coloresrgb[i];
        colores[i] = coloresrgbs.toInt();
       }

      
// variables para colores led rgb 1
int r1=colores[0];
int g1=colores[1];
int b1=colores[2];

// variables para colores led rgb 2
int r2=colores[3];
int g2=colores[4];
int b2=colores[5];
    
         
      
}

/**
 * error
 * se activara avisando que la señal serial no ha llegado correctamente
 */
void error()
{
       activarRGB(0,1,2,255,0,0); 
       activarRGB(3,4,5,255,0,0);
       delay(500);
       desactivarRGB(0,1,2);
       desactivarRGB(3,4,5);
       delay(500);
       activarRGB(0,1,2,255,0,0); 
       activarRGB(3,4,5,255,0,0);
       delay(500);
       desactivarRGB(0,1,2);
       desactivarRGB(3,4,5);
    
}

/**
 * demostracion
 * secuencia de luces y motores que se activara para demostracion
 */
void demostracion()
{
     
   randomSeed(millis());
  int demorandom = 0 ;

  for(int i = 0 ;i<6;i++){
    demorandom = random(255);
    colores[i] = demorandom;
  }
   
   activarRGB(0,1,2,colores[0],colores[1],colores[2]); 
   activarMotor(mot1);
   delay(10000);     
   desactivarRGB(0,1,2);
   desactivarMotor(mot1);
   delay(1000);     
   activarRGB(3,4,5,colores[3],colores[4],colores[5]);
   activarMotor(mot2);
   delay(10000);     
   desactivarRGB(3,4,5);
   desactivarMotor(mot2);
   delay(1000);     
   activarRGB(0,1,2,colores[0],colores[1],colores[2]); 
   activarMotor(mot1);
   activarRGB(3,4,5,colores[3],colores[4],colores[5]);
   activarMotor(mot2);
   delay(10000);
   desactivarRGB(0,1,2);
   desactivarMotor(mot1);
   desactivarRGB(3,4,5);
   desactivarMotor(mot2);
  
}


/**
 * activarMotor
 * enciende el motor del tubo asigando
 */
void activarMotor(int mot)
{
  digitalWrite(mot,HIGH);
}


/**
 * desactivarMotor
 * apaga el motor del tubo especificado
 */
void desactivarMotor(int mot)
{
  digitalWrite(mot,LOW);
}

/**
 * activarRGB
 * enciende la iluminción del tubo
 */
void activarRGB(int pr, int pg, int pb, int r, int g, int b)
{
   analogWrite(pinColores[pr],r);  
   analogWrite(pinColores[pg],g);  
   analogWrite(pinColores[pb],b);
}

/**
 * desactivarRGBtubo
 * apaga la iluminción del tubo
 */
void desactivarRGB(int pr, int pg, int pb)
{
   analogWrite(pinColores[pr],0);  
   analogWrite(pinColores[pg],0);  
   analogWrite(pinColores[pb],0);
}


