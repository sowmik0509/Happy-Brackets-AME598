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

public class patternTest implements HBAction {

    int counter = 0;

    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below

        int id = Math.abs(hb.myIndex());
        hb.setStatus("myId: " + id);

        // To create this, just type clockTimer
        Clock clock = HB.createClock(500/16);
        clock.start();// End Clock Timer


        Bead pattern01 = new Bead() {
            @Override
            protected void messageReceived(Bead message) {

               if (clock.getNumberTicks() % 16 == 0){

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

            }
        };

        Bead pattern02 = new Bead() {
            @Override
            protected void messageReceived(Bead message) {

                if (clock.getNumberTicks() % 24 == 0){

                    int note = Pitch.forceToScale(hb.rng.nextInt(7) +36, Pitch.major);

                    //hb.setStatus("MIDI step #: " + note);
                    //convert MIDI note frequencey in HZ
                    float freq = Pitch.mtof(note);

                    WavePlayer wp = new WavePlayer(freq, Buffer.SAW);

                    Envelope eGain = new Envelope(0f);
                    eGain.addSegment(0.3f, 100);

                    Gain g = new Gain(1, eGain);
                    eGain.addSegment(0f, 100, new KillTrigger(g));
                    g.addInput(wp);
                    hb.sound(g);


                }
            }
        };


        Bead sequencer = new Bead() {
            @Override
            protected void messageReceived(Bead message) {


                if (clock.getNumberTicks() % 32 == 0) {

                    hb.setStatus("counter: " + counter);

                    if (counter < 8) {
                        pattern01.pause(false);
                        pattern02.pause(true);
                        counter++;
                    }

                   else if (counter < 16){
                        pattern01.pause(false);

                        if (id%2==0) {
                            pattern02.pause(false);
                        }
                        counter++;
                    }

                    else if (counter < 24){
                        pattern01.pause(true);
                        pattern02.pause(false);
                        counter++;
                    }

                    else if (counter >= 24){
                        counter = 0;
                    }



                }
            }
        };


                clock.addClockTickListener((offset, this_clock) -> {// Write your code below this line

                    pattern01.message(null);
                    pattern02.message(null);
                    sequencer.message(null);
                    // Write your code above this line
                });

            pattern01.pause(true);
            pattern02.pause(true);


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
