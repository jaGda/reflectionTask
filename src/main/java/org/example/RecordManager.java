package org.example;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class RecordManager {
    Map<Class<?>, HashMap<Integer, Object>> records = new HashMap<>();

    public RecordManager(List<String> files) {
        files.forEach(this::addRecords);
    }

    private void addRecords(String s) {
        Path of = Path.of("./" + s);
        switch (s) {
            case "employee.csv" -> {
                try (Stream<String> lines = Files.lines(of)) {
                    lines.map(line -> line.split(","))
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
            case "project.csv" -> readFromFile(Project.class, of);
            case "task.csv" -> {
                try (Stream<String> lines = Files.lines(of)) {
                    lines.map(line -> line.split(","))
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
            default -> Logger.getLogger(RecordManager.class.getName()).log(Level.SEVERE, "Bad file name or type");
        }
    }

    private <T> void readFromFile(Class<T> cl, Path of) {
        Constructor<T> cons = null;
        try {
            cons = cl.getConstructor();
        } catch (NoSuchMethodException e) {
            Logger.getLogger(RecordManager.class.getName()).log(Level.SEVERE, e.getMessage());
        }

        Field[] fields = null;
        T t = null;
        try {
            assert cons != null;
            t = cons.newInstance();
            fields = cl.getDeclaredFields();
            AccessibleObject.setAccessible(fields, true);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Logger.getLogger(RecordManager.class.getName()).log(Level.SEVERE, e.getMessage());
        }

        try (Stream<String> lines = Files.lines(of)) {
            Field[] finalFields = fields;
            T finalT = t;
            lines.map(line -> line.split(","))
                    .map(arr -> {
                        for (int i = 0; i < arr.length; i++) {
                            if (Pattern.compile("^\\d.+$").matcher(arr[i]).matches()) {
                                try {
                                    finalFields[i].set(finalT, Integer.parseInt(arr[i]));
                                } catch (IllegalAccessException e) {
                                    Logger.getLogger(RecordManager.class.getName()).log(Level.SEVERE, e.getMessage());
                                }
                            } else {
                                try {
                                    finalFields[i].set(finalT, arr[i]);
                                } catch (IllegalAccessException e) {
                                    Logger.getLogger(RecordManager.class.getName()).log(Level.SEVERE, e.getMessage());
                                }
                            }
                        }
                        return finalT;
                    }).forEach(obj -> {
                        int id = 0;
                        try {
                            id = (int) finalT.getClass().getDeclaredField("id").get(finalT);
                        } catch (IllegalAccessException | NoSuchFieldException e) {
                            Logger.getLogger(RecordManager.class.getName()).log(Level.SEVERE, e.getMessage());
                        }
                        if (records.containsKey(obj.getClass())) {
                            records.get(obj.getClass()).computeIfAbsent(id, i -> obj);
                        } else {
                            HashMap<Integer, Object> projects = new HashMap<>();
                            projects.put(id, obj);
                            records.put(obj.getClass(), projects);
                        }
                    });
        } catch (IOException e) {
            Logger.getLogger(RecordManager.class.getName()).log(Level.SEVERE, e.getMessage());
        }
    }

    public <T> T find(Class<T> recordClass, Integer id) {
        return (T) records.get(recordClass).get(id);
    }

    public <T> List<T> list(Class<T> recordClass) {
        return (List<T>) records.get(recordClass).values().stream().collect(Collectors.toList());
    }
}
