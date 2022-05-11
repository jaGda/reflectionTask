package org.example;

final class Task {
    final Integer id;
    final String description;
    final Project project;
    final Employee owner;

    public Task(Integer id, String description, Project project, Employee owner) {
        this.id = id;
        this.description = description;
        this.project = project;
        this.owner = owner;
    }
}
