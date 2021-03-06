<img src="https://yt3.ggpht.com/JO-pAp65T9KKVymwFXKqGONpCiRjGWqpS3at3WPoKnyQqWuP3MYo6oDRwxD54vknFyWsuKIZYA=w2560-fcrop64=1,00005a57ffffa5a8-k-c0xffffffff-no-nd-rj">  

# A Minecraft plugin for the [SpigotMC](https://www.spigotmc.org) server.

## Tele-grave on [YouTube](https://www.youtube.com/channel/UCinO1QSRjtQi6hiabNDhhzw) and [Twitter](https://twitter.com/TeleGrave)

Erik and Owen wanted a name change for the YouTube/Twitter channel.
The project name is still the same.

I am creating this plugin for my two sons Erik and Owen.\
So they can use it in Minecraft when they are recording videos for our YouTube channel.

- YouTube video 1: [Showing some of the features](https://www.youtube.com/watch?v=u3wUQMlg6dA)
- A few features have already changed sinds the first video and you can see it in the next one.\
YouTube video 2: [YouTube Subscribers counter and more](https://www.youtube.com/watch?v=uWnNGf31Cw8)
- Short video that has no sound and is a bit lagging.\
YouTube video 3: [Pasting an entire village and active redstone](https://www.youtube.com/watch?v=Z5vm7HjdGlk)
- YouTube video 4: [Improved Copy/Paste function and mini-game](https://www.youtube.com/watch?v=LGHf3h3XZ1I)

## Disclaimer:
This is **NOT** a mod for clientside single player Minecraft.\
This **is** a plugin and you'll need a SpigotMC server.

## Also:
This may not be the best code.
It has been refactored a lot during the time that it exists (even before I put it on GitHub). Some API methods/classes of SpigotMC have become deprecated and replaced with new ones. I still have to update some of it in my code.
I didnt want to spend much time on restructuring the code. Just enough to keep it readable/understandable/runnable.

I try not to be to messy with my code.\
I do use JavaDoc and comments.\
And I will refactor bits of code to better structured code whenever I have the time or when I need it for new features.

## Features it has:
- Copy blocks: a Copy FROM and Copy TO block that can copy everything between them and saves it to a file with a given name(from a sign on the block). Seems to work for all the materials now. [Pasting an entire village and active redstone](https://www.youtube.com/watch?v=Z5vm7HjdGlk)
- Destroy blocks: a Destroy FROM and Destroy TO block that can destroy everthing between them.
- Two by Two block: that creates 8 blocks using clicked block material or left-click creating 8 blocks of AIR.
- Paste block: Right-click sign with filename (saved with Copy blocks) and everywhere you'll right-click in the world it will paste all the blocks from the file.
- Special actions:
  - Youtube Subscribers Counter: creates a subscribers count on a sign or with blocks. This uses a Google API-key (generated on [Google Developers](https://developers.google.com) that everybody can generate and use for themselfs)
You can give a channel name (or ID) and how many seconds it has to wait before getting the number of subscribers again.
  - Countdown Timer: creates a time on a sign or with blocks to begin from with the countdown. Use hours, minutes and seconds.
  - Paste: can paste everything from a file that was created with the Copy blocks.
- Recipes: are put into recipe crafting book.
- Mini-game:
  - DoubleOrNothing: You have to place items in a chest to start the game.
  Then you have to push buttons on a wall containing 21 buttons. There are two random bad buttons and if you hit one of those two buttons then you'll lose the game and also the items that you placed in the chest. If you hit 7 correct buttons then you win and your items will be doubled.
    
## Usable commands in chat:
- facing: Gives the direction you are facing. NORTH, EAST, etc.
- debug_messages: Turns debug messages ON or OFF (messages that I placed in the code for testing).
  
## Memory system:
I have created a simple memory system that saves the coordinates of each **special block** (from the features in the text above) for the player that places them. Only that player can do interactions with the blocks/signs to activate them.

So you can ONLY have multiple Copy FROM blocks in the world if they are from DIFFERENT players.
This is needed for the copy/destroy blocks so you can check if there already exists one of each (for that player). If it already exists then it will replace the block with AIR and changes the memory with the new placed block.
If we dont do that then we could for example place multiple Copy FROM blocks (or TO blocks) and then the server doesn't know where to calculate FROM en TO. So it would mess things up pretty bad.
It will also remove blocks from the memory system if they break.

## What to do in the future:
- A server memory system, that remembers where the placed Games/Tools are in the world. This by saving to a file and loading from a file if the server has been restarted. Because the player memory system gets cleaned up when the server stops or restarts. The YouTube Subscribers Counter won't work anymore and the Countdown Timer will stop counting down.
- More YouTube videos about this plugin on our channel.\
[YouTube Channel TeleGrave](https://www.youtube.com/channel/UCinO1QSRjtQi6hiabNDhhzw)
- Here is a [TODO](https://github.com/RichardGrave/ErowTV/blob/master/TODO.md) list with Bugs, Changes and New Features.




