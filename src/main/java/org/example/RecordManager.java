package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class RecordManager {
    Map<Class<?>, Map<Integer, Object>> records = new HashMap<>();

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
                                if (records.containsKey(employee.getClass())){

                                } else {
                                    records.put(employee.getClass(),)
                                }
                                records.computeIfAbsent(employee.getClass(), aClass ->
                                        (Map<Integer, Object>) new HashMap<>().put(employee.id, employee));
                                records.computeIfPresent(employee.getClass(), (aClass, map) ->
                                        (Map<Integer, Object>) map.put(employee.id, employee));
                            });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } ;
            case "project.csv" -> ;
            case "task.csv" -> ;
        }
    }

    public <T> T find(Class<T> recordClass, Integer id) {
        return null;
    }

    public <T> List<T> list(Class<T> recordClass) {
        return null;
    }
}