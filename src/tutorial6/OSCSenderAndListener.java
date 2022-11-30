package tutorial6;

import de.sciss.net.OSCChannel;
import de.sciss.net.OSCListener;
import de.sciss.net.OSCMessage;
import de.sciss.net.OSCServer;
import net.happybrackets.core.HBAction;
import net.happybrackets.core.HBReset;
import net.happybrackets.device.HB;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class OSCSenderAndListener implements HBAction, HBReset {

    OSCServer server = null;

    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device

        //this example won't actually do anything useful, but it shows how you can build more regular
        //custom OSC senders and receivers.
        try {
            //server listens on port 5555
            server = OSCServer.newUsing(OSCChannel.UDP, 5555);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //add an OSC listener
        server.addOSCListener(new OSCListener() {
            @Override
            public void messageReceived(OSCMessage oscMessage, SocketAddress socketAddress, long l) {
                //receive incoming messages here on port 5555
                if(oscMessage.getName().equals("/hello")) {
                    hb.testBleep();
                    hb.setStatus("RECEIVED!");
                }
            }
        });

        //try sending an OSC message (this will be received by the listener above because the
        //hostname (localhost) and port are correct). If you want to send to specific devices you
        //can enter their hostnames and choose specifc ports as you require.
        OSCMessage message = new OSCMessage("/hello");
        try {
            server.send(message, new InetSocketAddress("127.0.0.1", 5555));
            hb.setStatus("SEND!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void doReset() {
        if(server != null) {
            server.dispose();
        }
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
