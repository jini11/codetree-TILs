import java.io.*;
import java.util.*;

public class Main {

    static int n, m;
    static LinkedList<Integer>[] belt;
    static int[] gift;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;
        int q = Integer.parseInt(br.readLine());

        // 처음엔 공장 설립
        st = new StringTokenizer(br.readLine());
        st.nextToken();
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        belt = new LinkedList[n];
        gift = new int[m + 1];
        for (int i = 0; i < n; i++) {
            belt[i] = new LinkedList<>();
        }

        for (int i = 0; i < m; i++) {
            int b = Integer.parseInt(st.nextToken()) - 1;
            belt[b].add(i + 1);
            gift[i + 1] = b;
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
            // printDeque();
        }
        System.out.println(sb.toString());
    }

    private static int getBeltInfo(int num) {
        if (belt[num].size() == 0) {
            return -3;
        }
        int a = belt[num].peekFirst();
        int b = belt[num].peekLast();
        int c = belt[num].size();
        // System.out.println(num + "번 벨트 " + a + " " + b + " " + c);
        return a + (2 * b) + (3 * c);
    }

    private static int getGiftInfo(int num) {
        int bIdx = gift[num];
        int a = 0;
        int b = 0;
        int size = belt[bIdx].size() - 1;
        int giftIdx = belt[bIdx].indexOf(num);
        if (belt[bIdx].size() == 1) {
            a = -1;
            b = -1;
        } else if (giftIdx == 0) {
            a = -1;
            b = belt[bIdx].get(giftIdx + 1);
        } else if (giftIdx == size) {
            a = belt[bIdx].get(giftIdx - 1);
            b = -1;
        } else {
            a = belt[bIdx].get(giftIdx - 1);
            b = belt[bIdx].get(giftIdx + 1);
        }
        // System.out.println(num + "번 박스 " + a + " : " + b + " -> " + giftIdx);
        return a + (2 * b);
    }

    private static int divide(int from, int to) {
        int floor = belt[from].size() / 2;

        for (int i = floor - 1; i >= 0; i--) {
            // int fromGift = belt[from].pollFirst();
            int fromGift = belt[from].remove(i);
            belt[to].addFirst(fromGift);
            gift[fromGift] = to;
        }
        return belt[to].size();
    }

    private static int change(int from, int to) {
        if (belt[from].isEmpty() && belt[to].isEmpty()) {
            return belt[to].size();
        }
        if (belt[from].isEmpty()) {
            int toGift = belt[to].pollFirst();
            belt[from].add(toGift);
            gift[toGift] = from;
        } else if (belt[to].isEmpty()) {
            int fromGift = belt[from].pollFirst();
            belt[to].add(fromGift);
            gift[fromGift] = to;
        } else {
            int fromDump = belt[from].pollFirst();
            int toDump = belt[to].pollFirst();
            belt[from].addFirst(toDump);
            belt[to].addFirst(fromDump);
            gift[fromDump] = to;
            gift[toDump] = from;
        }
        return belt[to].size();
    }
    
    private static int moveAll(int from, int to) {
        while (!belt[from].isEmpty()) {
            int fromGift = belt[from].pollLast();
            belt[to].addFirst(fromGift);
            gift[fromGift] = to;
        }
        return belt[to].size();
    }

    private static void printDeque() {
        for (int i = 0; i < n; i++) {
            System.out.println(i +"번째 벨트");
            for (int j = 0; j < belt[i].size(); j++) {
                System.out.print(belt[i].get(j) + " ");
            }
            System.out.println();
        }
        System.out.println("----------------------------------");
    }
}