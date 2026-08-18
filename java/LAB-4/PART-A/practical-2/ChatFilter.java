public class ChatFilter {

    public static String filter(String[] logs, String keyword) {

        int count = 0;

        StringBuilder sb = new StringBuilder();

        for (String line : logs) {

            String[] parts = line.split(" ", 3);

            if (parts.length < 3)
                continue;

            String time = parts[0];
            String user = parts[1];
            String message = parts[2];

            if (message.toLowerCase().contains(keyword.toLowerCase())) {

                count++;

                sb.append(time)
                  .append(" ")
                  .append(user)
                  .append(": ")
                  .append(message)
                  .append("\n");

            }

        }

        return "Matches : " + count + "\n" + sb.toString();

    }

}