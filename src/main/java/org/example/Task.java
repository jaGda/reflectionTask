package org.example;

final class Task {
    Integer id;
    String description;
    Project project;
    Employee owner;

    Task(Integer id, String description, Integer projectID, Integer owner) {
        this.id = id;
        this.description = description;
        this.project = new Project(projectID);
        this.owner = new Employee(owner);
    }

    public Task() {
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
