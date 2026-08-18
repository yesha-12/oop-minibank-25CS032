public class Main {

    public static void main(String[] args) {

        System.out.println("Mobile");

        System.out.println(
                Validator.isValidMobile("9876543210"));

        System.out.println(
                Validator.isValidMobile("12345"));

        System.out.println();

        System.out.println("Email");

        System.out.println(
                Validator.isValidEmail("abc@gmail.com"));

        System.out.println(
                Validator.isValidEmail("abcgmail"));

        System.out.println();

        System.out.println("PAN");

        System.out.println(
                Validator.isValidPan("ABCDE1234F"));

        System.out.println(
                Validator.isValidPan("ABC123"));

        System.out.println();

        System.out.println("IFSC");

        System.out.println(
                Validator.isValidIfsc("SBIN0001234"));

        System.out.println(
                Validator.isValidIfsc("1234"));

        System.out.println();

        Command c =
                CommandParser.parse("DEPOSIT AC0001 500");

        System.out.println(c.type());

        System.out.println(c.accountNumber());

        System.out.println(c.amount());

    }

}