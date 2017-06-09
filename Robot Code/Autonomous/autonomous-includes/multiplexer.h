// Include files to handle the multiplexer and all the different sensors
#include "hitechnic-sensormux.h"
#include "lego-light.h"
#include "hitechnic-irseeker-v2.h"
#include "hitechnic-touchmux.h"
#include "hitechnic-gyro.h"

const tMUXSensor irLeft = msensor_S2_1;
const tMUXSensor irRight = msensor_S2_2;
const tMUXSensor lightSenseLeft 	= msensor_S2_3;
const tMUXSensor lightSenseRight 	= msensor_S2_4;

int rawLightLeft;
int rawLightRight;
int irStrengthLeft;
int irStrengthRight;

bool gettingIr = false;
int dummy;

/////////////////////////////////////////////////////////////////////////
//
//	Update the sensor values
//
////////////////////////////////////////////////////////////////////////
task getSmux()
{
	LSsetActive(lightSenseLeft);
	LSsetActive(lightSenseRight);
	HTGYROstartCal(sGyro);
	writeDebugStreamLine("Multiplexer setup ready");

	while (true) {

		rawLightLeft = LSvalRaw(lightSenseLeft);
		rawLightRight = LSvalRaw(lightSenseRight);

		// Only update the IR seeker variables when the IR seekers are turned on
		// This keeps the debug stream clear
		if(gettingIr){
			if(!HTIRS2readEnhanced(irLeft, dummy, irStrengthLeft)){
				writeDebugStreamLine("Something's wrong with the IR");
			}
			if(!HTIRS2readEnhanced(irRight, dummy, irStrengthRight)){
				writeDebugStreamLine("Something's wrong with the IR");
			}
		}
	}
}
