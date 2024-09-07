/*
장비의 개수 N, 장비 작업량 C, G, H
장비들이 선호하는 온도 범위

선호 온도를 어떻게 구할 것인가

*/
import java.util.*;
import java.io.*;

public class Main {

    static int N, C, G, H, max;
    static int[][] temp;
    static int[] all;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        G = Integer.parseInt(st.nextToken());
        H = Integer.parseInt(st.nextToken());

        temp = new int[N][2];
        all = new int[N * 2];
        int idx = 0;
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            temp[i][0] = Integer.parseInt(st.nextToken());
            temp[i][1] = Integer.parseInt(st.nextToken());
            all[idx++] = temp[i][0];
            all[idx++] = temp[i][1];
        }

        for (int i = 0; i < all.length; i++) {
            max = Math.max(max, calc(all[i]));
        }
        System.out.println(max);
    }

    private static int calc(int range) {
        int sum = 0;
        for (int[] tem: temp) {
            if (range < tem[0]) {
                sum += C;
            } else if (tem[0] <= range && range <= tem[1]) {
                sum += G;
            } else if (range > tem[1]) {
                sum += H;
            }
        }
        return sum;
    }
}