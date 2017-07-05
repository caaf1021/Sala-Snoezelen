//Declaracion de salida de colores rgb (PMW)
int rojo1 = 9;
int  verde1 = 10;
int azul1 = 11;

//Salidas Digitales (LEDS)
int Led1 = 12;
int Led2 = 13;

//Variables necesarias

String a = "";
String nombremo = "";
char *myStrings[3];
String metodoString = "";
int metodo = 3;
int repetir = 0;
String rgbcol = "";
int configcolores[3];
char *coloresRGB[3];
int rrandom = 0;
int grandom = 0;
int brandom = 0;
bool confi = true;


void setup() {

  pinMode(rojo1,OUTPUT);
  pinMode(azul1,OUTPUT);
  pinMode(verde1,OUTPUT);
  
  pinMode(Led1,OUTPUT);
  pinMode(Led2, OUTPUT); 
  Serial.begin(9600);

}

void loop() {

  

//Verificamos si existe datos en el puerto serial

  if (Serial.available()>0) {
    a = Serial.readString();
    char sz[500];  
    a.toCharArray(sz, 500);
    char *p = sz;
    char *str;
    for(int i=0;i<3;i++){
    str = strtok_r(p, ",", &p);
    myStrings[i]=str;
    }
    
    nombremo = myStrings[0];
  }else{
    nombremo = "Nocambio";
  }

// Si los datos pertenecen  a este modulo 

  if(nombremo == "M06"){
    
    metodoString = myStrings[1];
    metodo = metodoString.toInt();
    confi = true ;
    String hh = myStrings[2];
    if(hh == ""){
      repetir = 1;
      metodo = 3;
         
    }
    
  }

  //Encedemos y configuramos el color a la tira de leds para la ilumincaion de la sala 
  
  if(metodo == 1 && confi){

    
      rgbcol = myStrings[2]; 
     
      char sz[100];  
      rgbcol.toCharArray(sz, 100);
      char *p = sz;
      char *str;
      
      for(int i=0;i<3;i++){
        str = strtok_r(p, ";", &p);
        coloresRGB[i]=str;
        String coloresrgbs = coloresRGB[i];
        configcolores[i] = coloresrgbs.toInt();
      }

      color(configcolores[0],configcolores[1],configcolores[2]);
      digitalWrite(Led1,HIGH);
      digitalWrite(Led2,HIGH);
      confi = false;
 
   }

   if(metodo == 2){
      ganado();
    }


    if(metodo == 3){

      if(repetir == 1){
      error();
      repetir = 0; 
     
    }

    digitalWrite(Led1,LOW);
    digitalWrite(Led2,LOW);
    }

}


void color(int rojo,int verde,int azul){
   
    analogWrite(rojo1,rojo);
    analogWrite(verde1,verde);
    analogWrite(azul1,azul);
  
}

void error(){

  
 color(255 ,0,0);

    digitalWrite(Led1,HIGH);
    digitalWrite(Led2,HIGH);
    delay(2000);
    digitalWrite(Led1,LOW);
    digitalWrite(Led2,LOW);
    delay(1000);
  
}


void ganado(){
  

 randomSeed(millis());
 rrandom = random(255);
 grandom = random(255);
 brandom = random(255);
 color(rrandom ,grandom,brandom);

  digitalWrite(Led1,HIGH);
    digitalWrite(Led2,HIGH);
    delay(10000);
    digitalWrite(Led1,LOW);
    digitalWrite(Led2,LOW);
   delay(2000);

  
}
