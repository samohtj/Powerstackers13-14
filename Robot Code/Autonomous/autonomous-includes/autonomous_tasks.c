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

/*
* Turn to the specified degree angle
*/
void turnXDegrees(float degreesToTurn){
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

}
