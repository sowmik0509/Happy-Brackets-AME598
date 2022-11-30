package tutorial1;

import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.*;
import net.happybrackets.core.HBAction;
import net.happybrackets.core.instruments.WaveModule;
import net.happybrackets.device.HB;

public class RandomToneWithDelay implements HBAction {
    @Override
    public void action(HB hb) {

        hb.reset();       //try taking this line out

        //create a sine-wave player that plays a random frequency
        WavePlayer wp = new WavePlayer(hb.rng.nextFloat() * 1000 + 500, Buffer.SINE);

        //create a gain modifier that scales the gain by 0.1 (=turns down the volume)
        Gain g = new Gain(1, 0.1f);

        //connect them together waveplayer plugs into gain, gain plugs into speakers
        g.addInput(wp);
        hb.getAudioOutput().addInput(g);


        //create an envelope that controls the volume
        Envelope gainEnv = new Envelope(0f);
        g.setGain(gainEnv);

        //make the envelope fade up and then down again
        gainEnv.addSegment(0.1f, 200f);
        gainEnv.addSegment(0f, 300f, 2);

        //create a delay
        TapIn tin = new TapIn(10000);
        TapOut tout = new TapOut(tin, 500);

        //connect the sound through the delay
        tin.addInput(g);
        hb.getAudioOutput().addInput(tout);

        //make the delay feed back on itself
        Gain feedbackGain = new Gain(1, 0.7f);
        feedbackGain.addInput(tout);
        tin.addInput(feedbackGain);


    }


}
