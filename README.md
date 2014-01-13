Powerstackers13-14
==================
Code for  the Powerstackers (FTC #5029) 2013-2014 season, _Block Party_
----
These files include all the tele-op and autonomous code that we use. Most of it won't be of any use to anyone else, though there are some helpful examples and useful functions buried in there. This year, we focused a lot on portability and modularity, making sure that everything could be used more than one way. Most of the functions in our /autonomous-includes folder could be useful, and there's a lot of neat functions buried in there.

Below is a list of all the functions we've got, and their uses.

**autonomous-tasks.c**

    bool foundIr;
    A flag that is activated when the findIr() task is completed.

    bool placedBlock;
    A flag that is activated when the placeBlock() task is completed.

    short blockTurnDirection;
    A flag that dictates the direction relative to the robot that the robot will turn to place the block.
    Uses constants CLOCKWISE and COUNTERCLOCKWISE. Defaults to CLOCKWISE.

    void allMotorsTo(int i);
    Sets all motors on the robot to the parameter value.

    void driveMotorsTo(int i);
    Sets all motors in the drive train to the input value.

    void turnXDegrees(float degreesToTurn);
    Given a positive or negative degree measure, the robot will turn to that degree measure. 0 position
    defaults to the direction the gyro sensor is pointing.

    task findIr();
    Moves the robot forward until a certain threshold is reached, dictated by the global constant int 
    slowThresh; Stops when the sensor value reaches the global constant int stopThresh; Switches foundIr 
    flag to true.

    task placeBlock();
    Turns the robot 90 degrees in the direction specified by the flag blockTurnDirection; Raises the sucker,
    drops the block into the basket, lowers the sucker, and turns back to its original orientation.

**color-mode-picker.c**
   
    short getAutoMode();
    Reads two color sensors, and returns a number from 1-36 based on the combination of colors. 
    Used to set an autonomous game mode option based on colored LEGO block inputs.

