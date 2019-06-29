#[ErowTV](https://www.youtube.com/channel/UCinO1QSRjtQi6hiabNDhhzw)
## A Minecraft plugin for a [SpigotMC](https://www.spigotmc.org) server.

This is **NOT** a mod for clientside single player Minecraft.

## Features it has:
- Copy blocks: a Copy FROM and Copy TO block that can copy everything between them and saves it to a file with a given name(from a sign).
- Destroy blocks: a Destroy FROM and Destroy TO block that can destroy everthing between them.
- Two by Two block: that creates 8 blocks using clicked block material or left-click creating 8 blocks of AIR.
- Paste block: Right-click sign with filename (saved with Copy blocks) and every where you'll right click in the world it will paste all the blocks from the file.
- Special sign:
  - Youtube Subscribers Counter: creates a subscribers count on a sign or with blocks. This uses a Google API-key (generated on [Google Developers](https://developers.google.com) that everybody can genereate en use for themselfs)
You can give a channel name (or ID) and how many seconds it has to wait before getting the number of subscribers again.
  - Countdown Timer: creates a time on a sign or with blocks to begin from with the countdown. Use hours, minutes and seconds.
  - Paste: can paste everything from a file that was created with the Copy blocks.
  
 [Some of the features](https://www.youtube.com/watch?v=u3wUQMlg6dA) 
  
## Memory system:
I have created a simple memory system that saves the coordinates of each **special block** (the features above) for the player that places them. Only that player can do interactions with the blocks/signs to activate them.

This is needed for the copy/destroy blocks so you can check if there already exists one of each. If it does it will replace the placed block. If we didnt do that we could for example place multiple Copy FROM blocks and then the server doesn't know where to calculate FROM en TO. So it would mess things up.

It will remember multiple special signs for a player, because a sign only needs itself.
They each get a unique memory name so the server always knows where the signs with their actions are.

## What I want to make in the future:
- Little games
- Extra commands usable in the chat
- More tools to use (dont know what they will be, yet)
- More YouTube videos of this plugin on our channel
[channel ErowTV](https://www.youtube.com/channel/UCinO1QSRjtQi6hiabNDhhzw)


