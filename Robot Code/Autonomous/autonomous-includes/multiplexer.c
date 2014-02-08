#include "hitechnic-sensormux.h"
#include "lego-light.h"
#include "hitechnic-irseeker-v2.h"
#include "hitechnic-touchmux.h"

const tMUXSensor irLeft = msensor_S2_1;
const tMUXSensor irRight = msensor_S2_2;
const tMUXSensor lightSenseLeft 	= msensor_S2_3;
const tMUXSensor lightSenseRight 	= msensor_S2_4;

int rawLightLeft;
int rawLightRight;
int irStrengthLeft;
int irStrengthRight;

bool touchInput1;
bool touchInput2;
bool touchInput3;

int dummy;

task getSmux()
{
	LSsetActive(lightSenseLeft);
	LSsetActive(lightSenseRight);
	writeDebugStreamLine("Multiplexer setup ready");

	while (true){
		rawLightLeft = LSvalRaw(lightSenseLeft);
		rawLightRight = LSvalRaw(lightSenseRight);

		if(!HTIRS2readEnhanced(msensor_S2_1, dummy, irStrengthLeft)){
			writeDebugStreamLine("Something's wrong");
		}
		if(!HTIRS2readEnhanced(msensor_S2_2, dummy, irStrengthRight)){
			writeDebugStreamLine("Something's wrong");
		}

		writeDebugStreamLine("IR Left:%d IR Right: %d", irStrengthLeft, irStrengthRight);

		touchInput1 = HTTMUXisActive(TMUX, 1);
		touchInput2 = HTTMUXisActive(TMUX, 2);
		touchInput3 = HTTMUXisActive(TMUX, 3);

	}


}
