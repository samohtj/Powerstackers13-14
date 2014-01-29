/*
* Include some necessary files
*/
//#include "get_ir.c"
#include "multiplexer.c"
#include "color_mode_picker.c"

/*
* Flags to check that certain tasks are done
*/

/*
* Set all motors to the input value
*/
void allMotorsTo(int i){
	motor[mDriveLeft] 	= i;
	motor[mDriveRight] 	= i;
	motor[mBsAngle] 		= i;
	motor[mBsConveyor] 	= i;
	writeDebugStreamLine("Set all motors to %d", i);
}

/*
* Set all drive motors to the input value
*/
void driveMotorsTo(int i){
	motor[mDriveLeft] 	= i;
	motor[mDriveRight] 	= i;
	if (time100[T1] % 10 == 0) writeDebugStreamLine("Set drive motors to %d", i);
}

float getTicksForFeet(float feet){
	float ticks =  (float) feet * (12000 / PI);
	return  ticks;
}

void goFeet(float feet, int speed){
	writeDebugStreamLine("Moving %d feet", feet);
	float ticks = getTicksForFeet(feet);
	long encoderStartValue = nMotorEncoder[mDriveLeft];
	long encoderTargetValue = (long) encoderStartValue + ticks;
	if(feet > 0){
		while(nMotorEncoder[mDriveLeft] < encoderTargetValue){
			driveMotorsTo(speed);
		}
		driveMotorsTo(0);
	}else{
		while(nMotorEncoder[mDriveLeft] > encoderTargetValue){
			driveMotorsTo(-1 * speed);
		}
	}
	writeDebugStreamLine("Done");
}

/*
* Turn to the specified degree angle
*/
void turnDegrees(float degreesToTurn){
	const int turnStrength = 25;
	float degreesSoFar = 0;						// Degrees turned thus far
	int initialTurnReading = SensorValue[sGyro];	// Take an initial reading from the gyro

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
		int currentGyroReading = sGyro - initialTurnReading;	// Edit the current gyro reading
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
		nxtDisplayTextLine(4, "touch:%d,%d,%d", touchInput1, touchInput2, touchInput3);
		nxtDisplayTextLine(5, "irRL:%d,%d", SensorValue[irRight], SensorValue[irLeft]);
		nxtDisplayTextLine(6, "");
		nxtDisplayTextLine(7, "");
	}
}

/*
* Go forward, and don't stop until you find the IR beacon
*/
void findIrRight(int fullStrength, int minStrength, int turnStrength)
{
	writeDebugStreamLine("findIrRight entered");
	/*
	* Declare some local variables
	*/
	const int slowThresh 			= 70;	// IR detection level where you slow down
	const short stopThresh 		= 150;	// IR detection level where you stop completely
	bool foundIr = false;
	writeDebugStreamLine("Variables declared without incident");

	while (!foundIr){		// Loop until IR is found

		writeDebugStreamLine("Got IR strength");
		if (irStrengthRight < slowThresh){		// If the IR signal is less than the slow threshold
			driveMotorsTo(fullStrength);// Go full power
		}
		if (irStrengthRight >= slowThresh && irStrengthRight < stopThresh){	// If the IR signal is larger than the slow threshold
			driveMotorsTo(minStrength);													// Go to low power
		}
		if (irStrengthRight >= stopThresh){	// If the IR signal is greater than the stop threshold
			driveMotorsTo(0);						// Stop the motors
			foundIr = true;							// Toggle the flag
			PlaySound(soundBeepBeep);
		}
	}
}

void findIrLeft(int fullStrength, int minStrength, int turnStrength)
{
	writeDebugStreamLine("findIrLeft entered");
	/*
	* Declare some local variables
	*/
	const int slowThresh 			= 70;	// IR detection level where you slow down
	const short stopThresh 		= 150;	// IR detection level where you stop completely
	bool foundIr = false;
	writeDebugStreamLine("Variables declared without incident");

	while (!foundIr){		// Loop until IR is found

		writeDebugStreamLine("Got IR strength");
		if (irStrengthLeft < slowThresh){		// If the IR signal is less than the slow threshold
			driveMotorsTo(fullStrength);// Go full power
		}
		if (irStrengthLeft >= slowThresh && irStrengthLeft < stopThresh){	// If the IR signal is larger than the slow threshold
			driveMotorsTo(minStrength);													// Go to low power
		}
		if (irStrengthLeft >= stopThresh){	// If the IR signal is greater than the stop threshold
			driveMotorsTo(0);						// Stop the motors
			foundIr = true;							// Toggle the flag
			PlaySound(soundBeepBeep);
		}
	}
}
/*
* Turn the robot and place the block in the crate
*/
void placeBlock(int turnDirection){
	const long liftEncoderValue	= 9;		// Number of motor rotations needed to lift the BS all the way up

	turnDegrees(90 * turnDirection);			// Turn the robot 90 degrees in the chosen direction
	while (nMotorEncoder[mBsAngle] < (liftEncoderValue)){	// While the lift motor is below the upper value
			motor[mBsAngle] = 100;																	// Run the motor
	}
	motor[mBsAngle] = 0;						// Stop the motor
	motor[mBsConveyor] = 100;				// Run the conveyor, drop the block
	wait10Msec(200);								// Let some time pass
	motor[mBsConveyor] = 0;					// Stop the motor
	turnDegrees(90 * (-1 * turnDirection));	// Turn 90 degrees back in the opposite direcgtion as before
}

/*
* Find the white line, and use it to align the robot
*/
void findWhiteLine(bool skipNearLine, int turnDirection){
	const int slowThresh = 64;
	bool foundWhiteLine = false;
	bool nearLineSkipped = true;
	if(!skipNearLine){
		nearLineSkipped = false;
	}

	driveMotorsTo(100);
	while(!foundWhiteLine){
		if(rawLightLeft > slowThresh || rawLightRight > slowThresh){
			if(nearLineSkipped){
				driveMotorsTo(0);
				foundWhiteLine = true;
			}
			nearLineSkipped = true;
		}
	}
}

void returnToSpot(long distanceFromHome, long home){
	while(nMotorEncoder[mDriveLeft] > (home + getTicksForFeet(distanceFromHome))){
		driveMotorsTo(-100);
	}
	driveMotorsTo(0);
}
