import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

class Student {
    private String name;
    private int age;
    private double grade;

    public Student(String name, int age, double grade) {
        this.name = name;
        this.age = age;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return "Student{name='" + name + "', age=" + age + ", grade=" + grade + '}';
    }
}

class StudentProcessor {
    private List<Student> students;

    public StudentProcessor() {
        students = new ArrayList<>();
    }

    // Чтение данных из файла
    public void loadStudentsFromFile(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                String name = parts[0].trim();
                int age = Integer.parseInt(parts[1].trim());
                double grade = Double.parseDouble(parts[2].trim());
                students.add(new Student(name, age, grade));
            }
        }
    }

    // Добавление нового студента
    public void addStudent(Student student) {
        students.add(student);
    }

    // Сохранение студентов в файл
    public void saveStudentsToFile(String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (Student student : students) {
            writer.write(student.getName() + "," + student.getAge() + "," + student.getGrade());
            writer.newLine();
        }
        writer.close();
    }

    // Сортировка студентов по оценкам
    public List<Student> sortStudentsByGrade() {
        return students.stream()
                .sorted(Comparator.comparingDouble(Student::getGrade).reversed())
                .collect(Collectors.toList());
    }

    // Фильтрация студентов по минимальной оценке
    public List<Student> filterStudentsByGrade(double minGrade) {
        return students.stream()
                .filter(student -> student.getGrade() >= minGrade)
                .collect(Collectors.toList());
    }

    // Печать студентов
    public void printStudents(List<Student> studentList) {
        for (Student student : studentList) {
            System.out.println(student);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentProcessor processor = new StudentProcessor();

        try {
            // Чтение данных из файла
            processor.loadStudentsFromFile("students.txt");
            System.out.println("Данные загружены из файла 'students.txt'.");

            // Демонстрация сортировки
            System.out.println("\nСортировка студентов по оценке:");
            List<Student> sortedStudents = processor.sortStudentsByGrade();
            processor.printStudents(sortedStudents);

            // Фильтрация студентов по минимальной оценке
            System.out.print("\nВведите минимальную оценку для фильтрации: ");
            double minGrade = scanner.nextDouble();
            List<Student> filteredStudents = processor.filterStudentsByGrade(minGrade);
            System.out.println("\nСтуденты с оценкой выше " + minGrade + ":");
            processor.printStudents(filteredStudents);

            // Ввод новых данных от пользователя
            scanner.nextLine();  // Очистка буфера
            System.out.print("\nВведите имя нового студента: ");
            String name = scanner.nextLine();
            System.out.print("Введите возраст нового студента: ");
            int age = scanner.nextInt();
            System.out.print("Введите оценку нового студента: ");
            double grade = scanner.nextDouble();

            Student newStudent = new Student(name, age, grade);
            processor.addStudent(newStudent);
            System.out.println("Новый студент добавлен.");

            // Сохранение данных в файл
            processor.saveStudentsToFile("students.txt");
            System.out.println("Данные сохранены в файл 'students.txt'.");

        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлом: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}