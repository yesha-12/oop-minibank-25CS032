public class Driver {

    public static void main(String[] args) {

        String[] passwords = {
                "abc",
                "Abcd1234!",
                "Hello12",
                "Password@1"
        };

        for (String pw : passwords) {

            System.out.println("Password : " + pw);

            System.out.println("Length : " + PasswordChecker.hasLength(pw));
            System.out.println("Uppercase : " + PasswordChecker.hasUppercase(pw));
            System.out.println("Digit : " + PasswordChecker.hasDigit(pw));
            System.out.println("Special : " + PasswordChecker.hasSpecial(pw));

            System.out.println("Strength : " + PasswordChecker.strength(pw));

            System.out.println();
        }

    }

}