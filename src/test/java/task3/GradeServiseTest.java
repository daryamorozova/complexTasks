package task3;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GradeServiseTest {

    private GradeService<Double> gradeService;

    @BeforeEach
    public void setUp() {
        gradeService = new GradeService<>();
    }

    // Кейс 1: Проверить что изначально коллекция пустая

    @Test
    public void testInitialCollectionIsEmpty() {
        assertEquals(0,  gradeService.calculateAverage("Любой предмет"), 0.01, "Коллекция должна быть пустой");
    }

    // Кейс 2: Добавить одну отрицательную оценку
    @Test
    public void testAddNegativeGrade() {
        assertThrows(InvalidGradeException.class, () -> {
            gradeService.addGrade(new StudentGrade<>("Студент", "Математика", -5.0));
        });
    }


    // Кейс 3: Расчет среднего значения среди 5 оценок по 2 предметам
    @Test
    public void testCalculateAverageGrades() throws InvalidGradeException {
        gradeService.addGrade(new StudentGrade<>("Студент1", "Математика", 80.0));
        gradeService.addGrade(new StudentGrade<>("Студент2", "Физика", 70.0));
        gradeService.addGrade(new StudentGrade<>("Студент3", "Математика", 90.0));
        gradeService.addGrade(new StudentGrade<>("Студент4", "Физика", 85.0));
        gradeService.addGrade(new StudentGrade<>("Студент5", "Математика", 95.0));

        assertEquals(88.33,  gradeService.calculateAverage("Математика"), 0.01);
        assertEquals(77.5,  gradeService.calculateAverage("Физика"), 0.01);
    }

    // Кейс 4: Расчет среднего значения если все оценки равны нулю
    @Test
    public void testCalculateAverageAllZeroGrades() throws InvalidGradeException {
        gradeService.addGrade(new StudentGrade<>("Студент1", "Математика", 0.0));
        gradeService.addGrade(new StudentGrade<>("Студент2", "Математика", 0.0));
        gradeService.addGrade(new StudentGrade<>("Студент3", "Математика", 0.0));

        assertEquals(0.0, gradeService.calculateAverage("Математика"), 0.01);
    }

    // Кейс 5: Проверка потокобезопасности
    @Test
    public void testThreadSafety() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try{
                for (int i = 0; i < 1000; i++) {
                    gradeService.addGrade(new StudentGrade<>("Студент1", "Математика", 85.0));
                }
            }
            catch (InvalidGradeException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try{
                for (int i = 0; i < 1000; i++) {
                    gradeService.addGrade(new StudentGrade<>("Студент2", "Математика", 90.0));
                }
            }
            catch (InvalidGradeException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // Поскольку у нас 2000 оценок, из которых половина по 85 и половина по 90,
        // среднее значение должно быть (85 * 1000 + 90 * 1000) / 2000 = 87.5
        assertEquals(87.5, gradeService.calculateAverage("Математика"), 0.01);
    }



}
