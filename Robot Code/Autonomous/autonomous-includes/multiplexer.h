/*
*	Include some necessary files
*/
#include "hitechnic-sensormux.h"
#include "lego-light.h"
#include "hitechnic-irseeker-v2.h"
#include "hitechnic-touchmux.h"
#include "hitechnic-gyro.h"
																																			// CONSTANTS
const tMUXSensor irLeft = msensor_S2_1;																// Left IR seeker address
const tMUXSensor irRight = msensor_S2_2;															// Right IR seeker address
const tMUXSensor lightSenseLeft 	= msensor_S2_3;											// Left light sensor address
const tMUXSensor lightSenseRight 	= msensor_S2_4;											// Right light sensor address
																																			// SENSOR VALUES
int rawLightLeft;																											// Left IR seeker value
int rawLightRight;																										// Right IR seeker value
int irStrengthLeft;																										// Left light sensor value
int irStrengthRight;																									// Right light sensor value

int dummy;																														// Dummy variable

/*
*	Constantly update the variables with the sensor values
*/
task getSmux()
{
	LSsetActive(lightSenseLeft);																				// Turn the light sensors on to show that we're working
	LSsetActive(lightSenseRight);
	writeDebugStreamLine("Multiplexer setup ready");										// Print a "ready" message

	while (true){
		rawLightLeft = LSvalRaw(lightSenseLeft);													// Set all the variables to the sensor readings
		rawLightRight = LSvalRaw(lightSenseRight);

		if(!HTIRS2readEnhanced(irLeft, dummy, irStrengthLeft)){						// If the function returns false:
			writeDebugStreamLine("Something's wrong with the IR");					// Let the operator know that something is wrong
		}
		if(!HTIRS2readEnhanced(irRight, dummy, irStrengthRight)){
			writeDebugStreamLine("Something's wrong with the IR");
		}
	}
}
