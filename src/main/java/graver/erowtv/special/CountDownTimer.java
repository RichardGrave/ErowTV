package graver.erowtv.special;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.main.ErowTV;
import graver.erowtv.tools.NumbersTool;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CountDownTimer extends BukkitRunnable implements ErowTVConstants {

    private final String END_TIME_SECOND = "0";

    private Player player;
    private Block blockToUse;
    private BlockFace blockFace;
    private Sign sign;

    //Date should not be used, replace this in the future.
    private Date date;
    private String formattedTimeString = "";
    private String timeFormat;
    private boolean isWallSign;
    private String memoryName;

    private NumbersTool numbersTool;

    /**
     * Create a CountDownTimer on a Sign or with block Numbers (interval is every second)
     *
     * @param player     player that creates this
     * @param blockFace  direction
     * @param blockToUse the block that is needed to get the Material from and Material from block on top of it
     * @param sign       the sign to update the text on
     * @param isWallSign yes, then create the block numbers. No, then just update the sign.
     * @param memoryName use to look if special sign still exists
     */
    public CountDownTimer(Player player, BlockFace blockFace, Block blockToUse, Sign sign, boolean isWallSign, String memoryName) {
        this.player = player;
        this.blockToUse = blockToUse;
        this.blockFace = blockFace;
        this.sign = sign;
        this.isWallSign = isWallSign;
        this.memoryName = memoryName;

        this.numbersTool = new NumbersTool(player, blockToUse, blockFace, false);

        setTime(sign.getLine(SPECIAL_SIGN_PARAMETER_1));
    }

    //Get the time from Sign text.
    private void setTime(String time) {
        try {
            String[] timeSplit = time.split(":");
            if (timeSplit.length == 1) {
                //Length 1 means start with one Second position
                timeFormat = timeSplit[0].length() == 1 ? TIME_FORMAT_S : TIME_FORMAT_SS;
                formattedTimeString += timeSplit[0];

            } else if (timeSplit.length == 2) {
                //Length 1 means start with one Minute position
                timeFormat = timeSplit[0].length() == 1 ? TIME_FORMAT_M_SS : TIME_FORMAT_MM_SS;
                formattedTimeString += timeSplit[0];
                formattedTimeString += ":";
                //Just to be sure there are 2 position for the seconds
                formattedTimeString += timeSplit[1].length() == 1 ? "0" + timeSplit[1] : timeSplit[1];

            } else if (timeSplit.length == 3) {
                //Length 1 means start with one Hour position
                timeFormat = timeSplit[0].length() == 1 ? TIME_FORMAT_H_MM_SS : TIME_FORMAT_HH_MM_SS;

                formattedTimeString += timeSplit[0];
                formattedTimeString += ":";
                //Just to be sure there are 2 position for the minutes
                formattedTimeString += timeSplit[1].length() == 1 ? "0" + timeSplit[1] : timeSplit[1];
                formattedTimeString += ":";
                //Just to be sure there are 2 position for the seconds
                formattedTimeString += timeSplit[2].length() == 1 ? "0" + timeSplit[2] : timeSplit[2];
            }
            if(ErowTV.isDebug) {
                player.sendMessage("FormattedTimeString = " + formattedTimeString);
            }
        } catch (Exception ex) {
            player.sendMessage("[CountDownTimer][Exception][Not a valid time so set]");
        }

        //If length is greater then 3 or 0 then somethins is wrong and dont do anything.
    }

    @Override
    public void run() {
        try {
            createNumberBlocksOrUpdateSign(formattedTimeString, formattedTimeString);

            //It stops if time is zero or whitespace
            if (formattedTimeString.equalsIgnoreCase(END_TIME_SECOND) || formattedTimeString.equalsIgnoreCase(" ")) {
                this.cancel();
                //Remove the memory after the timer ends
                ErowTV.removeMemoryFromPlayerMemory(player, memoryName);

                //Play sound to notify that the CountdownTimer is ended
                new RingTheBell().runTaskTimer(ErowTV.javaPluginErowTV, TIME_SECOND, TIME_SECOND);

                //Last thing to do
                createNumberBlocksOrUpdateSign(" ", "Time's up");
            }else{
                //Build new time string for next number
                buildTimeString(formattedTimeString);
            }
        } catch (Exception ex) {
            player.sendMessage("[CountDownTimer-run][Exception][" + ex.getMessage() + "]");
        }
    }

    //Check if we need to create numbers or just update the sign with the new time.
    private void createNumberBlocksOrUpdateSign(String bigNumber, String textSignLineONe){
        if(isWallSign) {
            //Clean up last number
            numbersTool.buildEntireNumber(bigNumber);
        }else{
            //Else set this text to the sign
            sign.setLine(0, textSignLineONe);
            sign.setLine(1, "");
            sign.update();
        }
    }

    //Get the String with the time to use.
    private void buildTimeString(String timeToUse) {
        try {
            //Remove whitespaces from reformatTimeString method
            formattedTimeString = formattedTimeString.trim();

            SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);
            this.date = formatter.parse(timeToUse);

            Calendar c = Calendar.getInstance();
            c.setTime(date);
            //Minus one second
            c.add(Calendar.SECOND, -1);

            formattedTimeString = formatter.format(c.getTime());

            //get rid of zero's and : that are unnecessary
            reformatTimeString();
        } catch (Exception ex) {
            player.sendMessage("[buildTimeString][Exception][" + ex.getMessage() + "]");
            formattedTimeString = END_TIME_SECOND;
        }
    }

    //Removed starting zero's
    //So, 01:25 becomes 1:25
    private void reformatTimeString(){
        //If it starts with 0: then remove for better viewing and set new timeFormat
        if(formattedTimeString.startsWith("0:")){
            //Replace chars with 2 whitespaces so they disappear int the world.
            formattedTimeString = "  " + formattedTimeString.substring(2);
            changeTimeFormat();

            //If it starts with a zero then remove it for better viewing
        }else if(formattedTimeString.startsWith("0")){
            //Replace char with whitespace so they disappear int the world.
            formattedTimeString = " " + formattedTimeString.substring(1);
            changeTimeFormat();
        }
    }

    //Change timeformat if starting zero's are gone.
    //So, mm:ss becomes m:ss
    private void changeTimeFormat(){
        switch (timeFormat){
            case TIME_FORMAT_HH_MM_SS:
                timeFormat = TIME_FORMAT_H_MM_SS;
                break;
            case TIME_FORMAT_H_MM_SS:
                timeFormat = TIME_FORMAT_MM_SS;
                break;
            case TIME_FORMAT_MM_SS:
                timeFormat = TIME_FORMAT_M_SS;
                break;
            case TIME_FORMAT_M_SS:
                timeFormat = TIME_FORMAT_SS;
                break;
            case TIME_FORMAT_SS:
                timeFormat = TIME_FORMAT_S;
                break;
        }
    }

    //Only for ringing a bell to notify the user that the Countdown has ended.
    private class RingTheBell extends BukkitRunnable{

        private final int RING_THE_BELL_SIX_TIMES = 6;
        private int rangTheBell = 1;

        @Override
        public void run() {
            try {
                player.playSound(player.getLocation(), Sound.BLOCK_BELL_USE, 1.0f, 1.0f);

                //Cancel if we reach the number of time the bell rang
                if(rangTheBell == RING_THE_BELL_SIX_TIMES){
                    this.cancel();
                }
                rangTheBell++;
            } catch (Exception ex) {
                player.sendMessage("[CountDownTimer-run][Exception][" + ex.getMessage() + "]");
            }
        }
    }
}
