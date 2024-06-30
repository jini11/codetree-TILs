import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        int N = Integer.parseInt(br.readLine());
        int num = 100000;
        int size = 20002;
        int bitSize = size * size;
        long[] bitset = new long[(bitsetSize + 63) / 64];
        // boolean[][] map = new boolean[20002][20002];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int startR = Integer.parseInt(st.nextToken()) + num;
            int startC = Integer.parseInt(st.nextToken()) + num;
            int endR = Integer.parseInt(st.nextToken()) + num;
            int endC = Integer.parseInt(st.nextToken()) + num;
            for (int k = startR; k <= startC; k++) {
                for (int j = endR; j <= endC; j++) {
                    // map[k][j] = true;
                    int index = k * size + j;
                    bitset[index / 64] |= (1L << (index % 64));
                }
            }
        }

        int sum = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                // if (map[i][j]) sum++;
                sum += Long.bitCount(bitset[i]);
            }
        }

        System.out.println(sum);
    }
}