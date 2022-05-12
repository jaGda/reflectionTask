package org.example;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    static List<String> files = List.of("employee.csv", "project.csv", "task.csv");

    public static void main(String[] args) {
        RecordManager recordManager = new RecordManager(files);
        printTasks(recordManager);

//        recordManager.records.forEach((aClass, integerObjectHashMap) -> {
//            System.out.println(aClass + " :");
//            integerObjectHashMap.forEach((integer, o) -> System.out.println("    ID_" + integer + " = " + o));
//        });
//        System.out.println("\nMethods find and list:");
//        System.out.println(recordManager.find(Employee.class, 4));
//        System.out.println(recordManager.list(Project.class));
    }

    private static void printTasks(RecordManager recordManager) {
        recordManager.records.get(Task.class).forEach((key, value) -> {
            System.out.println("Zadanie " + key + ":");
            try {
                Field projectField = value.getClass().getDeclaredField("project");
                Project project = (Project) projectField.get(value);
                int projectId = project.id;
                System.out.println("    Projekt ID: " + projectId);

                Project project1 = recordManager.find(Project.class, projectId);
                System.out.println("    Nazwa projektu: " + project1.name);

                Task task = (Task) value;
                System.out.println("    Opis zadania: " + task.description);

                Field employeeField = value.getClass().getDeclaredField("owner");
                Employee employee = (Employee) employeeField.get(value);

                Employee employee1 = recordManager.list(Employee.class).stream().filter(e -> Objects.equals(e.id, employee.id)).findFirst().get();
                System.out.println("    Właściciel: " + employee1.firstName + " " + employee1.lastName);

            } catch (NoSuchFieldException | IllegalAccessException e) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage());
            }
        });
    }
}
