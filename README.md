# [YouTube Channel ErowTV](https://www.youtube.com/channel/UCinO1QSRjtQi6hiabNDhhzw)
## A Minecraft plugin for a [SpigotMC](https://www.spigotmc.org) server.

## Disclaimer:
This is **NOT** a mod for clientside single player Minecraft.

## Also:
This may not be the best code.
It has been refactored a lot during the time that it exists (even before I put it on GitHub). Some API methods/classes of SpigotMC have become deprecated and replaced with new ones. I still have to update some of it in my code.
So i didnt want to spend much time on restructuring the code. Just enough to keep it readable/understandable.

I try not to be to messy with my code.
I do use JavaDoc and comments.
And I will refactor bits of code to better structured code whenever I have the time or when I need it for new features.


## Features it has:
- Copy blocks: a Copy FROM and Copy TO block that can copy everything between them and saves it to a file with a given name(from a sign). At the moment this works for all the blocks, but some blocks with special data will have a wrong direction when placed. Stairs for example. They can be upside down and those kind of things.
- Destroy blocks: a Destroy FROM and Destroy TO block that can destroy everthing between them.
- Two by Two block: that creates 8 blocks using clicked block material or left-click creating 8 blocks of AIR.
- Paste block: Right-click sign with filename (saved with Copy blocks) and every where you'll right click in the world it will paste all the blocks from the file.
- Special sign:
  - Youtube Subscribers Counter: creates a subscribers count on a sign or with blocks. This uses a Google API-key (generated on [Google Developers](https://developers.google.com) that everybody can generate and use for themselfs)
You can give a channel name (or ID) and how many seconds it has to wait before getting the number of subscribers again.
  - Countdown Timer: creates a time on a sign or with blocks to begin from with the countdown. Use hours, minutes and seconds.
  - Paste: can paste everything from a file that was created with the Copy blocks.
- Recipes: are put into recipe crafting book
  
 YouTube video: [Some of the features](https://www.youtube.com/watch?v=u3wUQMlg6dA)
 A few features have already changed/add sinds the video.
 Maybe a new video in the near future.
  
## Memory system:
I have created a simple memory system that saves the coordinates of each **special block** (from the features in the text above) for the player that places them. Only that player can do interactions with the blocks/signs to activate them.

This is needed for the copy/destroy blocks so you can check if there already exists one of each. If it already exists then it will replace the block with AIR and changes the memory with the new placed block.
If we dont do that then we could for example place multiple Copy FROM blocks (or TO blocks) and then the server doesn't know where to calculate FROM en TO. So it would mess things up pretty bad.
It will also remove blocks from memory if they break.

It will remember multiple special signs for a player, because a sign only needs itself.
They each get a unique memory name so the server always knows where the signs with their actions are.

## What I want to make in the future:
- Expand the Copy feature so that all needed data from certain blocks like Stairs, pistons, etc. get copied.
- A server memory system, that remembers where the placed Games/Tools are in the world. This by saving to a file and loading from a file if the server has been restarted. Because the player memory system gets cleaned up when the server stops or restarts. The YouTube Subscribers Counter won't work anymore and the Countdown Timer will stop counting down.
- Little games
- Maybe a Twitter Subscribers Counter
- Extra commands usable in the chat. At least one command that lets me activate debug messages in chat when programming new features.
- More tools to use (dont know what they will be, yet) Have to brainstorm on that one :)
- More YouTube videos of this plugin on our channel
[YouTube Channel ErowTV](https://www.youtube.com/channel/UCinO1QSRjtQi6hiabNDhhzw)


