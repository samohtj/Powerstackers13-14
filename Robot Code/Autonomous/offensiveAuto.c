#pragma config(Hubs,  S1, HTMotor,  HTMotor,  HTServo,  none)
#pragma config(Sensor, S1,     ,               sensorI2CMuxController)
#pragma config(Sensor, S2,     SMUX,           sensorI2CCustom)
#pragma config(Sensor, S3,     sGyro,          sensorI2CHiTechnicGyro)
#pragma config(Sensor, S4,     sLiftStop,      sensorTouch)
#pragma config(Motor,  motorA,          mFlagRaise1,   tmotorNXT, openLoop)
#pragma config(Motor,  motorB,          mFlagRaise2,   tmotorNXT, openLoop)
#pragma config(Motor,  motorC,          mBlockStop,    tmotorNXT, openLoop, reversed)
#pragma config(Motor,  mtr_S1_C1_1,     mDriveLeft,    tmotorTetrix, openLoop, encoder)
#pragma config(Motor,  mtr_S1_C1_2,     mDriveRight,   tmotorTetrix, openLoop, reversed, encoder)
#pragma config(Motor,  mtr_S1_C2_1,     mBsConveyor,   tmotorTetrix, openLoop, reversed)
#pragma config(Motor,  mtr_S1_C2_2,     mBsAngle,      tmotorTetrix, openLoop, reversed, encoder)
#pragma config(Servo,  srvo_S1_C3_1,    rConveyorTight,       tServoStandard)
#pragma config(Servo,  srvo_S1_C3_2,    rBlockDropLeft,       tServoStandard)
#pragma config(Servo,  srvo_S1_C3_3,    rBlockDropRight,      tServoStandard)
#pragma config(Servo,  srvo_S1_C3_4,    servo4,               tServoNone)
#pragma config(Servo,  srvo_S1_C3_5,    servo5,               tServoNone)
#pragma config(Servo,  srvo_S1_C3_6,    servo6,               tServoNone)
//*!!Code automatically generated by 'ROBOTC' configuration wizard               !!*//

		// 1 wheel rot = 1350 ticks
#include "JoystickDriver.c"
#include "autonomous-includes/autonomousTasks.h"
#include "autonomous-includes/autoMenu.h"
#include "autonomous-includes/powerStackerSplash.h"

task main(){

	// Run the strategy selection menu
	StartTask(runMenuOffensive);

	// Initialize the robot's motors and servos
	initializeRobot();

	// Wait for the start of the match
	waitForStart();

	// Stop the menu, and print the results
	StopTask(runMenuOffensive);
	printMenuChoices();

	// Start tasks
	StartTask(showDebugInfo);
	StartTask(getSmux);
	startEncoderPos = nMotorEncoder[mDriveRight];

	// If the delay time is greater than zero:
	if(delay > 0){
		writeDebugStreamLine("Wait detected");
		// Wait the amount of time given by the delay
		wait10Msec(delay * 100);
		writeDebugStreamLine("Wait over");
	}

	// If the user has chosen to place the IR block:
	//int basketPosition = 0;
	if(doIr){
		// Find the IR basket
		findIrIncremental();

		// If we are going to go around the far end of the ramp
		if(goAround){
			writeDebugStreamLine("Going to far end of ramp");

			// Figure out how far we have to go to get to the far end of the ramp, and go that distance
			// (Starting position + 65 inches)
			long farEncoderPos = startEncoderPos + inchesToTicks(72);
			goTicks(farEncoderPos - nMotorEncoder[mDriveRight], 100);
		}

		// If we are going to go aroudn the near end of the ramp:
		else{
			writeDebugStreamLine("Going to near end of ramp");

			// Figure out how far back we have to go to get to the near end of the ramp, and go that distance
			// (Starting positon + 3 inches)
			long nearEncoderPos = startEncoderPos + inchesToTicks(3);
			goTicks(-1 * (nMotorEncoder[mDriveRight] - nearEncoderPos), 100);
		}

		// If the robobt started on the near side:
		// (Ramp to the robot's right)
		if(startNear){
			// Turn clockwise
			turnDegrees(-90, turnSpeed);

			// If we are going up the other alliance's half of the ramp:
			if(rampOtherSide)
				// Skip over the first white line
				goTicks(inchesToTicks(36), 100);

			// Locate the white line, and stop on it
			findWhiteLine();

			// Turn towards the ramp
			turnDegrees(90, turnSpeed);
		}

		// If the robot started on the far side:
		// (Ramp to the robot's left)
		else{
			// Turn counterclockwise
			turnDegrees(90, turnSpeed);

			// If we are going up the other alliance's half of the ramp:
			if(rampOtherSide)
				// Skip over the first white line
				goTicks(inchesToTicks(36), 100);

			// Locate the white line, and stop on it
			findWhiteLine();

			// Turn towards the ramp
			turnDegrees(-90, turnSpeed);
		}

		// Go up the ramp
		goTicks(inchesToTicks(36), 100);
	}

	// If the user has chosen not to place the IR block:
	else
		writeDebugStreamLine("Did not do IR. You really should have.");
}
