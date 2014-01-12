#include "get_ir.c"
#include "multiplexer.c"


const int slowThresh = 180;
const int fullStrength = 25;
const int minStrength = 10;
const int turnStrength = 25;
const short stopThresh = 250;
bool foundIr = false;
short maxIrSig = 0;

// Set all motors to the input value
void allMotorsTo(int i){
	motor[mDriveLeft] 	= i;
	motor[mDriveRight] 	= i;
	motor[mBsAngle] 		= i;
	motor[mBsConveyor] 	= i;
	if (time100[T1] % 10 == 0) writeDebugStreamLine("Set all motors to %d", i);
}

// Set all drive motors to the input value
void driveMotorsTo(int i){
	motor[mDriveLeft] 	= i;
	motor[mDriveRight] 	= i;
	if (time100[T1] % 10 == 0) writeDebugStreamLine("Set drive motors to %d", i);
}

// turn the specified degree angle
void turnXDegrees(float degreesToTurn){
	float degreesSoFar = 0;
	int initialTurnReading = rawGyro;

	if (degreesToTurn > 0){
		motor[mDriveLeft] = -1 * turnStrength;
		motor[mDriveRight] = turnStrength;
		writeDebugStreamLine("Decided to turn counterclockwise");
		}else{
		motor[mDriveLeft] = turnStrength;
		motor[mDriveRight] = -1 * turnStrength;
		writeDebugStreamLine("Decided to turn clockwise");
	}

	while (abs(degreesSoFar) < abs(degreesToTurn)){
		wait1Msec(10);
		int currentGyroReading = rawGyro - initialTurnReading;
		degreesSoFar = degreesSoFar + (currentGyroReading * 0.01); // d = r * t
		if (true /*time100[T1] % 10 == 0*/){
			//clearDebugStream();
			//writeDebugStreamLine("Current angle: %2.2f", degreesSoFar);
			//writeDebugStreamLine("Target angle: %2.2f", degreesToTurn);
			nxtDisplayTextLine(0, "CurrAn: %2.2f", degreesSoFar);
			nxtDisplayTextLine(1, "TarAng: %2.2f", degreesToTurn);
		}

	}
	driveMotorsTo(0);
	clearDebugStream();
	writeDebugStreamLine("Current angle: %2.2f", degreesSoFar);
	writeDebugStreamLine("Target angle: %2.2f", degreesToTurn);
	writeDebugStreamLine("While loop over");
}

// Go and don't stop until the IR sensor detects a certain strength
// (May change to detecting direction, not distance)
task findIr()
{
	while (true){
		maxIrSig = getIrStrength(IRS_R);

		if (time100[T1] % 10 == 0){
			clearDebugStream();
			writeDebugStreamLine("Searching for IR");
			if (maxIrSig > 0) writeDebugStreamLine("Got signal");
			else writeDebugStreamLine("No or bad signal");
			writeDebugStreamLine("Signal = %d", maxIrSig);
			if (maxIrSig < slowThresh){ if (time100[T1] % 10 == 0) writeDebugStreamLine("MAX POWAH");}
			if (maxIrSig >= slowThresh && maxIrSig < stopThresh){ if (time100[T1] % 10 == 0) writeDebugStreamLine("Min power");}
			if (maxIrSig > stopThresh){if (time100[T1] % 10 == 0) writeDebugStreamLine("Found IR. Stopping...");}
		}


		if (maxIrSig < slowThresh){
			driveMotorsTo(fullStrength);
		}
		if (maxIrSig >= slowThresh && maxIrSig < stopThresh){
			driveMotorsTo(minStrength);
		}
		if (maxIrSig >= stopThresh){
			driveMotorsTo(0);
			foundIr = true;
		}
	}
}

task raiseSuckerToLevel(){

}

bool placeBlock(bool placeMode = true, float degreesToTurn = 90.0, float gearRatio = 9.0){
	if (placeMode){
		// place from front
	}
	else{
		// place from back
	}
}
