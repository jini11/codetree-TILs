import java.util.*;
import java.io.*;

public class Main {

    static int N, M, P, C, D;
    static int rooR, rooC, santaCnt;
    static State[] santa;
    static int[] score;
    static int[][] map;

    static int[] dr = {-1, 0, 1, 0};
    static int[] dc = {0, 1, 0, -1};

    static class State implements Comparable<State> {
        int num, r, c, k;
        boolean alive;
        public State (int num, int r, int c, int k, boolean alive) {
            this.num = num;
            this.r = r;
            this.c = c;
            this.k = k;
            this.alive = alive;
        }
        public int compareTo(State o) {
            int a = (int) Math.pow(this.r - rooR, 2) + (int) Math.pow(this.c - rooC, 2);
            int b = (int) Math.pow(o.r - rooR, 2) + (int) Math.pow(o.c - rooC, 2);
            if (a == b) {
                if (this.r == o.r) {
                    return o.c - this.c;
                }
                return o.r - this.r;
            }
            return a - b;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        P = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        D = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        rooR = Integer.parseInt(st.nextToken()) - 1;
        rooC = Integer.parseInt(st.nextToken()) - 1;
        santa = new State[P + 1];
        score = new int[P + 1];
        map = new int[N][N];

        int[][] com = new int[P][3];
        for (int i = 1; i < P + 1; i++) {
            st = new StringTokenizer(br.readLine());
            int num = Integer.parseInt(st.nextToken());
            int r = Integer.parseInt(st.nextToken()) - 1;
            int c = Integer.parseInt(st.nextToken()) - 1;
            // santa[i] = new State(num, r, c, 0, true);
            com[i-1][0] = num;
            com[i-1][1] = r;
            com[i-1][2] = c;
            map[r][c] = num;
        }

        Arrays.sort(com, new Comparator<int[]>() {
            public int compare(int[] o1, int[] o2) {
                return o1[0] - o2[0];
            }
        });

        for (int i = 0; i < com.length; i++) {
            santa[i + 1] = new State(com[i][0], com[i][1], com[i][2], 0, true);
        }
        // System.out.println(" -- init --");
        // printMap();

        for (int i = 0; i < M; i++) {
            if (santaCnt >= P) {
                break;
            }

            for (int j = 1; j < P + 1; j++) { // 기절했던 산타 깨우기
                if (santa[j].k >= 2) {
                    santa[j].k = 0;
                } else if (santa[j].k == 1) {
                    santa[j].k += 1;
                }
            }

            // 루돌프 움직이기
            moveRoo();

            // System.out.println(" -- move Roo --");
            // printMap();
            // 산타 움직이기
            moveSanta();
            // System.out.println(" -- move Santa --");
            // printMap();
            for (int j = 1; j < P + 1; j++) { // 추가 점수 
                if (santa[j].alive) {
                    score[j] += 1;
                }
            }
            
            // printScore();
            // printMap();
        }

        for (int i = 1; i < P + 1; i++) {
            System.out.print(score[i] + " ");
        }
    }

    private static void printScore() {
        for (int i = 1; i < P + 1; i++) {
            System.out.print(score[i] + " ");
        }
        System.out.println();
    }

    private static void moveRoo() {
        // 가장 가까운 산타 찾기
        PriorityQueue<State> pq = new PriorityQueue<>();
        for (int i = 1; i < P + 1; i++) {
            if (santa[i].alive) {
                pq.add(santa[i]);
            }
        }
        State near = pq.poll(); // 가장 가까운 산타

        // 가장 가까워지는 칸으로 이동(밖으로 가면 안됨)
        int disR = near.r - rooR;
        int disC = near.c - rooC;
        disR = getDir(disR);
        disC = getDir(disC);
        // 여기 isOut 처리 해야되나...?
        
        int nr = rooR + disR;
        int nc = rooC + disC;
        // 해당 칸에 산타가 있으면 충돌 함수 호출
        if (map[nr][nc] != 0) {
            // 해당 산타 기절 처리
            int num = map[nr][nc];
            santa[num].k = 1;
            map[nr][nc] = 0;
            score[num] += C;
            crash(num, disR, disC, C);
        }
        rooR = nr;
        rooC = nc;
    }

    private static void moveSanta() {
        // 1번 ~ P번 산타 이동(기절하거나, 탈락하지 않은)
        for (int i = 1; i < P + 1; i++) {
            if (!santa[i].alive || santa[i].k != 0) continue;
            if (santaCnt >= P) break;
            // 루돌프에게 가장 가까운 방향으로 이동
            int canGo = 0;
            int dis = (int) Math.pow(santa[i].r - rooR, 2) + (int) Math.pow(santa[i].c - rooC, 2);
            int minDir = -1;
            for (int j = 0; j < 4; j++) {
                int nr = santa[i].r + dr[j];
                int nc = santa[i].c + dc[j];
                // 해당 칸에 다른 산타나 밖이면 이동 불가
                if (isOut(nr, nc)) continue;
                int distance = (int) Math.pow(nr - rooR, 2) + (int) Math.pow(nc - rooC, 2);

                // 이 부분 개어렵,,
                if (map[nr][nc] != 0) {
                    canGo++;
                    continue;
                }
                if (distance < dis) {
                    dis = distance;
                    minDir = j;
                }
            }
            if (canGo >= 4) continue;
            // System.out.println(i + "번 산타 -> " + minDir);
            // if (map[santa[i].r + dr[minDir]][santa[i].c + dc[minDir]] != 0) continue;
            if (dis == (int) Math.pow(santa[i].r - rooR, 2) + (int) Math.pow(santa[i].c - rooC, 2)) continue;
            
            // System.out.println(map[santa[i].r][santa[i].c]);
            map[santa[i].r][santa[i].c] = 0;
            // System.out.println(map[santa[i].r][santa[i].c]);
            if (rooR == santa[i].r + dr[minDir] && rooC == santa[i].c + dc[minDir]) {
                // 해당 칸에 루돌프가 있으면 충돌 함수 호출
                santa[i].r = rooR;
                santa[i].c = rooC;
                score[i] += D;
                // 해당 산타 기절 처리
                santa[i].k = 1;
                crash(i, dr[minDir] * -1, dc[minDir] * -1, D);
            } else {
                santa[i].r += dr[minDir];
                santa[i].c += dc[minDir];
                map[santa[i].r][santa[i].c] = i;
            }
            // System.out.println(i + "번 산타");
            // printMap();
        }
    }

    // 충돌 & 상호 작용
    private static void crash(int num, int dirR, int dirC, int move) {
        // num번 산타가 dir 방향으로 move 만큼 밀림
        int santaR = santa[num].r;
        int santaC = santa[num].c;

        int nr = santaR + (dirR * move);
        int nc = santaC + (dirC * move);
        // 해당 자리가 밖으로 나가면 alive 설정 
        if (isOut(nr, nc)) {
            santa[num].alive = false;
            santaCnt++;
            return;
        }
        // 해당 자리에 산타가 있으면 crash 재귀
        santa[num].r = nr;
        santa[num].c = nc;
        if (map[nr][nc] != 0) {
            int san = map[nr][nc];
            map[nr][nc] = num;
            crash(san, dirR, dirC, 1);
        } else {
            map[nr][nc] = num;
        }
    }

    private static boolean isOut(int nr, int nc) {
        if (nr < 0 || nc < 0 || nr >= N || nc >= N) {
            return true;
        }
        return false;
    }

    private static int getDir(int dir) {
        if (dir > 0) {
            return 1;
        }
        if (dir < 0) {
            return -1;
        }
        return 0;
    }

    private static void printMap() {
        System.out.println("Roo: " + rooR + ", " + rooC);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}