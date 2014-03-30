#pragma config(Hubs,  S1, HTMotor,  HTMotor,  HTServo,  none)
#pragma config(Sensor, S2,     SMUX,           sensorI2CCustom)
#pragma config(Sensor, S3,     sGyro,          sensorI2CCustom)
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

#include "autonomousTasks.h"
#include "autoMenu.h"

task main()
{
	bDisplayDiagnostics = false;

	StartTask(runMenuOffensive);
	StartTask(getSmux);
	wait10Msec(100);
	initializeRobot();
	waitForStart();
	StopTask(runMenuOffensive);
	StartTask(showDebugInfo);
	eraseDisplay();

	goTicks(inchesToTicks(36), 75);
	wait10Msec(100);
	goTicks(-1 * inchesToTicks(36), 75);


}
