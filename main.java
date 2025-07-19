import java.util.*;

public class main {
    public static void main(String[] args) {
        List<Process> processes = Arrays.asList(
            new Process("P1", 0, 5, 2),
            new Process("P2", 1, 3, 1),
            new Process("P3", 2, 8, 3),
            new Process("P4", 3, 6, 2)
        );

        Scanner sc = new Scanner(System.in);
        System.out.println("Choose Algorithm:\n1. FCFS\n2. SJF\n3. Round Robin\n4. Priority");
        int choice = sc.nextInt();

        Scheduler scheduler = new Scheduler(cloneProcessList(processes));
        switch (choice) {
            case 1 -> scheduler.fcfs();
            case 2 -> scheduler.sjf();
            case 3 -> {
                System.out.print("Enter Time Quantum: ");
                int q = sc.nextInt();
                scheduler.roundRobin(q);
            }
            case 4 -> scheduler.priorityScheduling();
            default -> System.out.println("Invalid choice.");
        }
    }

    // Cloning for fresh runs
    private static List<Process> cloneProcessList(List<Process> original) {
        List<Process> clone = new ArrayList<>();
        for (Process p : original) {
            clone.add(new Process(p.name, p.arrivalTime, p.burstTime, p.priority));
        }
        return clone;
    }
}
