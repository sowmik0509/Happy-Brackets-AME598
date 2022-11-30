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

public class ArrayPatches implements HBAction {

    int arr[] = {60, 64, 67, 72, 67, 64, 60};
    int base[] = {0, 12, 17, 24, 17, 12, 0};
    int count = 0;
    int secCount = 0;

    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below

        // To create this, just type clockTimer
        Clock clock = HB.createClock(500/16).addClockTickListener((offset, this_clock) -> {// Write your code below this line

            if(this_clock.getNumberTicks() % 4 == 0) {


                int note = arr[count] + base[secCount];
                float freq = Pitch.mtof(note);

                hb.setStatus("note: "+count);

                WavePlayer wp = new WavePlayer(freq, Buffer.SINE);

                Envelope e = new Envelope(0);
                e.addSegment(0.5f, 10);
                e.addSegment(0.5f, 200);

                Gain g = new Gain(1, e);

                e.addSegment(0, 300, new KillTrigger(g));

                g.addInput(wp);

                hb.sound(g);

                count++;
                if(count > arr.length-1)
                    count = 0;

            }

            if(this_clock.getNumberTicks() % 32 == 0) {

                float freq = Pitch.mtof(24);

                WavePlayer wp = new WavePlayer(freq, Buffer.SAW);

                Envelope e = new Envelope(0);
                e.addSegment(0.5f, 10);
                e.addSegment(0.5f, 200);

                Gain g = new Gain(1, e);

                e.addSegment(0, 300, new KillTrigger(g));

                g.addInput(wp);

                hb.sound(g);

                secCount++;
                if(secCount > base.length-1)
                    secCount = 0;


            }


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
