/*
* Include some necessary files
*/
#include "get_ir.c"
#include "multiplexer.c"
#include "color_mode_picker.c"

/*
* Define constants that define rotation directions
*/
#define CLOCKWISE 1
#define COUNTERCLOCKWISE -1
short blockTurnDirection = CLOCKWISE;

/*
* Flags to check that certain tasks are done
*/
bool foundIr = false;
bool placedBlock = false;
bool foundWhiteLine = false;

/*
* Set all motors to the input value
*/
void allMotorsTo(int i){
	motor[mDriveLeft] 	= i;
	motor[mDriveRight] 	= i;
	motor[mBsAngle] 		= i;
	motor[mBsConveyor] 	= i;
	if (time100[T1] % 10 == 0) writeDebugStreamLine("Set all motors to %d", i);
}

/*
* Set all drive motors to the input value
*/
void driveMotorsTo(int i){
	motor[mDriveLeft] 	= i;
	motor[mDriveRight] 	= i;
	if (time100[T1] % 10 == 0) writeDebugStreamLine("Set drive motors to %d", i);
}

void moveXWheelRoations(int rotations, int inMotor){
	writeDebugStreamLine("Moving %d rotations", rotations);
	const int fullStrength = 75;
	int encoderStartValue = nMotorEncoder[inMotor];
	int encoderTargetValue = encoderStartValue + (rotations * 4000);
	if(rotations > 0){
		while(nMotorEncoder[inMotor] < encoderTargetValue){
			driveMotorsTo(fullStrength);
		}
		driveMotorsTo(0);
	}else{
		while(nMotorEncoder[inMotor] > encoderTargetValue){
			driveMotorsTo(-1 * fullStrength);
		}
	}
	writeDebugStreamLine("Done");
}

/*
* Turn to the specified degree angle
*/
void turnXDegrees(float degreesToTurn){
	const int turnStrength = 25;
	float degreesSoFar = 0;						// Degrees turned thus far
	int initialTurnReading = rawGyro;	// Take an initial reading from the gyro

	/*
	* Decide to turn right or left
	*/
	if (degreesToTurn > 0){
		motor[mDriveLeft] = -1 * turnStrength;	// Turn left
		motor[mDriveRight] = turnStrength;
		writeDebugStreamLine("Decided to turn counterclockwise");
		}else{
		motor[mDriveLeft] = turnStrength;				// Turn right
		motor[mDriveRight] = -1 * turnStrength;
		writeDebugStreamLine("Decided to turn clockwise");
	}

	/*
	* While the degrees we've turned is less than the total degrees we have to turn
	*/
	while (abs(degreesSoFar) < abs(degreesToTurn)){
		wait1Msec(10);	// Let some time pass
		int currentGyroReading = rawGyro - initialTurnReading;	// Edit the current gyro reading
		degreesSoFar = degreesSoFar + (currentGyroReading * 0.01); // Calculate the degrees turned so far (d=r*t)
	}
	driveMotorsTo(0);	// Stop the motors
	clearDebugStream();
	writeDebugStreamLine("Current angle: %2.2f", degreesSoFar);
	writeDebugStreamLine("Target angle: %2.2f", degreesToTurn);
	writeDebugStreamLine("While loop over");
}

task showDebugInfo(){
	while(true){
		nxtDisplayTextLine(0, "mtrEncL:%d", nMotorEncoder[mDriveLeft]);
		nxtDisplayTextLine(1, "mtrEncR:%d", nMotorEncoder[mDriveRight]);
		nxtDisplayTextLine(2, "LiL:%d", rawLightLeft);
		nxtDisplayTextLine(3, "LiR:%d", rawLightRight);
		nxtDisplayTextLine(4, "");
		nxtDisplayTextLine(5, "");
		nxtDisplayTextLine(6, "");
		nxtDisplayTextLine(7, "");
	}
}

/*
* Go forward, and don't stop until you find the IR beacon
*/
task findIr()
{
	/*
	* Declare some local variables
	*/
	const int slowThresh 			= 180;	// IR detection level where you slow down
	const short stopThresh 		= 250;	// IR detection level where you stop completely
	const int fullStrength 		= 25;		// max motor strenght for finding the IR
	const int minStrength 		= 10;		// min motor strength for finding the IR
	const int turnStrength 		= 25;		// strength of the motors while turning
	short maxIrSig = 0;

	while (true){		// Loop forever
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


		if (maxIrSig < slowThresh){		// If the IR signal is less than the slow threshold
			driveMotorsTo(fullStrength);// Go full power
		}
		if (maxIrSig >= slowThresh && maxIrSig < stopThresh){	// If the IR signal is larger than the slow threshold
			driveMotorsTo(minStrength);													// Go to low power
		}
		if (maxIrSig >= stopThresh){	// If the IR signal is greater than the stop threshold
			driveMotorsTo(0);						// Stop the motors
			foundIr = true;							// Toggle the flag
		}
	}
}

/*
* Turn the robot and place the block in the crate
*/
task placeBlock(){
	const int raiseRotations 	= 9;		// Number of motor rotations needed to lift the BS all the way up

	turnXDegrees(90 * blockTurnDirection);			// Turn the robot 90 degrees in the chosen direction
	writeDebugStreamLine("Raising blockSucker");// Print stuff to the debug screen
	while (nMotorEncoder[mBsAngle] < (raiseRotations * 4000)){	// While the lift motor is below the upper value
			motor[mBsAngle] = 100;																	// Run the motor
	}
	motor[mBsAngle] = 0;						// Stop the motor
	writeDebugStreamLine("blockSucker raised, placing block");
	motor[mBsConveyor] = 100;				// Run the conveyor, drop the block
	wait10Msec(200);								// Let some time pass
	motor[mBsConveyor] = 0;					// Stop the motor
	writeDebugStreamLine("Block placed, turning back");
	turnXDegrees(90 * (-1 * blockTurnDirection));	// Turn 90 degrees back in the opposite direcgtion as before
	writeDebugStreamLine("Done placing block");

	placedBlock = true;							// Toggle the flag
}

/*
* Find the white line, and use it to align the robot
*/
task findWhiteLine(){
	const int slowThresh = 64;
	bool foundLeft, foundRight = false;

	foundWhiteLine = false;

	driveMotorsTo(60);
	while(!foundLeft || !foundRight){
		if(rawLightLeft > slowThresh){
			motor[mDriveLeft] = 0;
			foundLeft = true;
		}
		if(rawLightRight > slowThresh){
			motor[mDriveRight] = 0;
			foundRight = true;
		}

	}
	turnXDegrees(((getAutoMode() % 2 == 0)? 90 : -90));	// Turn left if the drive mode is even, right if it is odd

	foundWhiteLine = true;
}
