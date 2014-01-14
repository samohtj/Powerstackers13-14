#include "get_ir.c"
#include "multiplexer.c"
#include "color_mode_picker.c"

#define CLOCKWISE 1
#define COUNTERCLOCKWISE -1


const int slowThresh 			= 180;	// IR detection level where you slow down
const short stopThresh 		= 250;	// IR detection level where you stop completely
const int fullStrength 		= 25;		// max motor strenght for finding the IR
const int minStrength 		= 10;		// min motor strength for finding the IR
const int turnStrength 		= 25;		// strength of the motors while turning
const int raiseRotations 	= 9;		// number of motor rotations needed to lift the BS all the way up

bool foundIr = false;
bool placedBlock = false;
short maxIrSig = 0;
short blockTurnDirection = CLOCKWISE;

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

task placeBlock(){
	turnXDegrees(90 * blockTurnDirection);
	writeDebugStreamLine("Raising blockSucker");
	while (nMotorEncoder[mBsAngle] < (raiseRotations * 4000)){
			motor[mBsAngle] = 100;
	}
	motor[mBsAngle] = 0;
	writeDebugStreamLine("blockSucker raised, placing block");
	motor[mBsConveyor] = 100;
	wait10Msec(200);
	motor[mBsConveyor] = 0;
	writeDebugStreamLine("Block placed, turning back");
	turnXDegrees(90 * (-1 * blockTurnDirection));
	writeDebugStreamLine("Done placing block");

	placedBlock = true;
}

task findWhiteLine(){

}
