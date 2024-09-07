import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        int n = sc.nextInt();
        int c = sc.nextInt();
        int g = sc.nextInt();
        int h = sc.nextInt();
        
        List<int[]> dev = new ArrayList<>();
        int minta = Integer.MAX_VALUE;
        int maxtb = Integer.MIN_VALUE;

        // 입력받은 구간을 리스트에 저장하고 minta, maxtb 계산
        for (int i = 0; i < n; i++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            
            if (minta == Integer.MAX_VALUE) {
                minta = a;
            } else {
                minta = Math.min(minta, a);
            }
            maxtb = Math.max(maxtb, b);
            
            dev.add(new int[] {a, b});
        }

        Map<Integer, Integer> count = new HashMap<>();

        // 각 구간에 대해 값 증가 처리
        for (int[] pair : dev) {
            int a = pair[0];
            int b = pair[1];
            
            // g를 a~b 구간에 더하기
            for (int num = a; num <= b; num++) {
                count.put(num, count.getOrDefault(num, 0) + g);
            }

            // c를 minta부터 a-1 구간에 더하기
            for (int num = minta; num < a; num++) {
                count.put(num, count.getOrDefault(num, 0) + c);
            }

            // h를 b+1부터 maxtb 구간에 더하기
            for (int num = b + 1; num <= maxtb; num++) {
                count.put(num, count.getOrDefault(num, 0) + h);
            }
        }

        // Map을 값 기준으로 정렬하고 가장 큰 값 찾기
        int maxValue = Collections.max(count.values());

        // 결과 출력
        System.out.println(maxValue);
    }
}