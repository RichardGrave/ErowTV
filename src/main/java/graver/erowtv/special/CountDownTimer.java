package graver.erowtv.special;

import graver.erowtv.constants.ErowTVConstants;
import graver.erowtv.main.ErowTV;
import graver.erowtv.tools.NumbersTool;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CountDownTimer extends BukkitRunnable {

    private final String END_TIME_HOUR = "00:00:00";
    private final String END_TIME_MINUTE = "00:00";
    private final String END_TIME_SECOND = "00";

    private Player player;
    private Block blockToUse;
    private BlockFace blockFace;

    private Date date;
    private String formattedTimeString = "";
    private String timeFormat = "HH:mm:ss";
    private String cleanUpString = "";

    public CountDownTimer(Player player, BlockFace blockFace, Block blockToUse, String time) {
        this.player = player;
        this.blockToUse = blockToUse;
        this.blockFace = blockFace;
        setTime(time);
    }

    private void setTime(String time) {
        try {
            String[] timeSplit = time.split(":");
            if (timeSplit.length == 1) {
                player.sendMessage("TimeSplit = 1");
                timeFormat = "ss";
                formattedTimeString += timeSplit[0].length() == 1 ? "0" + timeSplit[0] : timeSplit[0];

                cleanUpString = "  ";
            } else if (timeSplit.length == 2) {
                player.sendMessage("TimeSplit = 2");
                timeFormat = "mm:ss";
                formattedTimeString += timeSplit[0].length() == 1 ? "0" + timeSplit[0] : timeSplit[0];
                formattedTimeString += ":";
                formattedTimeString += timeSplit[1].length() == 1 ? "0" + timeSplit[1] : timeSplit[1];

                cleanUpString = "     ";
            } else if (timeSplit.length == 3) {
                player.sendMessage("TimeSplit = 3");
                //timeFormat stays default
                formattedTimeString += timeSplit[0].length() == 1 ? "0" + timeSplit[0] : timeSplit[0];
                formattedTimeString += ":";
                formattedTimeString += timeSplit[1].length() == 1 ? "0" + timeSplit[1] : timeSplit[1];
                formattedTimeString += ":";
                formattedTimeString += timeSplit[2].length() == 1 ? "0" + timeSplit[2] : timeSplit[2];

                cleanUpString = "        ";
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
            NumbersTool.buildEntireNumber(player, formattedTimeString, blockToUse, blockFace);

            player.sendMessage("1 RUN-FormattedTimeString = " + formattedTimeString);

            //TODO:RG only one number shows up

            buildTimeString(formattedTimeString);

            player.sendMessage("2 RUN-FormattedTimeString = " + formattedTimeString);

            if (formattedTimeString.equalsIgnoreCase(END_TIME_HOUR) || formattedTimeString.equalsIgnoreCase(END_TIME_MINUTE)
                    || formattedTimeString.equalsIgnoreCase(END_TIME_SECOND)) {
                player.sendMessage("END_TIME");
                this.cancel();
                //Remove the memory after the timer ends
                ErowTV.removeMemoryFromPlayerMemory(player, ErowTVConstants.MEMORY_CLOCK_POSITION);
                NumbersTool.buildEntireNumber(player, cleanUpString, blockToUse, blockFace);
            }
        } catch (Exception ex) {
            player.sendMessage("[CountDownTimer-run][Exception][" + ex.getMessage() + "]");
        }

    }

    private void buildTimeString(String timeToUse) {
        try {
            player.sendMessage("1 timeToUse = " + formattedTimeString);
            SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);
            this.date = formatter.parse(timeToUse);

            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.SECOND, -1);

            formattedTimeString = formatter.format(c.getTime());
            player.sendMessage("2 timeToUse = " + formattedTimeString);

        } catch (Exception ex) {
            player.sendMessage("[buildTimeString][Exception][" + ex.getMessage() + "]");
            formattedTimeString = END_TIME_SECOND;
        }
    }
}
