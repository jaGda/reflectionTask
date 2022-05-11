package org.example;

import java.lang.reflect.Field;
import java.util.List;
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
                Field project = value.getClass().getField("project");
                System.out.println(project.getName());
                Class<?> projectInstantiation = project.getClass();
                Field projectId = projectInstantiation.getField("id");
                int idVal = projectId.getInt(projectInstantiation);
                System.out.println("    Projekt ID: " + idVal);

                Project project1 = recordManager.find(Project.class, idVal);
                System.out.println("    Nazwa projektu: " + project1.name);

                Class<?> task = value.getClass();
                String taskDescription = (String) task.getField("description").get(task);
                System.out.println("    Opis zadania: " + taskDescription);

                Field employee = value.getClass().getField("owner");
                Class<?> employeeInstantiation = employee.getClass();
                int employeeId = (int) employeeInstantiation.getField("id").get(employeeInstantiation);

                Employee employee1 = recordManager.list(Employee.class).stream().filter(e -> e.id == employeeId).findFirst().get();
                System.out.println("    Właściciel: " + employee1.firstName + " " + employee1.lastName);

            } catch (NoSuchFieldException | IllegalAccessException e) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, e.getMessage());
            }
        });
    }
}
