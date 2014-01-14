#include "drivers/hitechnic-irseeker-v2.h"

int getIrStrength (int sensor)
{
	// maximum direct signal
	int maxSig = 0;

	// signal of individaul sensors
	int acS1, acS2, acS3, acS4, acS5 = 0;

	// we are going to set DSP mode to 1200 Hz.
	tHTIRS2DSPMode _mode = DSP_1200;

	// Read the individual signal strengths of the internal sensors
	if (!HTIRS2readAllACStrength(sensor, acS1, acS2, acS3, acS4, acS5 ))
		return -1; // I2C read error occurred

	maxSig = (acS1 > acS2)? acS1 : acS2;
	maxSig = (maxSig < acS3)? acS3 : maxSig;
	maxSig = (maxSig < acS4)? acS4 : maxSig;
	maxSig = (maxSig < acS5)? acS5 : maxSig;
	return maxSig;
}

int getIrDirection(int sensor){
	int dirDC = HTIRS2readDCDir(sensor);


	return dirDC;
}
