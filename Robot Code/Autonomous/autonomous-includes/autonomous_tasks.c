/*
* Include some necessary files
*/
//#include "get_ir.c"
#include "multiplexer.c"
#include "color_mode_picker.c"

/*
* Set all motors to the input value
*/
void allMotorsTo(int i){
	motor[mDriveLeft] 	= i;
	motor[mDriveRight] 	= i;
	motor[mBsAngle] 		= i;
	motor[mBsConveyor] 	= i;
}

/*
* Set all drive motors to the input value
*/
void driveMotorsTo(int i){
	motor[mDriveLeft] 	= i;
	motor[mDriveRight] 	= i;
}

/*
*	DEPRECATED
* Convert a distance in feet to a distance in encoder ticks
*/
float getTicksForFeet(float feet){
	float ticks =  (float) feet * (12000 / PI);
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
*	DEPRECATED
*	Go a certain distance in feet
*/
void goFeet(float feet, int speed){
	writeDebugStreamLine("Moving %d feet", feet);
	float ticks = getTicksForFeet(feet);
	writeDebugStreamLine("Moving %5.2f ticks", ticks);
	nxtDisplayTextLine(7, "encTar:%5.2f", ticks);
	writeDebugStreamLine("Rotations to go: %5.2f", (ticks / 4000));
	long encoderStartValue = nMotorEncoder[mDriveRight];
	long encoderTargetValue = (long) encoderStartValue + ticks;
	writeDebugStreamLine("encoder target = %5.2f", encoderTargetValue);

	if(feet > 0){
		writeDebugStreamLine("going forwards");
		while(abs(nMotorEncoder[mDriveRight]) < encoderTargetValue){
			driveMotorsTo(speed);
			writeDebugStreamLine("encoder value: %5.2f ; target value: %5.2f", nMotorEncoder[mDriveRight], encoderTargetValue);
		}
		writeDebugStreamLine("loop over");
		driveMotorsTo(0);
		}else{
		writeDebugStreamLine("Going backwards");
		while(abs(nMotorEncoder[mDriveLeft]) < encoderTargetValue){
			driveMotorsTo(-1 * speed);
		}
		driveMotorsTo(0);
	}
	writeDebugStreamLine("Done");
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
* Go forward, and don't stop until you find the IR beacon
*/
short findIrRight(int fullStrength, int minStrength, int turnStrength)
{
	writeDebugStreamLine("findIrRight entered");	// Print some info

	/*
	* Declare some local variables
	*/
	const int slowThresh 			= 50;	// IR detection level where you slow down
	const short stopThresh 		= 50;	// IR detection level where you stop completely
	bool foundIr = false;						// Flag, IR found or not found
	writeDebugStreamLine("Variables declared without incident");

	while (!foundIr){												// Loop until IR is found
		if (irStrengthRight < slowThresh){		// If the IR signal is less than the slow threshold
			driveMotorsTo(fullStrength);				// Go full power
		}
		if (irStrengthRight >= slowThresh && irStrengthRight < stopThresh){	// If the IR signal is larger than the slow threshold
			driveMotorsTo(minStrength);																				// Go to low power
		}
		if (irStrengthRight >= stopThresh){	// If the IR signal is greater than the stop threshold
			driveMotorsTo(0);									// Stop the motors
			foundIr = true;										// Toggle the flag
			PlaySound(soundBeepBeep);
		}
	}

		/*
		*	Figure out which basket we stopped at (Work In Progress)
		*/
		const long basketDist1 = 1300;	// Distance from start to near basket
		const long basketDist2 = 2600;	// Distance from start to second basket
		const long basketDist3 = 3900;	// Distance from start to third basket
		short basket;										// Variable to store our current position
		if(nMotorEncoder[mDriveRight] < basketDist1)	// If we are closer than the near basket
			basket = 1;																	// First position
		else if(nMotorEncoder[mDriveRight] < basketDist2)	// If we are closer than the second basket
			basket = 2;																			// Second position
		else if(nMotorEncoder[mDriveRight] < basketDist3)	// If we are closer than the third basket
			basket = 3;																			// Third position
		else																							// Farther than the third basket
			basket = 4;																			// Fourth position
		return basket;
}

/*
*	Locate the IR basket and stop in front of it
*/
short findIrLeft(int fullStrength, int minStrength, int turnStrength)
{
	writeDebugStreamLine("findIrLeft entered");	// Print some info
	/*
	* Declare some local variables
	*/
	const int slowThresh 			= 50;	// IR detection level where you slow down
	const short stopThresh 		= 50;	// IR detection level where you stop completely
	bool foundIr = false;						// Flag, IR found or not found

	while (!foundIr){																	// Loop until IR is found
		writeDebugStreamLine("IR=%d", irStrengthLeft);	// Print the starting IR strength
		if (irStrengthLeft < slowThresh){								// If the IR signal is less than the slow threshold
			driveMotorsTo(fullStrength);									// Go full power
		}
		if (irStrengthLeft >= slowThresh && irStrengthLeft < stopThresh){	// If the IR signal is larger than the slow threshold
			driveMotorsTo(minStrength);										// Go to low power
		}
		if (irStrengthLeft >= stopThresh){							// If the IR signal is greater than the stop threshold
			driveMotorsTo(0);															// Stop the motors
			foundIr = true;																// Toggle the flag
			PlaySound(soundBeepBeep);											// Play an NXT sound to alert the operators
		}
	}
	writeDebugStreamLine("Maximum value detected: %d", irStrengthLeft);	// Print the current value detected (the maximum)

		const long basketDist1 = 1300;
		const long basketDist2 = 2600;
		const long basketDist3 = 3900;
		short basket;
		if(nMotorEncoder[mDriveRight] < basketDist1)
			basket = 1;
		else if(nMotorEncoder[mDriveRight] < basketDist2)
			basket = 2;
		else if(nMotorEncoder[mDriveRight] < basketDist3)
			basket = 3;
		else
			basket = 4;
		return basket;
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
void findWhiteLine(bool skipNearLine){
	const int slowThresh = 64;		// The signal strength at which we slow down
	bool foundWhiteLine = false;	// Flag, white line found or not found
	bool nearLineSkipped = true;	// Flag, white line skipped or not skipped
	if(!skipNearLine){						// If we are not going to skip the first line
		nearLineSkipped = false;		// Set nearLineSkipped to false
	}

	driveMotorsTo(100);						// Motors to full power
	while(!foundWhiteLine){				// While white line hasn't been found
		if(rawLightLeft > slowThresh || rawLightRight > slowThresh){	// If both signals are higher than the slow threshold
			if(nearLineSkipped){			// If the line has already been skipped
				driveMotorsTo(0);				// Stop the motors
				foundWhiteLine = true;	// foundWhiteLine to true
			}
			nearLineSkipped = true;		// Set nearLineSkipped to true, if it wasn't already
		}
	}
}

/*
*	Return the robot to its starting position
*/
void returnToSpot(long distanceFromHome, long home){
	while(nMotorEncoder[mDriveLeft] > (home + getTicksForFeet(distanceFromHome))){	// While the current distance is greater than the starting distance
		driveMotorsTo(-100);	// Move backwards
	}
	driveMotorsTo(0);				// Stop the motors
}

/*
*	NOT IMPLEMENTED
*	Use the gyro sensor to keep the robot moving in a straight line, no matter what
*/
//task gyroAlign(){
//	while(true){
//		writeDebugStreamLine("hi");	// Print some info
//		// As soon as we figure out how to get this to work, we'll let you know ;)
//	}
//}

/*
*	Raise the
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

/*
*	NOT IMPLEMENTED
*	Ask the operator if we're on the right or the left
*/
int getFieldSide(){
	bool gotInput = false;	// Flag, input entered or not entered
	int side = 0;						// Integer to store the side. Will change to 1 or -1
	eraseDisplay();					// Clear the screen
	do{											// Print everything, and loop until the input is entered
		nxtDisplayTextLine(0, "Right or left side?");
		nxtDisplayTextLine(3, "Left   Right");
		nxtDisplayTextLine(4, " <       >");
		if(nNxtButtonPressed == 2){				// If left arrow button pressed
			side = -1;
		}else if(nNxtButtonPressed == 1){	// If right arrow button is pressed
			side = 1;
		}
	}while(!gotInput);
	return side;
}

long inchesToTicks(float inches){
	return (long) inches / 0.01185;
}
