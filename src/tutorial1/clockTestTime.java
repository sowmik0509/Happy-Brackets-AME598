package tutorial1;

import net.happybrackets.core.HBAction;
import net.happybrackets.core.scheduling.Clock;
import net.happybrackets.device.HB;

import java.lang.invoke.MethodHandles;

public class clockTestTime implements HBAction {
    int clockTime01 = 1000;
    int clockTime02 = 1000;
    int clockTime03 = 1000;

    @Override
    public void action(HB hb) {
        hb.reset(); //Clears any running code on the device
        //Write your sketch below

        int myIndex = Math.abs(hb.myIndex());

        int[] clockTimes = {200, 300, 1200, 1500, 500, 600, 800, 1200, 1500, 600, 700, 2200, 900, 1200, 1500, 1800, 2000, 3000, 5000};

        // To create this, just type clockTimer
        if (myIndex % 3 == 0) {

            Clock clock = HB.createClock(clockTime01).addClockTickListener((offset, this_clock) -> {// Write your code below this line


                clockTime01 = clockTimes[hb.rng.nextInt((clockTimes.length - 1))];
                hb.setStatus("clockTime01: " + clockTime01);
                this_clock.setInterval(clockTime01);
                // Write your code above this line
            });

            clock.start();// End Clock Timer

        }

        if (myIndex % 3 == 1) {

            Clock clock = HB.createClock(clockTime02).addClockTickListener((offset, this_clock) -> {// Write your code below this line


                clockTime02 = clockTimes[hb.rng.nextInt((clockTimes.length - 1))];
                hb.setStatus("clockTime02 : " + clockTime02);
                this_clock.setInterval(clockTime02);
                // Write your code above this line
            });

            clock.start();// End Clock Timer

        }

        if (myIndex % 3 == 2) {

            Clock clock = HB.createClock(clockTime03).addClockTickListener((offset, this_clock) -> {// Write your code below this line


                clockTime03 = clockTimes[hb.rng.nextInt((clockTimes.length - 1))];
                hb.setStatus("clockTime03 : " + clockTime03);
                this_clock.setInterval(clockTime03);
                // Write your code above this line
            });

            clock.start();// End Clock Timer

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
