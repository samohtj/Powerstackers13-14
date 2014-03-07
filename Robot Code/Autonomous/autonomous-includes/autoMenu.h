/**
*	Declare constants to identify the buttons
*/
TButtons NEXT_BUTTON = kRightButton;
TButtons PREV_BUTTON = kLeftButton;
TButtons DOWN_BUTTON = kEnterButton;

void switchBool(bool* in, TButtons activeButton){
	if(activeButton == NEXT_BUTTON || activeButton == PREV_BUTTON)
		*in = !*in;
}

void switchInt(int* in, TButtons activeButton){
	if(activeButton == NEXT_BUTTON)
		*in = *in + 1;
	if(activeButton == PREV_BUTTON)
		*in = *in - 1;
}

/* OFFENSIVE OPTIONS */
bool startNear = false;
bool doIr = false;
bool goAround = false;
bool rampOtherSide = false;

/* DEFENSIVE OPTIONS */
bool blockRamp = false;

int delay = 0;

task runMenuOffensive()
{
	bDisplayDiagnostics = false;
	void* currVar;
	char currType;

	currVar = &startNear;
	currType = 'b';

	while (true){
		if(delay < 0)
			delay = 0;
		else if(delay > 10)
			delay = 10;

		nxtDisplayString(0, "Near:     %s", startNear ? "yes":"no ");
		nxtDisplayString(1, "Do Ir:    %s", doIr ? "yes":"no ");
		nxtDisplayString(2, "Go Around:%s", goAround ? "yes":"no ");
		nxtDisplayString(3, "RmpOthrSd:%s", rampOtherSide ? "yes":"no ");
		nxtDisplayString(4, "Delay:    %2d", delay);

		if(currVar == &startNear){
			nxtDisplayStringAt(94, 63, "]");
			nxtDisplayStringAt(94, 55, " ");
			nxtDisplayStringAt(94, 47, " ");
			nxtDisplayStringAt(94, 39, " ");
			nxtDisplayStringAt(94, 31, " ");
		}else if(currVar == &doIr){
			nxtDisplayStringAt(94, 63, " ");
			nxtDisplayStringAt(94, 55, "]");
			nxtDisplayStringAt(94, 47, " ");
			nxtDisplayStringAt(94, 39, " ");
			nxtDisplayStringAt(94, 31, " ");
		}else if(currVar == &goAround){
			nxtDisplayStringAt(94, 63, " ");
			nxtDisplayStringAt(94, 55, " ");
			nxtDisplayStringAt(94, 47, "]");
			nxtDisplayStringAt(94, 39, " ");
			nxtDisplayStringAt(94, 31, " ");
		}else if(currVar == &rampOtherSide){
			nxtDisplayStringAt(94, 63, " ");
			nxtDisplayStringAt(94, 55, " ");
			nxtDisplayStringAt(94, 47, " ");
			nxtDisplayStringAt(94, 39, "]");
			nxtDisplayStringAt(94, 31, " ");
		}else if(currVar == &delay){
			nxtDisplayStringAt(94, 63, " ");
			nxtDisplayStringAt(94, 55, " ");
			nxtDisplayStringAt(94, 47, " ");
			nxtDisplayStringAt(94, 39, " ");
			nxtDisplayStringAt(94, 31, "]");
		}

		if(nNxtButtonPressed == NEXT_BUTTON || nNxtButtonPressed == PREV_BUTTON){
			if(currType == 'b')
				switchBool(currVar, nNxtButtonPressed);
			else if(currType == 'i')
				switchInt(currVar, nNxtButtonPressed);

			ClearTimer(T1);
			while(nNxtButtonPressed != kNoButton && time1[T1] <= 400){

			}
		}

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
				currVar = &startNear;
				currType = 'b';
			}

			ClearTimer(T1);
			while(nNxtButtonPressed != kNoButton && time1[T1] <= 400){
				writeDebugStreamLine("hi");
			}
		}
	}


}
