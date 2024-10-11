import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
	
	static int L, N, Q, res;
	static int[][] map, wall;
	static boolean[][] visited;
	static List<Integer> push;
	static Point[] persons;
	static int[] dr = {-1, 0, 1, 0};
	static int[] dc = {0, 1, 0, -1};
	
	static class Point {
		int r, c, h, w, k, damage;
		boolean alive;
		
		public Point(int r, int c, int h, int w, int k, int damage, boolean alive) {
			this.r = r;
			this.c = c;
			this.h = h;
			this.w = w;
			this.k = k;
			this.damage = damage;
			this.alive = alive;
		}
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		L = Integer.parseInt(st.nextToken());
		N = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());
		
		map = new int[L + 1][L + 1];
		wall = new int[L + 1][L + 1];
		persons = new Point[N + 1];
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
			persons[i] = new Point(r, c, h, w, k, 0, true);
			for (int n = r; n < r + h; n++) {
				for (int m = c; m < c + w; m++) {
					map[n][m] = i;
				}
			}
		}

		for (int i = 0; i < Q; i++) {
//			System.out.println((i + 1) + "번째 명령");
			st = new StringTokenizer(br.readLine());
			int num = Integer.parseInt(st.nextToken());
			int dir = Integer.parseInt(st.nextToken());
			
			// 기사가 살아있지 않으면 명령 듣지 않음
			if (!persons[num].alive) continue;
			
			// 해당 방향으로 기사 이동
			push = new ArrayList<>();
			visited = new boolean[L + 1][L + 1];
			int[][] copy = copyMap(map);
			if (!movePerson(copy, num, dir)) {
				continue;
			}

			map = copyMap(copy);
			// 실제 좌표값 옮겨주기
			for (Integer idx : push) {
				persons[idx].r += dr[dir];
				persons[idx].c += dc[dir];
				// 대결 대미지
				if (idx == num) continue;
				calcDamage(idx);
			}
			
//			System.out.println("밀 수 있음");
//			printMap(map);
		}
		
		for (Point person : persons) {
			if (person == null || !person.alive) continue;
			res += person.damage;
		}
		
		System.out.println(res);
	}
	
	private static void calcDamage(int idx) {
		for (int i = persons[idx].r; i < persons[idx].r + persons[idx].h; i++) {
			for (int j = persons[idx].c; j < persons[idx].c + persons[idx].w; j++) {
				if (wall[i][j] == 1) {
					persons[idx].damage++;
				}
			}
		}
		if (persons[idx].damage >= persons[idx].k) {
			persons[idx].alive = false;
			for (int i = persons[idx].r; i < persons[idx].r + persons[idx].h; i++) {
				for (int j = persons[idx].c; j < persons[idx].c + persons[idx].w; j++) {
					map[i][j] = 0;
				}
			}
		}
	}
	
	private static boolean movePerson(int[][] map, int num, int dir) {
		for (int i = persons[num].r; i < persons[num].r + persons[num].h; i++) {
			for (int j = persons[num].c; j < persons[num].c + persons[num].w; j++) {
				int nr = i + dr[dir];
				int nc = j + dc[dir];
				
				if (!visited[i][j]) {
					map[i][j] = 0;
				}
				
				if (isOut(nr, nc) || wall[nr][nc] == 2) return false;
				
				// 다음칸이 비어있는 경우
//				if (map[nr][nc] == 0) {
//					map[nr][nc] = num;
//					map[i][j] = 0;
//					continue;
//				}
//				// 다음칸이 자기 구역인 경우
//				if (map[nr][nc] == num) {
//					map[i][j] = 0;
//					continue;
//				}
				// 다른 기사가 있는 경우
				if (map[nr][nc] != 0 && map[nr][nc] != num) {
					int next = map[nr][nc];
					if (!movePerson(map, next, dir)) {
						return false;
					}
				}
				
				map[nr][nc] = num;
				visited[nr][nc] = true;
			}
		}
		
		push.add(num);
		return true;
	}
	
	private static int[][] copyMap(int[][] map) {
		int[][] copy = new int[map.length][map[0].length];
		for (int i = 0; i < map.length; i++) {
			copy[i] = map[i].clone();
		}
		return copy;
	}
	
	private static boolean isOut(int nr, int nc) {
		return nr < 1 || nc < 1 || nr > L || nc > L;
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
}