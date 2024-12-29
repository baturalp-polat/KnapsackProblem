import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Holds a knapsack instance:
 *   - A list of Items.
 *   - The knapsack capacity.
 * 
 * Also has a static method to parse the instance from a text file.
 */
public class KnapsackInstance {
    private final List<Item> items;
    private final long capacity;

    public KnapsackInstance(List<Item> items, long capacity) {
        this.items = items;
        this.capacity = capacity;
    }

    public List<Item> getItems() {
        return items;
    }

    public long getCapacity() {
        return capacity;
    }

    /**
     * Reads an instance from a file named test.in (or any given filename).
     * 
     * Format expected:
     *   n
     *   id_1 profit_1 weight_1
     *   id_2 profit_2 weight_2
     *   ...
     *   id_n profit_n weight_n
     *   capacity
     */
    public static KnapsackInstance fromFile(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            // First line: number of items
            int n = Integer.parseInt(br.readLine().trim());
            List<Item> items = new ArrayList<>(n);
            
            // Next n lines: each line -> "id profit weight"
            for (int i = 0; i < n; i++) {
                String[] parts = br.readLine().split("\\s+");
                long id     = Long.parseLong(parts[0]);
                long profit = Long.parseLong(parts[1]);
                long weight = Long.parseLong(parts[2]);
                items.add(new Item(id, profit, weight));
            }

            // Last line: capacity
            long capacity = Long.parseLong(br.readLine().trim());

            return new KnapsackInstance(items, capacity);
        }
    }
}
