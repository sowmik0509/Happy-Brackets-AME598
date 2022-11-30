package tutorial1;

import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.ugens.*;
import net.happybrackets.core.HBAction;
import net.happybrackets.core.scheduling.Clock;
import net.happybrackets.device.HB;

import java.lang.invoke.MethodHandles;

public class RPi_Test implements HBAction {
    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below

        // To create this, just type clockTimer
        Clock clock = HB.createClock(500).addClockTickListener((offset, this_clock) -> {// Write your code below this line

            WavePlayer wp = new WavePlayer(hb.rng.nextFloat() * 1000 + 500, Buffer.SINE);
            Gain g = new Gain(1, 0.01f);
            g.addInput(wp);
            hb.sound(g);

            Envelope gainEnv = new Envelope(0f);
            g.setGain(gainEnv);

            //make the envelope fade up and then down again
            gainEnv.addSegment(0.01f, 200f);
            gainEnv.addSegment(0f, 300f, new KillTrigger(g));

            TapIn tin = new TapIn(100);
            TapOut tout = new TapOut(tin, 50);

            //connect the sound through the delay
            tin.addInput(g);
            hb.sound(tout);

            //make the delay feed back on itself
            Gain feedbackGain = new Gain(1, 0.07f);
            feedbackGain.addInput(tout);
            tin.addInput(feedbackGain);
            // Write your code above this line
        });

        clock.start();// End Clock Timer

        // write your code above this line
    }


    //<editor-fold defaultstate="collapsed" desc="Debug Start">

    /**
     * This function is used when running sketch in IntelliJ IDE for debugging or testing
     *
     * @param args standard args required
     */
    public static void main(String[] args) {

        try {
            HB.runDebug(MethodHandles.lookup().lookupClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //</editor-fold>
}
