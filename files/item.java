/**
 * Represents a single Item in the 0-1 Knapsack problem.
 */
public record Item(long id, long profit, long weight) {
    @Override
    public String toString() {
        return String.format("Item{id=%d, profit=%d, weight=%d}", id, profit, weight);
    }
}
