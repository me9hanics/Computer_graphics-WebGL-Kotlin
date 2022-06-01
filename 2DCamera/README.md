Select with Mouse + W, Move selected with Mouse + M, rotate selected with Mouse + R, delete them with DELETE
Add new to Mouse coordinates with holding P, and pressing N, and to middle of screen with holding C and pressing N.
Camera: Z to zoom in, X to zoom out, mouse + T to move the camera 

BUGS: As mentioned, the Duplication doesn't work.
The items, which only should take action at the moment when a key is pressed, despite being held down for longer
(thus: adding new items, and zooming in, and zooming out) are reasonably working well. When you hold them down, they only
count as one action for the first 1.5seconds, but after that, it continously takes action. This is weird, since I pass a
mutable list named "press" to Scene from App, which is similar to keysPressed, but instead of removing a button from the 
list when the button is no longer held, I remove it as soon as Scene detects it in the press list. I don't think it's 
supposed to malfunction after 1.5 secs.

That being said, this wasn't my first take on trying to make things happen only once, not in every frame. I first tried a
similar take to what we do with Time in Scene: store the keysPressed's previous value (I mean the set) in keysPressedBefore
and only take action when a key appears in keysPressed, but not in keysPressedBefore. This should theoratically work, but
the two sets always seemed to be equal, and I couldn't make a copy of keysPressed as I didn't find the way to make a copy
of an Arraylist.
(Another bug which I found mostly at the start of developing, very rarely, and haven't seen it since quite some time, is that every now and then, when I spam selection and deselect, one item somehow doesn't get
deselected.)
