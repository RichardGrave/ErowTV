package graver.erowtv.special;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.main.ErowTV;
import graver.erowtv.tools.NumbersTool;
import graver.erowtv.tools.YmlFileTool;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

public class YoutubeSubCounter extends BukkitRunnable implements ErowTVConstants {

    //You will need a file with the Google API-KEY and default channel.
    private final String API_KEY_FILE = DIR_GENERAL + "api_key_and_channel.yml";
    //'api_key' is important the others can be empty.
    //File looks like this. Each on a new line!
    //    api_key: 'TheGoogleApiKeyHere'
    //    default_channel_name: 'DefaultChannelName'
    //    default_channel_id: 'DefaultChannelId'

    private final String YOUTUBE_STATISTICS = "statistics";
    private final String YOUTUBE_APP_NAME = "YoutubeSubsCounter";

    //Get these from the .yml file
    public final static String YML_API_KEY = "api_key";
    public final static String YML_CHANNEL_NAME = "channel_name";
    public final static String YML_CHANNEL_ID = "channel_id";


    //TODO:RG if server can load this in the future then we need something to stop this if sign is destroyed
    //TODO:RG Kijken of een subs counter mogelijk is
    //Dat om de 30 seconden checkt hoeveel subs we hebben.
    //Bij bepaalde waarden vuurwerk afschiet. B.v. 100, 1000, 10.000, 50.000, 100.000, 500.000
    //Bij bepaalde waarden die belangrijk zijn iets speciaals? Chat met kleuren etc??
    //Gekleurde blocks per waarden?

    private YouTube youTube;
    private Player player;
    private Block blockToUse;
    private BlockFace blockFace;
    private Sign sign;
    private NumbersTool numbersTool;

    private boolean isWallSign = false;
    private String youtubeChannel = "";
    private String memoryName;

    //API-KEY is necessary
    private String youtubeApiKey;
    //You will need one. Channel ID or Channel name
    //!!!Google has rules for when you can use a channel name!!!
    private String youtubeChannelId;
    private String youtubeChannelName;

    //TODO:RG laten stoppen als de sign weg is. Niet zo moeilijk
    //Geef positie van sign mee, zodat bij stuk gaan het this.cancel() aanroept

    public YoutubeSubCounter(Player player, Block blockToUse, BlockFace blockFace, Sign sign, boolean isWallSign, String memoryName) {
        this.player = player;
        this.blockToUse = blockToUse;
        this.blockFace = blockFace;
        this.sign = sign;
        this.youtubeChannel = sign.getLine(SPECIAL_SIGN_PARAMETER_1);
        this.isWallSign = isWallSign;
        this.memoryName = memoryName; //TODO:RG iets mee doen? kunnen laten eindigen?

        this.numbersTool = new NumbersTool(player, blockToUse, blockFace);

        initializeConnection();
        setApiKeyAndDefaultChannel();
    }

    private void setApiKeyAndDefaultChannel() {
        //Check if file with name exists
        File youtube_file;
        if ((youtube_file = YmlFileTool.doesFileExist(API_KEY_FILE)) != null) {
            //file will be 'plugins/saved_files/general/api_key_and_channel.yml'
            FileConfiguration blockConfig = YamlConfiguration.loadConfiguration(youtube_file);

            if (blockConfig.contains(YML_API_KEY)) {
                youtubeApiKey = blockConfig.get(YML_API_KEY).toString();
            }

            //Only if there is not a YoutubeChannel on the Sign.
            if (youtubeChannel.isEmpty()) {
                if (blockConfig.contains(YML_CHANNEL_ID)) {
                    youtubeChannelId = blockConfig.get(YML_CHANNEL_ID).toString();
                }
                if (blockConfig.contains(YML_API_KEY)) {
                    youtubeChannelName = blockConfig.get(YML_CHANNEL_NAME).toString();
                }
            }
        }
    }

    //Initialize once
    private void initializeConnection() {
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
            if(youtubeApiKey.isEmpty()){
                this.cancel();
            }
            if(isDebug) {
                player.sendMessage("Running");
            }
            String numberOfSubscribers = "";

            YouTube.Channels.List search = youTube.channels().list(YOUTUBE_STATISTICS);
            setYoutubeSearchParameters(search);

            numberOfSubscribers = getChannelsResponse(search);

            updateNumbersOrSign(numberOfSubscribers);

            //If there are no subscribers or the memory for the specialsign is gone, then cancel.
            if(numberOfSubscribers.isEmpty() ||
                    !ErowTV.doesPlayerHaveSpecificMemory(player, ErowTVConstants.MEMORY_SPECIAL_SIGN_POSITION)){
                if(isDebug) {
                    player.sendMessage("Cancel YoutubeSubCounter");
                }

                //Something is wrong, so stop
                this.cancel();
            }
        } catch (Exception ex) {
            player.sendMessage("[Youtube::run()][Exception][" + ex.getMessage() + "]");
        }
    }

    private void updateNumbersOrSign(String numberOfSubscribers){
        //Should is be placed with block or just text on a sign
        if (isWallSign) {
            //Create number for how many subscribers we have
            numbersTool.buildEntireNumber(numberOfSubscribers);
        } else {
            //Else set this text to the sign
            sign.setLine(0, (youtubeChannel.isEmpty() ? youtubeChannelName : youtubeChannel));
            sign.setLine(1, numberOfSubscribers);
            sign.update();
        }
    }

    private void setYoutubeSearchParameters(YouTube.Channels.List search) {
        //If the sign row is empty
        if (youtubeChannel.isEmpty()) {
            //If youtubeChannel empty then default channel name or channel id
            if (!youtubeChannelName.isEmpty()) {
                //Only available if you comply with the Google rules
                search.setForUsername(youtubeChannelName);
            } else {
                search.setId(youtubeChannelId);
            }
        } else {
            search.setForUsername(youtubeChannel);
        }

        search.setKey(youtubeApiKey);
    }

    private String getChannelsResponse(YouTube.Channels.List search) {
        try {
            String numberOfSubscribers = "";
            ChannelListResponse response = search.execute();

            //Go through all the channels that come back and print number of subscribers
            List<Channel> channels = response.getItems();
            for (Channel channel : channels) {

                //Set returned data with number separators
                numberOfSubscribers = NumberFormat.getIntegerInstance().
                        format(channel.getStatistics().getSubscriberCount().intValue());
            }

            return numberOfSubscribers;
        } catch (Exception ex) {
            player.sendMessage("[Youtube::getChannelsResponse()][Exception][" + ex.getMessage() + "]");
        }
        return "";
    }
}
