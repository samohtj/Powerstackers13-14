#include "hitechnic-irseeker-v2.h"
#include "multiplexer.c"
int getIrStrengthLeft()
{
	int direction, strength;
	if(!HTIRS2readEnhanced(irLeft, direction, strength)){
		writeDebugStreamLine("something's wrong");
		return -1;
	}
	return strength;
}

int getIrStrengthRight()
{
	int direction, strength;
	if(!HTIRS2readEnhanced(irRight, direction, strength)){
		writeDebugStreamLine("something's wrong");
		return -1;
	}
	return strength;
}
int getIrDirection(short sensor){
	int dirDC = HTIRS2readDCDir(sensor);


	return dirDC;
}
