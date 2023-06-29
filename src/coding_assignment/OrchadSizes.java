package coding_assignment;
import java.util.ArrayList;
import java.util.List;
public class OrchadSizes {
	// Function to compute the sizes of all orchards in the matrix
    public static List<Integer> computeOrchardSizes(char[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        List<Integer> sizes = new ArrayList<>();

        // Initialize a boolean matrix to track visited locations
        boolean[][] visited = new boolean[rows][cols];

        // Iterate through each cell in the matrix
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == 'T' && !visited[i][j]) {
                    // If a tree is found and it is not visited yet,
                    // start a depth-first search to find the connected trees
                    int size = dfs(matrix, visited, i, j);
                    sizes.add(size);
                }
            }
        }

        return sizes;
    }

    // Depth-first search to find the connected trees in the orchard
    private static int dfs(char[][] matrix, boolean[][] visited, int row, int col) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        // Check if the current location is within the matrix boundaries and contains a tree
        if (row < 0 || row >= rows || col < 0 || col >= cols || matrix[row][col] != 'T' || visited[row][col]) {
            return 0;
        }

        // Mark the current location as visited
        visited[row][col] = true;

        // Recursive calls to search in all 8 adjacent directions
        int size = 1;
        size += dfs(matrix, visited, row - 1, col);     // up
        size += dfs(matrix, visited, row + 1, col);     // down
        size += dfs(matrix, visited, row, col - 1);     // left
        size += dfs(matrix, visited, row, col + 1);     // right
        size += dfs(matrix, visited, row - 1, col - 1); // top-left
        size += dfs(matrix, visited, row - 1, col + 1); // top-right
        size += dfs(matrix, visited, row + 1, col - 1); // bottom-left
        size += dfs(matrix, visited, row + 1, col + 1); // bottom-right

        return size;
    }

    // Test the program with the provided example input
    public static void main(String[] args) {
        char[][] matrix = {
                {'O', 'T', 'O', 'O'},
                {'O', 'T', 'O', 'T'},
                {'T', 'T', 'O', 'T'},
                {'O', 'T', 'O', 'T'},
                {'O', 'T', 'O', 'T'}
        };

        List<Integer> orchardSizes = computeOrchardSizes(matrix);

        // Print the sizes of all orchards
        for (int size : orchardSizes) {
            System.out.print(size + " ");
        }
    }

}
