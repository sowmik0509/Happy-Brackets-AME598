package tutorial1;

import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.happybrackets.core.HBAction;
import net.happybrackets.core.scheduling.Clock;
import net.happybrackets.device.HB;

import java.lang.invoke.MethodHandles;

public class SamplePlayerGuitar implements HBAction {
    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below

        SampleManager.group("Guitar", "data/audio/Nylon_Guitar");

        // To create this, just type clockTimer
        Clock clock = HB.createClock(500).addClockTickListener((offset, this_clock) -> {// Write your code below this line

            Sample sample = SampleManager.randomFromGroup("Guitar");
            SamplePlayer samplePlayer = new SamplePlayer(sample);

            Gain g = new Gain(1, 0.5f);
            g.addInput(samplePlayer);

            hb.sound(g);
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
