//#pragma config(Sensor, S1,     colorLego1,     sensorCOLORFULL)
//#pragma config(Sensor, S2,     colorLego2,     sensorCOLORFULL)
////*!!Code automatically generated by 'ROBOTC' configuration wizard               !!*//

//#include "hitechnic-colour-v2.h"

#define black 1
#define white 0
#define blue 2
#define green 3
#define yellow 4
#define red 5

//short autoMode = 1;

int colorSense1;
int colorSense2;

string colorString1;
string colorString2;

short getAutoMode(){
	short colorPicked =
((colorSense1 == white && colorSense2 == white)? 	1:
((colorSense1 == white && colorSense2 == black)? 	2:
((colorSense1 == white && colorSense2 == blue)? 	3:
((colorSense1 == white && colorSense2 == green)? 	4:
((colorSense1 == white && colorSense2 == yellow)? 5:
((colorSense1 == white && colorSense2 == red)? 		6:

((colorSense1 == black && colorSense2 == white)? 	7:
((colorSense1 == black && colorSense2 == black)? 	8:
((colorSense1 == black && colorSense2 == blue)? 	9:
((colorSense1 == black && colorSense2 == green)? 	10:
((colorSense1 == black && colorSense2 == yellow)?	11:
((colorSense1 == black && colorSense2 == red)? 		12:

((colorSense1 == blue && colorSense2 == white)? 	13:
((colorSense1 == blue && colorSense2 == black)? 	14:
((colorSense1 == blue && colorSense2 == blue)? 		15:
((colorSense1 == blue && colorSense2 == green)? 	16:
((colorSense1 == blue && colorSense2 == yellow)? 	17:
((colorSense1 == blue && colorSense2 == red)? 		18:

((colorSense1 == green && colorSense2 == white)? 	19:
((colorSense1 == green && colorSense2 == black)? 	20:
((colorSense1 == green && colorSense2 == blue)? 	21:
((colorSense1 == green && colorSense2 == green)? 	22:
((colorSense1 == green && colorSense2 == yellow)? 23:
((colorSense1 == green && colorSense2 == red)? 		24:

((colorSense1 == yellow && colorSense2 == white)? 25:
((colorSense1 == yellow && colorSense2 == black)? 26:
((colorSense1 == yellow && colorSense2 == blue)? 	27:
((colorSense1 == yellow && colorSense2 == green)? 28:
((colorSense1 == yellow && colorSense2 == yellow)? 29:
((colorSense1 == yellow && colorSense2 == red)? 	30:

((colorSense1 == red && colorSense2 == white)? 		31:
((colorSense1 == red && colorSense2 == black)? 		32:
((colorSense1 == red && colorSense2 == blue)? 		33:
((colorSense1 == red && colorSense2 == green)? 		34:
((colorSense1 == red && colorSense2 == yellow)? 	35:
((colorSense1 == red && colorSense2 == red)?			36: 0
	))))))))))))))))))))))))))))))))))));
	return colorPicked;

}

//task main()
//{
//	ClearTimer(T1);

//	while (true)
//	{
//		colorSense1 = SensorValue[colorLego1];
//		colorSense2 = SensorValue[colorLego1];

//		colorString1 = ((colorSense1 == black)? "Black" :
//		((colorSense1 == blue)? "Blue" :
//		((colorSense1 == white)? "White" :
//		((colorSense1 == green)? "Green" :
//		((colorSense1 == yellow)? "Yellow":"Red")))));

//		colorString2 = ((colorSense2 == black)? "Black" :
//		((colorSense2 == blue)? "Blue" :
//		((colorSense2 == white)? "White" :
//		((colorSense2 == green)? "Green" :
//		((colorSense2 == yellow)? "Yellow":"Red")))));

//		autoMode = getAutoMode();
//		if (time100[T1] % 10 == 0){
//			clearDebugStream();
//			writeDebugStreamLine("LegoSense1: %s", colorString1);
//			writeDebugStreamLine("LegoSesne2: %s", colorString2);
//			writeDebugStreamLine("Mode: %d", autoMode);
//		}


//	}


//}
