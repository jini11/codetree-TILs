import java.util.*;
import java.io.*;

public class Main {

    static int K, cnt;
    static final int N = 5;
    static boolean[][] visited = new boolean[N][N];

    static int[] dr = {1, 0, -1, 0};
    static int[] dc = {0, 1, 0, -1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        K = Integer.parseInt(br.readLine());

        for (int i = 0; i < K; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int row = Integer.parseInt(st.nextToken()) - 1;
            int col = Integer.parseInt(st.nextToken()) - 1;
            visited[row][col] = true;
        }

        visited[0][0] = true;
        visited[4][4] = true;
        backtracking(0, 0, N - 1, N - 1, true);
        
        System.out.println(cnt);
    }

    private static void backtracking(int aRow, int aCol, int bRow, int bCol, boolean flag) {
        if (flag) {
            for (int i = 0; i < 4; i++) {
                int naRow = aRow + dr[i];
                int naCol = aCol + dc[i];

                if (isOut(naRow, naCol) || visited[naRow][naCol]) continue;
                visited[naRow][naCol] = true;
                backtracking(naRow, naCol, bRow, bCol, !flag);
                visited[naRow][naCol] = false;
            }
        } else {
            for (int j = 0; j < 4; j++) {
                int nbRow = bRow + dr[j];
                int nbCol = bCol + dc[j];

                if (isOut(nbRow, nbCol)) continue;

                if (aRow == nbRow && aCol == nbCol) {
                    if (isCorrect()) {
                        cnt++;
                        return;
                    }
                    continue;
                }   
                if (visited[nbRow][nbCol]) continue;
                visited[nbRow][nbCol] = true;
                backtracking(aRow, aCol, nbRow, nbCol, !flag);
                visited[nbRow][nbCol] = false;
            }
        }
    }

    private static boolean isCorrect() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (!visited[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isOut(int nr, int nc) {
        if (nr < 0 || nc < 0 || nr >= N || nc >= N ) {
            return true;
        }
        return false;
    }
}