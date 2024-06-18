import java.util.*;
import java.io.*;

public class Main {
    
    static final int SIZE = 100;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int N = Integer.parseInt(br.readLine());
        int startR = SIZE;
        int startC = SIZE;
        int endR = 0;
        int endC = 0;
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            startR = Math.min(startR, r);
            startC = Math.min(startC, c);
            endR = Math.max(endR, r);
            endC = Math.max(endC, c);
        }
        System.out.println((endR - startR + 1) * 2 + (endC - startC + 1) * 2);
    }
}