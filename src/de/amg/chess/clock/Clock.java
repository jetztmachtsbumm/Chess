package de.amg.chess.clock;

import javax.swing.*;
import java.awt.*;

public class Clock {

    private final Timer timer;
    private int seconds;
    private int minutes;
    private int hours;
    private String secondsString;
    private String minutesString;
    private String hoursString;

    public Clock(int seconds, int minutes, int hours, Label timeLabel) {
        this.seconds = seconds;
        this.minutes = minutes;
        this.hours = hours;

        timer = new Timer(1000, e -> {
            this.seconds--;

            if(this.seconds < 0){
                this.seconds = 59;
                this.minutes--;
                if(this.minutes < 0){
                    this.minutes = 59;
                    this.hours--;
                    if(this.hours < 0){
                        //Time's up!
                        stop();
                        return;
                    }
                }
            }

            secondsString = String.format("%02d", this.seconds);
            minutesString = String.format("%02d", this.minutes);
            hoursString = String.format("%02d", this.hours);

            timeLabel.setText(getTimeString());
            System.out.println(getTimeString());
        });
    }

    public void start(){
        timer.start();
    }

    public void stop(){
        timer.stop();
    }

    public String getTimeString(){
        return hoursString + ":" + minutesString + ":" + secondsString;
    }

}
