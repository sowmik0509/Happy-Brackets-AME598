package Tutorial5;

import net.beadsproject.beads.core.Bead;
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

public class PatternStarter implements HBAction {
    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below

        hb.setStatus("Patten01 Playing");


                // To create this, just type clockTimer
                Clock clock = HB.createClock(500);
                clock.start();


             Bead melodyPattern = new Bead() {
                 @Override
                 protected void messageReceived(Bead message) {

                     int note = Pitch.forceToScale(hb.rng.nextInt(7) + 60, Pitch.major);

                     hb.setStatus("MIDI step #: " + note);
                     //convert MIDI note frequencey in HZ
                     float freq = Pitch.mtof(note);

                     WavePlayer wp = new WavePlayer(freq, Buffer.SINE);

                     Envelope eGain = new Envelope(0f);
                     eGain.addSegment(0.5f, 100);

                     Gain g = new Gain(1, eGain);
                     eGain.addSegment(0f, 100, new KillTrigger(g));
                     g.addInput(wp);
                     hb.sound(g);
                 }
             };



        //attach a clockListener to the pattern Bead
        clock.addClockTickListener((offset, this_clock) -> {// Write your code below this line

            melodyPattern.message(null);
        });

        //start off with all the music patterns paused
        //patten01.pause(true);
        melodyPattern.pause(false);


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
