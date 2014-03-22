/*
* Include some necessary files
*/
#include "multiplexer.h"

/*
*	Global Variables
*/
const int lightThreshold = 144;
const int irThresh = 225;
long startEncoderPos = 0;

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
long inchesToTicks(float inches){
	long ticks =  (long) inches * (1350 / (4 * PI));
	return  ticks;
}

float ticksToInches(long ticks){
	float inches = (float) ticks / (1350 / (4 * PI));
	return inches;
}

/*
*	Move forward until the motor encoder reaches a certain value
*/
void goTicks(long ticks, int speed){
	ClearTimer(T1);
	long target = nMotorEncoder[mDriveRight] + ticks;						// Target distance
	writeDebugStreamLine("-- MOVING TICKS --\ntarget: %5.2f, current:%5.2f (%d inches) (speed: %d)", target, nMotorEncoder[mDriveRight], ticksToInches(ticks), speed);// Print some info
	//int leftMotorSpeed = speed + 15;														// Compensate for the hardware issues
	float leftMotorRatio = (float) 80 / 100;
	if(ticks > 0){																	// If the distance is negative...
		while(nMotorEncoder[mDriveRight] < target){		// While the encoder value is less than the target
			//if(time100[T1] % 10 == 0)
				//writeDebugStreamLine("current: %5.2f", nMotorEncoder[mDriveRight]);
			motor[mDriveRight] = (int) speed * leftMotorRatio;									// Move backward
			motor[mDriveLeft] = speed;

		}
	}else{																					// If the distance is positive...
		while(nMotorEncoder[mDriveRight] > target){		// While the encoder value is less than the target
			writeDebugStreamLine("current: %5.2f", nMotorEncoder[mDriveRight]);
			motor[mDriveRight] = (float) -1 * (speed * leftMotorRatio);						// Move forward
			motor[mDriveLeft] = -1 * speed;
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
	writeDebugStreamLine("-- TURNING --\ninitial reading: %d\nTarget angle: %2.2f", initialTurnReading, degreesToTurn);	// Print some info
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
*	Operate the flippers
*/
const short blockDropLeftStart = 0;
const short blockDropRightStart = 245;

const short blockDropLeftIdle = 128;
const short blockDropRightIdle = 128;

const short blockDropLeftDrop = 180;
const short blockDropRightDrop = 32;

const short conveyorTightStart = 150;
const short conveyorTightActive = 170;

void blockDrop(){
	servo[rBlockDropLeft] = blockDropLeftDrop;
	servo[rBlockDropRight] = blockDropRightDrop;
}

void blockRetract(){
	servo[rBlockDropLeft] = blockDropLeftStart;
	servo[rBlockDropRight] = blockDropRightStart;
}

void blockIdle(){
	servo[rBlockDropLeft] = blockDropLeftIdle;
	servo[rBlockDropRight] = blockDropRightIdle;
}

/*
* Turn the robot and place the block in the crate
*/
void placeBlock(){
	writeDebugStreamLine("-- PLACING BLOCK --");
	// Move the robot so that the droppers are lined up with the basket
	//long dist = 0;
	//if(basketNum == 0 || basketNum == 1)
	//	dist = inchesToTicks(8);
	//if(basketNum == 2 || basketNum == 3)
	//	dist = inchesToTicks(6);
	//goTicks(inchesToTicks(8), 50);

	// Extend the servos and drop the blocks
	servo[rBlockDropLeft] = blockDropLeftDrop;
	servo[rBlockDropRight] = blockDropRightDrop;

	wait10Msec(100);

	// Put the servos into the rest position
	servo[rBlockDropLeft] = blockDropLeftIdle;
	servo[rBlockDropRight] = blockDropRightIdle;
}

/*
* Find the white line, and use it to align the robot
*/
void findWhiteLine(){
	bool foundLineLeft = false;
	bool foundLineRight = false;

	while(!foundLineLeft || !foundLineRight){
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

/*
*	Initialize the robot before the start of the autonomous
*/
void initializeRobot(){

	allMotorsTo(0);
	nMotorEncoder[mDriveLeft] = 0;
	servo[rBlockDropLeft] = blockDropLeftStart;
	servo[rBlockDropRight] = blockDropRightStart;
	servo[rConveyorTight] = conveyorTightStart;
	writeDebugStreamLine("\n\n -- NEW INSTANCE -- \n\n");
}

void findIrIncremental(){
	long blockDistancesCumulative[3] = {startEncoderPos + inchesToTicks(20), startEncoderPos + inchesToTicks(43), startEncoderPos + inchesToTicks(53)};
	goTicks(inchesToTicks(10), 25);
	writeDebugStreamLine("At first basket, ready to start.\n");
	//wait10Msec(100);
	PlaySound(soundBeepBeep);
	for(int i = 0; i <= 3; i++){
		//if(i == 2){
		//	placeBlock();
		//	break;
		//}
		if(irStrengthLeft > irThresh || irStrengthRight > irThresh){
			// InfraRed has been found
			goTicks(inchesToTicks(6), 25);
			placeBlock();
			writeDebugStreamLine("Block placed, moving on.\n");
			break;
		}
		else{
			// InfraRed has not been found, go to next basket
			goTicks(blockDistancesCumulative[i] - nMotorEncoder[mDriveRight], 25);
			writeDebugStreamLine("Going to next basket");
			//wait10Msec(100);
		}

		writeDebugStreamLine("\n-- BASKET #%d --\nCurrent Value: %d. Need %d to stop.", i+1, (irStrengthLeft > irStrengthRight)? irStrengthLeft:irStrengthRight, irThresh);
	}
}

void findIrContinuous(){
	while(irStrengthLeft < irThresh || irStrengthRight > irThresh){
		driveMotorsTo(25);
	}
	driveMotorsTo(0);
}
