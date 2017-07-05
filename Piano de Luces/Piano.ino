//Declaracion de salida de colores rgb (PMW)
int rojo1 = 8;
int azul1 = 10;
int verde1 = 9;

//Salidas Digitales (LEDS)
int Led1 = 22;
int Led2 = 24;
int Led3 = 26;
int Led4 = 28;
int Led5 = 30;
int Led6 = 32;
int Led7 = 34;
int Led8 = 36;

//Entradas Analogicas (Potenciometros)
const int p1 = 0;
const int p2 = 1;
const int p3 = 2;
const int p4 = 3;
const int p5 = 4;
const int p6 = 5;
const int p7 = 6;
const int p8 = 7;

//Valores maximos marcados de cada uno de las entradas digitales
int valMax1;
int valMax2;
int valMax3;
int valMax4;
int valMax5;
int valMax6;
int valMax7;
int valMax8;

//Valor real dado en el poteciomentro
int valp1 = 0;
int valp2 = 0;
int valp3 = 0;
int valp4 = 0;
int valp5 = 0;
int valp6 = 0;
int valp7 = 0;
int valp8 = 0;

//Porcetaje de fuerza aplicada, marcado por el potenciometro
int val1 = 0;
int val2 = 0;
int val3 = 0;
int val4 = 0;
int val5 = 0;
int val6 = 0;
int val7 = 0;
int val8 = 0;

//Variables necesarias para el funcionamiento del modulo

bool bandera = true;
int contador = 1;
int contador2;
int medida = 0;
int maximo = 0;
int minimo = 0;
char *myStrings[8];
int metodo;
char *coloresRGB[24];
int configcolores[24];
String nombresrasp[8];
String codigorasp[8];
char *nombrepic[8];
char *codigopic[8];
bool configi = false;
String nombremo = "";
String metodoString = "";
String rgbcol = "";
String nombres = "";
String pictogramas = "";
String medidaString = "";
String se = "";
String tamaString = "";
int tama = 0 ;
bool banderametodo = true;
int rrandom = 0;
int grandom = 0;
int brandom = 0;
String a = "";
bool datosbandera = true;
int repetir = 0;

void setup() {

  // Declaramos los pines de entrada y salida 
  
  pinMode(rojo1, OUTPUT);
  pinMode(azul1, OUTPUT);
  pinMode(verde1, OUTPUT);

  pinMode(Led1, OUTPUT);
  pinMode(Led2, OUTPUT);
  pinMode(Led3, OUTPUT);
  pinMode(Led4, OUTPUT);
  pinMode(Led5, OUTPUT);
  pinMode(Led6, OUTPUT);
  pinMode(Led7, OUTPUT);
  pinMode(Led8, OUTPUT);

  pinMode(p1, INPUT);
  pinMode(p2, INPUT);
  pinMode(p3, INPUT);
  pinMode(p4, INPUT);
  pinMode(p5, INPUT);
  pinMode(p6, INPUT);
  pinMode(p7, INPUT);
  pinMode(p8, INPUT);


  valMax1 = analogRead(p1);
  valMax2 = analogRead(p2);
  valMax3 = analogRead(p3);
  valMax4 = analogRead(p4);
  valMax5 = analogRead(p5);
  valMax6 = analogRead(p6);
  valMax7 = analogRead(p7);
  valMax8 = analogRead(p8);

  Serial.begin(9600);

}

void loop() {

  datosbandera = true ;


  //Verificamos si el puerto serial tiene algun contenido para seguir con los siguientes metodos y si existe datos partimos el texto y le guardamos en un array
  
  if (Serial.available()>0) {
    
    a  = Serial.readString() ;
    char sz[500];
    a.toCharArray(sz, 500);
    char *p = sz;
    char *str;
    for (int i = 0; i < 8; i++) {
      str = strtok_r(p, ",", &p);
      myStrings[i] = str;
    }
    nombremo = myStrings[0];
  } else {
    nombremo = "Nocambio";
  }

  //Verificamos si los datos pertenecen a este modulo

  if (nombremo == "M02") {

    metodoString = myStrings[1];
    metodo = metodoString.toInt();
    configi = true;
    contador = 1;
    String hh = myStrings[7];
    if (hh == "") {
      repetir = 1;
      metodo = 3;

    }


  }



  if (metodo == 1) {

    //Realizamos las configuraciones necesarias para el funcionamiento de este modulo
     
    if (configi) {
      

      rgbcol = myStrings[2];

      char sz[100];
      rgbcol.toCharArray(sz, 100);
      char *p = sz;
      char *str;
      for (int i = 0; i < 24; i++) {
        str = strtok_r(p, ";", &p);
        coloresRGB[i] = str;
        String coloresrgbs = coloresRGB[i];
        configcolores[i] = coloresrgbs.toInt();

      }

      nombres = myStrings[3];
      char szno[100];
      nombres.toCharArray(szno, 100);
      char *pno = szno;
      char *strno;
      for (int i = 0; i < 8; i++) {
        strno = strtok_r(pno, ";", &pno);
        nombrepic[i] = strno;
        String nombrepir = nombrepic[i];
        nombresrasp [i] = nombrepir ;
      }



      pictogramas = myStrings[4];
      char szpi[100];

      pictogramas.toCharArray(szpi, 100);
      char *ppi = szpi;
      char *strpi;
      for (int i = 0; i < 8; i++) {
        strpi = strtok_r(ppi, ";", &ppi);
        codigopic[i] = strpi;
        String codigomo = codigopic[i];
        codigorasp[i] = codigomo;
      }


      medidaString = myStrings[7];
      tamaString = myStrings[5];
      medida = medidaString.toInt();
      tama = tamaString.toInt();
      configi = false;


    }




    int secuenciaarray[tama];
    char *secuencia[tama];
    se = myStrings[6];
    char szse[100];
    se.toCharArray(szse, 100);
    char *pse = szse;
    char *strse;
    
    for (int i = 0; i < tama; i++) {
      strse = strtok_r(pse, ";", &pse);
      secuencia[i] = strse;
      String arrayi = secuencia[i];
      secuenciaarray[i] = arrayi.toInt();

    }

    //Almacenamos los valores maximos de cada uno de los potecimetros 

    valMax1 = analogRead(p1);
    valMax2 = analogRead(p2);
    valMax3 = analogRead(p3);
    valMax4 = analogRead(p4);
    valMax5 = analogRead(p5);
    valMax6 = analogRead(p6);
    valMax7 = analogRead(p7);
    valMax8 = analogRead(p8);

    if (medida  == 0) {
      maximo = 80;
      minimo = 0;
    }

    if (medida  == 1) {
      maximo = 60;
      minimo = 30;
    }

    if (medida  == 2) {
      maximo = 40;
      minimo = 0;
    }


    //Iniciamos la secuencia dependiento el numero de aciertos y tamaño del array 
    if (bandera) {
      for (int i = 0; i < contador; i++) {
        encendido(secuenciaarray[i]);
      }
      bandera = false;
    }

    contador2 = 0;

    datosbandera = true;
    
    //Verificamos si se presiono el pulsante con la fuerza apropiada  y si es correcto o no 
    
    while (contador > contador2 && datosbandera) {

      
      if (Serial.available()>0) {
        a  = Serial.readString() ;
        char sz[500];
        a.toCharArray(sz, 500);
        char *p = sz;
        char *str;
        for (int i = 0; i < 8; i++) {
          str = strtok_r(p, ",", &p);
          myStrings[i] = str;
        }
        nombremo = myStrings[0];
      }
      else {
        nombremo = "Nocambio";
      }

      if (nombremo == "M02") {

        metodoString = myStrings[1];
        metodo = metodoString.toInt();
        configi = true;
        datosbandera = false;
        bandera = true;
        contador = 1;
        String hh = myStrings[7];
        if (hh == "") {
          repetir = 1;
          metodo = 3;

        }
      }
      valp1 = analogRead(p1);
      valp2 = analogRead(p2);
      valp3 = analogRead(p3);
      valp4 = analogRead(p4);
      valp5 = analogRead(p5);
      valp6 = analogRead(p6);
      valp7 = analogRead(p7);
      valp8 = analogRead(p8);


      val1 = map(valp1, 0, valMax1, 0, 100);
      val2 = map(valp2, 0, valMax2, 0, 100);
      val3 = map(valp3, 0, valMax3, 0, 100);
      val4 = map(valp4, 0, valMax4, 0, 100);
      val5 = map(valp5, 0, valMax5, 0, 100);
      val6 = map(valp6, 0, valMax6, 0, 100);
      val7 = map(valp7, 0, valMax7, 0, 100);
      val8 = map(valp8, 0, valMax8, 0, 100);

      if (val1 <= maximo && val1 > minimo && secuenciaarray[contador2] == 1) {
        Serial.println("M02,"+codigorasp[0]);
        encendido(1);
        contador2++;
        
      }
      else {
        if (val1 <= maximo && val1 > minimo) {
          Serial.println("M02,"+codigorasp[0]);
          encendido(1);
          contador2 = contador + 1;
          contador--;
          bandera = true;
          
        }
      }

      if (val2 <= maximo && val2 > minimo && secuenciaarray[contador2] == 2) {
        Serial.println("M02,"+codigorasp[1]);
        encendido(2);
        contador2++;
      }
      else {
        if (val2 <= maximo && val2 > minimo) {
          Serial.println("M02,"+codigorasp[1]);
          encendido(2);
          contador2 = contador + 1;
          contador--;
          bandera = true;
        }
      }


      if (val3 <= maximo && val3 > minimo && secuenciaarray[contador2] == 3) {
        Serial.println("M02,"+codigorasp[2]);
        encendido(3);
        contador2++;
      }
      else {
        if (val3 <= maximo && val3 > minimo) {
          Serial.println("M02,"+codigorasp[2]);
          encendido(3);
          contador2 = contador + 1;
          contador--;
          bandera = true;
        }
      }


      if (val4 <= maximo && val4 > minimo && secuenciaarray[contador2] == 4) {
        Serial.println("M02,"+codigorasp[3]);
        encendido(4);
        contador2++;
      }
      else {
        if (val4 <= maximo && val4 > minimo) {
          Serial.println("M02,"+codigorasp[3]);
          encendido(4);
          contador2 = contador + 1;
          contador--;
          bandera = true;
        }
      }



      if (val5 <= maximo && val5 > minimo && secuenciaarray[contador2] == 5) {
        Serial.println("M02,"+codigorasp[4]);
        encendido(5);
        contador2++;
      }
      else {
        if (val5 <= maximo && val5 > minimo) {
          Serial.println("M02,"+codigorasp[4]);
          encendido(5);
          contador2 = contador + 1;
          contador--;
          bandera = true;
        }
      }


      if (val6 <= maximo && val6 > minimo && secuenciaarray[contador2] == 6) {
        Serial.println("M02,"+codigorasp[5]);
        encendido(6);
        contador2++;
      }
      else {
        if (val6 <= maximo && val6 > minimo) {
          Serial.println("M02,"+codigorasp[5]);
          encendido(6);
          contador2 = contador + 1;
          contador--;
          bandera = true;
        }
      }


      if (val7 <= maximo && val7 > minimo && secuenciaarray[contador2] == 7) {
        Serial.println("M02,"+codigorasp[6]);
        encendido(7);
        contador2++;
      }
      else {
        if (val7 <= maximo && val7 > minimo) {
          Serial.println("M02,"+codigorasp[6]);
          encendido(7);
          contador2 = contador + 1;
          contador--;
          bandera = true;
        }
      }


      if (val8 <= maximo && val8 > minimo && secuenciaarray[contador2] == 8) {
        Serial.println("M02,"+codigorasp[7]);
        encendido(8);
        contador2++;
      }
      else {
        if (val8 <= maximo && val8 > minimo) {
          Serial.println("M02,"+codigorasp[7]);
          encendido(8);
          contador2 = contador + 1;
          contador--;
          bandera = true;
        }
      }


    }



    if (contador == contador2) {
      bandera = true;
      contador++;
    }

    if (contador > tama) {
      banderametodo = false;
      metodo = 3;
      ganado();

    }


  }

  //Metodo de demostracion 

  if (metodo == 2) {
    ganado();
    if (Serial.available()>0) {
      a  = Serial.readString() ;
      char sz[500];
      a.toCharArray(sz, 500);
      char *p = sz;
      char *str;
      for (int i = 0; i < 8; i++) {
        str = strtok_r(p, ",", &p);
        myStrings[i] = str;
      }
      nombremo = myStrings[0];
    }
    else {
      nombremo = "Nocambio";
    }

    if (nombremo == "M02") {

      metodoString = myStrings[1];
      metodo = metodoString.toInt();
      bandera = true;
      configi = true;
      datosbandera = true;
      String hh = myStrings[7];

      if (hh == "") {

        metodo = 3;
        repetir = 1;

      }
    }
  }
  if (metodo == 3) {

    if (repetir == 1) {
      error();
      repetir = 0;
    }

   
  }






}


void demostracion() {

  randomSeed(millis());
  int demorandom = 0 ;

  for (int i = 0 ; i < 24; i++) {
    demorandom = random(255);
    configcolores[i] = demorandom;
  }

  for (int i = 1; i < 9; i++) {
    encendido(i);
  }

  for (int i = 9; i > 0; i--) {
    encendido(i);
  }


}

//Metodo se lo llama si existe algun erro en comunicacion

void error() {


  color(255 , 0, 0);

  digitalWrite(Led1, HIGH);
  digitalWrite(Led2, HIGH);
  digitalWrite(Led3, HIGH);
  digitalWrite(Led4, HIGH);
  digitalWrite(Led5, HIGH);
  digitalWrite(Led6, HIGH);
  digitalWrite(Led7, HIGH);
  digitalWrite(Led8, HIGH);
  delay(1000);
  digitalWrite(Led1, LOW);
  digitalWrite(Led2, LOW);
  digitalWrite(Led3, LOW);
  digitalWrite(Led4, LOW);
  digitalWrite(Led5, LOW);
  digitalWrite(Led6, LOW);
  digitalWrite(Led7, LOW);
  digitalWrite(Led8, LOW);
  delay(1000);

}

//Metodo se lo llama cuando se realiza toda la secuencia de forma correcta

void ganado() {


  randomSeed(millis());
  rrandom = random(255);
  grandom = random(255);
  brandom = random(255);
  color(rrandom , grandom, brandom);

  digitalWrite(Led1, HIGH);
  digitalWrite(Led2, HIGH);
  digitalWrite(Led3, HIGH);
  digitalWrite(Led4, HIGH);
  digitalWrite(Led5, HIGH);
  digitalWrite(Led6, HIGH);
  digitalWrite(Led7, HIGH);
  digitalWrite(Led8, HIGH);
  delay(1000);
  digitalWrite(Led1, LOW);
  digitalWrite(Led2, LOW);
  digitalWrite(Led3, LOW);
  digitalWrite(Led4, LOW);
  digitalWrite(Led5, LOW);
  digitalWrite(Led6, LOW);
  digitalWrite(Led7, LOW);
  digitalWrite(Led8, LOW);
  delay(1000);

  rrandom = random(255);
  grandom = random(255);
  brandom = random(255);
  color(rrandom , grandom, brandom);

  digitalWrite(Led1, HIGH);
  digitalWrite(Led2, HIGH);
  digitalWrite(Led3, HIGH);
  digitalWrite(Led4, HIGH);
  digitalWrite(Led5, HIGH);
  digitalWrite(Led6, HIGH);
  digitalWrite(Led7, HIGH);
  digitalWrite(Led8, HIGH);
  delay(1000);
  digitalWrite(Led1, LOW);
  digitalWrite(Led2, LOW);
  digitalWrite(Led3, LOW);
  digitalWrite(Led4, LOW);
  digitalWrite(Led5, LOW);
  digitalWrite(Led6, LOW);
  digitalWrite(Led7, LOW);
  digitalWrite(Led8, LOW);
  delay(1000);


  rrandom = random(100, 255);
  grandom = random(100, 255);
  brandom = random(100, 255);
  color(rrandom , grandom, brandom);

  digitalWrite(Led1, HIGH);
  digitalWrite(Led2, HIGH);
  digitalWrite(Led3, HIGH);
  digitalWrite(Led4, HIGH);
  digitalWrite(Led5, HIGH);
  digitalWrite(Led6, HIGH);
  digitalWrite(Led7, HIGH);
  digitalWrite(Led8, HIGH);
  delay(1000);
  digitalWrite(Led1, LOW);
  digitalWrite(Led2, LOW);
  digitalWrite(Led3, LOW);
  digitalWrite(Led4, LOW);
  digitalWrite(Led5, LOW);
  digitalWrite(Led6, LOW);
  digitalWrite(Led7, LOW);
  digitalWrite(Led8, LOW);
  delay(1000);

}


//Metodo para dar el colr a los modulos RGB

void color(int rojo, int verde, int azul) {

  analogWrite(rojo1, rojo);
  analogWrite(verde1, verde);
  analogWrite(azul1, azul);

}


//Metodo para verificar que led se debe enceder

void encendido(int var) {
  switch (var) {
    case 1:
      color(configcolores[0], configcolores[1], configcolores[2]);
      digitalWrite(Led1, HIGH);
      delay(1000);
      digitalWrite(Led1, LOW);
      delay(1000);
      break;
    case 2:
      color(configcolores[3], configcolores[4], configcolores[5]);
      digitalWrite(Led2, HIGH);
      delay(1000);
      digitalWrite(Led2, LOW);
      delay(1000);
      break;
    case 3:
      color(configcolores[6], configcolores[7], configcolores[8]);
      digitalWrite(Led3, HIGH);
      delay(1000);
      digitalWrite(Led3, LOW);
      delay(1000);
      break;

    case 4:
      color(configcolores[9], configcolores[10], configcolores[11]);
      digitalWrite(Led4, HIGH);
      delay(1000);
      digitalWrite(Led4, LOW);
      delay(1000);
      break;

    case 5:
      color(configcolores[12], configcolores[13], configcolores[14]);
      digitalWrite(Led5, HIGH);
      delay(1000);
      digitalWrite(Led5, LOW);
      delay(1000);
      break;


    case 6:
      color(configcolores[15], configcolores[16], configcolores[17]);
      digitalWrite(Led6, HIGH);
      delay(1000);
      digitalWrite(Led6, LOW);
      delay(1000);
      break;

    case 7:
      color(configcolores[18], configcolores[19], configcolores[20]);
      digitalWrite(Led7, HIGH);
      delay(1000);
      digitalWrite(Led7, LOW);
      delay(1000);
      break;


    case 8:
      color(configcolores[21], configcolores[22], configcolores[23]);
      digitalWrite(Led8, HIGH);
      delay(1000);
      digitalWrite(Led8, LOW);
      delay(1000);
      break;

    default:
      break;
  }
}
