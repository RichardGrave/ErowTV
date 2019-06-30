package graver.erowtv.special;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.main.ErowTV;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/*
 * This Class if for handling SpecialSign methods with special needs
 */
public class SpecialHandler implements ErowTVConstants {

    /*
     * Create a YoutubeSubCounter that check every X seconds
     * Default is 30 seconds
     */
    public void handleYoutubeSubCounter(Player player, Block blockBehindSign, BlockFace blockFace, Sign toolSign,
                                               boolean isWallSign, String memoryName) {

        String secondParameter = toolSign.getLine(SPECIAL_SIGN_PARAMETER_2);
        //Default 30 seconds check
        int interval = TIME_30_SECONDS;
        int intervalInSeconds = TIME_REAL_30_SECONDS;

        try {
            //Only parse when not empty and bigger then zero
            if (!secondParameter.isEmpty() && (intervalInSeconds = Integer.parseInt(secondParameter)) > 0) {
                //Game ticks (20) * real seconds
                interval = TIME_SECOND * intervalInSeconds;
            }
        } catch (Exception ex) {
            player.sendMessage("Second parameter on the SpecialSign is not a Number");
            ex.printStackTrace();
        }

        //Run YoutubeSubCounter
        new YoutubeSubCounter(player, blockBehindSign, blockFace, toolSign, isWallSign, intervalInSeconds, memoryName).
                runTaskTimer(ErowTV.javaPluginErowTV, TIME_SECOND, interval);
    }

}
