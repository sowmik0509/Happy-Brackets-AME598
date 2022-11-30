package tutorial6;

import de.sciss.net.OSCListener;
import de.sciss.net.OSCMessage;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;
import net.happybrackets.core.HBAction;
import net.happybrackets.core.scheduling.Clock;
import net.happybrackets.device.HB;

import java.lang.invoke.MethodHandles;
import java.net.SocketAddress;

public class BroadcastSenderAndListener implements HBAction {
    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below

        //set up a WavePlayer as usual. This time we create a gain envelope which will
        //be used to control the sound via incoming messages
        WavePlayer wp = new WavePlayer(500, Buffer.SINE);
        Envelope gainEnvelope = new Envelope(0);
        Gain g = new Gain(1, gainEnvelope);
        g.addInput(wp);
        hb.sound(g);

        // To create this, just type clockTimer
        Clock clock = HB.createClock(500).addClockTickListener((offset, this_clock) -> {// Write your code below this line
            //we're making things happen at random intervals
            if(hb.rng.nextFloat() < 0.1f) {
                //play a quick loud bleep
                gainEnvelope.clear();
                gainEnvelope.addSegment(0.2f, 10);
                gainEnvelope.addSegment(0, 100);
                //and also send a message announcing the bleep
                hb.broadcast("/bleep", hb.myIndex());
            }
        });

        clock.start();// End Clock Timer

        hb.addBroadcastListener(new OSCListener() {
            @Override
            public void messageReceived(OSCMessage oscMessage, SocketAddress socketAddress, long l) {
                if(oscMessage.getName().equals("/bleep")) {
                    if(hb.getFloatArg(oscMessage, 0) != hb.myIndex()) {
                        //play a slow quieter bleep
                        gainEnvelope.clear();
                        gainEnvelope.addSegment(0.1f, 100);
                        gainEnvelope.addSegment(0, 1000);
                    }
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
