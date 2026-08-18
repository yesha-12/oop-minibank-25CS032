interface Switchable {
    void on();
    void off();

    default void toggle() {
        off();
        on();
    }
}

class Fan implements Switchable {
    private boolean on = false;

    public void on() {
        on = true;
        System.out.println("Fan is ON");
    }

    public void off() {
        on = false;
        System.out.println("Fan is OFF");
    }
}

class Light implements Switchable {
    private boolean on = false;

    public void on() {
        on = true;
        System.out.println("Light is ON");
    }

    public void off() {
        on = false;
        System.out.println("Light is OFF");
    }
}

@FunctionalInterface
interface SwitchRule {
    boolean maySwitchOn(Switchable device, int hour);
}

public class RemoteControlDemo {

    public static void main(String[] args) {

        Switchable[] devices = {
            new Fan(),
            new Light()
        };

        System.out.println("Toggling Devices:");

        for (Switchable device : devices) {
            device.toggle();
            System.out.println();
        }

        int hour = 20;

        SwitchRule anonymousRule = new SwitchRule() {
            @Override
            public boolean maySwitchOn(Switchable device, int hour) {
                return hour >= 6 && hour <= 22;
            }
        };

        System.out.println("Anonymous Class Rule:");
        System.out.println(
            "Can switch ON at " + hour + ":00 = " +
            anonymousRule.maySwitchOn(devices[0], hour)
        );

        SwitchRule lambdaRule =
            (device, time) -> time >= 6 && time <= 22;

        System.out.println();

        System.out.println("Lambda Rule:");
        System.out.println(
            "Can switch ON at " + hour + ":00 = " +
            lambdaRule.maySwitchOn(devices[1], hour)
        );
    }
}