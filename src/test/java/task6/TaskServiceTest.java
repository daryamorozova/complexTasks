package task6;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskServiceTest {

    private TaskService<String> taskService;
    private Task<String> task1;
    private Task<String> task2;
    private Task<String> task3;
    private Task<String> task4;
    private Task<String> task5;
    private Task<String> task6;

    @BeforeEach
    public void setUp() {
        taskService = new TaskService<>();
        task1 = new Task<>("ID1", "В работе", "Высокий", LocalDate.of(2025, 1, 5));
        task2 = new Task<>("ID2", "Анализ", "Низкий", LocalDate.of(2025, 2, 5));
        task3 = new Task<>("ID3", "Анализ", "ВЫСОКИЙ", LocalDate.of(2025, 2, 6));
        task4 = new Task<>("ID4", "тестирование", "высокий", LocalDate.of(2025, 3, 7));
        task5 = new Task<>("ID5", "Приостановлено", "Средний", LocalDate.of(2025, 4, 25));
        task6 = new Task<>("ID6", "Тестирование", "Низкий", LocalDate.of(2025, 7, 8));
    }

    @Test
    public void testAddNewTask() {
        taskService.addTask(task1);
        assertEquals(1, taskService.sortTasksByDate(true).size());
        System.out.println("Тест добавления новой задачи: " + taskService.sortTasksByDate(true));
    }

    @Test
    public void testAddTaskWithExistingId() {
        taskService.addTask(task1);
        taskService.addTask(task1);
        assertEquals(1, taskService.sortTasksByDate(true).size());
        System.out.println("Тест добавления задачи с существующим ID: " + taskService.sortTasksByDate(true));
    }

    @Test
    public void testRemoveTaskWithExistingId() {
        taskService.addTask(task1);
        taskService.removeTask("ID1");
        assertEquals(0, taskService.sortTasksByDate(true).size());
        System.out.println("Тест удаления задачи с существующим ID");
    }

    @Test
    public void testRemoveTaskWithNonExistingId() {
        taskService.addTask(task1);
        taskService.removeTask("ID2");
        assertEquals(1, taskService.sortTasksByDate(true).size());
        System.out.println("Тест удаления задачи с несуществующим ID");
    }

    @Test
    public void testFilterTasksByStatus() {
        taskService.addTask(task4);
        taskService.addTask(task6);
        List<Task<String>> result = taskService.filterTasksByStatus("тестирование");
        assertEquals(2, result.size());
        System.out.println("Тест фильтрации задач по статусу: " + result);
    }

    @Test
    public void testFilterTasksByPriority() {
        taskService.addTask(task3);
        taskService.addTask(task4);
        List<Task<String>> result = taskService.filterTasksByPriority("высокий");
        assertEquals(2, result.size());
        System.out.println("Тест фильтрации задач по приоритету: " + result);
    }

    @Test
    public void testSortTasksByDateDescending() {
        taskService.addTask(task1);
        taskService.addTask(task2);
        taskService.addTask(task3);

        List<Task<String>> sortedTasks = taskService.sortTasksByDate(false);
        assertEquals(task3, sortedTasks.get(0));

        System.out.println("Тест сортировки задач по дате по убыванию: " + sortedTasks);
    }

    @Test
    public void testSortTasksByDateAscending() {
        taskService.addTask(task1);
        taskService.addTask(task2);
        taskService.addTask(task3);

        List<Task<String>> sortedTasks = taskService.sortTasksByDate(true);
        assertEquals(task1, sortedTasks.get(0));

        System.out.println("Тест сортировки задач по дате по возрастанию: " + sortedTasks);
    }

    @Test
    public void testConcurrentAddTasks() throws InterruptedException {
        Runnable taskAdder = () -> {
            for (int i = 0; i < 1000; i++) {
                taskService.addTask(new Task<>("ID" + i, "В работе", "Средний", LocalDate.now()));
            }
        };

        Thread thread1 = new Thread(taskAdder);
        Thread thread2 = new Thread(taskAdder);

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        // Проверяем, что добавлено 1000 уникальных задач (несмотря на попытку добавить 2000)
        assertEquals(1000, taskService.sortTasksByDate(true).size());
        System.out.println("Тест потокобезопасности метода добавления задач: " + taskService.sortTasksByDate(true).size());
    }

    @Test
    public void testConcurrentRemoveTasks() throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            taskService.addTask(new Task<>("ID" + i, "Тестирование", "Высокий", LocalDate.now()));
        }

        Runnable taskRemover = () -> {
            for (int i = 0; i < 1000; i++) {
                taskService.removeTask("ID" + i);
            }
        };

        Thread thread1 = new Thread(taskRemover);
        Thread thread2 = new Thread(taskRemover);

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        // Проверяем, что все задачи были удалены
        assertEquals(0, taskService.sortTasksByDate(true).size());
        System.out.println("Тест потокобезопасности метода удаления задач: " + taskService.sortTasksByDate(true).size());
    }

}
