import java.util.*;
import java.io.*;

public class Main {

    static int N;
    static long B;
    // static List<Integer> lights;
    static int[] lights;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        B = Long.parseLong(st.nextToken());

        // lights = new ArrayList<>();
        lights = new int[N];
        for (int i = 0; i < N; i++) {
            lights[i] = Integer.parseInt(br.readLine());
            // lights.add(Integer.parseInt(br.readLine()));
        }

        while (B-- > 0) {
            int[] temp = new int[N];
            for (int i = N - 1; i >= 0; i--) {
                if (lights[i] == 1) {
                    if (i == N - 1) {
                        temp[0] = lights[0] == 0 ? 1 : 0;
                    } else {
                        temp[i + 1] = lights[i + 1] == 0 ? 1 : 0;
                    }
                } else {
                    if (i == N - 1) {
                        temp[0] = lights[0];
                    } else {
                        temp[i + 1] = lights[i + 1];
                    }
                }
            }
            lights = temp.clone();
        }

        for (Integer light : lights) {
            System.out.println(light);
        }
    }
}