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
            // System.out.println(command + " 명령어일 때");
            // printDeque();
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
    //     if (cntGift[from] < 2) {
    //         return cntGift[to];
    //     }
    //    int floor = cntGift[from] / 2;
    //    int fromHead = head[from];
    //    int fromTail = head[from];
    //    int cur = head[from];
    //    for (int i = 0; i < floor - 1; i++) {
    //         fromTail = next[cur];
    //         cur = next[cur];
    //    }
    //     int toHead = head[to];
    //     prev[next[fromTail]] = 0;
    //     head[from] = next[fromTail];
    //     prev[toHead] = fromTail;
    //     next[fromTail] = toHead;
    //     head[to] = fromHead;

    //    cntGift[to] += floor;
    //    cntGift[from] -= floor;

    //     if (cntGift[to] == floor) {
    //         tail[to] = fromTail;
    //     }

        int cnt = cntGift[from];
        List<Integer> boxId = new ArrayList<>();
        for (int i = 0; i < cnt / 2; i++) {
            boxId.add(removeHead(from));
        }

        for (int i = boxId.size() - 1; i >= 0; i--) {
            pushHead(to, boxId.get(i));
        }

        return cntGift[to];
    }

    private static int change(int from, int to) {
        int fromHead = removeHead(from);
        int toHead = removeHead(to);
        pushHead(to, fromHead);
        pushHead(from, toHead);
       return cntGift[to];
    }

    private static int removeHead(int num) {
        if (cntGift[num] == 0) {
            return 0;
        }

        if (cntGift[num] == 1) {
            int id = head[num];
            head[num] = tail[num] = 0;
            cntGift[num] = 0;
            return id;
        }

        int hid = head[num];
        int nextHead = next[hid];
        next[hid] = prev[nextHead] = 0;
        cntGift[num]--;
        head[num] = nextHead;

        return hid;
    }

    private static void pushHead(int num, int hid) {
        if (hid == 0) {
            return;
        }

        if (cntGift[num] == 0) {
            head[num] = tail[num] = hid;
            cntGift[num] = 1;
        } else {
            int beforeHead = head[num];
            next[hid] = beforeHead;
            prev[beforeHead] = hid;
            head[num] = hid;
            cntGift[num]++;
        }
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