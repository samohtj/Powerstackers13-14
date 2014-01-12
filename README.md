Powerstackers13-14
==================
Code for  the Powerstackers (FTC #5029) 2013-2014 season, _Block Party_
----
These files include all the tele-op and autonomous code that we use. Most of it won't be of any use to anyone else, though there are some helpful examples and useful functions buried in there. This year, we focused a lot on portability and modularity, making sure that everything could be used more than one way. Most of the functions in our /autonomous-includes folder could be useful, and there's a lot of neat functions buried in there.

Below is a list of all the functions we've got, and their uses.

**color-mode-picker.c**
   
    short getAutoMode();
    Reads two color sensors, and returns a number from 1-36 based on the combination of colors. 
    Used to set an autonomous game mode option based on colored LEGO block inputs.
