/*
*	Include some necessary files
*/
#include "hitechnic-sensormux.h"
#include "lego-light.h"
#include "hitechnic-irseeker-v2.h"
#include "hitechnic-touchmux.h"
#include "hitechnic-gyro.h"

/*
*	Declare some sendor constants
*/
const tMUXSensor irLeft = msensor_S2_1;
const tMUXSensor irRight = msensor_S2_2;
const tMUXSensor lightSenseLeft 	= msensor_S2_3;
const tMUXSensor lightSenseRight 	= msensor_S2_4;

/*
*	Variables to store the sensor values
*/
int rawLightLeft;
int rawLightRight;
int irStrengthLeft;
int irStrengthRight;
int gyroVel;

bool touchInput1;
bool touchInput2;
bool touchInput3;

int dummy;

/*
*	Constantly update the variables with the sensor values
*/
task getSmux()
{
	LSsetActive(lightSenseLeft);											// Turn the light sensors on to show that we're working
	LSsetActive(lightSenseRight);
	writeDebugStreamLine("Multiplexer setup ready");	// Print some info

	while (true){
		rawLightLeft = LSvalRaw(lightSenseLeft);				// Set all the variables to the sensor readings
		rawLightRight = LSvalRaw(lightSenseRight);

		if(!HTIRS2readEnhanced(IRS, dummy, irStrengthLeft)){
			writeDebugStreamLine("Something's wrong");
		}
		//if(!HTIRS2readEnhanced(msensor_S2_2, dummy, irStrengthRight)){
		//	writeDebugStreamLine("Something's wrong");
		//}

		gyroVel = HTGYROreadRot(msensor_S2_1);

		//writeDebugStreamLine("IR Left:%d IR Right: %d", irStrengthLeft, irStrengthRight);

		//touchInput1 = HTTMUXisActive(TMUX, 1);
		//touchInput2 = HTTMUXisActive(TMUX, 2);
		//touchInput3 = HTTMUXisActive(TMUX, 3);

	}


}
