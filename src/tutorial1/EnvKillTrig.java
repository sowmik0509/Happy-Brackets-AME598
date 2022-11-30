package tutorial1;

import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;
import net.happybrackets.core.HBAction;
import net.happybrackets.core.scheduling.Clock;
import net.happybrackets.device.HB;

import java.lang.invoke.MethodHandles;

public class EnvKillTrig implements HBAction {
    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below

        int io = hb.getAudioOutput().getIns();
        hb.setStatus("IO: " + io);

        // To create this, just type clockTimer
        Clock clock = HB.createClock(300).addClockTickListener((offset, this_clock) -> {// Write your code below this line

            WavePlayer momentary = new WavePlayer(hb.rng.nextFloat() * 500 + 2000, Buffer.SINE);

            Envelope amp = new Envelope(0);
            amp.addSegment(0.3f, 199);
            amp.addSegment(0.2f, 50);
            amp.addSegment(0.2f, 100);

            Gain g = new Gain(1, amp);

            amp.addSegment(0, 200, new KillTrigger(g));

            g.addInput(momentary);

            hb.getAudioOutput().addInput(g);
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
