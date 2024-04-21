import java.util.*;
import java.io.*;

public class Main {

    static int N, max;
    static int[] arr;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());
        arr = new int[N];
        for (int i = 0; i < N; i++) {
            arr[i] = Integer.parseInt(br.readLine());
        }

        combi(0, 0, 0);

        System.out.println(max);
    }

    private static void combi(int start, int cnt, int sum) {
        max = Math.max(max, cnt);
        if (cnt == N) {
            return;
        }

        for (int i = start; i < N; i++) {
            if (!isCheck(sum, arr[i])) continue;
            combi(i + 1, cnt + 1, sum + arr[i]);
        }
    }

    private static boolean isCheck(int sum, int num) {
        while (true) {
            if (sum == 0 || num == 0) break;

            int temp = (sum % 10) + (num % 10);
            if (temp > 9) {
                return false;
            }
            sum /= 10;
            num /= 10;
        }

        return true;
    }

}