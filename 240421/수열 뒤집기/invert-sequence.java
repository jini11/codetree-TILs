import java.util.*;
import java.io.*;

public class Main {

    static int N, K;
    static int A1, A2, B1, B2;
    static int[] arr;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken()) % 4;
        arr = new int[N + 1];

        for (int i = 1; i < N + 1; i++) {
            arr[i] = i;
        }

        st = new StringTokenizer(br.readLine());
        A1 = Integer.parseInt(st.nextToken());
        A2 = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());
        B1 = Integer.parseInt(st.nextToken());
        B2 = Integer.parseInt(st.nextToken());

        for (int i = 0; i < K; i++) {
            swap(A1, A2);
            swap(B1, B2);
        }
        
        for (int i = 1; i < N + 1; i++) {
            System.out.println(arr[i]);
        }
    }

    private static void swap(int left, int right) {
        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }
}
// 1 2 3 4 5 6 7
// 1 5 4 3 2 6 7  2 ~ 5
// 1 5 7 6 2 3 4  3 ~ 7
// 3 ~ 5

// 1 2 6 7 5 3 4
// 1 2 4 3 5 7 6