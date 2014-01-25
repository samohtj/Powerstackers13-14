#include "drivers/hitechnic-sensormux.h"
#include "drivers/lego-light.h"
#include "drivers/hitechnic-gyro.h"

const tMUXSensor lightSenseLeft 	= msensor_S2_1;
const tMUXSensor lightSenseRight 	= msensor_S2_2;
#define gyroSense			msensor_S2_3
//const tMUXSensor colorSense 			= msensor_S2_4;

int rawLightLeft;
int rawLightRight;
int rawGyro;
int rawColor;

task getSmux()
{
	bNxtLCDStatusDisplay = false;

	LSsetActive(lightSenseLeft);
	LSsetActive(lightSenseRight);
	writeDebugStreamLine("Multiplexer setup ready");

	while (true){
		rawLightLeft = LSvalRaw(lightSenseLeft);
		rawLightRight = LSvalRaw(lightSenseRight);
		rawGyro = HTGYROreadRot(gyroSense);
		//rawColor = LSvalRaw(colorSense);

		//nxtDisplayTextLine(1, "LightLeft: %4d", rawLightLeft);
		//nxtDisplayTextLine(2, "LightRight: %4d",rawLightRight);
		//nxtDisplayTextLine(3, "Gyro: %4d", rawGyro);
		//nxtDisplayTextLine(4, "Color: %4d", rawColor);
	}


}
