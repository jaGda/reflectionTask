package org.example;

final class Task {
    final Integer id;
    final String description;
    final Project project;
    final Employee owner;

    Task(Integer id, String description, Integer projectID, Integer owner) {
        this.id = id;
        this.description = description;
        this.project = new Project(projectID);
        this.owner = new Employee(owner);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", project=" + project +
                ", owner=" + owner +
                '}';
    }
}
