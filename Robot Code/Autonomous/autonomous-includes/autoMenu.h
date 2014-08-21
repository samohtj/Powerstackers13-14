/////////////////////////////////////////////////////////////
//	AUTONOMOUS PROGRAM STRATEGY CHOOSER
//	ORIGINAL CODE BY FTC TEAM# 3785, THE BESTIE BOTS
//	https://github.com/hprobotics/ftcresources/blob/master/AutonomousChooser/menu_helper.h
//	MODIFIED BY FTC TEAM# 5029, THE POWERSTACKERS
////////////////////////////////////////////////////////////
#include "JoystickDriver.c"

// Create constants to make it easier to use the buttons
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

// Game option variables (offensive play style):
// startNear: Starting with the ramp to the robot's right or its left
// doIr: Place the IR block or not
// goAround: After placing the IR block, go around the far side of the ramp, or the near side
// rampOtherSide: Go up our alliance's half of the ramp, or the other alliance's half
bool startNear = true;
bool doIr = true;
bool goAround = false;
bool rampOtherSide = false;

// Game option variables (general):
// delay: Amount of time to wait before starting the run (in seconds)
// maxDelay: The maximum time you can wait
// forwardMotorRatio: The ratio of the right wheel to the left wheel speed, going forward
// backwardMotorRatio: The ratio of the right wheel to the left wheel speed, going backward
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
	// Turn off the diagnostic display, and clear the screen
	bDisplayDiagnostics = false;
	eraseDisplay();

	// Declare variables to store the currently selected variable,
	// And the data type of the currently selected variable
	void* currVar;
	char currType;

	// Initialize the current variable to startNear,
	// And initialize the data type to "boolean"
	currVar = &startNear;
	currType = 'b';

	// Run this code until the task is ended
	while (true){

		// If the delay is below zero, set it to zero
		if(delay < 0)
			delay = 0;

		// If the delay is above the maximum delay, set it to the maximum delay
		else if(delay > maxDelay)
			delay = maxDelay;

		// Print all the variable names and their current values to the screen
		nxtDisplayString(0, "Near:     %s", startNear ? "yes":"no ");
		nxtDisplayString(1, "Do Ir:    %s", doIr ? "yes":"no ");
		nxtDisplayString(2, "Go Around:%s", goAround ? "yes":"no ");
		nxtDisplayString(3, "RmpOthrSd:%s", rampOtherSide ? "yes":"no ");
		nxtDisplayString(4, "Delay:    %2d", delay);
		nxtDisplayString(5, "FrwrdRatio:%d ", forwardMotorRatio);
		nxtDisplayString(6, "BkwrdRatio:%d ", backwardMotorRatio);

		// Print a selection icon next to the active variable name
		if(currVar == &startNear){
			nxtDisplayStringAt(94, 63, "]");
			nxtDisplayStringAt(94, 17, " ");
		}else if(currVar == &doIr){
			nxtDisplayStringAt(94, 63, " ");
			nxtDisplayStringAt(94, 55, "]");
		}else if(currVar == &goAround){
			nxtDisplayStringAt(94, 55, " ");
			nxtDisplayStringAt(94, 47, "]");
		}else if(currVar == &rampOtherSide){
			nxtDisplayStringAt(94, 47, " ");
			nxtDisplayStringAt(94, 39, "]");
		}else if(currVar == &delay){
			nxtDisplayStringAt(94, 39, " ");
			nxtDisplayStringAt(94, 31, "]");
		}else if(currVar == &forwardMotorRatio){
			nxtDisplayStringAt(94, 31, " ");
			nxtDisplayStringAt(94, 24, "]");
		}else if(currVar == &backwardMotorRatio){
			nxtDisplayStringAt(94, 24, " ");
			nxtDisplayStringAt(94, 17, "]");
		}

		// If the right or left arrow button is pressed on the NXT,
		// Perform the appropriate action for the data type of the selected variable
		if(nNxtButtonPressed == NEXT_BUTTON ||
			nNxtButtonPressed == PREV_BUTTON){
			if(currType == 'b')
				switchBool(currVar, nNxtButtonPressed);
			else if(currType == 'i')
				switchInt(currVar, nNxtButtonPressed);
			else if(currType == 'l')
				switchIntByFive(currVar, nNxtButtonPressed);

			// Play a short sound
			PlaySound(soundBlip);

			// Clear the timer. While the timer reads less than four seconds
			// And any button is pressed, do nothing
			ClearTimer(T1);
			while(nNxtButtonPressed != kNoButton && time1[T1] <= 400){}
		}

		// If the orange button is pressed on the NXT,
		// Switch the active variable to the next variable in the list
		if(nNxtButtonPressed == DOWN_BUTTON){
			if(currVar == &startNear){
				currVar = &doIr;
				currType = 'b';
			}else if(currVar == &doIr){
				currVar = &goAround;
				currType = 'b';
			}else if(currVar == &goAround){
				currVar = &rampOtherSide;
				currType = 'b';
			}else if(currVar == &rampOtherSide){
				currVar = &delay;
				currType = 'i';
			}else if(currVar == &delay){
				currVar = &forwardMotorRatio;
				currType = 'l';
			}else if(currVar == &forwardMotorRatio){
				currVar = &backwardMotorRatio;
				currType = 'l';
			}else if(currVar == &backwardMotorRatio){
				currVar = &startNear;
				currType = 'b';
			}

			// Play a short sound
			PlaySound(soundBlip);

			// Clear the timer. While the timer reads less than four seconds
			// And any button is pressed, do nothing
			ClearTimer(T1);
			while(nNxtButtonPressed != kNoButton && time1[T1] <= 400){}
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
