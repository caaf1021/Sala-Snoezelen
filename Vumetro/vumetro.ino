int colores[]={2,3,4, //Declarar en un vector de tipo entero  con los pines que se utilizaran para encender las luces
               5,6,7,
               8,9,10,
               11,12,13,
               22,23,24,
               25,26,27,
               28,29,30,
               31,32,33};
               
      
int rangos[] = {2,4,6,8,10,11,12,14,500};// Declaramos y inicializamos el vactor rangos donde se establecen los los valores de los rangos para proceder a encender cada nivel
  


int datos[27];// Dleclaramos una vector datos donde se almacenaran los valores recibidos desde el servidor
int ta=8;//
int tiempo=30;// Declaramos y inicializamos la variable tiempo, la cual definira cuanto tarda en cencender cada piso del vumetro
int voz=A0;// Declaramos la variable voz  doinde se recibiran los datos analogicos correspondientes al pin A0
int sonido=0;// Declaramos la variable sonido donde se recibira los datos recibidos dessde el microfono
int tipo=0;// Recibimos el valor de los rangos
int bandera=1;// Declaramos una variable bandera el cual se cargara el valor anterior
String comprueba="";//Declaramos la variable comprueba donde se guardara la informacion del ultimo valor de la cadema recibida por el servidor




void setup() {
       Serial.begin(9600);
    
       for(int l=0;l< 24;l++)
      {
        pinMode(colores[l],OUTPUT);
      }


  
}


void loop() {



      //***************************************************************************************************************

   // String a = "M05,1,1,255,255,255,255,0,0,0,255,0,0,0,255,255,255,0,255,0,255,0,255,255,255,100,160,ok";
   if (Serial.available() > 0) {// Comprobamos si existen datos en el serial para ser leidos
  String a=Serial.readString();// Recibir los datos del serial en la variable a
    String moduleId ="";//
    moduleId=a.substring(0, 3);
    
 if(moduleId=="M05"){
 
    char sz[200];  
      a.toCharArray(sz, 500);
      char *p = sz;
      char *str;
      char *myStrings[27];
      

      for(int i=0;i<28;i++){
          str = strtok_r(p, ",", &p);
          myStrings[i]=str;
      }
  
        moduleId=myStrings[0];

        comprueba=myStrings[27];
       
      // Convertir a entero
      for(int i=1;i<27;i++){
          String b="";
          b=myStrings[i];
          datos[i-1]=b.toInt();
      }
      
        
 }
 
   }
 
  

     
//************************************************************************************************************************



    if(comprueba.equals("ok")){

         
            //MODULO ENCENDIDO
          if( datos[0]==1){// Realizamos la comprobacion donde datos de la poscion [0] retresenta al modo de funcionamiento del vumetro en este caso es un modo encendido
            
                 terapia();// llamamos al metodo terapia
          }
          
      
      
           //MODULO EN DESMOSTTRACION
        
           if(datos[0]==2){//Realizamos la comprobacion donde datos de la poscion [0] retresenta al modo de funcionamiento del vumetro en este caso es un modo demostracion
                  demostracion();// llamamos al metodo demostracion
              }
           
     
          
    }

        else{// si existe error en los datos
            
           for(int l=0;l<= 24;l=l+3)
            {
              color(colores[l],colores[l+1],colores[l+2],255,0,0);//Encender todas las luces de color rojo, de acuerdo a los pines del vector "colores"
            }
             
              delay(100);// esperar 100 milisegundos
          
            for(int l=0;l<=24;l=l+3)
            {
              color(colores[l],colores[l+1],colores[l+2],0,0,0);//Apagar todas las luces, de acuerdo a los pines del vector "colores"
            }
       
      
    }


  

}









//MODULO ENCENDIDO

void terapia(){//Encaso de que el modulo este encendido para iniciar la terapia
    sonido=analogRead(voz);//Leer la entrada analogica
    sonido = map(sonido,0,1023,0,300);// pasar a un rando de sonido entre 0 y 300
    tipo=rango(sonido);
             
          
  //Si decea q cambie la voz (VOZ BAJA)
    if(datos[1]==1 && sonido >0){// En caso de que se decee una voz baja, el valor del vector datos[1] sera de 1
        tipo=(10-tipo)+1;// Invertimos el rango del sonido leido
     }  
        
    encendido(tipo);// Llamar al metodo para encender las luces 
}






//MODULO DEMOSTRACION

void demostracion(){ // Llamar al modo desotracion, las luces se encenderan de acuerdo a un orden que se explicara mas adelante

  //Crear colores aleatoreos
  randomSeed(millis());// Inicializamos la semilla
  int demorandom = 0 ;// definimos la variable en la que se creara el random

  for(int i = 2 ;i<26;i++){// Recorremos el vector datos desde la posicion dos que es desde donde se establecen los colores ya que en modo demostracion no se enviaran colores definidos desde la tablet
    demorandom = random(255);//Dterminamos el valor del color
    datos[i] = demorandom;// Designamos el valor  del color al vector de datos
  }

  

  
   int demo[] = {2,6,5,8,3,5,2,7,3,8,7,6,5,1,6}; // determinamos un vector con diferentes valores los cuales mas adelante encenderan las luces 
   for(int i=0; i<15; i++){// Recorremos el vector antes creado
      encendido(demo[i]);//llamamos al metodo encendido para proceder a encender las luces
    } 

  
}








//METODO PARA DETERMINA LOS RANGOS
int rango(int soni){
  
       int valor=0;//definimos una variable temporal

       for(int l=0;l< ta ;l++)//recorrenos el vector de rangos 
      {
          if(soni >=rangos[l] && soni < rangos[l+1]){// Realizamos una comparacion de los valores existemntes en el vector
              valor=l+1;   //Estableces el nuemero de luces que se debe encender
           }    
      }
   
        return valor; // Retornamos el valor               
}


//DETERMINAR COLOR
void color(int r, int v, int a, int rojo,int verde,int azul){// Recibimos losparametros tanto de los pines como de los colores
   
    analogWrite(r,rojo);// Definimos el color rojo
    analogWrite(v,verde);// Definimos el color verde
    analogWrite(a,azul);// Definimos el color azul

}











///ENCENDER LED
void encendido(int var){// Recibir como parametro el valor referente al numero de luces que se deberan encender

  if(bandera<var){// Comprobamos si el valor anteriar es menor al valor recibido
           for(int i=1; i<=var; i++){// Recorremos hasta que se encienda el numero de escalones necesarios
                switch (i) {// Encenderemos los escalones hasta que el for se termine
                  case 1:
                  delay(tiempo);// Esperamos un tiempo establecido en la variable tiempo
                      color(colores[0],colores[1],colores[2],datos[2],datos[3],datos[4]);// llamamos al metodo color donde enviamos como parametros los pines de acuerdo al vector color y los colores establecidos en el vector datos
                          
                      break;
                  case 2:
                  delay(tiempo);
                      color(colores[3],colores[4],colores[5],datos[5],datos[6],datos[7]);
                      break;
                  case 3:
                  delay(tiempo);
                      color(colores[6],colores[7],colores[8],datos[8],datos[9],datos[10]);
                      break;
                      
                  case 4:
                  delay(tiempo);
                      color(colores[9],colores[10],colores[11],datos[11],datos[12],datos[13]);
                      break;
              
                  case 5:
                  delay(tiempo);
                     color(colores[12],colores[13],colores[14],datos[14],datos[15],datos[16]);
                      break;    
              
                  case 6:
                  delay(tiempo);
                      color(colores[15],colores[16],colores[17],datos[17],datos[18],datos[19]);
                      break;
              
                  case 7:
                  delay(tiempo);
                     color(colores[18],colores[19],colores[20],datos[20],datos[21],datos[22]);
                      break;
              
              
                  case 8:
                  delay(tiempo);
                    color(colores[21],colores[22],colores[23],datos[23],datos[24],datos[25]);
                      break;
                  
                  default:
                      break;
                }
  
        }
    
  }

 else{// Si el valor anteteriar es mayor al valor nuevo

    int b=bandera*3;//el balor de la bandera se multiplicara por 3 para lograr el numero de pines que estan encendiendo las luces
         for(int l=b;l>= var;l=l-3)// Este for inicia de el numero de pines encendidos y va de forma decendente
        {
          
          color(colores[l],colores[l+1],colores[l+2],0,0,0);// apagamos los colores encendidos anteriamente hasta el valor actual
          delay(tiempo);// esperamos un tiempo establecido en la variable tiempo
        }
       
  
 }
  bandera=var;// determinamos en la variable vandera el valor anterior
     
      
            
}


