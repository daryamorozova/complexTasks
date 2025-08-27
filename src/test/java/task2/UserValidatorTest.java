package task2;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ТЕСТ КЕЙСЫ
 *
 *  Флаг валидации установлен в false.
 Все данные пользователя корректны.
 Имя пользователя пустое.
 Имя пользователя начинается с маленькой буквы.
 Имя пользователя равно null.
 Возраст пользователя меньше 18.
 Возраст пользователя больше 100.
 Email пользователя некорректен.
 Email пользователя равен null.


 */

public class UserValidatorTest {

    @Test
    public void testVadationDisabled() {
        UserValidator.setValidationEnabled(false);
        User invalidUser = new User("alice", 17, "invalid-email");

        assertDoesNotThrow(() -> UserValidator.validate(invalidUser));

        UserValidator.setValidationEnabled(true);
    }


    @ParameterizedTest
    @CsvSource({
            "Alice, 25, alice@example.com"           // Позитивный кейс
    })

    public void testVadation(String name, int age, String email) {
        assertDoesNotThrow(() -> UserValidator.validate(new User(name, age, email)));
    }

    @ParameterizedTest
    @CsvSource({
            "'', 25, alice@example.com"              // Имя пустое
    })

    public void testEmptyName(String name, int age, String email) {
        InvalidUserException actualException = assertThrows(InvalidUserException.class, () -> UserValidator.validate(new User(name, age, email)));
        assertEquals("Имя не должно быть пустым и начинаться с заглавной буквы", actualException.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "alice, 25, alice@example.com"           // Имя с маленькой буквы
    })

    public void testNameStartsWithLowercase(String name, int age, String email) {
        InvalidUserException actualException = assertThrows(InvalidUserException.class, () -> UserValidator.validate(new User(name, age, email)));
        assertEquals("Имя не должно быть пустым и начинаться с заглавной буквы", actualException.getMessage());
    }

    @ParameterizedTest
    @NullSource
        public void testNameISNull(String name) {
        InvalidUserException actualException = assertThrows(InvalidUserException.class, () -> UserValidator.validate(new User(name, 25, "alice@example.com")));
        assertEquals("Имя не должно быть пустым и начинаться с заглавной буквы", actualException.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "Alice, 17, alice@example.com"           // Возраст меньше 18
    })
    public void testAgeTooYoung(String name, int age, String email) {
        InvalidUserException actualException = assertThrows(InvalidUserException.class, () -> UserValidator.validate(new User(name, age, email)));
        assertEquals("Возраст должен быть в пределах от 18 до 100 лет", actualException.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "Alice, 101, alice@example.com"           // Возраст больше 100
    })
    public void testAgeTooOld(String name, int age, String email) {
        InvalidUserException actualException = assertThrows(InvalidUserException.class, () -> UserValidator.validate(new User(name, age, email)));
        assertEquals("Возраст должен быть в пределах от 18 до 100 лет", actualException.getMessage());
    }

    @ParameterizedTest
    @NullSource
    public void testEmailISNull(String email) {
        InvalidUserException actualException = assertThrows(InvalidUserException.class, () -> UserValidator.validate(new User("Alice", 25, email)));
        assertEquals("Email должен соответствовать стандартному формату", actualException.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "Alice, 25, invalid-email"               // Некорректный email
    })
    public void testEmailInvalid(String name, int age, String email) {
        InvalidUserException actualException = assertThrows(InvalidUserException.class, () -> UserValidator.validate(new User(name, age, email)));
        assertEquals("Email должен соответствовать стандартному формату", actualException.getMessage());
    }

}