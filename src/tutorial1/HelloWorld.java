package tutorial1;

import net.beadsproject.beads.ugens.Noise;
import net.happybrackets.core.HBAction;
import net.happybrackets.device.HB;

public class HelloWorld implements HBAction {
    @Override
    public void action(HB hb) {

        hb.reset(); //Clears any running code on the device

        Noise n = new Noise();
        hb.sound(n);

    }

}
