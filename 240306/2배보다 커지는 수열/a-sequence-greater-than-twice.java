import java.util.*;
import java.io.*;

public class Main {

    static int N, M;
    static int[][] dp;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        dp = new int[N + 1][M + 1];
        for (int i = 1; i < M + 1; i++) {
            dp[1][i] = i;
        }

        if (N > 1) {
            for (int i = 2; i < N + 1; i++) {
                int start = (int) Math.pow(2, i - 1);
                for (int j = start; j < M + 1; j++) {
                    dp[i][j] = dp[i - 1][j - (int) Math.pow(2, i - 2)] + dp[i][j - (int) Math.pow(2, i - 1)];
                }
            }
        }

        System.out.println(dp[N][M]);
    }
}