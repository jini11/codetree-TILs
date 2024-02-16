import java.util.*;
import java.io.*;

public class Main {

    static int n, res;
    static int[][] map, groupMap;
    static boolean[][] visited, isGo;
    static int[] groupCnt;

    static int[] dr = {1, 0, -1, 0};
    static int[] dc = {0, 1, 0, -1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        n = Integer.parseInt(br.readLine());
        map = new int[n][n];

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int i = 0; i < 3; i++) {

            makeGroup();
            res += getScore();

            rotate();
            rotateSquare();
        }

        makeGroup();
        res += getScore();

        System.out.println(res);
    }

    private static void makeGroup() {
        visited = new boolean[n][n];
        groupMap = new int[n][n];
        groupCnt = new int[n * n + 1];
        int idx = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (visited[i][j]) continue;
                idx++;
                groupCnt[idx] = 1;
                bfs(i, j, idx);
            }
        }
    }

    private static void bfs(int r, int c, int idx) {
        Queue<int[]> queue = new ArrayDeque<>();
        groupMap[r][c] = idx;
        visited[r][c] = true;
        queue.add(new int[] {r, c});

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            for (int i = 0; i < 4; i++) {
                int nr = cur[0] + dr[i];
                int nc = cur[1] + dc[i];

                if (isOut(nr, nc) || visited[nr][nc]) continue;

                if (map[cur[0]][cur[1]] == map[nr][nc]) {
                    groupCnt[idx]++;
                    visited[nr][nc] = true;
                    groupMap[nr][nc] = idx;
                    queue.add(new int[] {nr, nc});
                }
            }
        }
    }

    private static int getScore() {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < 4; k++) {
                    int nr = i + dr[k];
                    int nc = j + dc[k];

                    if (isOut(nr, nc)) continue;

                    if (map[i][j] != map[nr][nc]) {
                        sum += ((groupCnt[groupMap[i][j]] + groupCnt[groupMap[nr][nc]]) * map[i][j] * map[nr][nc]);
                    }
                }
            }
        }
        return sum / 2;
    }

    private static void rotate() {
        // 십자가
        int[] temp = new int[n/2];
        
        for (int i = 0; i < 4; i++) {
            int row = n/2;
            int col = n/2;
            int idx = 0;
            while (true) {
                row += dr[i];
                col += dc[i];

                if (isOut(row, col)) break;

                int em = map[row][col];
                map[row][col] = temp[idx];
                temp[idx++] = em;
            }
        }

        int idx = 0;
        for (int i = n/2+1; i < n; i++) {
            map[i][n/2] = temp[idx++];
        }

    }

    private static void rotateSquare() {
        int[][] tmp = new int[n][n];
        copy(tmp, map);
	   
	   for (int i = 0; i < n / 2; i++)
		   for (int j = 0; j < n / 2; j++)
	            tmp[i][j] = map[n / 2 - 1 - j][i];
	 
	    for (int i = 0; i < n / 2; i++)
	    	for (int j = n / 2 + 1; j < n; j++)
	            tmp[i][j] = map[n - 1 - j][n / 2 + 1 + i];

	   for (int i = 0; i < n / 2; i++)
	    	for (int j = n / 2 + 1; j < n; j++)
	    		tmp[n/ 2 + 1 + i][n / 2 * 2 - j] = map[j][i];
	    
	   for (int i = n / 2 + 1; i < n; i++)
	    	for (int j = 0; j < n / 2; j++)
	            tmp[i][j + n / 2 + 1] = map[n - 1 - j][i];
	    
	    copy(map, tmp);
    }

    private static void copy(int[][] map, int[][] tmp) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                map[i][j] = tmp[i][j];
            }
        }
    }

    private static boolean isOut(int nr, int nc) {
        if (nr < 0 || nr >= n || nc < 0 || nc >= n) {
            return true;
        }
        return false;
    }
}