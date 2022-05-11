package org.example;

final class Employee {
    final Integer id;
    final String firstName;
    final String lastName;
    final String position;

    Employee(Integer id, String firstName, String lastName, String position) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
    }

    Employee(Integer id) {
        this.id = id;
        this.firstName = "";
        this.lastName = "";
        this.position = "";
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}
