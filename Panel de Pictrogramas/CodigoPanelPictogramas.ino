// Codigo para panel de pictogramas
// Sala CIMA
//Variables para modulos RGB
int red = 8;  int green = 9; int blue = 10;
//Variables para configuracion de color para modulos RGB
int r = 0;    int g = 0;     int b = 0;
//Variables de pulsantes = Led RGB
int botones [12] = {23, 25, 29, 31, 35, 39, 41, 45, 47, 51, 53, 5};
//Variables para controlar pulso VALUEPULSO
int pulsos  [12] = {22, 24, 28, 30, 34, 38, 40, 44, 46, 50, 52, 4};
//Variable para detectar pulso
int valorPulso[12] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

int game2OK[12] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//{"", "", "", "", "", "", "", "", "", "", "", ""};

String a = "";
String aux = "";

//int valp1 = 0; int valp2 = 0;  int valp3 = 0;  int valp4 = 0;
//int valp5 = 0; int valp6 = 0;  int valp7 = 0;  int valp8 = 0;
//int valp9 = 0; int valp10 = 0; int valp11 = 0; int valp12 = 0;

//Vector que servira para determinar el pictograma y su posicion en el panel
char *pictogramColor[36];
char *pictogramName[12];
char *pictogramCode[12];
char *pictogramPos[12];

int posPic[12];
String strPicCod[12];
String strPicColor[12];
String strposPic = "";
String strpiccol = "";

char *moduleData[8];

bool flagSequence = true;
bool flagPulse = true;
bool flagGame = true;
bool flagMode2 = false;
bool flagdemo = true;


int numberPic = 0;
int contGameMode2 = 0;

String moduleId = "";   String totalPic = "";
String picPos = "";     String picColor = "";
String picName = "";    String picCode = "";
String mode = "0";      String game = "";
String gameM = "";      String strMode2 = "";
String modeDemo = "";   String strMod = "";

void setup() {
  // Configuracion para pines de los modulos RGB
  pinMode(red, OUTPUT);
  pinMode(blue, OUTPUT);
  pinMode(green, OUTPUT);
  //Configuración de pines de botones como salidas (para RGB)
  for (int i = 0; i < 12 ; i++) {
    pinMode(botones[i], OUTPUT);
  }
  //Configuración de pines de pulsos como entradas (para detectar pulso)
  for (int i = 0; i < 12 ; i++) {
    pinMode(pulsos[i], INPUT);
  }

  for (int i = 0; i < 12; i++) {
    valorPulso[i] = digitalRead(pulsos[i]);
  }

  Serial.begin(9600);

}

void loop() {

  if (Serial.available() > 0) {
    a = Serial.readString();
    ///    delay(3000);
    //    Serial.println((String)a);
    char sz[700];
    a.toCharArray(sz, 700);
    char *p = sz;
    char *str;
    for (int i = 0; i < 8; i++) {
      str = strtok_r(p, ",", &p);
      moduleData[i] = str;
    }
    moduleId = moduleData[0];
    game =     moduleData[7];
  }


  //------------------- Comprobacion de datos para activacion del módulo ---------------------------
  if (moduleId == "M01") {
    if (game != "") {
      mode =     moduleData[1];
      totalPic = moduleData[2];
      picColor = moduleData[3];
      picPos =   moduleData[4];
      picName =  moduleData[5];
      picCode =  moduleData[6];
      game =     moduleData[7];

      //Llenado de datos de pictogramas (color,nombre,codigo);
      numberPic = totalPic.toInt();

      char sizeData[200];
      picColor.toCharArray(sizeData, 200);
      char *pc = sizeData;
      char *stra;
      for (int i = 0; i < numberPic * 3; i++) {
        stra = strtok_r(pc, ";", &pc);
        pictogramColor[i] = stra;
        strpiccol = (String)pictogramColor[i];
        strPicColor[i] = strpiccol;
        //  Serial.println(stra);
      }

      char sizeD[200];
      picPos.toCharArray(sizeD, 200);
      char *pcps = sizeD;
      char *strps;
      for (int i = 0; i < numberPic; i++) {
        strps = strtok_r(pcps, ";", &pcps);
        pictogramPos[i] = strps;
        strposPic = (String)pictogramPos[i];
        posPic[i] = strposPic.toInt();
      }
      char sizeNm[200];
      picName.toCharArray(sizeNm, 200);
      char *pcnm = sizeNm;
      char *strnm;
      for (int i = 0; i < numberPic; i++) {
        strnm = strtok_r(pcnm, ";", &pcnm);
        pictogramName[i] = strnm;
        //  Serial.println(strnm);
      }
      //Serial.println("Nombre--------");
      //Serial.println(pictogramName[0]);

      char sizeCd[200];
      picCode.toCharArray(sizeCd, 200);
      char *pccd = sizeCd;
      char *strcd;
      for (int i = 0; i < numberPic; i++) {
        strcd = strtok_r(pccd, ";", &pccd);
        pictogramCode[i] = strcd;
        String picCode = (String)pictogramCode[i];
        strPicCod[i] = picCode;
        //  Serial.println(strcd);
      }

    } else {
      imcompleteData();
      delay(1000);
      a = Serial.readString();
      Serial.println(a);
      char sz[700];
      a.toCharArray(sz, 700);
      char *p = sz;
      char *str;
      for (int i = 0; i < 8; i++) {
        str = strtok_r(p, ",", &p);
        moduleData[i] = str;
      }
      moduleId = moduleData[0];
      game =     moduleData[7];
    }
  }
  //Fin de comprobacion IF M01


  // Verificar el modo de juego 1
  if (mode == "1") {
    //    Serial.println("Iniciando Panel de pictogramas... MODO:::: ");
    //    Serial.println(mode);
    // MODO DE JUEGO 1
    if (game == "1" ) {
      //      Serial.println("Modo juego" + (String)game);
      int j = 0; int auxj = 0; int auxp = 2;
      String numberButton = "";
      int cont = 0; int rgb1 = 0 ; int rgb2 = 1; int rgb3 = 2;
      while (flagSequence && flagPulse) {
        String picps = (String)posPic[cont];
        String r = (String)pictogramColor[rgb1];
        String g = (String)pictogramColor[rgb2];
        String b = (String)pictogramColor[rgb3];
        sequenceButton(picps, cont, r, g, b);
        rgb1 += 3;
        rgb2 += 3;
        rgb3 += 3;
        cont++;
        if (numberPic == cont) {
          flagSequence = false;
          poweroff();
        }
      }

      flagSequence = true;
      flagPulse = true;
      //      Serial.println("Cambiando a modo 3....  flagSequence: " + (String)flagSequence);
      mode = "3";

    }
    // MODO DE JUEGO 2
    if (game == "2") {
      //      Serial.println("Modo juego 2");
      //      Serial.println(strMode2);
      int cont = 0;
      String r = (String)strPicColor[0];
      String g = (String)strPicColor[1];
      String b = (String)strPicColor[2];
      //      Serial.println((String)r+";"+(String)g+";"+(String)b);
      //      String picps = (String)pictogramPos[cont];
      //Serial.println("+++" + (String)contGameMode2);
      while (flagSequence && flagGame) {

        gameMode(r.toInt(), g.toInt(), b.toInt());

        //        Serial.println("+++INI" + (String)strMode2);
        //        if (flagMode2) {
//        Serial.println(strMode2);
        
        if (strMode2 == "OK" ) {
          strMode2 = " ";
          flagSequence = false;
          contGameMode2 = 0;
          poweroff();

        }
      }
      flagSequence = true;
      flagGame = true;
      mode = "3";
    }
  }
  // Fin modo juego 

  if (mode == "2") { // Modo demostracion

    int r = 0; int b = 1; int c = 2;
    modeDemo = mode;

    while (mode == "2") {
      //      while(flagdemo){
      //      Serial.println("DEMOSTRACION");
      demo(255, 0, 0);
      //      demo(0, 255, 0);
      //      demo(0, 0, 255);
    }
  }

  if (mode == "3") { //Modo Apagado
    //    Serial.println("APAGADO");
    flagSequence = true;
    flagPulse = true;
    flagGame = true;
  }

}

/// METODOS

void sequenceButton(String pos, int contador, String rd, String gn, String be) {
  int sizeBt = pos.toInt();
  int r = rd.toInt();
  int g = gn.toInt();
  int b = be.toInt();

  analogWrite(red, 255);
  analogWrite(green, 255);
  analogWrite(blue, 255);

  for (int i = 0; i < sizeBt; i++) {
    digitalWrite(botones[i], HIGH);
    delay(400);
    digitalWrite(botones[i], LOW);
    if (i == sizeBt - 2) {
      analogWrite(red, r);
      analogWrite(green, g);
      analogWrite(blue, b);
      digitalWrite(botones[sizeBt - 1], HIGH);
      delay(500);
      digitalWrite(botones[sizeBt - 1], LOW);


    }
  }

  //  Serial.println("while de pulso");
  analogWrite(red, r);
  analogWrite(green, g);
  analogWrite(blue, b);

  while (flagPulse) {
    valorPulso[sizeBt - 1] = digitalRead(pulsos[sizeBt - 1]);
    // digitalWrite(botones[sizeBt - 1], HIGH);
    // Serial.println(valorPulso[sizeBt-1]);
    if (valorPulso[sizeBt - 1] == 1) {

      if (botones[sizeBt] == 23) {
        digitalWrite(botones[sizeBt], HIGH);
        Serial.println("M01," + (String)strPicCod[contador]);
        delay(500);
        flagPulse = false;
        digitalWrite(botones[sizeBt], LOW);
      }



      //            Serial.println("CORRECTO)))");
      digitalWrite(botones[sizeBt - 1], HIGH);
      //int poscod = sizeBt.toInt();
      //      Serial.println(sizeBt);
      Serial.println("M01," + (String)strPicCod[contador]);
      delay(500);
      flagPulse = false;
      digitalWrite(botones[sizeBt - 1], LOW);
    } else {
      //INCORRECTO
      //      digitalWrite(botones[sizeBt - 1], HIGH);
      //      delay(500);
      //
      //      digitalWrite(botones[sizeBt - 1], LOW);
      //      Serial.println(pictogramCode[sizeBt]);
    }
  }
  flagPulse = true;
}

void poweroff() {

  analogWrite(red, 255);
  analogWrite(green, 255);
  analogWrite(blue, 255);
  for (int i = 0; i < 12; i++) {
    digitalWrite(botones[i], HIGH);
  }

  delay(1000);
  digitalWrite(botones[0], LOW);
  digitalWrite(botones[1], LOW);
  digitalWrite(botones[2], LOW);
  digitalWrite(botones[3], LOW);
  delay(500);
  digitalWrite(botones[4], LOW);
  digitalWrite(botones[5], LOW);
  digitalWrite(botones[6], LOW);
  digitalWrite(botones[7], LOW);
  delay(500);
  digitalWrite(botones[8], LOW);
  digitalWrite(botones[9], LOW);
  digitalWrite(botones[10], LOW);
  digitalWrite(botones[11], LOW);
  delay(500);

}

void demo (int r, int g, int b) {
  analogWrite(red, r);
  analogWrite(green, g);
  analogWrite(blue, b);
  for (int i = 0 ; i < 12 ; i ++ ) {
    digitalWrite(botones[i], HIGH);
    delay(300);

  }
  if (Serial.available() > 0) {
    a = Serial.readString();
    Serial.println(a);
    char sz[200];
    a.toCharArray(sz, 200);
    char *p = sz;
    char *str;
    for (int i = 0; i < 8; i++) {
      str = strtok_r(p, ",", &p);
      moduleData[i] = str;
    }
    moduleId = moduleData[0];
    mode =     moduleData[1];
    game =     moduleData[7];
  }

  //  if(modeDemo!= mode){
  //    flagdemo = false;
  //  }


  for (int j = 0 ; j < 12; j++) {
    digitalWrite(botones[j], LOW);
    //            digitalWrite(botones[j], HIGH);
    delay(300);
  }
}


void gameMode( int r, int g, int b) {

  analogWrite(red, r);
  analogWrite(green, g);
  analogWrite(blue, b);

  for (int i = 0 ; i < 12 ; i++) {
    digitalWrite(botones[i], HIGH);
  }

  while (flagGame) {
    for (int i = 0; i < 12; i++) {
      valorPulso[i] = digitalRead(pulsos[i]);

      for (int j = 0 ; j < numberPic ; j++) {

        String p1 = (String)posPic[j];
        int pp1 = p1.toInt();
        if (valorPulso[i] == 1) {
          //          Serial.println("P" + (String)(i + 1));
          //          Serial.println("pulso pp1: " + (String)(pulsos[pp1 - 1]));
          //          Serial.println("pulsos: " + (String)pulsos[i]);
          if (pulsos[pp1 - 1] == pulsos[i]) {

            game2OK[i] = 1;
            //                        Serial.println("Pulso Correcto" + (String)" : " +(String) (i+1));
            analogWrite(red, 255);
            analogWrite(green, 255);
            analogWrite(blue, 255);
            digitalWrite(botones[pp1 - 1], HIGH);
            delay(400);
            //digitalWrite(botones[pp1 - 1], LOW);


          } else {

            //            Serial.println("Pulso Incorrecto");
          }
        }
      }

      flagGame = false;

    }

    delay(100);
    //    flagGame = false;
    //    for (int i = 0; i < 12; i++) {
    //      Serial.println("-----" + (String) game2OK[i]);
    //
    //    }
    contGameMode2 = 0;
    int contGameMode2 =  (int) game2OK[0] + (int) game2OK[1] + (int) game2OK[2] + (int) game2OK[3] + (int) game2OK[4] +
                         (int) game2OK[5] + (int) game2OK[6] + (int) game2OK[7] + (int) game2OK[8] + (int) game2OK[9] + (int) game2OK[10] + (int) game2OK[11];
    //
    if (contGameMode2 == numberPic) {
      strMode2 = "OK";
      //      Serial.println("+++INI" + (String)flagMode2);
      contGameMode2 = 0;
      flagMode2 = true;
      //      Serial.println("+++FIN: " + (String)flagMode2);
    }

  }
  flagGame = true;
}


void imcompleteData() {
  analogWrite(red, 255);
  analogWrite(green, 0);
  analogWrite(blue, 0);

  delay(1000);
  for (int i = 0; i < 12; i++) {
    digitalWrite(botones[i], HIGH);
  }
  delay(500);
  for (int i = 0; i < 12; i++) {
    digitalWrite(botones[i], LOW);
  }
  delay(500);
  for (int i = 0; i < 12; i++) {
    digitalWrite(botones[i], HIGH);
  }
  delay(500);
  for (int i = 0; i < 12; i++) {
    digitalWrite(botones[i], LOW);
  }
  mode = "3";
}

