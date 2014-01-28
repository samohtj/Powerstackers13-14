#include "hitechnic-sensormux.h"
#include "lego-light.h"
#include "hitechnic-irseeker-v2.h"
#include "hitechnic-touchmux.h"

const tMUXSensor irLeft 	= msensor_S2_1;
const tMUXSensor irRight 	= msensor_S2_2;
const tMUXSensor lightSenseLeft 	= msensor_S2_3;
const tMUXSensor lightSenseRight 	= msensor_S2_4;

int rawLightLeft;
int rawLightRight;

bool touchInput1;
bool touchInput2;
bool touchInput3;

task getSmux()
{
	LSsetActive(lightSenseLeft);
	LSsetActive(lightSenseRight);
	writeDebugStreamLine("Multiplexer setup ready");

	while (true){
		rawLightLeft = LSvalRaw(lightSenseLeft);
		rawLightRight = LSvalRaw(lightSenseRight);

		touchInput1 = HTTMUXisActive(TMUX, 1);
		touchInput2 = HTTMUXisActive(TMUX, 2);
		touchInput3 = HTTMUXisActive(TMUX, 3);

	}


}
