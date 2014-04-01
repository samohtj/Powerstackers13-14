// Include files to handle the multiplexer and all the different sensors
#include "hitechnic-sensormux.h"
#include "lego-light.h"
#include "hitechnic-irseeker-v2.h"
#include "hitechnic-touchmux.h"
#include "hitechnic-gyro.h"

// Create variables to store the sensor addresses
const tMUXSensor irLeft = msensor_S2_1;
const tMUXSensor irRight = msensor_S2_2;
const tMUXSensor lightSenseLeft 	= msensor_S2_3;
const tMUXSensor lightSenseRight 	= msensor_S2_4;

// Create variables to store the sensor values
int rawLightLeft;
int rawLightRight;
int irStrengthLeft;
int irStrengthRight;

// Flag to turn on or off the IR seekers
bool gettingIr = false;
int dummy;

/////////////////////////////////////////////////////////////////////////
//
//	Update the sensor values
//
////////////////////////////////////////////////////////////////////////
task getSmux()
{
	// Turn on the red lights on the light sensors to show that we're working
	LSsetActive(lightSenseLeft);
	LSsetActive(lightSenseRight);

	// Calibrate the gyroscope
	HTGYROstartCal(sGyro);

	// Print a ready message
	writeDebugStreamLine("Multiplexer setup ready");

	// Loop forever
	while (true){
		// Set the light sensor variables to the light sensor signal values
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
