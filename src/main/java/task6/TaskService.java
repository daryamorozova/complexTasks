package task6;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskService<T> {

    private final List<Task<T>> tasks = new ArrayList<Task<T>>();

    /**
     Optional — это класс в Java, который представляет собой контейнер для значения, которое может быть null или иметь определённое значение.
     Он был введён в Java 8 как способ избежать NullPointerException и сделать код более читаемым и безопасным.
     isPresent() — проверяет, содержит ли Optional значение (возвращает true, если значение присутствует, и false, если оно отсутствует);
     */

    public synchronized void addTask(Task<T> task) {

        Optional<Task<T>> existingTask = tasks.stream()
            .filter(t -> t.getId().equals(task.getId()))
                .findFirst();

        if (existingTask.isPresent()) {
            System.out.println("Задача с ID " + task.getId() + " уже существует");
        } else {
            tasks.add(task);
            System.out.println("Задача добавлена " + task);
        }
    }

    public synchronized void removeTask(T id) {
        boolean removed = tasks.removeIf(t -> t.getId().equals(id));
        if (removed) {
            System.out.println("Задача с ID " + id + " удалена");
        } else {
            System.out.println("Задача с ID " + id + " не найдена");
        }
    }

    /**
     equalsIgnoreCase — это метод в Java, который используется для сравнения двух строк на равенство,
     игнорируя при этом регистр букв (то есть, большие и маленькие буквы считаются одинаковыми).
     Этот метод возвращает true, если строки равны, не учитывая регистр, и false, если они различаются.
     */

    public List<Task<T>> filterTasksByStatus(String status) {
        return tasks.stream()
                .filter(task -> task.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    public List<Task<T>> filterTasksByPriority(String priority) {
        return tasks.stream()
                .filter(task -> task.getPriority().equalsIgnoreCase(priority))
                .collect(Collectors.toList());
    }

    /**
     * В качестве параметра Comparator используется лямбда-выражение, которое сравнивает даты задач task1 и task2.
     * Если параметр ascending равен true, то задачи сортируются по возрастанию дат.
     * В этом случае метод compareTo объекта task1.getDate() сравнивает дату задачи task1 с датой задачи task2.
     * Если дата задачи task1 меньше даты задачи task2, то возвращается отрицательное число, если равна — ноль, если больше — положительное
     */
    public List<Task<T>> sortTasksByDate(boolean ascending) {
        return tasks.stream()
                .sorted((task1, task2) -> ascending ? task1.getDate().compareTo(task2.getDate())
                        : task2.getDate().compareTo(task1.getDate()))
                        .collect(Collectors.toList());
    }


}
