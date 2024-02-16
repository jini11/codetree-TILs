import java.io.*;
import java.util.*;

public class Main {

    public static final int MAX_M = 100000;
    public static final int MAX_N = 100000;

    static int n, m;
    static LinkedList<Integer>[] belt;
    
    // 벨트
    public static int[] head = new int[MAX_N];
    public static int[] tail = new int[MAX_N];
    public static int[] cntGift = new int[MAX_N];

    // 선물
    public static int[] prev = new int[MAX_M + 1];
    public static int[] next = new int[MAX_M + 1];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;
        int q = Integer.parseInt(br.readLine());

        st = new StringTokenizer(br.readLine());
        st.nextToken();
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        belt = new LinkedList[n];
        // gift = new int[m + 1];
        for (int i = 0; i < n; i++) {
            belt[i] = new LinkedList<>();
        }

        for (int i = 0; i < m; i++) {
            int b = Integer.parseInt(st.nextToken()) - 1;
            belt[b].add(i + 1);
        }

        // 링크드 리스트 초기값 세팅
        for (int i = 0; i < n; i++) {
            if (belt[i].size() == 0) continue;

            head[i] = belt[i].get(0);
            tail[i] = belt[i].get(belt[i].size() - 1);

            cntGift[i] = belt[i].size();

            for (int j = 0; j < belt[i].size() - 1; j++) {
                prev[belt[i].get(j + 1)] = belt[i].get(j);
                next[belt[i].get(j)] = belt[i].get(j + 1);
            }
        }

        
        // printDeque();
        for (int i = 0; i < q - 1; i++) {
            st = new StringTokenizer(br.readLine());
            int command = Integer.parseInt(st.nextToken());
            switch (command) {
                case 200:
                    int from = Integer.parseInt(st.nextToken()) - 1;
                    int to = Integer.parseInt(st.nextToken()) - 1;
                    sb.append(moveAll(from, to)).append("\n");
                    break;
                case 300:
                    from = Integer.parseInt(st.nextToken()) - 1;
                    to = Integer.parseInt(st.nextToken()) - 1;
                    sb.append(change(from, to)).append("\n");
                    break;
                case 400:
                    from = Integer.parseInt(st.nextToken()) - 1;
                    to = Integer.parseInt(st.nextToken()) - 1;
                    sb.append(divide(from, to)).append("\n");
                    break;
                case 500:
                    int num = Integer.parseInt(st.nextToken());
                    sb.append(getGiftInfo(num)).append("\n");
                    break;
                case 600:
                    num = Integer.parseInt(st.nextToken()) - 1;
                    sb.append(getBeltInfo(num)).append("\n");
                    break;
            }
            // if (i < 5) {
            // System.out.println(command + " 명령어일 때");
            // printDeque();
            // }
        }
        System.out.println(sb.toString());
    }

    private static int getBeltInfo(int num) {
        if (cntGift[num] == 0) {
            return -3;
        }

        int a = head[num] == 0 ? -1 : head[num];
        int b = tail[num] == 0 ? -1 : tail[num];

        return a + 2 * b + 3 * cntGift[num];
    }

    private static int getGiftInfo(int num) {
        int a = prev[num] == 0 ? -1 : prev[num];
        int b = next[num] == 0 ? -1 : next[num];
        return a + 2 * b;
    }

    private static int divide(int from, int to) {
        if (cntGift[from] < 2) {
            return cntGift[to];
        }
       int floor = cntGift[from] / 2;
       int fromHead = head[from];
       int fromTail = head[from];
       int cur = head[from];
       for (int i = 0; i < floor - 1; i++) {
            fromTail = next[cur];
            cur = next[cur];
       }
        int toHead = head[to];
        prev[next[fromTail]] = 0;
        head[from] = next[fromTail];
        prev[toHead] = fromTail;
        next[fromTail] = toHead;
        head[to] = fromHead;

       cntGift[to] += floor;
       cntGift[from] -= floor;

        return cntGift[to];
    }

    private static int change(int from, int to) {
       if (cntGift[from] == 0 && cntGift[to] == 0) {
            return 0;
       }
       if (cntGift[from] == 0) {
            int toHead = head[to];

            head[from] = toHead;

            head[to] = next[head[from]];

            prev[head[to]] = 0;
            next[head[from]] = 0;
            tail[from] = head[from];

            cntGift[from] = 1;
            cntGift[to] -= 1;
       } else if (cntGift[to] == 0) {
            int fromHead = head[from];

            head[to] = fromHead;

            head[from] = next[head[to]];
            next[fromHead] = 0;

            prev[head[from]] = 0;
            next[head[to]] = 0;
            tail[to] = head[to];

            cntGift[to] = 1;
            cntGift[from] -= 1;
       } else {
            int fromHead = head[from];
            int toHead = head[to];

            prev[next[fromHead]] = toHead;
            prev[next[toHead]] = fromHead;

            int temp = next[fromHead];
            next[fromHead] = next[toHead];
            next[toHead] = temp;

            head[from] = toHead;
            head[to] = fromHead;

       }
       return cntGift[to];
    }
    
    private static int moveAll(int from, int to) {
        // from에 물건이 없는 경우
        if (cntGift[from] == 0) {
            return cntGift[to];
        } 

        // to에 물건이 없는 경우
        if (cntGift[to] == 0) {
            head[to] = head[from];
            tail[to] = tail[from];
        } else {
            int toHead = head[to];
            int fromTail = tail[from];

            head[to] = head[from];
            
            next[fromTail] = toHead;
            prev[toHead] = fromTail;
        }

        head[from] = tail[from] = 0;

        // 상자 개수 갱신
        cntGift[to] += cntGift[from];
        cntGift[from] = 0;

        return cntGift[to];
    }

    private static void printDeque() {
        for (int i = 0; i < n; i++) {
            System.out.println(i +"번째 벨트");
            System.out.print(head[i] + " ");
            int before = head[i];
            for (int j = 1; j < cntGift[i]; j++) {
                System.out.print(next[before] + " ");
                before = next[before];
            }

            System.out.println();
        }
        System.out.println("----------------------------------");
    }
}