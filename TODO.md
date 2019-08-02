## TODO ##
- [ ] Add more actions that can be used to the SIGN_ACTIONS.md
- [ ] The Copy/Paste function needs to give the MAIN Minecraft thread time to keep up (or the server could get a timeout/crash). When copying or pasting the function should stop for a moment after every X number of blocks before continuing.
- [ ] Making a Copy/Paste function that does everything in chunks (like Minecraft does). Best for copying and pasting bigger pieces of the world. Also needs to stop for a moment before continuing to give the MAIN Minecraft thread time to catch up.

## Bugs ##
- [x] Fix bug with COPY/DESTROY FROM and TO blocks. Sometimes they only COPY/DESTROY a small part between the blocks.

## Changes ##
- [ ] This is one that I'm not sure on yet. I am looking at what kind of Copy/Pasting works best.\
I'm testing pasting in row vs pasting in chunks (like Minecraft uses).\
At first I didn't want to focus on pasting in chunks, but it seems that chunks are better handled by the Spigot server
and less likely to let the server crash if you copy a piece of the world that is to big.
- [x] I want to remmove the Special sign so that every sign can be use for special actions.\
At first I used it so players can't click other players signs with the memory system. But for the mini-games everybody must have the ability to click the sign. I don't see any reason to block other players for clicking Timer, YouTube or paste signs either.

## New features ##
- [ ] Copy chest contents when using the Copy/Paste function
- [ ] A server memory system, that remembers where the placed signs (with special actions) are in the world that need to be loaded at startup. For example the YouTube subscribers counter.
- [x] Mini-games: Atleast one (to start with)
- [ ] Turn lights on/off with special buttons that connect to a light source through coordinates. (also needs system memory)
- [ ] A bridge builder to get over water or canyons.
- [ ] Maybe a Twitter Subscribers Counter
- [ ] A chest with books that explain how to use all the features (and games).
- [ ] Extra commands usable in the chat (Maybe block building rows/heigth etc. + show list files from COPY)
- [ ] A stair builder to get down from a high place or to go up.
- [ ] Picture builder. Read image file and use colored blocks to build the picture in Minecraft.
- [ ] A block that builds a house with bed, chests(with tools in it), lights, etc. So everything you'll need to survive whereever your are. Or maybe some sort of portable house.
