public class Main {

    public static void main(String[] args) {

        SignupForm form = new SignupForm("", "abcdefghijk");

        String[] errors = FormValidator.validate(form);

        for (String error : errors) {
            System.out.println(error);
        }
    }
}
