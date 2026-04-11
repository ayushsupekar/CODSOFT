import java.io.*;
import java.util.*;

public class StudentManagementSystem {

    private static final String FILE_PATH = "students.txt";
    private static List<Student> students = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadFromFile();
        printBanner();

        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addStudent();
                case "2" -> editStudent();
                case "3" -> removeStudent();
                case "4" -> searchStudent();
                case "5" -> displayAllStudents();
                case "6" -> {
                    saveToFile();
                    System.out.println("\n  Data saved. Goodbye!\n");
                    return;
                }
                default -> System.out.println("  [!] Invalid option. Try again.");
            }
        }
    }

    // ─── MENU ────────────────────────────────────────────────────────────────

    static void printBanner() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║     STUDENT MANAGEMENT SYSTEM  v1.0     ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }

    static void printMenu() {
        System.out.println("\n┌─────────────────────────────┐");
        System.out.println("│         MAIN MENU           │");
        System.out.println("├─────────────────────────────┤");
        System.out.println("│  1. Add Student             │");
        System.out.println("│  2. Edit Student            │");
        System.out.println("│  3. Remove Student          │");
        System.out.println("│  4. Search Student          │");
        System.out.println("│  5. Display All Students    │");
        System.out.println("│  6. Save & Exit             │");
        System.out.println("└─────────────────────────────┘");
        System.out.print("  Choose an option: ");
    }

    // ─── ADD ─────────────────────────────────────────────────────────────────

    static void addStudent() {
        System.out.println("\n── Add New Student ──");

        int roll = readUniqueRollNumber();
        String name  = readNonEmpty("  Name       : ");
        String grade = readGrade();
        String email = readEmail();
        String phone = readPhone();

        students.add(new Student(name, roll, grade, email, phone));
        saveToFile();
        System.out.println("  [✓] Student added successfully!");
    }

    // ─── EDIT ────────────────────────────────────────────────────────────────

    static void editStudent() {
        System.out.println("\n── Edit Student ──");
        Student s = findByRoll("  Enter Roll Number to edit: ");
        if (s == null) return;

        System.out.println("  Leave field blank to keep current value.");

        String name = readOptional("  Name [" + s.getName() + "]: ");
        if (!name.isEmpty()) s.setName(name);

        String grade = readOptionalGrade("  Grade [" + s.getGrade() + "]: ");
        if (!grade.isEmpty()) s.setGrade(grade);

        String email = readOptionalEmail("  Email [" + s.getEmail() + "]: ");
        if (!email.isEmpty()) s.setEmail(email);

        String phone = readOptionalPhone("  Phone [" + s.getPhone() + "]: ");
        if (!phone.isEmpty()) s.setPhone(phone);

        saveToFile();
        System.out.println("  [✓] Student updated successfully!");
    }

    // ─── REMOVE ──────────────────────────────────────────────────────────────

    static void removeStudent() {
        System.out.println("\n── Remove Student ──");
        Student s = findByRoll("  Enter Roll Number to remove: ");
        if (s == null) return;

        System.out.print("  Confirm remove " + s.getName() + "? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            students.remove(s);
            saveToFile();
            System.out.println("  [✓] Student removed.");
        } else {
            System.out.println("  Cancelled.");
        }
    }

    // ─── SEARCH ──────────────────────────────────────────────────────────────

    static void searchStudent() {
        System.out.println("\n── Search Student ──");
        System.out.print("  Enter name or roll number: ");
        String query = scanner.nextLine().trim().toLowerCase();

        List<Student> results = new ArrayList<>();
        for (Student s : students) {
            if (s.getName().toLowerCase().contains(query) ||
                String.valueOf(s.getRollNumber()).equals(query)) {
                results.add(s);
            }
        }

        if (results.isEmpty()) {
            System.out.println("  [!] No students found.");
        } else {
            printTable(results);
        }
    }

    // ─── DISPLAY ALL ─────────────────────────────────────────────────────────

    static void displayAllStudents() {
        System.out.println("\n── All Students ──");
        if (students.isEmpty()) {
            System.out.println("  [!] No students enrolled yet.");
            return;
        }
        printTable(students);
    }

    // ─── TABLE PRINT ─────────────────────────────────────────────────────────

    static void printTable(List<Student> list) {
        String line = "+-------+----------------------+--------+---------------------------+--------------+";
        System.out.println(line);
        System.out.printf("| %-5s | %-20s | %-6s | %-25s | %-12s |%n",
                "Roll", "Name", "Grade", "Email", "Phone");
        System.out.println(line);
        for (Student s : list) System.out.println(s);
        System.out.println(line);
        System.out.println("  Total: " + list.size() + " student(s)");
    }

    // ─── FILE I/O ────────────────────────────────────────────────────────────

    static void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Student s : students) pw.println(s.toFileString());
        } catch (IOException e) {
            System.out.println("  [!] Error saving data: " + e.getMessage());
        }
    }

    static void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Student s = Student.fromFileString(line);
                if (s != null) students.add(s);
            }
        } catch (IOException e) {
            System.out.println("  [!] Error loading data: " + e.getMessage());
        }
    }

    // ─── VALIDATION HELPERS ──────────────────────────────────────────────────

    static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String val = scanner.nextLine().trim();
            if (!val.isEmpty()) return val;
            System.out.println("  [!] This field cannot be empty.");
        }
    }

    static int readUniqueRollNumber() {
        while (true) {
            System.out.print("  Roll Number  : ");
            String input = scanner.nextLine().trim();
            try {
                int roll = Integer.parseInt(input);
                if (roll <= 0) { System.out.println("  [!] Roll number must be positive."); continue; }
                boolean exists = students.stream().anyMatch(s -> s.getRollNumber() == roll);
                if (exists) { System.out.println("  [!] Roll number already exists."); continue; }
                return roll;
            } catch (NumberFormatException e) {
                System.out.println("  [!] Enter a valid numeric roll number.");
            }
        }
    }

    static String readGrade() {
        while (true) {
            System.out.print("  Grade (A/B/C/D/F): ");
            String g = scanner.nextLine().trim().toUpperCase();
            if (g.matches("[ABCDF]")) return g;
            System.out.println("  [!] Grade must be A, B, C, D, or F.");
        }
    }

    static String readEmail() {
        while (true) {
            System.out.print("  Email        : ");
            String e = scanner.nextLine().trim();
            if (e.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) return e;
            System.out.println("  [!] Enter a valid email address.");
        }
    }

    static String readPhone() {
        while (true) {
            System.out.print("  Phone        : ");
            String p = scanner.nextLine().trim();
            if (p.matches("\\d{10}")) return p;
            System.out.println("  [!] Phone must be 10 digits.");
        }
    }

    static String readOptional(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    static String readOptionalGrade(String prompt) {
        while (true) {
            System.out.print(prompt);
            String g = scanner.nextLine().trim().toUpperCase();
            if (g.isEmpty() || g.matches("[ABCDF]")) return g;
            System.out.println("  [!] Grade must be A, B, C, D, or F.");
        }
    }

    static String readOptionalEmail(String prompt) {
        while (true) {
            System.out.print(prompt);
            String e = scanner.nextLine().trim();
            if (e.isEmpty() || e.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) return e;
            System.out.println("  [!] Enter a valid email address.");
        }
    }

    static String readOptionalPhone(String prompt) {
        while (true) {
            System.out.print(prompt);
            String p = scanner.nextLine().trim();
            if (p.isEmpty() || p.matches("\\d{10}")) return p;
            System.out.println("  [!] Phone must be 10 digits.");
        }
    }

    static Student findByRoll(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int roll = Integer.parseInt(input);
                for (Student s : students) {
                    if (s.getRollNumber() == roll) return s;
                }
                System.out.println("  [!] No student found with roll number " + roll + ".");
                return null;
            } catch (NumberFormatException e) {
                System.out.println("  [!] Enter a valid numeric roll number.");
            }
        }
    }
}
