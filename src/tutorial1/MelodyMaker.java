package tutorial1;

import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;
import net.happybrackets.core.HBAction;
import net.happybrackets.core.scheduling.Clock;
import net.happybrackets.device.HB;

import java.lang.invoke.MethodHandles;

public class MelodyMaker implements HBAction {
    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below

        // To create this, just type clockTimer
        Clock clock = HB.createClock(500).addClockTickListener((offset, this_clock) -> {// Write your code below this line

            int note = 48 + Pitch.major[hb.rng.nextInt(7)];
            hb.setStatus("Note: "+note);

            float freq = Pitch.mtof(note);

            WavePlayer wp = new WavePlayer(freq, Buffer.SINE);

            Envelope e = new Envelope(0);
            e.addSegment(0.5f, 50);

            Gain g = new Gain(1, e);
            e.addSegment(0, 200, new KillTrigger(g));

            g.addInput(wp);

            hb.sound(g);
            // Write your code above this line
        });

        clock.start();// End Clock Timer

        // To create this, just type clockTimer
        Clock addClockTickListener = HB.createClock(500).addClockTickListener((offset, this_clock) -> {// Write your code below this line

            int note = 72 + Pitch.major[hb.rng.nextInt(7)];
            hb.setStatus("Note: "+note);

            float freq = Pitch.mtof(note);

            WavePlayer wp = new WavePlayer(freq, Buffer.SINE);

            Envelope e = new Envelope(0);
            e.addSegment(0.5f, 50);

            Gain g = new Gain(1, e);
            e.addSegment(0, 200, new KillTrigger(g));

            g.addInput(wp);

            hb.sound(g);
            // Write your code above this line
        });

        addClockTickListener.start();// End Clock Timer

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
