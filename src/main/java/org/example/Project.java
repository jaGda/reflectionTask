package org.example;

final class Project {
    Integer id;
    String name;

    Project(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    Project(Integer id) {
        this.id = id;
        name = "";
    }

    public Project() {
    }

    @Override
    public String toString() {
        if (name.isEmpty()) {
            return "Project{" + "id=" + id + '}';
        } else {
            return "Project{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
