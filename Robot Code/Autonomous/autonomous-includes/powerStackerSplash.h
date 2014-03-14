/*
Function to display the Powerstackers team logo to the NXT screen.
By Jonathan Thomas, 27 May 2013

*/



void drawPowerstacker(int x, int y){
	// left top right bottom
    nxtFillEllipse(x+23, 	y+56, x+37, y+45); 	// Head
    nxtFillRect		(x+22, 	y+44, x+38, y+31); 	// Upper torso
    nxtFillRect		(x+20, 	y+30, x+40, y+26); 	// Middle torso
    nxtFillRect		(x+16, 	y+26, x+44, y+21); 	// Hips
    nxtFillRect		(x+16, 	y+20, x+23, y); 		// Left leg
    nxtFillRect		(x+8, 	y+5, 	x+15, y); 		// Left foot
    nxtFillRect		(x+37, 	y+20, x+44, y); 		// Right leg
    nxtFillRect		(x+45, 	y+5, 	x+52, y); 		// Right foot
    nxtFillRect		(x+8, 	y+44, x+21, y+40); 	// Left arm
    nxtFillRect		(x, 		y+48, x+13, y+45); 	// Left hand
    nxtFillRect		(x+39, 	y+44, x+52, y+40); 	// Right arm
    nxtFillRect		(x+47, 	y+48, x+60, y+45); 	// Right hand
}

void erasePowerstacker(int x, int y){

    nxtEraseEllipse(x+23, 	y+56, x+37, y+45); 	// Head
    nxtEraseRect		(x+22, 	y+44, x+38, y+31); 	// Upper torso
    nxtEraseRect		(x+20, 	y+30, x+40, y+26); 	// Middle torso
    nxtEraseRect		(x+16, 	y+26, x+44, y+21); 	// Hips
    nxtEraseRect		(x+16, 	y+20, x+23, y); 		// Left leg
    nxtEraseRect		(x+8, 	y+5, 	x+15, y); 		// Left foot
    nxtEraseRect		(x+37, 	y+20, x+44, y); 		// Right leg
    nxtEraseRect		(x+45, 	y+5, 	x+52, y); 		// Right foot
    nxtEraseRect		(x+8, 	y+44, x+21, y+40); 	// Left arm
    nxtEraseRect		(x, 		y+48, x+13, y+45); 	// Left hand
    nxtEraseRect		(x+39, 	y+44, x+52, y+40); 	// Right arm
    nxtEraseRect		(x+47, 	y+48, x+60, y+45); 	// Right hand
}

void drawCenteredPowerstacker(){
	drawPowerstacker(20, 4);
}

void eraseCenteredPowerstacker(){
	erasePowerstacker(20, 4);
}

void drawP(){
    nxtFillRect(4, 34, 12, 21); //Big stem thing
    nxtFillRect(10, 50, 21, 47); // Top flat bit
    nxtFillRect(4, 43, 7, 35); // More stem
}

void showPowStaSplash(){
    bNxtLCDStatusDisplay = false;
    eraseDisplay();
    /* Make sure the screen is blank before we start */

    drawPowerstacker(0, 0); // Draw the powerstacker character

}

void doPowerstackerSplash(){
	eraseDisplay();
	drawCenteredPowerstacker();
	wait10Msec(100);
	eraseDisplay();
	wait10Msec(50);
	nxtFillRect(0, 64, 100, 0);
	eraseCenteredPowerstacker();
	wait10Msec(300);
}

//task main(){
//	doPowerstackerSplash();
//}
