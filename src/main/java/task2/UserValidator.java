package task2;

public class UserValidator {

    private static boolean validationEnabled = true;

    public static void setValidationEnabled(boolean isEnabled) {
        validationEnabled = isEnabled;
    }

    public static void validate(User user) throws InvalidUserException {
        if (!validationEnabled) {
            return; // Если валидация выключена, то выходим
        }

        validateName(user.getName());
        validateAge(user.getAge());
        validateEmail(user.getEmail());

    }


    public static void validateName(String name) throws InvalidUserException {
        if (name == null || name.length() == 0 || !Character.isUpperCase(name.charAt(0))) {
            throw new InvalidUserException("Имя не должно быть пустым и начинаться с заглавной буквы");
        }
    }

    public static void validateAge(int age) throws InvalidUserException {
        if (age < 18 || age > 100) {
            throw new InvalidUserException("Возраст должен быть в пределах от 18 до 100 лет");
        }
    }

    public static void validateEmail(String email) throws InvalidUserException {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            throw new InvalidUserException("Email должен соответствовать стандартному формату");
        }
    }



}


