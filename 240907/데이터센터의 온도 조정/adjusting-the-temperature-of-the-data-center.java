/*
장비의 개수 N, 장비 작업량 C, G, H
장비들이 선호하는 온도 범위

선호 온도를 어떻게 구할 것인가

*/
import java.util.*;
import java.io.*;

public class Main {

    static int N, C, G, H, min, max;
    static List<int[]> temp;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        G = Integer.parseInt(st.nextToken());
        H = Integer.parseInt(st.nextToken());

        // temp = new ArrayList<>();
        min = Integer.MAX_VALUE;
        max = Integer.MIN_VALUE;

        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            // min = Math.min(min, a);
            // max = Math.max(max, b);
            if (a == 0) {
                if (map.containsKey(a)) {
                    map.put(a, map.get(a) + G);
                } else {
                    map.put(a, G);
                }
            } else {
                if (map.containsKey(a)) {
                    map.put(a, map.get(a) + G - C);
                } else {
                    map.put(s, G - C);
                }
            }
            if (s != 0) {
                if (map.containsKey(0)) {
                    map.put(0, map.get(0) + C);
                } else {
                    map.put(0, C);
                }
            }

            if (map.containsKey(b + 1)) {
                map.put(b + 1, map.get(b + 1) + H - G);
            } else {
                map.put(b + 1, H - G);
            }
        }
        
        // Map<Integer, Integer> map = new HashMap<>();
        
        // for (int[] tem : temp) {
        //     for (int i = min; i < tem[0]; i++) {
        //         map.put(i, map.getOrDefault(i, 0) + C);
        //     }
        //     for (int i = tem[0]; i <= tem[1]; i++) {
        //         map.put(i, map.getOrDefault(i, 0) + G);
        //     }
        //     for (int i = tem[1] + 1; i <= max; i++) {
        //         map.put(i, map.getOrDefault(i, 0) + H);
        //     }
        // }
        // int res = 0;
        // for (int num : map.values()) {
        //     res = Math.max(res, num);
        // }
        // System.out.println(res);
        temp = new ArrayList<>(map.keySet());
        Collections.sort(temp);
        int sum = 0;
        int res = 0;
        for (int key : temp) {
            sum += map.get(key);
            res = Math.max(res, sum);
        }
        System.out.println(res);
    }
}