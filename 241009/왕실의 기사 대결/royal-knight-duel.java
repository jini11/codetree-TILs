import java.util.*;
import java.io.*;

public class Main {

	static int L, N, Q, res;
	static Point[] soldier;
	static int[][] map, wall, copy;
	static boolean[][] visited;
	static List<Integer> move, remove;
	static int[] dr = {-1, 0, 1, 0};
	static int[] dc = {0, 1, 0, -1};

	static class Point {
		int r, c, h, w, k, d;
		boolean alive;
		public Point(int r, int c, int h, int w, int k, int d, boolean alive) {
			this.r = r;
			this.c = c;
			this.h = h;
			this.w = w;
			this.k = k;
			this.d = d;
			this.alive = alive;
		}
	}

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		L = Integer.parseInt(st.nextToken());
		N = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());

		soldier = new Point[N + 1];
		map = new int[L + 1][L + 1];
		wall = new int[L + 1][L + 1];
		
		for (int i = 1; i <= L; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 1; j <= L; j++) {
				wall[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		for (int i = 1; i <= N; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			int h = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			int k = Integer.parseInt(st.nextToken());
			soldier[i] = new Point(r, c, h, w, k, 0, true);
			for (int n = r; n < r + h; n++) {
				for (int m = c; m < c + w; m++) {
					map[n][m] = i;
				}
			}
		}

		for (int i = 0; i < Q; i++) {
			st = new StringTokenizer(br.readLine());
			int num = Integer.parseInt(st.nextToken());
			int dir = Integer.parseInt(st.nextToken());
			// System.out.println((i+1) +"번째 명령: " + num + "번 기사 " + dir + " 방향으로 이동");
			// printMap(map);
			if (!soldier[num].alive) continue;

			copy = copyMap(map);
			move = new ArrayList<>();
			remove = new ArrayList<>();
			visited = new boolean[L + 1][L + 1];
			// 해당 방향으로 갈 수 있는지 체크
			if (canMove(copy, num, dir)) {
				// 갈수 있으면 가기
				map = copyMap(copy);
				
				// 밀친 기사 있으면 실제로 옮기기
				if (move.size() > 0) {
					for (Integer n : move) {
						soldier[n].r += dr[dir];
						soldier[n].c += dc[dir];
						if (n == num) continue;
						calcDamage(soldier[n]);
					}
				}
				
				// 죽은 기사는 맵에서 지우기
				if (remove.size() > 0) {
					for (Integer n : remove) {
						removeSolider(soldier[n]);
					}
				}
			}
		}

		for (int i = 1; i < soldier.length; i++) {
			if (soldier[i].alive) {
				res += soldier[i].d;
			}
		}
		System.out.println(res);
    }

	private static void removeSolider(Point man) {
		for (int i = man.r; i < man.r + man.h; i++) {
			for (int j = man.c; j < man.c + man.w; j++) {
				map[i][j] = 0;
			}
		}
	}

	private static void calcDamage(Point man) {
		for (int i = man.r; i < man.r + man.h; i++) {
			for (int j = man.c; j < man.c + man.w; j++) {
				if (wall[i][j] == 1) {
					man.d++;
				}
			}
		}
		if (man.d >= man.k) {
			remove.add(map[man.r][man.c]);
			man.alive = false;
		}
	}

	private static boolean canMove(int[][] map, int num, int dir) {
		// printMap(map);
		for (int i = soldier[num].r; i < soldier[num].r + soldier[num].h; i++) {
			for (int j = soldier[num].c; j < soldier[num].c + soldier[num].w; j++) {
				int nr = i + dr[dir];
				int nc = j + dc[dir];
				
				if (!visited[i][j]) {
					map[i][j] = 0;
				}

				// 다음 가려는 칸에 벽이 있으면 return false
				if (isOut(nr, nc) || wall[nr][nc] == 2) {
					return false;
				}

				// 다음 가려는 칸에 다른 기사 있으면 move에 추가, 재귀
				if (map[nr][nc] != 0 && map[nr][nc] != num) {
					if (!canMove(map, map[nr][nc], dir)) {
						return false;
					}
				}
				map[nr][nc] = num;
				visited[nr][nc] = true;
			}
		}
		
		move.add(num);
		return true;
	}

	private static void printMap(int[][] map) {
		for (int i = 1; i < map.length; i++) {
			for (int j = 1; j < map[i].length; j++) {
				System.out.print(map[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	private static boolean isOut(int nr, int nc) {
		return nr < 1 || nc < 1 || nr > L || nc > L;
	}

	private static int[][] copyMap(int[][] map) {
		int[][] copy = new int[map.length][map[0].length];
		for (int i = 0; i < map.length; i++) {
			copy[i] = map[i].clone();
		}
		return copy;
	}
}