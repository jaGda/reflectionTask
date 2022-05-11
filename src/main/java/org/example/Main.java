package org.example;

import java.util.List;

/**
 * Zadanie <ID zadania>:
 * Projekt: <ID projektu>
 * Opis: <Opis projektu>
 * Właściciel: <Imię i nazwisko pracownika>
 */
public class Main {
    static List<String> files = List.of("employee.csv", "project.csv", "task.csv");

    public static void main(String[] args) {
        RecordManager recordManager = new RecordManager(files);
        recordManager.records.forEach((aClass, integerObjectHashMap) -> {
            System.out.println(aClass + " :");
            integerObjectHashMap.forEach((integer, o) -> System.out.println("    ID_" + integer + " = " + o));
        });
        System.out.println("\nMethods find and list:");
        System.out.println(recordManager.find(Employee.class, 4));
        System.out.println(recordManager.list(Project.class));
    }
}


