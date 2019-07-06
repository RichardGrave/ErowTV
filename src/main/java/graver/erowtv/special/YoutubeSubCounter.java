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

    //Important
    //!!!! You will need a file ('plugins/saved_files/general/api_key_and_channel.yml') with the Google API-KEY and default channel.
    private final String API_KEY_FILE = DIR_GENERAL + "api_key_and_channel.yml";
    /*'api_key' is required the others can be left out or empty.
     *File contents looks like this. Each on a new line!
     *    api_key: 'TheGoogleApiKeyHere'
     *    channel_name: 'DefaultChannelName'
     *    channel_id: 'DefaultChannelId'
     */

    private final String YOUTUBE_STATISTICS = "statistics";
    private final String YOUTUBE_APP_NAME = "YoutubeSubsCounter";

    //Get these from the .yml file
    public final static String YML_API_KEY = "api_key";
    public final static String YML_CHANNEL_NAME = "channel_name";
    public final static String YML_CHANNEL_ID = "channel_id";


    //TODO:RG if server can load this in the future then we need something to stop this if the sign is destroyed
    /*Default 30 second check.
     *Some ideas
     *Shoot fireworks when reaching certain subscriber values.
     *Maybe fireworks at 100, 1000, 10.000, 50.000, 100.000, 500.000 and more fireworks the higher the number?
     *Also notifications in chat with colors?
     *Maybe give block numbers the color of importency? Meaning bronze, silver, gold, diamond like Youtube Play Button.
     */

    private YouTube youTube;
    private Player player;
    private Sign sign;
    private NumbersTool numbersTool;

    private boolean isWallSign = false;
    private String youtubeChannel = "";
    private String previousNumberOfSubscribers = "";
    //default 30 seconds
    private int checkInXSeconds = 30;

    //API-KEY is necessary
    private String youtubeApiKey;
    private String memoryName;

    /*You will need one -> Channel ID or Channel name
     *!!!Google has rules for when you can use a custom channel name for your YouTube channel!!!
     *1:    Have 100 or more subscribers
     *2:    Be at least 30 days old
     *3:    Have an uploaded photo as a channel icon
     *4:    Have uploaded channel art
     *
     *So, if you dont comply to the rules, then use your channel ID (the random character/numbers)
     */

    private String youtubeChannelId = "";
    private String youtubeChannelName = "";


    /**
     * Create a YouTube counter on a Sign or with block Numbers
     *
     * @param player     player that creates this
     * @param blockToUse as Material for the numbers
     * @param blockFace  direction
     * @param sign       the sign to update the text on
     * @param isWallSign yes, then create the block numbers. No, then just update the sign.
     * @param interval   write on the sign what the interval is for checking YouTube.
     * @param memoryName use to look if special sign still exists
     */
    public YoutubeSubCounter(Player player, Block blockToUse, BlockFace blockFace, Sign sign, boolean isWallSign,
                             int interval, String memoryName) {

        this.player = player;
        this.sign = sign;
        this.youtubeChannel = sign.getLine(SPECIAL_SIGN_PARAMETER_1);
        this.isWallSign = isWallSign;
        this.checkInXSeconds = interval;
        this.memoryName = memoryName;

        this.numbersTool = new NumbersTool(player, blockToUse, blockFace, true);

        initializeConnection();
        setApiKeyAndDefaultChannel();
    }


    /*
     * Read .yml file and get the API_KEY to use with YouTube.
     * YML_CHANNEL_ID and YML_CHANNEL_NAME are optional and only used if there
     * is no channel information on the special sign.
     */
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
                if (blockConfig.contains(YML_CHANNEL_NAME)) {
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

    //TODO:RG Change color or blocks by number of subscribers like in YouTube bronze, silver, diamond etc.?

    @Override
    public void run() {
        try {
            if (youtubeApiKey.isEmpty()) {
                this.cancel();
            }
            if (ErowTV.isDebug) {
                player.sendMessage("Running");
            }
            String numberOfSubscribers = "";

            YouTube.Channels.List search = youTube.channels().list(YOUTUBE_STATISTICS);
            setYoutubeSearchParameters(search);

            numberOfSubscribers = getChannelsResponse(search);

            updateNumbersOrSign(numberOfSubscribers);

            //If there are no subscribers or the memory for the specialsign is gone, then cancel.
            if (numberOfSubscribers.isEmpty() ||
                    !ErowTV.doesPlayerHaveSpecificMemory(player, memoryName)) {

                if (ErowTV.isDebug) {
                    player.sendMessage("Cancel YoutubeSubCounter");
                }

                //Something is wrong, so stop
                this.cancel();
            }
        } catch (Exception ex) {
            player.sendMessage("[Youtube::run()][Exception][" + ex.getMessage() + "]");
        }
    }

    /*
     * Depending on the type of sign it does it's action.
     * WallSign -> Build block numbers and update sign text.
     * Sign -> only update the sign text.
     */
    private void updateNumbersOrSign(String numberOfSubscribers) {
        //Only need to update if subscribers count changes.
        if (!previousNumberOfSubscribers.equalsIgnoreCase(numberOfSubscribers)) {
            //set the new number to compare later
            previousNumberOfSubscribers = numberOfSubscribers;

            //This is the same for both situations
            sign.setLine(0, "Youtube Channel");
            sign.setLine(1, (youtubeChannel.isEmpty() ? youtubeChannelName : youtubeChannel));

            //Should is be placed with block or just text on a sign
            if (isWallSign) {
                //Create number for how many subscribers we have
                numbersTool.buildEntireNumber(numberOfSubscribers);

                sign.setLine(2, checkInXSeconds + "s check");
            } else {
                //Set number of subscribers
                sign.setLine(2, "Subs: " + numberOfSubscribers);
                sign.setLine(3, checkInXSeconds + "s check");
            }
            //Update sign is needed to see changes
            sign.update();
        }
    }

    /*
     * Checks what YouTube channel it needs to search for
     */
    private void setYoutubeSearchParameters(YouTube.Channels.List search) {
        //If the sign row is empty
        if (youtubeChannel.isEmpty()) {
            //If youtubeChannel empty then default channel name or channel id
            if (!youtubeChannelName.isEmpty()) {
                //Only available if you comply with the Google rules (info is on google)
                search.setForUsername(youtubeChannelName);
            } else {
                //Else use your channel ID (everybody has this if there is not channel name created)
                search.setId(youtubeChannelId);
            }
        } else {
            search.setForUsername(youtubeChannel);
        }

        search.setKey(youtubeApiKey);
    }

    /*
     * Handle channels response to find the number of channel subscribers
     */
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
