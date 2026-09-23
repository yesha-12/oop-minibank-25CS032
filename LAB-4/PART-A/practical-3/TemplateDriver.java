public class TemplateDriver {

    public static void main(String[] args) {

        String template = "Dear {name}, order {id} ships {date}.";

        String[] names = {
                "name",
                "id"
        };

        String[] values = {
                "Riya",
                "A07"
        };

        System.out.println(TemplateFiller.fill(template, names, values));

    }

}