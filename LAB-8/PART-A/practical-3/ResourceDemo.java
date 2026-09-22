public class ResourceDemo {
    static class DemoResource implements AutoCloseable {
        private boolean closed = false;

        @Override
        public void close() {
            closed = true;
            System.out.println("Resource closed automatically.");
        }

        public boolean isClosed() {
            return closed;
        }
    }

    public static void main(String[] args) {
        DemoResource resource = new DemoResource();

        try (resource) {
            System.out.println("Using the resource inside try-with-resources.");
            throw new RuntimeException("Original error inside block");
        } catch (RuntimeException e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println("Was the resource closed? " + resource.isClosed());
        }
    }
}
