/////////////////////////////////////
//
//	AUTONOMOUS PROGRAM FUNCTIONS AND SUBROUTINES
//	CODE BY FTC TEAM# 5029, THE POWERSTACKERS
//	github.com/samohtj/PowerstackersFTC-5029
//	powerstackersftc.weebly.com
//	UPDATED 3-22-2014
//
////////////////////////////////////

// Include the multiplexer driver, and the autonomous menu
#include "multiplexer.h"
#include "autoMenu.h"

// Declare global variables:
// lightThreshold: The robot looks for light levels above this value when finding the line
// irThresh: The robot looks for light levels above this value when placing the block
// turnSpeed: The speed of the motors when turning
// startEncoderPosition: The value of the motor encoders at the start of the match
int lightThreshold = 470;
const int irThresh = 180;
const int turnSpeed = 50;
long startEncoderPos = 0;

///////////////////////////////////////////////////////
//
//	Set all motors to the input parameter value
//
//////////////////////////////////////////////////////
void allMotorsTo(int i){
	motor[mDriveLeft] 	= i;
	motor[mDriveRight] 	= i;
	motor[mBsAngle] 	= i;
	motor[mBsConveyor] 	= i;
	motor[mFlagRaise1] 	= i;
	motor[mFlagRaise2] 	= i;
}

/////////////////////////////////////////////////////////
//
//	Set all drive motors to the input parameter value
//
/////////////////////////////////////////////////////////
void driveMotorsTo(int i){
	motor[mDriveLeft] 	= i;
	motor[mDriveRight] 	= i;
}

////////////////////////////////////////////////////////////////
//
//	Convert a distance in inches to a distance in encoder ticks
//
////////////////////////////////////////////////////////////////
long inchesToTicks(float inches){
	return (long) inches * (1350 / (4 * PI));
}

/////////////////////////////////////////////////////////////////////
//
//	Convert a distance in encoder ticks to a distance in inches
//
/////////////////////////////////////////////////////////////////////
float ticksToInches(long ticks){
	return (float) ticks / (1350 / (4 * PI));
}

////////////////////////////////////////////////////////////////////
//
//	Move the robot a distance in encoder ticks
//
////////////////////////////////////////////////////////////////////
void goTicks(long ticks, int speed){
	// Calculate the target encoder value (current value + desired distance)
	long target = nMotorEncoder[mDriveRight] + ticks;

	// Print a message to the debug stream, saying how far we are going, and the target enccoder value
	writeDebugStreamLine("-- MOVING TICKS --\ntarget: %5.2f, current:%5.2f (%d inches) (speed: %d)",
		target, nMotorEncoder[mDriveRight], ticksToInches(ticks), speed);

	// Create a variable to store the motor ratio
	float rightMotorRatio;

	// If we're going forwards, set the motor ratio to the forward motor ratio
	// If we're going backwards, set the motor ratio to the backward ratio
	// This is to ensure that the two sets of wheels turn at the same speed
	if(speed < 0)
		rightMotorRatio = (float) forwardMotorRatio / 100;
	else
		rightMotorRatio = (float) backwardMotorRatio / 100;
	writeDebugStreamLine("Left motor set to %d, right motor set to %d", speed, rightMotorRatio * speed);

	// If the distance is positive (we're moving forwards)
	// While the current encoder value is less than the target, run the motors
	if(ticks > 0){
		while(nMotorEncoder[mDriveRight] < target){
			motor[mDriveRight] = (int) speed * rightMotorRatio;
			motor[mDriveLeft] = speed;
		}
	}

	// If the distance is negative (we're going backwards)
	// While the current encoder value is greater than the target, run the motors
	else{
		while(nMotorEncoder[mDriveRight] > target){
			motor[mDriveRight] = (float) -1 * (speed * rightMotorRatio);
			motor[mDriveLeft] = -1 * speed;
		}
	}

	// Stop all the motors
	allMotorsTo(0);
	writeDebugStreamLine("final:  %5.2f", nMotorEncoder[mDriveRight]);
}

///////////////////////////////////////////////////////////////
//
//	Turn the robot a distance in degrees
//
///////////////////////////////////////////////////////////////
void turnDegrees(float degreesToTurn, int turnStrength){

	// Declare local variables:
	// degreesSoFar: The number of degrees turned since we started the function
	// initialTurnReading: The current reading from the gyroscope
	float degreesSoFar = 0;
	int initialTurnReading = HTGYROreadRot(sGyro);

	// Print a message to the debug stream containing the target angle and the current reading
	writeDebugStreamLine("-- TURNING --\ninitial reading: %d\nTarget angle: %2.2f",
		initialTurnReading,
		degreesToTurn);

		// If the degrees to turn is positive, spin the robot counterclockwise
	if (degreesToTurn > 0){
		motor[mDriveLeft] = -1 * turnStrength;
		motor[mDriveRight] = turnStrength;
		writeDebugStreamLine("Decided to turn counterclockwise");
	}

	// If the degrees to turn is negative, spin the robot clockwise
	else{
		motor[mDriveLeft] = turnStrength;
		motor[mDriveRight] = -1 * turnStrength;
		writeDebugStreamLine("Decided to turn clockwise");
	}

	// While the absolute value of the degrees so far is less than the absolute value of the degrees to turn,
	// Calculate the current degree value
	while (abs(degreesSoFar) < abs(degreesToTurn)){
		// Allow some time to pass
		wait10Msec(1);

		// Set the current reading to the sensor value minus the initial reading
		int currentGyroReading = HTGYROreadRot(sGyro) - initialTurnReading;
		nxtDisplayTextLine(7, "degreesSoFar: %d", degreesSoFar);

		// Add the degrees turned this iteration to the total degrees turned
		degreesSoFar = degreesSoFar + (currentGyroReading * 0.01);

	}

	// Stop the motors and print a message with the final degree measure
	driveMotorsTo(0);
	writeDebugStreamLine("final angle: %2.2f", degreesSoFar);
}

//////////////////////////////////////////////////////////////////////
//
//	Show infomation on the NXT screen
//
//////////////////////////////////////////////////////////////////////
task showDebugInfo(){
	while(true){
		nxtDisplayTextLine(0, "mtrL:%d", motor[mDriveLeft]);		// Left motor encoder
		nxtDisplayTextLine(1, "mtrR:%d", motor[mDriveRight]);		// Right motor encoder
		nxtDisplayTextLine(2, "LiL:%d", rawLightLeft);					// Left light sensor
		nxtDisplayTextLine(3, "LiR:%d", rawLightRight);					// Right light sensor
		nxtDisplayTextLine(5, "irRL:%d,%d",											// IR seekers
			irStrengthLeft,
			irStrengthRight);
		nxtDisplayTextLine(6, "HighestIR:%d", irStrengthRight);	// Maximum IR signal
	}
}

// Variables for the brick flipper:
// blockDropLeftStart/blockDropRightStart: "Starting" servo positions
// blockDropLeftIdle/blockDropRightIdle: "Out of the way" servo positions
// blockDropLeftDrop/blockDropRightDrop: "Dropping" servo positions
// conveyorTightStart: Servo value for the conveyor tension servo
const short blockDropLeftStart = 0;
const short blockDropRightStart = 245;
const short blockDropLeftIdle = 128;
const short blockDropRightIdle = 128;
const short blockDropLeftDrop = 180;
const short blockDropRightDrop = 32;
const short conveyorTightStart = 150;

///////////////////////////////////////////////////////////////////////
//
//	Put the flippers in the "Dropping" position
//
///////////////////////////////////////////////////////////////////////
void blockDrop(){
	servo[rBlockDropLeft] = blockDropLeftDrop;
	servo[rBlockDropRight] = blockDropRightDrop;
}

////////////////////////////////////////////////////////////////////////
//
//	Put the flippers in the "Starting" position
//
///////////////////////////////////////////////////////////////////////
void blockRetract(){
	servo[rBlockDropLeft] = blockDropLeftStart;
	servo[rBlockDropRight] = blockDropRightStart;
}

///////////////////////////////////////////////////////////////////////
//
//	Put the flippers in the "Out of the way" position
//
///////////////////////////////////////////////////////////////////////
void blockIdle(){
	servo[rBlockDropLeft] = blockDropLeftIdle;
	servo[rBlockDropRight] = blockDropRightIdle;
}

////////////////////////////////////////////////////////////////////////
//
//	Put the block into the basket
//	"It puts the block into the basket, or it gets the hose again."
//
///////////////////////////////////////////////////////////////////////
void placeBlock(int basketPos){
	writeDebugStreamLine("-- PLACING BLOCK --");

	// Adjust the robot position based on which basket we're at
	if(basketPos == 0 || basketPos == 1){
		goTicks(inchesToTicks(8), 25);
		writeDebugStreamLine("Moved up 8 inches, first or second basket");
	}
	if(basketPos == 2 || basketPos == 3){
		goTicks(inchesToTicks(6), 25);
		writeDebugStreamLine("Moved up 6 inches, third or fourth basket");
	}

	// Extend the servos, give them time to move, and then retract them
	blockDrop();
	wait10Msec(100);
	blockRetract();
}

////////////////////////////////////////////////////////////////////////
//
//	Calibrate the light sensors for the mats
//
////////////////////////////////////////////////////////////////////////
void calibrateLightSensors(){
	// If no signal is detected from the light sensors,
	// Print an error message and default the threshold to 450
	if(rawLightLeft == 0 || rawLightRight == 0){
		lightThreshold = 450;
		writeDebugStreamLine("No light signal detected. Threshold defaulted to %d", lightThreshold);
		PlaySound(soundDownwardTones);
		return;
	}

	// Average the right and left sensor values
	int matReading = (int) (rawLightLeft + rawLightRight)	/ 2;

	// Set the threshold to 75 above the average value, and print the threshold to the debug stream
	lightThreshold = matReading + 75;
	writeDebugStreamLine("Light level of mat: %d\nSet threshold to %d",
	matReading, lightThreshold);
}

/////////////////////////////////////////////////////////////////////////////////
//
//	Find the white line, and use it to align the robot
//
/////////////////////////////////////////////////////////////////////////////////
void findWhiteLine(){
	writeDebugStream("-- FINDING WHITE LINE --\n");

	// Declare variables to store whether the line has been found on the right and left
	bool foundLineLeft = false;
	bool foundLineRight = false;

	// Calibrate the light sensors
	calibrateLightSensors();

	// Set the drive motors to 25 (too high, and we'll drive right past the line)
	motor[mDriveLeft] = 25;
	motor[mDriveRight] = 25;

	// While both sides have not found the line, check for the line
	while(!foundLineLeft || !foundLineRight){

		// If the sensor value is above the threshold and the line has not already been found,
		// Set the foundLine flag to true,
		// Stop the motor on that side
		if(rawLightLeft > lightThreshold && !foundLineLeft){
			foundLineLeft = true;
			writeDebugStreamLine("Found white line on the left. Detected value: %d Threshold value: %d",
			rawLightLeft, lightThreshold);
			motor[mDriveLeft] = 0;
		}
		if(rawLightRight > lightThreshold && !foundLineRight){
			foundLineRight = true;
			writeDebugStreamLine("Found white line on the right. Detected value: %d Threshold value: %d",
			rawLightRight, lightThreshold);
			motor[mDriveRight] = 0;
		}
	}

	// Set both drive motors to 0 (just in case)
	motor[mDriveLeft] = 0;
	motor[mDriveRight] = 0;
}

/////////////////////////////////////////////////////////////////
//
//	Initialize the robot's motors, servos, and sensors
//
////////////////////////////////////////////////////////////////
void initializeRobot(){
	writeDebugStreamLine("\n\n -- NEW INSTANCE -- \n\n");
	// Lower the sucker plate
	while(SensorValue[sLiftStop] != 1)
		motor[mBsAngle] = -25;
	allMotorsTo(0);
	calibrateLightSensors();
	nMotorEncoder[mDriveLeft] = 0;
	blockRetract();
	servo[rConveyorTight] = conveyorTightStart;
	// Print out the current battery level. If it is below a certain value, send a warning
	writeDebugStreamLine("extBatt lvl: %2.2f volts\nNXT Batt lvl: %2.2f", externalBatteryAvg / 1000.0, nAvgBatteryLevel / 1000.0);
	if(externalBatteryAvg < 13000){
		PlaySound(soundException);
		writeDebugStreamLine("////////////////////\n//\n// MAIN BATTERY LOW\n// Avg Batt Level: %2.2f \n//\n////////////////////", externalBatteryAvg / 1000.0);
	}
	if(nAvgBatteryLevel < 7500){
		PlaySound(soundException);
		writeDebugStreamLine("///////////////////\n//\n// NXT BATTERY LOW\n// Avg Batt Level: %2.2f\n//\n///////////////////////", nAvgBatteryLevel / 1000.0);
	}
}

///////////////////////////////////////////////////////////////////////////////
//
//	Find the IR beacond and place the block there
//
//////////////////////////////////////////////////////////////////////////////
void findIrIncremental(){
	// If the IR seeker signal is over 600 (meaning there is a problem with the multiplexer),
	// Play a warning sound and print a message,
	// Place the block in the nearmost basket
	if(irStrengthLeft > 600 || irStrengthRight > 600){
		writeDebugStreamLine("////////////////////\n//\n// MULTIPLEXER BATTERY DEAD\n//\n////////////////////");
		PlaySound(soundDownwardTones);
		goTicks(inchesToTicks(10), 25);
		placeBlock(0);
		return;
	}

	// Set the gettingIr flag to true, turn on the IR seekers
	gettingIr = true;

	// Store the distance from the starting position to each basket
	long blockDistancesCumulative[3] = {startEncoderPos + inchesToTicks(20),
		startEncoderPos + inchesToTicks(43),
		startEncoderPos + inchesToTicks(53)};

	// Move up to the first basket, and play a sound
	goTicks(inchesToTicks(10), 100);
	writeDebugStreamLine("At first basket, ready to start.\n");
	PlaySound(soundBeepBeep);

	// Loop through four times
	for(int i = 0; i <= 3; i++){

		// If the signal to either IR seeker is above the threshold, place the block
		if(irStrengthLeft > irThresh || irStrengthRight > irThresh){

			writeDebugStreamLine("Block placed in basket number %d. Detected value: %d Threshold value: %d",
			i+1, (irStrengthLeft > irStrengthRight)? irStrengthLeft : irStrengthRight, irThresh);
			placeBlock(i);
			break;
		}

		// If both signals are below the threshold, go to the next basket
		else{
			// If the robot is not at the final basket, move to the next basket
			if(i != 3){
				goTicks(blockDistancesCumulative[i] -
				nMotorEncoder[mDriveRight], 50);
				writeDebugStreamLine("Going to next basket");
			}
			// If the robot is at the final basket, place the block
			// There was most likely a problem with the IR seeker
			else{
				placeBlock(i);
				PlaySound(soundDownwardTones);
				writeDebugStreamLine("Didn't find IR. Placed in last basket.");
			}
		}

		// Print out the current basket, the current IR value, and the IR threshold
		writeDebugStreamLine("\n-- BASKET #%d --\nCurrent Value: %d. Need %d to stop.",
		i+1, (irStrengthLeft > irStrengthRight)? irStrengthLeft:irStrengthRight, irThresh);
	}

	// Turn off the IR seeker
	gettingIr = false;
}
