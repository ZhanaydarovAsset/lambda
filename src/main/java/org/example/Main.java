package org.example;


import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) {
        List<Department> departments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            departments.add(new Department("Department #" + i));
        }

        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            persons.add(new Person(
                    "Person #" + i,
                    ThreadLocalRandom.current().nextInt(20, 61),
                    ThreadLocalRandom.current().nextInt(20_000, 100_000) * 1.0,
                    departments.get(ThreadLocalRandom.current().nextInt(departments.size()))
            ));
        }
//        printNamesOrdered(persons);
//        System.out.println(printDepartmentOldestPerson(persons));
//        System.out.println(findFirstPersons(persons));
        System.out.println(findTopDepartment(persons));
    }

    /**
     * Вывести на консоль отсортированные (по алфавиту) имена персонов
     */
    public static void printNamesOrdered(List<Person> persons) {
        persons.stream()
                .sorted()
                .forEach(System.out::println);
    }

    /**
     * В каждом департаменте найти самого взрослого сотрудника.
     * Вывести на консоль мапипнг department -> personName
     * Map<Department, Person>
     */
    public static Map<Department, Person> printDepartmentOldestPerson(List<Person> persons) {
        return persons.stream()
                .sorted(Comparator.comparingInt(Person::getAge).reversed())
                .collect(Collectors.toMap(
                        Person::getDepartment,
                        Function.identity(),
                        (existing, replacement) -> existing
                ));
    }

    /**
     * Найти 10 первых сотрудников, младше 30 лет, у которых зарплата выше 50_000
     */
    public static List<Person> findFirstPersons(List<Person> persons) {
        return persons.stream()
                .filter(it -> it.getAge() < 30)
                .filter(it -> it.getSalary() > 50_000)
                .sorted(Comparator.comparingDouble(Person::getSalary).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * Найти депаратмент, чья суммарная зарплата всех сотрудников максимальна
     */
    public static Optional<Department> findTopDepartment(List<Person> persons) {
        return persons.stream()
                .collect(Collectors.groupingBy(
                        Person::getDepartment,
                        Collectors.summingDouble(Person::getSalary)
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

}