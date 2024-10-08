import java.util.*;
import java.io.*;

public class Main {

    static final int N = 3;
    static int M, K, res;
    static int[][] map;
    static boolean[][] visited;
    static PriorityQueue<Point> pq;
    static Queue<Integer> left;

    static int[] dr = {1, 0, -1, 0};
    static int[] dc = {0, 1, 0, -1};

    static class Point implements Comparable<Point> {
        int r, c, d, sum;
        public Point(int r, int c, int d, int sum) {
            this.r = r;
            this.c = c;
            this.d = d;
            this.sum = sum;
        }

        public int compareTo(Point o1) {
            if (this.sum == o1.sum) {
                if (this.d == o1.d) {
                    if (this.c == o1.c) {
                        return this.r - o1.r;
                    }
                    return this.c - o1.c;
                }
                return this.d - o1.d;
            }
            return o1.sum - this.sum;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        K = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        map = new int[5][5];
        left = new ArrayDeque<>();

        for (int i = 0; i < 5; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 5; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < M; i++) {
            left.add(Integer.parseInt(st.nextToken()));
        }

        
        for (int i = 0; i < K; i++) {
            pq = new PriorityQueue<>();
            res = 0;

            // 3*3 구간 정하기
            findArea();

            // pq에서 꺼낸 값으로 실제 맵 회전
            if (pq.isEmpty()) break;
            Point best = pq.poll();
            if (best.sum == 0) break;
            // System.out.println(best.r + ", " + best.c);
            for (int j = 0; j < best.d; j++) {
                map = rotate(map, best.r, best.c);
            }
            // System.out.println("회전 후");
            // printMap(map);
            
            while(true) {
                // 유물 획득
                int cnt = calcGift(map, true);
                // System.out.println(i+"번쨰: " + cnt);
                if (cnt == 0) break;
                // res += cnt;
                // System.out.println("유물 획득 후");
                // printMap(map);

                // 벽면 채우기
                fillBlank();
                // System.out.println("채운 뒤");
                // printMap(map);
            }
            // printMap(map);

            System.out.print(res + " ");
        }
    }

    private static void fillBlank() {
        for (int i = 0; i < 5; i++) {
            for (int j = 4; j >= 0; j--) {
                if (map[j][i] == 0) {
                    map[j][i] = left.poll();
                }
            }
        }
    }

    private static void findArea() {
        // (1,1) 부터 (3,3)까지 진행
        // 90, 180, 270 회전 해보고 얻을 수 있는 유물 개수 계산해 PQ에 넣기
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                int[][] copy = copyMap(map);

                for (int k = 1; k < 4; k++) {
                    // System.out.println(k + "번째 회전");
                    copy = rotate(copy, i, j);
                    int cnt = calcGift(copy, false);
                    pq.add(new Point(i, j, k, cnt));
                    // System.out.println(i + ", " + j + " 회전횟수: " + k + " 개수: " + cnt);
                }
            }
        }
    }

    private static int[][] copyMap(int[][] map) {
        int[][] copy = new int[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            copy[i] = map[i].clone();
        }
        return copy;
    }

    private static int[][] rotate(int[][] map, int r, int c) { // 배열 90 회전
        int[][] newMap = new int[5][5];
        int[][] miniMap = new int[3][3];
        int nr = 0, nc = 0;
        
        for (int i = r - 1; i <= r + 1; i++) {
            for (int j = c - 1; j <= c + 1; j++) {
                miniMap[nr][nc++] = map[i][j];
                if (nc > 2) {
                    nc = 0;
                    nr++;
                }
                map[i][j] = 0;
                
            }
        }

        int[][] after = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                after[j][3-i-1] = miniMap[i][j];
            }
        }

        nr = 0;
        nc = 0;
        int test = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] != 0) {
                    newMap[i][j] = map[i][j];
                } else {
                    test++;
                    newMap[i][j] = after[nr][nc++];
                    if (nc > 2) {
                        nc = 0;
                        nr++;
                    }
                }
                
            }
        }
        return newMap;
    }

    private static void printMap(int[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static int calcGift(int[][] map, boolean flag) { // 유물 개수 카운팅
        int cnt = 0;
        visited = new boolean[map.length][map[0].length];
        // printMap(map);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (visited[i][j] || map[i][j] == 0) continue;
                int sum = bfs(i, j, false, map);
                // System.out.println("calcGift: " + sum);
                if (sum > 2) {
                    if (flag) {
                        // System.out.println(i + ", " + j + "부터 시작");
                        visited = new boolean[map.length][map[0].length];
                        int c = bfs(i, j, true, map);
                        // System.out.println(i +", " +j+ " 에서 시작");
                        // System.out.println(c +"만큼 만들어야 함");
                        map[i][j] = 0;
                        res += c;
                        // printMap(map);
                    }
                    // System.out.println(i +", " +j+ " 에서 시작");
                    // System.out.println("sum: " + sum);
                    cnt += sum;
                }
            }
        }
        return cnt;
    }

    private static int bfs(int r, int c, boolean flag, int[][] map) {
        Queue<int[]> queue = new ArrayDeque<>();
        visited[r][c] = true;
        queue.add(new int[] {r, c, map[r][c]});
        // if (flag) map[r][c] = 0;
        int cnt = 1;
        
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            for (int i = 0; i < 4; i++) {
                int nr = cur[0] + dr[i];
                int nc = cur[1] + dc[i];
                if (isOut(nr, nc) || visited[nr][nc] || cur[2] != map[nr][nc]) continue;
                // if (flag) System.out.println(nr +", " + nc);
                if (cur[2] == map[nr][nc]) {
                    visited[nr][nc] = true;
                    queue.add(new int[] {nr, nc, map[nr][nc]});
                    if (flag) map[nr][nc] = 0;
                    cnt++;
                }
            }
        }
        // if (flag) printMap(map);
        return cnt;
    }

    private static boolean isOut(int nr, int nc) {
        return nr < 0 || nc < 0 || nr >= 5 || nc >= 5;
    }

    // private static boolean isExist() {
    //     // 유물이 존재하는지 확인
    // }
}