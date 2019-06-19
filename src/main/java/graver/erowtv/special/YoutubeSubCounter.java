package graver.erowtv.special;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import graver.erowtv.constants.Enumerations;
import graver.erowtv.player.PlayerTools;
import graver.erowtv.tools.NumbersTool;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.List;

public class YoutubeSubCounter extends BukkitRunnable{

    private final String YOUTUBE_API_KEY = "AIzaSyAGe1jAtgU7A4NIjVhBIMSWJ9l0cn5wSv4";
    private final String YOUTUBE_APP_NAME = "ErowTV Channel";
    private final String YOUTUBE_CHANNEL_ID = "UCinO1QSRjtQi6hiabNDhhzw";

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
        private Block placedBlock;
        private Enumerations.PlayerDirection direction;
        private int checkCounter = 0;

        public YoutubeSubCounter(Player player, Block placedBlock) {
            this.player = player;
            this.placedBlock = placedBlock;
            this.direction = PlayerTools.getPlayerDirection(player);
        }

        //TODO:RG Kleur van blokken laten bepalen aan de hand van aantal subscribers
        //Zoals bronze, zilver, diamond??

        @Override
        public void run() {
            try {
                HttpRequestInitializer httpRequestInitializer = new HttpRequestInitializer() {
                    public void initialize(HttpRequest request) throws IOException {
                    }
                };

                YouTube youTube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), httpRequestInitializer)
                        .setApplicationName(YOUTUBE_APP_NAME)
                        .build();
                YouTube.Channels.List search = youTube.channels().list("statistics");
                search.setId(YOUTUBE_CHANNEL_ID);
                search.setKey(YOUTUBE_API_KEY);
                ChannelListResponse response = search.execute();

                List<Channel> channels = response.getItems();
                for (Channel channel : channels) {
                    player.sendMessage("[CheckCounter= "+checkCounter+"][ErowTV Channel subscribers: "+
                            channel.getStatistics().getSubscriberCount().toString() +"]");

                    NumbersTool.buildEntireNumber(player, channel.getStatistics().getSubscriberCount(),
                            placedBlock, direction);
                }

                checkCounter++;
            }catch (Exception ex){
                player.sendMessage("[Youtube][Exception]["+ex.getMessage()+"]");
            }

        }


}
