abstract class Media {

    protected String title;

    Media(String title) {
        this.title = title;
    }

    abstract double lateFee(int daysLate);
}

class Book extends Media {

    Book(String title) {
        super(title);
    }

    @Override
    double lateFee(int daysLate) {
        return daysLate * 2.0;
    }
}

class DVD extends Media {

    DVD(String title) {
        super(title);
    }

    @Override
    double lateFee(int daysLate) {
        return daysLate * 5.0;
    }
}

class Magazine extends Media {

    Magazine(String title) {
        super(title);
    }

    @Override
    double lateFee(int daysLate) {
        return daysLate * 1.5;
    }
}

public class MediaDemo {

    public static void main(String[] args) {

        Media[] media = {
            new Book("Java Programming"),
            new DVD("Inception"),
            new Magazine("Technology Today")
        };

        int daysLate = 4;

        for (Media item : media) {

            System.out.println(
                item.title +
                " Late Fee = " +
                item.lateFee(daysLate)
            );
        }
    }
}