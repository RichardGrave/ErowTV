package graver.erowtv.special;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class YoutubeSubCounter extends BukkitRunnable{


    //TODO:RG Kijken of een subs counter mogelijk is
    //Dat om de 30 seconden checkt hoeveel subs we hebben.
    //Bij bepaalde waarden vuurwerk afschiet. B.v. 100, 1000, 10.000, 50.000, 100.000, 500.000
    //Bij bepaalde waarden die belangrijk zijn iets speciaals? Chat met kleuren etc??
    //Gekleurde blocks per waarden?

//    @Override
//    public void onEnable() {
//        new MyTask().runTaskTimer(ErowTV.getJavaPluginErowTV(), 0, 20);
//    }

        private Player player;
        private int countdown;

        public YoutubeSubCounter(Player player) {
            this.player = player;
            this.countdown = 5;
        }

        @Override
        public void run() {
            if (countdown <= 0) {
                player.sendMessage("Countdown is over. Cancelling task.");

                this.cancel(); // cancel this task. countdown is over.
                return;
            }

            player.sendMessage("Countdown: " + countdown);
            countdown--; // decrement the counter

        }


}
