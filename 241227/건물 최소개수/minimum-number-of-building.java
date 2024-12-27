import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int N = Integer.parseInt(br.readLine());
        Stack<Integer> stack = new Stack<>();
        Set<Integer> set = new HashSet<>();
        int res = 0;

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            st.nextToken();
            int height = Integer.parseInt(st.nextToken());
            if (height == 0) {
                set = new HashSet<>();
                res += stack.size();
                stack = new Stack<>();
                continue;
            }
            if (!set.contains(height)) {
                if (stack.isEmpty() || stack.peek() <= height) {
                    stack.push(height);
                } else if (stack.peek() > height) {
                    res += stack.size();
                    stack = new Stack<>();
                }
            }
        }

        System.out.println(res + stack.size());
    }
}