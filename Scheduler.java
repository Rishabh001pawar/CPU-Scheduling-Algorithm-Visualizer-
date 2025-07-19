import java.util.*;

public class Scheduler {
    List<Process> processes;

    public Scheduler(List<Process> processes) {
        this.processes = processes;
    }

    // FCFS
    public void fcfs() {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int currentTime = 0;
        System.out.println("Gantt Chart (FCFS):");

        for (Process p : processes) {
            if (currentTime < p.arrivalTime) {
                currentTime = p.arrivalTime;
            }
            System.out.print("| " + p.name + " ");
            currentTime += p.burstTime;
            p.completionTime = currentTime;
            p.turnaroundTime = p.completionTime - p.arrivalTime;
            p.waitingTime = p.turnaroundTime - p.burstTime;
        }
        System.out.println("|");
        printMetrics("FCFS");
    }

    // SJF (Non-preemptive)
    public void sjf() {
        List<Process> queue = new ArrayList<>(processes);
        queue.sort(Comparator.comparingInt(p -> p.arrivalTime));
        List<Process> readyQueue = new ArrayList<>();
        int currentTime = 0;

        System.out.println("Gantt Chart (SJF):");

        while (!queue.isEmpty() || !readyQueue.isEmpty()) {
            for (Iterator<Process> it = queue.iterator(); it.hasNext(); ) {
                Process p = it.next();
                if (p.arrivalTime <= currentTime) {
                    readyQueue.add(p);
                    it.remove();
                }
            }

            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            readyQueue.sort(Comparator.comparingInt(p -> p.burstTime));
            Process p = readyQueue.remove(0);

            System.out.print("| " + p.name + " ");
            currentTime += p.burstTime;
            p.completionTime = currentTime;
            p.turnaroundTime = p.completionTime - p.arrivalTime;
            p.waitingTime = p.turnaroundTime - p.burstTime;
        }
        System.out.println("|");
        printMetrics("SJF");
    }

    // Round Robin
    public void roundRobin(int quantum) {
        Queue<Process> queue = new LinkedList<>();
        int currentTime = 0;
        int n = processes.size();
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        List<Process> remaining = new ArrayList<>(processes);

        System.out.println("Gantt Chart (RR):");

        while (!remaining.isEmpty() || !queue.isEmpty()) {
            while (!remaining.isEmpty() && remaining.get(0).arrivalTime <= currentTime) {
                queue.add(remaining.remove(0));
            }

            if (queue.isEmpty()) {
                currentTime++;
                continue;
            }

            Process p = queue.poll();
            System.out.print("| " + p.name + " ");
            int execTime = Math.min(p.remainingTime, quantum);
            currentTime += execTime;
            p.remainingTime -= execTime;

            while (!remaining.isEmpty() && remaining.get(0).arrivalTime <= currentTime) {
                queue.add(remaining.remove(0));
            }

            if (p.remainingTime > 0) {
                queue.add(p);
            } else {
                p.completionTime = currentTime;
                p.turnaroundTime = p.completionTime - p.arrivalTime;
                p.waitingTime = p.turnaroundTime - p.burstTime;
            }
        }
        System.out.println("|");
        printMetrics("Round Robin");
    }

    // Priority Scheduling (Non-preemptive)
    public void priorityScheduling() {
        List<Process> queue = new ArrayList<>(processes);
        queue.sort(Comparator.comparingInt(p -> p.arrivalTime));
        List<Process> readyQueue = new ArrayList<>();
        int currentTime = 0;

        System.out.println("Gantt Chart (Priority):");

        while (!queue.isEmpty() || !readyQueue.isEmpty()) {
            for (Iterator<Process> it = queue.iterator(); it.hasNext(); ) {
                Process p = it.next();
                if (p.arrivalTime <= currentTime) {
                    readyQueue.add(p);
                    it.remove();
                }
            }

            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            readyQueue.sort(Comparator.comparingInt(p -> p.priority));
            Process p = readyQueue.remove(0);

            System.out.print("| " + p.name + " ");
            currentTime += p.burstTime;
            p.completionTime = currentTime;
            p.turnaroundTime = p.completionTime - p.arrivalTime;
            p.waitingTime = p.turnaroundTime - p.burstTime;
        }
        System.out.println("|");
        printMetrics("Priority");
    }

    private void printMetrics(String algorithm) {
        System.out.println("\n--- " + algorithm + " Metrics ---");
        double totalTurnaround = 0, totalWaiting = 0;

        for (Process p : processes) {
            System.out.printf("Process %s | TAT: %d | WT: %d%n", p.name, p.turnaroundTime, p.waitingTime);
            totalTurnaround += p.turnaroundTime;
            totalWaiting += p.waitingTime;
        }

        System.out.printf("Avg Turnaround Time: %.2f\n", totalTurnaround / processes.size());
        System.out.printf("Avg Waiting Time   : %.2f\n", totalWaiting / processes.size());
    }
}
