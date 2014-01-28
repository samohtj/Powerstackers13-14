#pragma config(Hubs,  S1, HTMotor,  HTMotor,  none,     none)
#pragma config(Sensor, S1,     ,               sensorI2CMuxController)
#pragma config(Sensor, S3,     IRS_L,          sensorI2CCustom)
#pragma config(Sensor, S4,     IRS_R,          sensorI2CCustom)
#pragma config(Motor,  motorA,           ,             tmotorNXT, openLoop)
#pragma config(Motor,  motorB,           ,             tmotorNXT, openLoop)
#pragma config(Motor,  motorC,          mBlockStop,    tmotorNXT, openLoop, reversed)
#pragma config(Motor,  mtr_S1_C1_1,     mDriveLeft,    tmotorTetrix, openLoop, encoder)
#pragma config(Motor,  mtr_S1_C1_2,     mDriveRight,   tmotorTetrix, openLoop, reversed, encoder)
#pragma config(Motor,  mtr_S1_C2_1,     mBsConveyor,   tmotorTetrix, openLoop, reversed)
#pragma config(Motor,  mtr_S1_C2_2,     mBsAngle,      tmotorTetrix, openLoop, reversed, encoder)
//*!!Code automatically generated by 'ROBOTC' configuration wizard               !!*//

#include "JoystickDriver.c"
#include "autonomous-includes/autonomous_tasks.c"

//short autoMode = getAutoMode();
short autoMode = 3;

task main(){
	// do some housekeeping work. print that the program is running, clear the screen and debug stream
	bNxtLCDStatusDisplay = false;
	eraseDisplay();
	nxtDisplayBigTextLine(0, "Auto");
	nxtDisplayBigTextLine(2, "Mode");
	nxtDisplayBigTextLine(4, "Running");
	nxtDisplayTextLine(6, "Running task:");
	clearDebugStream();
	ClearTimer(T1);

	waitForStart();

	StartTask(getSmux); // run and update the sensor multiplexer
	StartTask(showDebugInfo);

	if (autoMode == 1){
		StartTask(findIr); // go until you find the ir beacon
		while (foundIr == false){
			nxtDisplayTextLine(7, "findIr");
		}
		StopTask(findIr);
		nxtDisplayTextLine(7, "None");
		clearDebugStream();
		writeDebugStreamLine("IR beacon found."); // found the ir beacon, go to next part

		writeDebugStreamLine("Turning..."); // start to place the block
		blockTurnDirection = CLOCKWISE;
		StartTask(placeBlock);
		while (placedBlock == false){
			nxtDisplayTextLine(7, "placeBlock");
		}
		StopTask(placeBlock);
		nxtDisplayTextLine(7, "None");
		clearDebugStream();
		writeDebugStreamLine("Block placed."); // placed the block in the basket


		writeDebugStreamLine("Done. Stopping...");
		wait10Msec(300);
	}

	if(autoMode == 3){
		StartTask(placeBlock);
		while(!placedBlock){
		}
		StopTask(placeBlock);
	}

	if(autoMode == 5){
		StartTask(findWhiteLine);
		while(!foundWhiteLine){

		}
	}
	long rotations = 2;
	while(nMotorEncoder[mDriveLeft] < (rotations * 4000)){
		driveMotorsTo(100);
	}
	driveMotorsTo(0);

}
