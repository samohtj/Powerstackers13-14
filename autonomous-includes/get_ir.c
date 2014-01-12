#include "drivers/hitechnic-irseeker-v2.h"

int getIrStrength (int sensor)
{
	// maximum direct signal
	int maxSig = 0;

	// dc and ac directional values.
	int _dirDC = 0;
	int _dirAC = 0;

	// DC and AC values from 5 internal detectors.
	int dcS1, dcS2, dcS3, dcS4, dcS5 = 0;
	int acS1, acS2, acS3, acS4, acS5 = 0;

	// we are going to set DSP mode to 1200 Hz.
	tHTIRS2DSPMode _mode = DSP_1200;

	// attempt to set to DSP mode.
	if (HTIRS2setDSPMode(sensor, _mode) == 0)
	{
		// unsuccessful at setting the mode.
		// display error message.
		eraseDisplay();
		nxtDisplayCenteredTextLine(0, "ERROR!");
		nxtDisplayCenteredTextLine(2, "Init failed!");
		nxtDisplayCenteredTextLine(3, "Connect sensor");
		nxtDisplayCenteredTextLine(4, "to Port 1.");

		// make a noise to get their attention.
		PlaySound(soundBeepBeep);

		// wait so user can read message, then leave main task. FTC-TD-0004-Rev-001-IRSeeker Page 12 of 16 9/14/2012 10:54:00 AM
		wait10Msec(300);
		return -1;
	}

	// Read the individual signal strengths of the internal sensors
	// Do this for both unmodulated (DC) and modulated signals (AC)
	if (!HTIRS2readAllDCStrength(sensor, dcS1, dcS2, dcS3, dcS4, dcS5))
		return -1; // I2C read error occurred
	if (!HTIRS2readAllACStrength(sensor, acS1, acS2, acS3, acS4, acS5 ))
		return -1; // I2C read error occurred

maxSig = (acS1 > acS2)? acS1 : acS2;
maxSig = (maxSig < acS3)? acS3 : maxSig;
maxSig = (maxSig < acS4)? acS4 : maxSig;
maxSig = (maxSig < acS5)? acS5 : maxSig;
	return maxSig;
}

int getIrDirection(int sensor){
	int direction, strength;
	if (!HTIRS2readEnhanced(sensor, direction, strength))
		writeDebugStreamLine("There was an error finding the direction");

	return direction;
}
