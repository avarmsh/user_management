package ap.aut;

import ap.aut.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("[L]ogin, [S]ign up: ");
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("S") || input.equalsIgnoreCase("Sign up")) {
            signUp(scanner);
        } else if (input.equalsIgnoreCase("L") || input.equalsIgnoreCase("Login")) {
            login(scanner);
        } else {
            System.out.println("Invalid option");
        }

        scanner.close();
        sessionFactory.close();
    }

    public static void signUp(Scanner scanner) {
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Age: ");
        int age = Integer.parseInt(scanner.nextLine());

        System.out.print("Email: ");
        String email = scanner.nextLine();

        if (isEmailTaken(email)) {
            System.out.println("An account with this email already exists");
            return;
        }

        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (password.length() < 8) {
            System.out.println("Weak password");
            return;
        }

        User user = new User(firstName, lastName, age, email, password);

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }

        System.out.println("Sign-up successful!");
    }

    public static void login(Scanner scanner) {
        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where email = :email and password = :password", User.class);
            query.setParameter("email", email);
            query.setParameter("password", password);
            List<User> users = query.list();

            if (users.isEmpty()) {
                System.out.println("Invalid email or password");
            } else {
                User user = users.get(0);
                System.out.println("Welcome, " + user.getFirstName() + " " + user.getLastName() + "!");
            }
        }
    }

    public static boolean isEmailTaken(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where email = :email", User.class);
            query.setParameter("email", email);
            return !query.list().isEmpty();
        }
    }
}
