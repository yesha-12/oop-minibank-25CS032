public class CounterService {
    private final CounterModel model = new CounterModel();

    public int addOne() {
        model.increment();
        return model.getCount();
    }

    public int getCount() {
        return model.getCount();
    }

    public void add(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        for (int i = 0; i < amount; i++) {
            model.increment();
        }
    }
}
