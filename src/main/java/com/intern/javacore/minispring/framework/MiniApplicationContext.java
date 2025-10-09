package com.intern.javacore.minispring.framework;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

public class MiniApplicationContext {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    public MiniApplicationContext(String basePackage) {
        try {
            Set<Class<?>> componentClasses = scanPackageForComponents(basePackage);
            instantiateComponents(componentClasses);
            injectFields();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize context for package: " + basePackage, e);
        }
    }

    public <T> T getBean(Class<T> type) {
        // direct match
        Object direct = beans.get(type);
        if (direct != null) {
            return type.cast(direct);
        }
        // find assignable
        List<Object> matches = beans.entrySet().stream()
                .filter(e -> type.isAssignableFrom(e.getKey()))
                .map(Map.Entry::getValue)
                .toList();
        if (matches.size() == 1) {
            return type.cast(matches.get(0));
        } else if (matches.isEmpty()) {
            throw new NoSuchElementException("No bean found for type: " + type);
        } else {
            throw new IllegalStateException("Multiple beans found for type: " + type + " -> " + matches);
        }
    }

    private void instantiateComponents(Set<Class<?>> componentClasses) throws Exception {
        for (Class<?> clz : componentClasses) {
            Object instance = clz.getDeclaredConstructor().newInstance();
            beans.put(clz, instance);
        }
    }

    private void injectFields() throws IllegalAccessException {
        for (Map.Entry<Class<?>, Object> entry : Collections.unmodifiableSet(beans.entrySet())) {
            Class<?> clz = entry.getKey();
            Object instance = entry.getValue();

            for (Field field : clz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Class<?> fieldType = field.getType();
                    Object dependency = resolveDependency(fieldType);
                    if (dependency == null) {
                        Autowired aut = field.getAnnotation(Autowired.class);
                        if (aut.required()) {
                            throw new IllegalStateException("Required dependency not found for field: "
                                    + field + " in " + clz);
                        } else {
                            continue;
                        }
                    }
                    boolean accessible = field.canAccess(instance);
                    field.setAccessible(true);
                    field.set(instance, dependency);
                    field.setAccessible(accessible);
                }
            }
        }
    }

    private Object resolveDependency(Class<?> type) {
        Object direct = beans.get(type);
        if (direct != null) return direct;
        List<Object> matches = beans.entrySet().stream()
                .filter(e -> type.isAssignableFrom(e.getKey()))
                .map(Map.Entry::getValue)
                .toList();
        if (matches.size() == 1) return matches.getFirst();
        if (matches.size() > 1) {
            throw new IllegalStateException("Multiple candidate beans for type: " + type);
        }
        return null;
    }

    private Set<Class<?>> scanPackageForComponents(String basePackage) throws IOException, ClassNotFoundException, URISyntaxException {
        String path = basePackage.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);
        Set<Class<?>> classes = new HashSet<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            System.out.println("resource = " + resource);
            if ("file".equals(resource.getProtocol())) {
                File directory = new File(resource.toURI());
                if (directory.exists()) {
                    classes.addAll(findClassesInDirectory(basePackage, directory));
                }
            }
        }
        // filter by @Component
        return classes.stream()
                .filter(c -> c.isAnnotationPresent(Component.class))
                .collect(Collectors.toSet());
    }

    private Set<Class<?>> findClassesInDirectory(String packageName, File dir) throws ClassNotFoundException {
        File[] files = dir.listFiles();
        Set<Class<?>> out = new HashSet<>();
        if (files == null) return out;
        for (File file : files) {
            System.out.println("file = " + file);
            if (file.isDirectory()) {
                out.addAll(findClassesInDirectory(packageName + "." + file.getName(), file));
            } else if (file.getName().endsWith(".class")) {
                System.out.println("class found = " + file.getName());
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                Class<?> cls = Class.forName(className);
                out.add(cls);
            }
        }
        return out;
    }
}

