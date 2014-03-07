/*
* Include some necessary files
*/
#include "multiplexer.h"

/*
* Set all motors to the input value
*/
void allMotorsTo(int i){
	motor[mDriveLeft] 	= i;
	motor[mDriveRight] 	= i;
	motor[mBsAngle] 		= i;
	motor[mBsConveyor] 	= i;
	motor[mFlagRaise1] 	= i;
	motor[mFlagRaise2] 	= i;
}

/*
* Set all drive motors to the input value
*/
void driveMotorsTo(int i){
	motor[mDriveLeft] 	= i;
	motor[mDriveRight] 	= i;
}

/*
* Convert a distance in inches to a distance in encoder ticks
*/
long inchesToTicks(float feet){
	long ticks =  (long) feet * (12000 / PI);
	return  ticks;
}

/*
*	Move forward until the motor encoder reaches a certain value
*/
void goTicks(long ticks, int speed){
	long target = nMotorEncoder[mDriveRight] + ticks;						// Target distance
	writeDebugStreamLine("MOVING TICKS\ntarget: %5.2f", target);// Print some info
	int leftMotorSpeed = speed + 15;														// Compensate for the hardware issues
	if(ticks > 0){																	// If the distance is negative...
		while(nMotorEncoder[mDriveRight] < target){		// While the encoder value is less than the target
			motor[mDriveRight] = speed;									// Move backward
			motor[mDriveLeft] = leftMotorSpeed;
		}
	}else{																					// If the distance is positive...
		while(nMotorEncoder[mDriveRight] > target){		// While the encoder value is less than the target
			motor[mDriveRight] = -1 * speed;						// Move forward
			motor[mDriveLeft] = -1 * leftMotorSpeed;
		}
	}
	writeDebugStreamLine("final:  %5.2f", nMotorEncoder[mDriveRight]);	// Print some info
	allMotorsTo(0);																	// Stop all motors
}

/*
* Turn to the specified degree angle
*/
void turnDegrees(float degreesToTurn, int turnStrength){
	float degreesSoFar = 0;													// Degrees turned thus far
	int leftTurnStrength = turnStrength + 15;
	int initialTurnReading = HTGYROreadRot(sGyro);	// Take an initial reading from the gyro
	writeDebugStreamLine("TURNING\ninitial reading: %d", initialTurnReading);	// Print some info
	writeDebugStreamLine("Target angle: %2.2f", degreesToTurn);
	/*
	* Decide to turn right or left
	*/
	if (degreesToTurn > 0){										// If the degree measure is positive
		motor[mDriveLeft] = -1 * leftTurnStrength;	// Turn left
		motor[mDriveRight] = turnStrength;
		writeDebugStreamLine("Decided to turn counterclockwise");
		}else{																	// If the degree measure is negative
		motor[mDriveLeft] = leftTurnStrength;				// Turn right
		motor[mDriveRight] = -1 * turnStrength;
		writeDebugStreamLine("Decided to turn clockwise");
	}

	/*
	* While the degrees we've turned is less than the total degrees we have to turn
	*/
	while (abs(degreesSoFar) < abs(degreesToTurn)){
		wait1Msec(10);																											// Let some time pass
		int currentGyroReading = HTGYROreadRot(sGyro) - initialTurnReading;	// Edit the current gyro reading
		degreesSoFar = degreesSoFar + (currentGyroReading * 0.01); 					// Calculate the degrees turned so far (d=r*t)
		writeDebugStreamLine("Currentangle: %d", degreesSoFar);
	}
	driveMotorsTo(0);	// Stop the motors
	writeDebugStreamLine("final angle: %2.2f", degreesSoFar);
}

/*
* Print some important debug info
*/
task showDebugInfo(){
	while(true){
		nxtDisplayTextLine(0, "mtrEncL:%d", nMotorEncoder[mDriveLeft]);		// Left motor encoder
		nxtDisplayTextLine(1, "mtrEncR:%d", nMotorEncoder[mDriveRight]);	// Right motor encoder
		nxtDisplayTextLine(2, "LiL:%d", rawLightLeft);										// Left light sensor
		nxtDisplayTextLine(3, "LiR:%d", rawLightRight);										// Right light sensor
		nxtDisplayTextLine(4, "touch:%d,%d,%d", touchInput1, touchInput2, touchInput3);	// Touch sensors
		nxtDisplayTextLine(5, "irRL:%d,%d", irStrengthLeft, irStrengthRight);	// IR seekers
		nxtDisplayTextLine(6, "HighestIR:%d", irStrengthRight);								// Right IR seeker
		//nxtDisplayTextLine(7, "");

		//clearDebugStream();
		//writeDebugStreamLine("R-Encoder: %d", nMotorEncoder[mDriveRight]);
		//writeDebugStreamLine("L-Encoder: %d", nMotorEncoder[mDriveLeft]);
	}
}

/*
* Turn the robot and place the block in the crate
*/
void placeBlock(int basketNum){
	// This is where the block placing code will go
}

/*
* Find the white line, and use it to align the robot
*/
void findWhiteLine(){
	bool foundLineLeft = false;
	bool foundLineRight = false;
	const int lightThreshold = 144;

	while(!foundLineLeft && !foundLineRight){
		if(!foundLineLeft)
			motor[mDriveLeft] = 50;
		else
			motor[mDriveLeft] = 0;

		if(!foundLineRight)
			motor[mDriveRight] = 50;
		else
			motor[mDriveRight] = 0;

		if(lightSenseLeft > lightThreshold)
			foundLineLeft = true;
		if(lightSenseRight > lightThreshold)
			foundLineRight = true;
	}
}

////////////
//	These next two functions are in here for backwards compatibility only.
//
///////////
/*
*	Raise the sucker
*/
void suckerToDropPosition(){
	const long topEncoderPos = 7500;													// Encoder position where we will stop
	const long startEncoderPos = nMotorEncoder[mBsAngle];			// Starting encoder position
	const long target = abs(startEncoderPos + topEncoderPos);	// Target encoder position
	writeDebugStreamLine("Startingn encoder value: %d, going to %d", nMotorEncoder[mBsAngle], target);	// Print some info
	while(nMotorEncoder[mBsAngle] < target){	// Loop until the encoder hits the target
		motor[mBsAngle] = 100;									// BS motor to full power
		writeDebugStreamLine("Lift encoder value: %d", nMotorEncoder[mBsAngle]);	// Print some info
	}
	motor[mBsAngle] = 0;											// Stop the motor
	writeDebugStreamLine("Done raising");			// Print some info
}

/*
*	Drop the block
*/
void spitBlock(){
	motor[mBsConveyor] = -100;	// Set conveyor motor to full reverse
	wait10Msec(300);						// Wait three seconds
	motor[mBsConveyor] = 0;			// Stop the motor
}
