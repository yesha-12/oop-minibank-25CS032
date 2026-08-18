@FunctionalInterface
interface Notifier {
    void send(String message);
}

interface Urgent {
}

class UrgentNotifier implements Notifier, Urgent {

    private String type;

    UrgentNotifier(String type) {
        this.type = type;
    }

    public void send(String message) {
        System.out.println(type + ": " + message);
    }
}

public class NotificationSenderDemo {

    public static void main(String[] args) {

        Notifier email = message ->
            System.out.println("Email: " + message);

        Notifier sms = message ->
            System.out.println("SMS: " + message);

        Notifier urgentEmail =
            new UrgentNotifier("Urgent Email");

        Notifier[] senders = {
            email,
            sms,
            urgentEmail
        };

        String message =
            "Your MiniBank practical is scheduled today.";

        System.out.println("Normal Broadcast:");

        for (Notifier sender : senders) {
            sender.send(message);
        }

        System.out.println();

        System.out.println("Urgent Broadcast:");

        for (Notifier sender : senders) {

            if (sender instanceof Urgent) {
                sender.send("URGENT: " + message);
            }
        }
    }
}