import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

	static int N, M, K, sum;
	static int[][] map, check;
	static boolean[][] visited;
	static State[] group;
	static int startRow, startCol, ballDir;
	
	static class State {
		int headR, headC, tailR, tailC, num;
		public State(int headR, int headC, int tailR, int tailC, int num) {
			this.headR = headR;
			this.headC = headC;
			this.tailR = tailR;
			this.tailC = tailC;
			this.num = num;
		}
	}
	
	static int[] dr = {0, -1, 0, 1};
	static int[] dc = {1, 0, -1, 0};
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		map = new int[N][N];
		check = new int[N][N];
		group = new State[M];
		int idx = 0;
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
	
		visited = new boolean[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (!visited[i][j] && (map[i][j] > 0 && map[i][j] < 4)) {
					group[idx] = new State(-1, -1, -1, -1, 0);
					findGroup(idx++, i, j);
				}
			}
		}
		
//		for (int i = 0; i < M; i++) {
//			System.out.println(group[i].headR + " " + group[i].headC + " | " + group[i].tailR + " " + group[i].tailC);
//		}
		
		for (int i = 0; i < K; i++) {
//			System.out.println(i + " 라운드");
			// 머리 사람을 따라 한칸 이동
			for (int j = 0; j < M; j++) {
//				System.out.println(j + "번 그룹 이동");
				move(group[j]);
			}
			

//			print(map);
			// 공 던지기
			ballStart(i);
			throwBall();
			

//			print(check);
//			print(map);
		}
		System.out.println(sum);
	}
	
	private static void print(int[][] map) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				System.out.print(map[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	private static void throwBall() {
		int nr = startRow;
		int nc = startCol;
//		System.out.println("시작: " + nr + " " + nc);
		while (true) {
			if (nr < 0 || nc < 0 || nr >= N || nc >= N) break;
			
			if (map[nr][nc] > 0 && map[nr][nc] < 4) {
//				System.out.println("잡힘!!!" + nr + " " + nc);
				int g = check[nr][nc] - 1;
				// 점수 get
				int k = getK(g, nr, nc);
//				System.out.println(k +"번째 "  + nr + " " + nc);
				sum += Math.pow(k, 2);
				
				// 머리 & 꼬리 바꾸기
				map[group[g].headR][group[g].headC] = 3;
				map[group[g].tailR][group[g].tailC] = 1;
				int tempR = group[g].headR;
				int tempC = group[g].headC;
				group[g].headR = group[g].tailR;
				group[g].headC = group[g].tailC;
				group[g].tailR = tempR;
				group[g].tailC = tempC;
				break;
			}
			
			nr += dr[ballDir];
			nc += dc[ballDir];
		}
	}
	
	private static int getK(int idx, int r, int c) {
		Queue<int[]> queue = new ArrayDeque<int[]>();
		boolean[][] v = new boolean[N][N];
		queue.add(new int[] { group[idx].headR, group[idx].headC, 1});
		v[group[idx].headR][group[idx].headC] = true;
		
		while (!queue.isEmpty()) {
			int[] cur = queue.poll();
			if (cur[0] == r && cur[1] == c) {
//				System.out.println("K 구함?!?");
				return cur[2];
			}
			for (int i = 0; i < 4; i++) {
				int nr = cur[0] + dr[i];
				int nc = cur[1] + dc[i];
				
				if (nr < 0 || nc < 0 || nr >= N || nc >= N || v[nr][nc]) continue;
				if (map[nr][nc] == 4 || map[nr][nc] == 0) continue;
				if (Math.abs(map[nr][nc] - map[cur[0]][cur[1]]) > 1) {
//					System.out.println("걸림 " + cur[2]);
					continue;
				}
				
				v[nr][nc] = true;
				queue.add(new int[] {nr, nc, cur[2] + 1});
			}
		}
		return 0;
	}
	
	private static void ballStart(int round) {
		ballDir = (round / N) % 4;
		int start = round % N;
		if (ballDir == 0) {
			startRow = start;
			startCol = 0;
		} else if (ballDir == 1) {
			startRow = N - 1;
			startCol = start;
		} else if (ballDir == 2) {
			startRow = N - start - 1;
			startCol = N - 1;
		} else {
			startRow = 0;
			startCol = N - start - 1;
		}
	}
	
	private static void move(State g) {
		int beforeR = g.headR;
		int beforeC = g.headC;
		
		// 머리 사람 이동
		map[g.headR][g.headC] = 2;
		int idx = check[g.headR][g.headC];
//		check[g.headR][g.headC] = 0;
		for (int i = 0; i < 4; i++) {
			int nr = g.headR + dr[i];
			int nc = g.headC + dc[i];
			
			if (nr < 0 || nc < 0 || nr >= N || nc >= N || map[nr][nc] == 0) continue;
			
			if (map[nr][nc] != 2) {
				map[nr][nc] = 1;
				check[nr][nc] = idx;
				g.headR = nr;
				g.headC = nc;
				break;
			}
		}
		
//		print(map);
		
		// 꼬리 사람 이동
		if (map[g.tailR][g.tailC] != 1) {
			map[g.tailR][g.tailC] = 4;
		
			check[g.tailR][g.tailC] = 0;
		}
		for (int i = 0; i < 4; i++) {
			int nr = g.tailR + dr[i];
			int nc = g.tailC + dc[i];
			
			if (nr < 0 || nc < 0 || nr >= N || nc >= N || map[nr][nc] == 0) continue;
			if (nr == beforeR && nc == beforeC) continue;
			
			if (map[nr][nc] == 2 || map[nr][nc] == 1) {
				map[nr][nc] = 3;
				check[nr][nc] = idx;
				g.tailR = nr;
				g.tailC = nc;
				break;
			}
		}
	}
	
	private static void findGroup(int idx, int r, int c) {
		Queue<int[]> queue = new ArrayDeque<int[]>();
		visited[r][c] = true;
		queue.add(new int[] {r, c});
		check[r][c] = idx + 1;
		if (map[r][c] == 1) {
			group[idx].headR = r;
			group[idx].headC = c;
		} else if (map[r][c] == 3) {
			group[idx].tailR = r;
			group[idx].tailC = c;
		}
		
		
		while (!queue.isEmpty()) {
			int[] cur = queue.poll();
			
			for (int i = 0; i < 4; i++) {
				int nr = cur[0] + dr[i];
				int nc = cur[1] + dc[i];
				
				if (nr < 0 || nc < 0 || nr >= N || nc >= N || visited[nr][nc]) continue;
				if (map[nr][nc] == 1) {
					group[idx].headR = nr;
					group[idx].headC = nc;
				} else if (map[nr][nc] == 3) {
					group[idx].tailR = nr;
					group[idx].tailC = nc;
				}
				
				if (map[nr][nc] > 0 &&map[nr][nc] < 4) {
					check[nr][nc] = idx + 1;
					visited[nr][nc] = true;
					queue.add(new int[] {nr, nc});
				}
			}
		}
	}
	
	
	
}

/*
 * 
7 3 973
3 2 1 0 0 0 0
4 0 4 0 2 1 4
4 4 4 0 2 0 4
0 0 0 0 3 4 4
2 1 3 2 0 0 0
2 0 0 2 0 0 0
2 2 2 2 0 0 0

7 2 14
3 2 1 0 0 0 0
4 0 4 0 2 1 4
4 4 4 0 2 0 4
0 0 0 0 3 0 4
0 0 4 4 4 0 4
0 0 4 0 0 0 4
0 0 4 4 4 4 4

 */