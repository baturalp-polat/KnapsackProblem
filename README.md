# Knapsack Problem (0–1 KP)

This repository contains a simple Java implementation of the **0–1 Knapsack** problem. The main objectives are:

- **Phase 1**: Parse a file describing an instance of the knapsack problem.  
- **Phase 2**: Check if the total weight of all items is less than or equal to the bag’s capacity (the trivial case).  
- **Phase 3**: Implement  
  1. A **random solution** generator,  
  2. A **greedy solution** generator (based on profit/weight ratio).  
- **Phase 4**: Generate 1,000,000 (or configurable) random solutions, gather min/avg/max profit, and see how often a random solution outperforms the greedy solution.  
- **Phase 5** (Optional): Demonstrate a **parallelized** version.

## File Descriptions

- **Item.java**  
  Defines a `record` that holds an item’s `id`, `profit`, and `weight`.

- **KnapsackInstance.java**  
  Holds the list of items and the knapsack capacity. Also includes a static method to read an instance from a `test.in` file.

- **Solution.java**  
  Encapsulates a chosen subset of items, providing total profit and weight.

- **KnapsackSolver.java**  
  Contains core methods for  
  1. Checking the trivial case,  
  2. Generating random solutions,  
  3. Generating a greedy solution,  
  4. Repeatedly comparing random solutions vs. a greedy solution,  
  5. An optional parallel approach.

- **Main.java**  
  A driver class tying everything together: reads the file, checks trivial vs. non-trivial, prints the greedy solution, and runs the random-solution comparisons.

## How to Compile and Run

1. **Clone** or download this repository.  
2. Ensure you have a **Java JDK** installed (Java 17+ recommended).
3. Prepare your **`test.in`** file (see below for format) in the **same directory** or provide the full path when running.

### Step-by-Step

1. **Compile** the source files:

   ```bash
   javac Item.java KnapsackInstance.java Solution.java KnapsackSolver.java Main.java

For running this script, **test.in** file should be in the same directory with the other files.
