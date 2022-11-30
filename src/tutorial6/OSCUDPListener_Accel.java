package tutorial6;

import de.sciss.net.OSCMessage;
import net.happybrackets.core.HBAction;
import net.happybrackets.core.OSCUDPListener;
import net.happybrackets.device.HB;

import java.lang.invoke.MethodHandles;
import java.net.SocketAddress;

public class OSCUDPListener_Accel implements HBAction {
    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below

        // type osclistener to create this code
        OSCUDPListener listener = new OSCUDPListener(9000) {
            @Override
            public void OSCReceived(OSCMessage oscMessage, SocketAddress socketAddress, long time) {
                // type your code below this line

                String myName = oscMessage.getName();
                hb.setStatus(myName + " | " + oscMessage.getArg(0));

                // type your code above this line
            }
        };
        if (listener.getPort() < 0) { //port less than zero is an error
            String error_message = listener.getLastError();
            System.out.println("Error opening port " + 0 + " " + error_message);
        } // end oscListener code

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
