package graver.erowtv.special;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import graver.erowtv.tools.NumbersTool;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.List;

public class YoutubeSubCounter extends BukkitRunnable{

    private final String YOUTUBE_STATISTICS = "statistics";
    private final String YOUTUBE_API_KEY = "AIzaSyAGe1jAtgU7A4NIjVhBIMSWJ9l0cn5wSv4";
    private final String YOUTUBE_APP_NAME = "ErowTV Channel";
    private final String YOUTUBE_CHANNEL_ID = "UCinO1QSRjtQi6hiabNDhhzw";
    private final String YOUTUBE_CHANNEL_EROWTV = "ErowTV";

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
        private Block blockToUse;
        private BlockFace blockFace;
        private Sign sign;
        private String youtubeChannel = "";
        private boolean isWallSign = false;
        private YouTube youTube;
        private String memoryName;

        //TODO:RG laten stoppen als de sign weg is. Niet zo moeilijk
        //Geef positie van sign mee, zodat bij stuk gaan het this.cancel() aanroept

        public YoutubeSubCounter(Player player, Block blockToUse, BlockFace blockFace, Sign sign, boolean isWallSign, String memoryName) {
            this.player = player;
            this.blockToUse = blockToUse;
            this.blockFace = blockFace;
            this.sign = sign;
            this.youtubeChannel = sign.getLine(1);
            this.isWallSign = isWallSign;
            this.memoryName = memoryName; //TODO:RG iets mee doen? kunnen laten eindigen?

            initializeConnection();
        }

        //Initialize once
        private void initializeConnection(){
            HttpRequestInitializer httpRequestInitializer = new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            };

            //From youtube example
            youTube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), httpRequestInitializer)
                    .setApplicationName(YOUTUBE_APP_NAME)
                    .build();
        }

        //TODO:RG Kleur van blokken laten bepalen aan de hand van aantal subscribers
        //Zoals bronze, zilver, diamond??

        @Override
        public void run() {
            try {
                String numberOfSubscribers = "";

                YouTube.Channels.List search = youTube.channels().list(YOUTUBE_STATISTICS);

                //Just for our own channel :)
                if(youtubeChannel.isEmpty()) {
                    //If youtubeChannel empty then default our own Channel
                    search.setId(YOUTUBE_CHANNEL_ID);
                }else {
                    search.setForUsername(youtubeChannel);
                }

                search.setKey(YOUTUBE_API_KEY);
                ChannelListResponse response = search.execute();

                //Go through all the channels that come back and print number of subscribers
                List<Channel> channels = response.getItems();
                for (Channel channel : channels) {

                    //Set returned data
                    numberOfSubscribers = channel.getStatistics().getSubscriberCount().toString();
                }

                //Should is be placed with block or just text on a sign
                if(isWallSign) {
                    //Create number for how many subscribers we have
                    NumbersTool.buildEntireNumber(player, numberOfSubscribers, blockToUse, blockFace);
                }else{
                    //Else set this text to the sign
                    sign.setLine(0, (youtubeChannel.isEmpty() ? YOUTUBE_CHANNEL_EROWTV : youtubeChannel));
                    sign.setLine(1, numberOfSubscribers);
                    sign.update();
                }

            }catch (Exception ex){
                player.sendMessage("[Youtube][Exception]["+ex.getMessage()+"]");
            }

        }


}
