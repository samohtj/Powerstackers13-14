/////////////////////////////////////////////////////////////
//	AUTONOMOUS PROGRAM STRATEGY CHOOSER
//	ORIGINAL CODE BY FTC TEAM# 3785, THE BEASTIE BOTS
//	https://github.com/hprobotics/ftcresources/blob/master/AutonomousChooser/menu_helper.h
//	MODIFIED BY FTC TEAM# 5029, THE POWERSTACKERS
////////////////////////////////////////////////////////////
#include "JoystickDriver.c"

TButtons NEXT_BUTTON = kRightButton;
TButtons PREV_BUTTON = kLeftButton;
TButtons DOWN_BUTTON = kEnterButton;

///////////////////////////////////////////////////////////
//
//	Switch a boolean to the opposite value
//
///////////////////////////////////////////////////////////
void switchBool(bool* in, TButtons activeButton){
	if(activeButton == NEXT_BUTTON || activeButton == PREV_BUTTON)
		*in = !*in;
}

//////////////////////////////////////////////////////////
//
//	Increment or decrement an integer by 1
//
/////////////////////////////////////////////////////////
void switchInt(int* in, TButtons activeButton){
	if(activeButton == NEXT_BUTTON)
		*in = *in + 1;
	if(activeButton == PREV_BUTTON)
		*in = *in - 1;
}

/////////////////////////////////////////////////////////
//
//	Increment or decrement an integer by 5
//
////////////////////////////////////////////////////////
void switchIntByFive(int* in, TButtons activeButton){
	if(activeButton == NEXT_BUTTON)
		*in = *in + 5;
	if(activeButton == PREV_BUTTON)
		*in = *in - 5;
}

////////////////////////////////////////////////////////////
//
//	Increment or decrement a floating point number by 0.1
//
////////////////////////////////////////////////////////////
void switchFloat(float* in, TButtons activeButton){
	if(activeButton == NEXT_BUTTON)
		*in = *in + 0.1;
	if(activeButton == PREV_BUTTON)
		*in = *in - 0.1;
}

// Game option variables (offensive play style)
bool startNear = true;
bool doIr = true;
bool goAround = false;
bool rampOtherSide = false;

// Game option variables (general)
int delay = 0;
const int maxDelay = 15;
int forwardMotorRatio = 90;
int backwardMotorRatio = 90;

/////////////////////////////////////////////////////////////
//
//	Run the menu
//
/////////////////////////////////////////////////////////////
task runMenuOffensive()
{
	bDisplayDiagnostics = false;
	eraseDisplay();

	void* currVar;
	char currType;

	currVar = &startNear;
	currType = 'b';

	while (true) {

		if(delay < 0) {
			delay = 0;
		} else if(delay > maxDelay) {
			delay = maxDelay;
		}

		nxtDisplayString(0, "Near:     %s", startNear ? "yes":"no ");
		nxtDisplayString(1, "Do Ir:    %s", doIr ? "yes":"no ");
		nxtDisplayString(2, "Go Around:%s", goAround ? "yes":"no ");
		nxtDisplayString(3, "RmpOthrSd:%s", rampOtherSide ? "yes":"no ");
		nxtDisplayString(4, "Delay:    %2d", delay);
		nxtDisplayString(5, "FrwrdRatio:%d ", forwardMotorRatio);
		nxtDisplayString(6, "BkwrdRatio:%d ", backwardMotorRatio);

		// Print a selection icon next to the active variable name
		if(currVar == &startNear) {
			nxtDisplayStringAt(94, 63, "]");
			nxtDisplayStringAt(94, 17, " ");
		} else if(currVar == &doIr) {
			nxtDisplayStringAt(94, 63, " ");
			nxtDisplayStringAt(94, 55, "]");
		} else if(currVar == &goAround) {
			nxtDisplayStringAt(94, 55, " ");
			nxtDisplayStringAt(94, 47, "]");
		} else if(currVar == &rampOtherSide) {
			nxtDisplayStringAt(94, 47, " ");
			nxtDisplayStringAt(94, 39, "]");
		} else if(currVar == &delay) {
			nxtDisplayStringAt(94, 39, " ");
			nxtDisplayStringAt(94, 31, "]");
		} else if(currVar == &forwardMotorRatio) {
			nxtDisplayStringAt(94, 31, " ");
			nxtDisplayStringAt(94, 24, "]");
		} else if(currVar == &backwardMotorRatio) {
			nxtDisplayStringAt(94, 24, " ");
			nxtDisplayStringAt(94, 17, "]");
		}

		// If the right or left arrow button is pressed on the NXT,
		// Perform the appropriate action for the data type of the selected variable
		if(nNxtButtonPressed == NEXT_BUTTON ||
			nNxtButtonPressed == PREV_BUTTON) {
			if(currType == 'b') {
				switchBool(currVar, nNxtButtonPressed);
			} else if(currType == 'i') {
				switchInt(currVar, nNxtButtonPressed);
			} else if(currType == 'l') {
				switchIntByFive(currVar, nNxtButtonPressed);
			}

			PlaySound(soundBlip);
			ClearTimer(T1);
			while(nNxtButtonPressed != kNoButton && time1[T1] <= 400) {}
		}

		if(nNxtButtonPressed == DOWN_BUTTON) {
			if(currVar == &startNear) {
				currVar = &doIr;
				currType = 'b';
			} else if(currVar == &doIr) {
				currVar = &goAround;
				currType = 'b';
			} else if(currVar == &goAround) {
				currVar = &rampOtherSide;
				currType = 'b';
			} else if(currVar == &rampOtherSide) {
				currVar = &delay;
				currType = 'i';
			} else if(currVar == &delay) {
				currVar = &forwardMotorRatio;
				currType = 'l';
			} else if(currVar == &forwardMotorRatio) {
				currVar = &backwardMotorRatio;
				currType = 'l';
			} else if(currVar == &backwardMotorRatio) {
				currVar = &startNear;
				currType = 'b';
			}

			PlaySound(soundBlip);
			ClearTimer(T1);
			while(nNxtButtonPressed != kNoButton && time1[T1] <= 400) {}
		}
	}
}

///////////////////////////////////////////////////////////////////////
//
//	Print all the chosen options to the debug stream
//
//////////////////////////////////////////////////////////////////////
void printMenuChoices(){
	writeDebugStreamLine("Start on near side: %s\nFind IR basket: %s\nGo around far end of ramp: %s\nGo to the other half of the ramp: %s\nDelay: %d seconds",
		(startNear)? "Yes":"No", (doIr)? "Yes":"No", (goAround)? "Yes":"No", (rampOtherSide)? "Yes":"No", delay);
}
