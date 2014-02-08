#pragma config(Hubs,  S1, HTMotor,  HTMotor,  none,     none)
#pragma config(Sensor, S2,     SMUX,           sensorI2CCustom)
#pragma config(Sensor, S3,     sGyro,          sensorI2CHiTechnicGyro)
#pragma config(Sensor, S4,     TMUX,           sensorAnalogInactive)
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

// 1 wheel rotation = 1350 ticks

task main()
{
	eraseDisplay();
	clearDebugStream();
	nxtDisplayCenteredBigTextLine(0, "AUTO");
	nxtDisplayCenteredBigTextLine(2, "READY");
	nMotorEncoder[mDriveLeft] = 0;
	nMotorEncoder[mDriveRight] = 0;
	nMotorEncoder[mBsAngle] = 0;
	//waitForStart();
	nxtDisplayCenteredBigTextLine(2, "RUNNING");
	StartTask(getSmux);
	StartTask(showDebugInfo);

	suckerToDropPosition();
	goTicks(2125, 50);
	spitBlock();
	goTicks(-1000, 50);
	turnDegrees(60, 75);
	goTicks(2125, 100);
	turnDegrees(-45, 75);
	goTicks(4000, 100);

}
