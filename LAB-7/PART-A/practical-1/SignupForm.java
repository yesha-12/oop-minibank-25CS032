@SuppressWarnings("unused")
class SignupForm {

    @SuppressWarnings("unused")
    @NotBlank
    String name;

    @SuppressWarnings("unused")
    @MaxLength(10)
    String username;

    SignupForm(String name, String username) {
        this.name = name;
        this.username = username;
    }
}