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
            case "employee.csv" -> readFromFile(Employee.class, of);
            case "project.csv" -> readFromFile(Project.class, of);
            case "task.csv" -> readFromFile(Task.class, of);
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
            Constructor<T> finalCons = cons;
            lines.map(line -> line.split(","))
                    .map(arr -> {
                        T finalT = null;
                        try {
                            finalT = finalCons.newInstance();
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                            Logger.getLogger(RecordManager.class.getName()).log(Level.SEVERE, e.getMessage());
                        }

                        for (int i = 0; i < arr.length; i++) {
                            try {
                                if (finalFields[i].getType() == Employee.class) {
                                    Constructor<?> constructor = Employee.class.getConstructor(Class.forName("java.lang.Integer"));
                                    Employee employee = (Employee) constructor.newInstance(Integer.parseInt(arr[i]));
                                    finalFields[i].set(finalT, employee);
                                } else if (finalFields[i].getType() == Project.class) {
                                    Constructor<?> constructor = Project.class.getConstructor(Class.forName("java.lang.Integer"));
                                    Project project = (Project) constructor.newInstance(Integer.parseInt(arr[i]));
                                    finalFields[i].set(finalT, project);
                                } else if (Pattern.compile("^\\d+$").matcher(arr[i]).matches()) {
                                    finalFields[i].set(finalT, Integer.parseInt(arr[i]));
                                } else {
                                    finalFields[i].set(finalT, arr[i]);
                                }
                            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException |
                                     InstantiationException | ClassNotFoundException e) {
                                Logger.getLogger(RecordManager.class.getName()).log(Level.SEVERE, e.getMessage());
                            }
                        }
                        return finalT;
                    }).forEach(obj -> {
                        int id = 0;
                        try {
                            id = (int) obj.getClass().getDeclaredField("id").get(obj);
                        } catch (IllegalAccessException | NoSuchFieldException e) {
                            Logger.getLogger(RecordManager.class.getName()).log(Level.SEVERE, e.getMessage());
                        }
                        if (records.containsKey(obj.getClass())) {
                            records.get(obj.getClass()).computeIfAbsent(id, i -> obj);
                        } else {
                            HashMap<Integer, Object> objects = new HashMap<>();
                            objects.put(id, obj);
                            records.put(obj.getClass(), objects);
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
