package task3;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GradeService<T extends Number> {

    private final List<StudentGrade<T>> grades = new ArrayList<>();

    public synchronized void addGrade(StudentGrade<T> studentGrade) throws InvalidGradeException {
        if (studentGrade.getGrade().doubleValue() < 0) {
            throw new InvalidGradeException("Оценка не может быть отрицательной");
        }
        grades.add(studentGrade);
    }

    public synchronized double calculateAverage(String subject) {
        List<StudentGrade<T>> subjectGrades = grades.stream()
                .filter(grade -> grade.getSubject().equals(subject))
                .collect(Collectors.toList());

        if (subjectGrades.isEmpty()){
            return 0;
        }

        double sum = 0;
        for (StudentGrade<T> grade: subjectGrades) {
            sum += grade.getGrade().doubleValue();
        }

        return sum / subjectGrades.size();

    }


}
