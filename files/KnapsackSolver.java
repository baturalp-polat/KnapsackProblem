import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Contains methods to:
 *   1. Check the trivial case.
 *   2. Generate a random feasible solution.
 *   3. Generate a greedy feasible solution (by profit/weight ratio).
 *   4. Compare random solutions vs. greedy solution 1,000,000 times.
 *   5. (Optional) a parallel version of step 4.
 */
public class KnapsackSolver {

    /**
     * Phase 2:
     * Checks if the sum of all item weights <= capacity.
     * If true, the trivial solution is "take all items."
     *
     * @param instance the knapsack instance
     * @return true if all items can be taken without exceeding capacity
     */
    public static boolean isTrivialCase(KnapsackInstance instance) {
        long totalWeight = instance.getItems().stream()
                                   .mapToLong(Item::weight)
                                   .sum();
        return totalWeight <= instance.getCapacity();
    }

    /**
     * Phase 3(i):
     * Generate a random feasible solution.
     *   - One simple approach: shuffle items, then pick them in random order
     *     until we can no longer fit the next item.
     *
     * @param instance the knapsack instance
     * @return a feasible Solution
     */
    public static Solution generateRandomSolution(KnapsackInstance instance) {
        List<Item> items = new ArrayList<>(instance.getItems());
        Collections.shuffle(items);  // random permutation

        long capacityLeft = instance.getCapacity();
        List<Item> chosen = new ArrayList<>();
        
        for (Item it : items) {
            if (it.weight() <= capacityLeft) {
                chosen.add(it);
                capacityLeft -= it.weight();
            }
            // else skip
        }

        return new Solution(chosen);
    }

    /**
     * Phase 3(ii):
     * Generate a greedy solution by sorting items descending by (profit/weight).
     * Then pick from the top until the bag is full.
     *
     * @param instance the knapsack instance
     * @return a feasible Solution using the greedy approach
     */
    public static Solution generateGreedySolution(KnapsackInstance instance) {
        // Sort by ratio = profit/weight in descending order
        List<Item> items = new ArrayList<>(instance.getItems());
        items.sort(Comparator.comparingDouble(
                it -> -1.0 * ((double)it.profit() / (double)it.weight())));

        long capacityLeft = instance.getCapacity();
        List<Item> chosen = new ArrayList<>();
        for (Item it : items) {
            if (it.weight() <= capacityLeft) {
                chosen.add(it);
                capacityLeft -= it.weight();
            }
        }
        return new Solution(chosen);
    }

    /**
     * Runs the random-solution generator N times, compares each to the greedy solution,
     * collects statistics (min/avg/max), and prints results.
     *
     * @param instance knapsack problem instance
     * @param iterations how many random solutions to generate
     */
    public static void compareRandomToGreedy(KnapsackInstance instance, int iterations) {

        // Get the greedy solution once
        Solution greedySol = generateGreedySolution(instance);
        long greedyProfit = greedySol.getTotalProfit();

        // Track min/avg/max among random solutions
        long minProfit = Long.MAX_VALUE;
        long maxProfit = Long.MIN_VALUE;
        long sumProfit = 0;
        long betterCount = 0; // how many times random beats greedy

        for (int i = 0; i < iterations; i++) {
            Solution randomSol = generateRandomSolution(instance);
            long profit = randomSol.getTotalProfit();

            // Update stats
            if (profit < minProfit) minProfit = profit;
            if (profit > maxProfit) maxProfit = profit;
            sumProfit += profit;

            if (profit > greedyProfit) {
                betterCount++;
            }
        }

        double avgProfit = (double) sumProfit / iterations;

        // Print results
        System.out.println("Greedy solution profit: " + greedyProfit);
        System.out.println("Random solutions (N=" + iterations + "):");
        System.out.println("  Min profit: " + minProfit);
        System.out.println("  Avg profit: " + avgProfit);
        System.out.println("  Max profit: " + maxProfit);

        System.out.println("Random was better than Greedy " 
                            + betterCount + "/" + iterations + " times.");
    }

    /**
     * (Optional) Parallel approach using streams and Atomic variables.
     * 
     * This is a simple demonstration of how you might parallelize
     * one million random solutions. 
     */
    public static void compareRandomToGreedyParallel(KnapsackInstance instance, int iterations) {

        // Greedy baseline
        Solution greedySol = generateGreedySolution(instance);
        long greedyProfit = greedySol.getTotalProfit();

        AtomicLong minProfit = new AtomicLong(Long.MAX_VALUE);
        AtomicLong maxProfit = new AtomicLong(Long.MIN_VALUE);
        AtomicLong sumProfit = new AtomicLong(0);
        AtomicLong betterCount = new AtomicLong(0);

        // We store each random solution profit in parallel
        IntStream.range(0, iterations).parallel().forEach(i -> {
            Solution randomSol = generateRandomSolution(instance);
            long profit = randomSol.getTotalProfit();

            // update min
            long oldMin;
            do {
                oldMin = minProfit.get();
            } while (profit < oldMin && !minProfit.compareAndSet(oldMin, profit));

            // update max
            long oldMax;
            do {
                oldMax = maxProfit.get();
            } while (profit > oldMax && !maxProfit.compareAndSet(oldMax, profit));

            // update sum
            sumProfit.addAndGet(profit);

            // update betterCount
            if (profit > greedyProfit) {
                betterCount.incrementAndGet();
            }
        });

        double avgProfit = (double) sumProfit.get() / iterations;

        // Print results
        System.out.println("Greedy solution profit: " + greedyProfit);
        System.out.println("Random solutions (N=" + iterations + ") PARALLEL:");
        System.out.println("  Min profit: " + minProfit.get());
        System.out.println("  Avg profit: " + avgProfit);
        System.out.println("  Max profit: " + maxProfit.get());
        System.out.println("Random was better than Greedy " 
                            + betterCount.get() + "/" + iterations + " times.");
    }
}
