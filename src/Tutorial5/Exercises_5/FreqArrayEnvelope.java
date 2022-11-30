package Tutorial5.Exercises_5;

import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.WavePlayer;
import net.happybrackets.core.HBAction;
import net.happybrackets.core.instruments.WaveModule;
import net.happybrackets.device.HB;

import java.lang.invoke.MethodHandles;
import java.util.Random;

public class FreqArrayEnvelope implements HBAction {
    final int NUMBER_AUDIO_CHANNELS = 1; // define how many audio channels our device is using


    // define the different frequencies we will be using in our envelope
    float LOW_FREQUENCY = 500;   // this is the low frequency of the waveform we will make
    float HIGH_FREQUENCY = 2000; // This is the high frequency of the waveform we will make

    @Override
    public void action(HB hb) {

        // remove this code if you do not want other compositions to run at the same time as this one
        hb.reset();

        //here is a trick to write into the status line of the simulator to tell you what patch has loaded
        hb.setStatus(this.getClass().getSimpleName() + " Loaded");

        //this Random number generator is a Java Class.  We will use it to randomly select the frequency to play
        Random random = new Random();

        // this is an Array.  In this case it is an array of Integers
        final int [] DIMINISHED_SCALE = {100, 200, 300, 500, 600, 800, 900, 1100};

        final float INITIAL_VOLUME = 0.05f; // Define how loud we want our sound

        // define the times it takes to reach the points in our envelope
        final float RAMP_UP_FREQUENCY_TIME = 1000; // 1 second (time is in milliseconds)
        final float HOLD_FREQUENCY_TIME = 500; // 0.5 seconds
        final float RAMP_DOWN_FREQUENCY_TIME = 1000; // 1 seconds


        //here we make a pattern - this pattern allows us to
        hb.pattern(new Bead() {
            @Override
            protected void messageReceived(Bead message) {

                if (hb.clock.getCount() % 32 == 0){

                    LOW_FREQUENCY = DIMINISHED_SCALE[random.nextInt(3)];
                    HIGH_FREQUENCY = DIMINISHED_SCALE[random.nextInt(7)];

                    DIMINISHED_SCALE[0] = 3000;

                    System.out.println("Scale number: " + DIMINISHED_SCALE[random.nextInt(7)]);

                    // Create our envelope using LOW_FREQUENCY as the starting value
                    Envelope frequencyEnvelope = new Envelope(LOW_FREQUENCY);

                    // create a wave player to generate a waveform using the frequencyEnvelope and a sinewave
                    WavePlayer waveformGenerator = new WavePlayer(frequencyEnvelope, Buffer.SINE);

                    WaveModule player = new WaveModule(frequencyEnvelope, INITIAL_VOLUME, Buffer.SINE);
                    player.connectTo(HB.getAudioOutput());


                    // Now start changing the frequency of frequencyEnvelope
                    // first add a segment to progress to the higher frequency over 5 seconds
                    frequencyEnvelope.addSegment(HIGH_FREQUENCY, RAMP_UP_FREQUENCY_TIME);

                    // now add a segment to make the frequencyEnvelope stay at that frequency
                    // we do this by setting the start of the segment to the value as our HIGH_FREQUENCY
                    frequencyEnvelope.addSegment(HIGH_FREQUENCY, HOLD_FREQUENCY_TIME);

                    //Now make our frequency go back to the lower frequency
                    frequencyEnvelope.addSegment(LOW_FREQUENCY, RAMP_DOWN_FREQUENCY_TIME);

                    //Now make our frequency hold to the lower frequency, and after holding, kill our gainAmplifier
                    frequencyEnvelope.addSegment(LOW_FREQUENCY, HOLD_FREQUENCY_TIME, new KillTrigger(player.getKillTrigger()));


                }


            }
        });



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


