import java.util.*;
import java.io.*;

public class Main {

    static int N, M, P, C, D, cnt;
    static Point[] santa;
    static int[] score;
    static Point rudolf;
    static int[][] map;
    
    static int[] dr = {-1, 0, 1, 0, -1, 1, 1, -1};
    static int[] dc = {0, 1, 0, -1, 1, 1, -1, -1};

    static class Point implements Comparable<Point> {
        int num, r, c, distance, faint;
        boolean alive;

        public Point(int num, int r, int c, boolean alive, int faint, int distance) {
            this.num = num;
            this.r = r;
            this.c = c;
            this.alive = alive;
            this.faint = faint;
            this.distance = distance;
        }

        public int compareTo(Point o) {
            if (this.distance == o.distance) {
                if (this.r == o.r) {
                    return o.c - this.c;
                }    
                return o.r - this.r;
            }
            return this.distance - o.distance;
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

        map = new int[N + 1][N + 1];
        st = new StringTokenizer(br.readLine());
        int r = Integer.parseInt(st.nextToken());
        int c = Integer.parseInt(st.nextToken());
        rudolf = new Point(0, r, c, true, 0, 0);

        santa = new Point[P + 1];
        score = new int[P + 1];
        for (int i = 0; i < P; i++) {
            st = new StringTokenizer(br.readLine());
            int num = Integer.parseInt(st.nextToken());
            r = Integer.parseInt(st.nextToken());
            c = Integer.parseInt(st.nextToken());
            map[r][c] = num;
            santa[i + 1] = new Point(num, r, c, true, 0, 0);
        }

        for (int i = 0; i < M; i++) {
            // 산타가 모두 탈락하면 종료
            if (cnt >= P) {
                break;
            }

            // 루돌프 움직이기
            moveRudolf();
            // System.out.println(rudolf.r + " : " + rudolf.c);

            // 산타 움직이기
            moveSanta();

            // System.out.println((i+1) +"번");
            // printMap();
            // 살아있는 산타 점수 추가
            getScore();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < P; i++) {
            sb.append(score[i + 1] + " ");
        }
        System.out.println(sb.toString());
    }

    // private static boolean allOver() {
    //     for (int i = 1; i < P + 1; i++) {
    //         if (santa[i].alive) {
    //             return false;
    //         }
    //     }
    //     return true;
    // }

    private static void getScore() {
        for (int i = 1; i < P + 1; i++) {
            if (santa[i].alive) {
                score[i] += 1;
            }
        }
    }

    private static boolean isOut(int nr, int nc, boolean isSanta) {
        if (nr < 1 || nc < 1 || nr > N || nc > N) {
            return true;
        }
        if (isSanta) {
            if (map[nr][nc] != 0) { // 가려는 곳에 산타가 있으면
                return true;
            }
        }
        return false;
    }

    private static void moveRudolf() {
        // 가장 가까운 산타 찾기
        PriorityQueue<Point> pq = new PriorityQueue<>();
        for (int i = 1; i < P + 1; i++) {
            if (!santa[i].alive) continue;
            int d = (int) Math.pow(rudolf.r - santa[i].r, 2) + (int) Math.pow(rudolf.c - santa[i].c, 2);
            santa[i].distance = d;
            pq.add(santa[i]);
        }
        Point nearSanta = pq.poll();

        // 방향 찾기
        int[] dir = findRudolfDir(nearSanta);
        int nr = rudolf.r + dir[0];
        int nc = rudolf.c + dir[1];

        // 해당 방향에 아무것도 없으면 그냥 이동
        if (map[nr][nc] == 0) {
            rudolf.r = nr;
            rudolf.c = nc;
        } else {
            // 해당 방향에 산타가 있으면
            // 충돌!! 산타 밀기, 해당 산타 기절
            // 해당 산타 C만큼 점수 얻기
            attack(nearSanta.num, dir[0], dir[1], false);
            rudolf.r = nr;
            rudolf.c = nc;
            score[nearSanta.num] += C;
        }
    }

    private static void moveSanta() {
        // 1 ~ P 순서대로 산타 이동
        // 기절하거나 죽은 산타는 움직이지 않음
        for (int i = 1; i < P + 1; i++) {
            if (!santa[i].alive || santa[i].faint > 0) {
                santa[i].faint -= 1;
                continue;
            }

            // 루돌프랑 가까워지는 방향 찾기
            int dir = findSantaDir(santa[i]);

            // 범위 벗어나거나 다른 산타 있으면 못감
            if (dir == -1) {
                continue;
            }
            int nr = santa[i].r + dr[dir];
            int nc = santa[i].c + dc[dir];

            // 움직일 수 있으면 움직이기
                // 해당 방향에 루돌프가 없으면 그냥 이동, map 갱신
            if (nr == rudolf.r && nc == rudolf.c) {
                attack(santa[i].num, dr[(dir + 2) % 4], dc[(dir + 2) % 4], true);
                score[santa[i].num] += D;
            } else {
                map[santa[i].r][santa[i].c] = 0;
                santa[i].r = nr;
                santa[i].c = nc;
                map[nr][nc] = santa[i].num;
            }
                // 해당 방향에 루돌프가 있으면 
                // 충돌!! 산타 밀림, 해당 산타 기절
                // 해당 산타 D만큼 점수 얻기
            // System.out.println(i+"번 산타 이동");
            // printMap();
        }
    }

    private static int[] findRudolfDir(Point santa) {
        // int dir = -1;
        // int minDistance = Integer.MAX_VALUE;
        // for (int i = 0; i < 8; i++) {
        //     int nr = rudolf.r + dr[i];
        //     int nc = rudolf.c + dc[i];
        //     if (isOut(nr, nc, false)) continue;
        //     int d = (int) Math.pow(nr - santa.r, 2) + (int) Math.pow(nc - santa.c, 2);
        //     if (minDistance > d) {
        //         minDistance = d;
        //         dir = i;
        //     }
        // }
        int dirRow = santa.r - rudolf.r;
        int dirCol = santa.c - rudolf.c;

        if (dirRow > 0) {
            dirRow = 1;
        } else if (dirRow < 0) {
            dirRow = -1;
        }

        if (dirCol > 0) {
            dirCol = 1;
        } else if (dirCol < 0) {
            dirCol = -1;
        }

        // for (int i = 0; i < 8; i++) {
        //     if (dr[i] == dirRow && dc[i] == dirCol) {
        //         return i;
        //     }
        // }
        return new int[] { dirRow, dirCol };
    }

    private static int findSantaDir(Point san) {
        int dir = -1;
        int minDistance = Integer.MAX_VALUE;
        for (int i = 0; i < 4; i++) {
            int nr = san.r + dr[i];
            int nc = san.c + dc[i];
            if (isOut(nr, nc, true)) continue;
            int d = (int) Math.pow(nr - rudolf.r, 2) + (int) Math.pow(nc - rudolf.c, 2);
            if (minDistance > d) {
                minDistance = d;
                dir = i;
            }
        }
        return dir;
    }

    // 충돌
    private static void attack(int num, int dirRow, int dirCol, boolean isSanta) {
        int push = 0;
        // num번째 산타 dir 방향으로 x만큼 밀기
        if (isSanta) {
            push = D - 1;
        } else {
            push = C;
        }

        // faint를 true로 
        santa[num].faint = 2;

        int nr = santa[num].r + (dirRow * push);
        int nc = santa[num].c + (dirCol * push);

        while (true) {
            if (isOut(nr, nc, false)) { // 격자 밖으로 나가면 alive false로
                santa[num].alive = false;
                map[santa[num].r][santa[num].c] = 0;
                cnt++;
                break;
            }
            if (map[nr][nc] != 0) { // 산타가 있으면
                // System.out.println("여기!!!");
                int nnr = nr;
                int nnc = nc;
                int idx = num;
                map[santa[idx].r][santa[idx].c] = 0;
                while (true) {
                    if (isOut(nnr, nnc, false)) {
                        santa[idx].alive = false;
                        map[santa[idx].r][santa[idx].c] = 0;
                        cnt++;
                        break;
                    }
                    if (map[nnr][nnc] == 0) {
                        map[nnr][nnc] = idx;
                        santa[idx].r = nnr;
                        santa[idx].c = nnc;
                        break;
                    }
                    santa[idx].r = nnr;
                    santa[idx].c = nnc;
                    int temp = map[nnr][nnc];
                    map[nnr][nnc] = idx;
                    idx = temp;
                    nnr += dirRow;
                    nnc += dirCol;
                }
                // map[nnr][nnc] = idx;
                // santa[idx].r = nnr;
                // santa[idx].c = nnc;
                break;
                // nr += dirRow;
                // nc += dirCol;
            } else { // 산타가 없으면
                // System.out.println("아니야..?");
                map[santa[num].r][santa[num].c] = 0;
                santa[num].r = nr;
                santa[num].c = nc;
                map[nr][nc] = num;
                break;
            }
        }
    }

    private static void printMap() {
        for (int i = 1; i < N + 1; i++) {
            for (int j = 1 ; j < N + 1; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}