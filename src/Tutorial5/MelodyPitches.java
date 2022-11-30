package Tutorial5;

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

public class MelodyPitches implements HBAction {
    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below

        // To create this, just type clockTimer
        Clock clock = HB.createClock(500).addClockTickListener((offset, this_clock) -> {// Write your code below this line

            //MIDI note number 0-127
            int note = Pitch.forceToScale(hb.rng.nextInt(12) + 24, Pitch.major);

            float freq = Pitch.mtof(note);


            WavePlayer wp = new WavePlayer(freq, Buffer.SAW);

            Envelope e = new Envelope(0f);
            e.addSegment(0.5f, 10);
            e.addSegment(0.3f, 100);
            e.addSegment(0.3f, 200);

            Gain g = new Gain(1, e);

            e.addSegment(0f, 200, new KillTrigger(g));

            g.addInput(wp);

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
