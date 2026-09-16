class Student {

    @Column(name = "name")
    String name;

    @Column(name = "age")
    int age;

    @Column(name = "city")
    String city;

    public void display() {
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("City: " + city);
    }
}