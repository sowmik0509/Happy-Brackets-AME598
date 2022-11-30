package tutorial6;

import de.sciss.net.OSCListener;
import de.sciss.net.OSCMessage;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;
import net.happybrackets.core.HBAction;
import net.happybrackets.device.HB;

import java.lang.invoke.MethodHandles;
import java.net.SocketAddress;

public class ControllerListener implements HBAction {
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

        //create a controller listener. This only responds to messages that are sent from the controller
        hb.addControllerListener(new OSCListener() {
            @Override
            public void messageReceived(OSCMessage oscMessage, SocketAddress socketAddress, long l) {
                //the incoming OSC message has a "name" which we can match
                if(oscMessage.getName().equals("/play")) {
                    if(oscMessage.getArgCount() > 0) {
                        float freq = hb.getFloatArg(oscMessage, 0);
                        wp.setFrequency(freq);
                    }
                    //note we should always clear an envelope if we're dynamically controlling it
                    //this means that any new segments are executed immediately, and are not stuck in a queue
                    gainEnvelope.clear();
                    gainEnvelope.addSegment(0.1f, 100);
                    gainEnvelope.addSegment(0, 1000);
                } else if(oscMessage.getName().equals("/square")) {
                    wp.setBuffer(Buffer.SQUARE);
                } else if(oscMessage.getName().equals("/sine")) {
                    wp.setBuffer(Buffer.SINE);
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
