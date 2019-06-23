package graver.erowtv.special;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.main.ErowTV;
import graver.erowtv.tools.NumbersTool;
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

    public CountDownTimer(Player player, BlockFace blockFace, Block blockToUse, Sign sign, boolean isWallSign) {
        this.player = player;
        this.blockToUse = blockToUse;
        this.blockFace = blockFace;
        this.sign = sign;
        this.isWallSign = isWallSign;
        setTime(sign.getLine(0));
    }

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
            player.sendMessage("FormattedTimeString = " + formattedTimeString);
        } catch (Exception ex) {
            player.sendMessage("[CountDownTimer][Exception][Not a valid time so set]");
        }

        //If length is greater then 3 or 0 then somethins is wrong and dont do anything.
    }

    //TODO:RG Use blockToUse as number block
    //If there is only a sign and no block behind it then change sign text to a countdown

    @Override
    public void run() {
        try {
            if(isWallSign) {
                //create number with blocks
                NumbersTool.buildEntireNumber(player, formattedTimeString, blockToUse, blockFace);
            }else{
                //else update the sign text
                sign.setLine(0, formattedTimeString);
            }

            //It stops if time is zero or whitespace
            if (formattedTimeString.equalsIgnoreCase(END_TIME_SECOND) || formattedTimeString.equalsIgnoreCase(" ")) {
                this.cancel();
                //Remove the memory after the timer ends
                ErowTV.removeMemoryFromPlayerMemory(player, ErowTVConstants.MEMORY_CLOCK_POSITION);

                if(isWallSign) {
                    //Clean up last number
                    NumbersTool.buildEntireNumber(player, " ", blockToUse, blockFace);
                }else{
                    //Else set this text to the sign
                    sign.setLine(0, "Time's up");
                }
            }else{
                //Build new time string for next number
                buildTimeString(formattedTimeString);
            }
        } catch (Exception ex) {
            player.sendMessage("[CountDownTimer-run][Exception][" + ex.getMessage() + "]");
        }
    }

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
}
