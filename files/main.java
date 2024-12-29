import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String filename = "test.in";  // or pass as an argument, etc.
        try {
            // 1. Read instance
            KnapsackInstance instance = KnapsackInstance.fromFile(filename);

            // 2. Check trivial case
            boolean trivial = KnapsackSolver.isTrivialCase(instance);
            if (trivial) {
                System.out.println("Trivial case: all items fit.");
                // The trivial solution is "take everything"
                long sumProfit = instance.getItems().stream()
                                         .mapToLong(Item::profit)
                                         .sum();
                System.out.println("Profit = " + sumProfit);
            } else {
                System.out.println("Non-trivial case: need to solve properly.");
            }

            // 3. Generate and print the greedy solution
            Solution greedySolution = KnapsackSolver.generateGreedySolution(instance);
            System.out.println("Greedy solution:");
            greedySolution.print();

            // 4. Compare random solutions vs. greedy
            System.out.println("\nComparing random solutions vs. greedy...");
            KnapsackSolver.compareRandomToGreedy(instance, 1_000_000);

            // 5. (Optional) parallel version:
            System.out.println("\nComparing random solutions vs. greedy in PARALLEL...");
            KnapsackSolver.compareRandomToGreedyParallel(instance, 1_000_000);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
