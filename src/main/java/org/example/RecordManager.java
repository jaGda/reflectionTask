package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class RecordManager {
    Map<Class<?>, HashMap<Integer, Object>> records = new HashMap<>();

    public RecordManager(List<String> files) {
        files.forEach(this::addRecords);
    }

    private void addRecords(String s) {
        switch (s) {
            case "employee.csv" -> {
                try (Stream<String> lines = Files.lines(Path.of("./" + s))) {
                    lines.map(record -> record.split(","))
                            .map(arr -> new Employee(Integer.valueOf(arr[0]), arr[1], arr[2], arr[3]))
                            .forEach(employee -> {
                                if (records.containsKey(employee.getClass())) {
                                    records.get(employee.getClass()).computeIfAbsent(employee.id, i -> employee);
                                } else {
                                    HashMap<Integer, Object> employees = new HashMap<>();
                                    employees.put(employee.id, employee);
                                    records.put(employee.getClass(), employees);
                                }
                            });
                } catch (IOException e) {
                    Logger.getLogger(RecordManager.class.getName()).log(Level.SEVERE, e.getMessage());
                }
            }
            case "project.csv" -> {
                try (Stream<String> lines = Files.lines(Path.of("./" + s))) {
                    lines.map(record -> record.split(","))
                            .map(arr -> new Project(Integer.valueOf(arr[0]), arr[1]))
                            .forEach(projecte -> {
                                if (records.containsKey(projecte.getClass())) {
                                    records.get(projecte.getClass()).computeIfAbsent(projecte.id, i -> projecte);
                                } else {
                                    HashMap<Integer, Object> projects = new HashMap<>();
                                    projects.put(projecte.id, projecte);
                                    records.put(projecte.getClass(), projects);
                                }
                            });
                } catch (IOException e) {
                    Logger.getLogger(RecordManager.class.getName()).log(Level.SEVERE, e.getMessage());
                }
            }
            case "task.csv" -> {
                try (Stream<String> lines = Files.lines(Path.of("./" + s))) {
                    lines.map(record -> record.split(","))
                            .map(arr -> new Task(Integer.valueOf(arr[0]), arr[1], Integer.valueOf(arr[2]), Integer.valueOf(arr[3])))
                            .forEach(task -> {
                                if (records.containsKey(task.getClass())) {
                                    records.get(task.getClass()).computeIfAbsent(task.id, i -> task);
                                } else {
                                    HashMap<Integer, Object> tasks = new HashMap<>();
                                    tasks.put(task.id, task);
                                    records.put(task.getClass(), tasks);
                                }
                            });
                } catch (IOException e) {
                    Logger.getLogger(RecordManager.class.getName()).log(Level.SEVERE, e.getMessage());
                }
            }
        }
    }

    public <T> T find(Class<T> recordClass, Integer id) {
        return (T) records.get(recordClass).entrySet().stream()
                .filter(entry -> entry.getKey() == id)
                .findFirst().orElse(null);
    }

    public <T> List<T> list(Class<T> recordClass) {
        return (List<T>) records.get(recordClass).values().stream().collect(Collectors.toList());
    }
}
