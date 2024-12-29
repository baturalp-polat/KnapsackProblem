import java.util.List;

/**
 * Represents a solution to the knapsack instance:
 *   - which items are chosen,
 *   - total profit,
 *   - total weight.
 */
public class Solution {
    private final List<Item> chosenItems;
    private final long totalProfit;
    private final long totalWeight;

    public Solution(List<Item> chosenItems) {
        this.chosenItems = chosenItems;
        long sumProfit = 0;
        long sumWeight = 0;
        for (Item it : chosenItems) {
            sumProfit += it.profit();
            sumWeight += it.weight();
        }
        this.totalProfit = sumProfit;
        this.totalWeight = sumWeight;
    }

    public List<Item> getChosenItems() {
        return chosenItems;
    }

    public long getTotalProfit() {
        return totalProfit;
    }

    public long getTotalWeight() {
        return totalWeight;
    }

    public void print() {
        System.out.println("Solution: profit=" + totalProfit + ", weight=" + totalWeight);
        // If you want to print the items themselves, you can do so here:
        // for (Item item : chosenItems) {
        //     System.out.println(item);
        // }
    }
}
