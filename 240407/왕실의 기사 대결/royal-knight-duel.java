import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class Main {
	
	static int L, N, Q;
	static int[][] map, wall, copy;
	static State[] person;
	static boolean[][] visited;
	static List<Integer> move;
	static Set<Integer> remove;
	
	static int[] dr = {-1, 0, 1, 0};
	static int[] dc = {0, 1, 0, -1};
	
	static class State {
		int num, r, c, h, w, k, d;
		boolean alive;
		public State(int num, int r, int c, int h, int w, int k, int d, boolean alive) {
			this.num = num;
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
		
		map = new int[L + 1][L + 1];
		wall = new int[L + 1][L + 1];
		person = new State[N + 1];
		
		for (int i = 1; i < L + 1; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 1; j < L + 1; j++) {
				wall[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		for (int i = 1; i < N + 1; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			int h = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			int k = Integer.parseInt(st.nextToken());
			person[i] = new State(i, r, c, h, w, k, 0, true);
			for (int j = r; j < r + h; j++) {
				for (int l = c; l < c + w; l++) {
					map[j][l] = i;
				}
			}
		}
		
		for (int i = 0; i < Q; i++) {
			st = new StringTokenizer(br.readLine());
			int num = Integer.parseInt(st.nextToken());
			int dir = Integer.parseInt(st.nextToken());
			
            if (!person[num].alive) continue;
            
			remove = new HashSet<Integer>();
			// 밀 수 있는지 확인
				// 밀 수 있으면 연쇄 밀기
			copy = copyMap(map);
			visited = new boolean[L + 1][L + 1];
			move = new ArrayList<>();
			if (canMove(copy, num, dir)) {
				map = copyMap(copy);
				for (Integer n : move) {
					person[n].r += dr[dir];
					person[n].c += dc[dir];
				}
//				move(num, dir);

				// 대미지 계산
				calcDamage(num);
				
//				System.out.println(" -- remove -- ");
//				for (Integer n : remove) {
//					System.out.println(n + "번 기사");
//				}
				
				// 지도에서 사라진 기사 없애기
				if (remove.size() > 0) {
					for (Integer n : remove) {
						for (int j = person[n].r; j < person[n].r + person[n].h; j++) {
							for (int l = person[n].c; l < person[n].c + person[n].w; l++) {
								map[j][l] = 0;
							}
						}
					}
				}
			}
			
//			print(map);
		}
		
		int damage = 0;
		for (int i = 1; i < N + 1; i++) {
			if (person[i].alive) {
				damage += person[i].d;
			}
		}
		System.out.println(damage);
		
	}
	
	private static int[][] copyMap(int[][] map) {
		int[][] copy = new int[map.length][map[0].length];
		for (int i = 0; i < map.length; i++) {
			copy[i] = map[i].clone();
		}
		return copy;
	}
	
	private static void calcDamage(int num) {
		for (Integer n : move) {
			if (num == n) continue;
			for (int i = person[n].r; i < person[n].r + person[n].h; i++) {
				for (int j = person[n].c; j < person[n].c + person[n].w; j++) {
					
					if (wall[i][j] == 1) {
						person[n].k--;
						person[n].d++;
					}
					if (person[n].k <= 0) {
						person[n].alive = false;
						remove.add(n);
					}
				}
			}
		}
	}
	
	private static boolean canMove(int[][] map, int num, int dir) {
		// num번 기사 탐색
			// 다른 번호 나오면 재귀
			// 벽이나 경계 밖으로 벗어나면 return false;
		for (int i = person[num].r; i < person[num].r + person[num].h; i++) {
			for (int j = person[num].c; j < person[num].c + person[num].w; j++) {
				int nr = i + dr[dir];
				int nc = j + dc[dir];
				
				if (!visited[i][j])
					map[i][j] = 0;
				
				if (nr < 1 || nc < 1 || nr > L || nc > L || wall[nr][nc] == 2) {
					return false;
				}
				
				if (map[nr][nc] != 0 && map[nr][nc] != num) {
					if (!canMove(map, map[nr][nc], dir)) {
						return false;
					}
//					canMove(map, map[nr][nc], dir);
				}
				map[nr][nc] = num;
				visited[nr][nc] = true;
//				map[i][j] = 0;
			}
		}
		move.add(num);
//		System.out.println(" -- canMove -- " + dir);
//		print(map);
		return true;
	}
	
//	private static void move(int num, int dir) {
//		
//	}
	
	private static void print(int[][] map) {
		for (int i = 1; i < map.length; i++) {
			for (int j = 1; j < map[i].length; j++) {
				System.out.print(map[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
}