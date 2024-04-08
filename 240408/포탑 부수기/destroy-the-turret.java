import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

	static int N, M, K, totalCnt;
	static int startRow, startCol, endRow, endCol;
	static int[][] map, attackMap;
	static boolean[][] noAttack;
	
	static int[] dr = { 0, -1, 0, 1, 1, 1, -1, -1};
	static int[] dc = { 1, 0, -1, 0, 1, -1, 1, -1};
	
	static class State implements Comparable<State> {
		int r, c, attack, p;
		public State(int r, int c, int attack, int p) {
			this.r = r;
			this.c = c;
			this.attack = attack;
			this.p = p;
		}
		
		public int compareTo(State o) {
			if (this.p == o.p) {
				if (this.attack == o.attack) {
					if (this.r + this.c == o.r + o.c) {
						return o.c - this.c;
					}
					return (o.r + o.c) - (this.r + this.c);
				}
				return o.attack - this.attack;
			}
			return o.p - this.p;
		}
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		map = new int[N + 1][M + 1];
		attackMap = new int[N + 1][M + 1];
		
		for (int i = 1; i < N + 1; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 1; j < M + 1; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				if (map[i][j] > 0) totalCnt++;
			}
		}
		
		for (int i = 1; i <= K; i++) {
			
			if (totalCnt < 2) {
				break;
			}
			
			// 공격자, 피해자 선정
			selectAttacker();
			attackMap[startRow][startCol] = i;
			noAttack = new boolean[N + 1][M + 1];
			noAttack[startRow][startCol] = true;
			noAttack[endRow][endCol] = true;
//			System.out.println("attacker: " + startRow + " " + startCol);
//			System.out.println("defender: " + endRow + " " + endCol);
			// 공격
			if (!attackLaser()) {
//				System.out.println("레이저 실패?");
				attackBomb();
			}
						
			// 정비
			ready();
			
//			print();
		}
		
		// 남아있는 포탑 중 가장 강한 포탐
		System.out.println(getStrong());
	}
	
	private static void ready() {
		for (int i = 1; i < N + 1; i++) {
			for (int j = 1; j < M + 1; j++) {
				if (!noAttack[i][j] && map[i][j] > 0) {
					map[i][j]++;
				}
			}
		}
	}
	
	private static boolean attackLaser() {
		Queue<int[]> queue = new ArrayDeque<int[]>();
		boolean[][] visited = new boolean[N + 1][M + 1];
		int[][] backR = new int[N + 1][M + 1];
		int[][] backC = new int[N + 1][M + 1];
		backR[startRow][startCol] = startRow;
		backC[startRow][startCol] = startCol;
		queue.add(new int[] { startRow, startCol });
		visited[startRow][startCol] = true;
		boolean canAttack = false;
		
		while (!queue.isEmpty()) {
			int[] cur = queue.poll();
			if (cur[0] == endRow && cur[1] == endCol) {
				canAttack = true;
				break;
			}
			
			for (int i = 0; i < 4; i++) {
				// 여기 수정
//				int nr = (cur[0] + dr[i] + N) % N;
//				int nc = (cur[1] + dc[i] + M) % M;
				int nr = getPoint(cur[0] + dr[i], N);
				int nc = getPoint(cur[1] + dc[i], M);
//				if (nr < 1 || nr > N) {
//					nr = (nr + N) % N;
//				}
//				if (nc < 1 || nc > M) {
//					nc = (nc + M) % M;
//				}
				if (visited[nr][nc] || map[nr][nc] == 0) continue;
				
				backR[nr][nc] = cur[0];
				backC[nr][nc] = cur[1];
				visited[nr][nc] = true;
				queue.add(new int[] {nr, nc});
			}
		}
//		for (int i = 1; i < N + 1; i++) {
//			for (int j = 1 ; j < M + 1; j++) {
//				System.out.print(backR[i][j] + " ");
//			}
//			System.out.println();
//		}
//		System.out.println();
//		for (int i = 1; i < N + 1; i++) {
//			for (int j = 1 ; j < M + 1; j++) {
//				System.out.print(backC[i][j] + " ");
//			}
//			System.out.println();
//		}
		
		if (canAttack) {
			map[endRow][endCol] -= map[startRow][startCol];
			if (map[endRow][endCol] <= 0) {
				map[endRow][endCol] = 0;
				totalCnt--;
			}
			
			int row = backR[endRow][endCol];
			int col = backC[endRow][endCol];
			while (true) {

				if (row == startRow && col == startCol) {
					break;
				}
				map[row][col] -= (map[startRow][startCol] / 2);
				noAttack[row][col] = true;
				if (map[row][col] <= 0) {
					map[row][col] = 0;
					totalCnt--;
				}
				int nr = backR[row][col];
				int nc = backC[row][col];
				row = nr;
				col = nc;
			}
		}
		
		
		return canAttack;
	}
	
	private static int getPoint(int point, int max) {
		if (point < 1) {
			return max;
		}
		if (point > max) {
			return 1;
		}
		return point;
	}
	
	private static void attackBomb() {
		map[endRow][endCol] -= map[startRow][startCol];
		if (map[endRow][endCol] <= 0) {
			map[endRow][endCol] = 0;
			totalCnt--;
		}
		
//		boolean[][] visited = new boolean[N + 1][M + 1];
		for (int i = 0; i < 8; i++) {
			// 여기 수정
			int nr = (endRow + dr[i] + N) % N;
			int nc = (endCol + dc[i] + M) % M;
			
			if (map[nr][nc] == 0) continue;
			
			map[nr][nc] -= (map[startRow][startCol] / 2);
			noAttack[nr][nc] = true;
			if (map[nr][nc] <= 0) {
				map[nr][nc] = 0;
				totalCnt--;
			}
		}
	}
	
	private static void selectAttacker() {
		List<State> top = new ArrayList<>();
		
		for (int i = 1; i < N + 1; i++) {
			for (int j = 1; j < M + 1; j++) {
				if (map[i][j] > 0) {
					top.add(new State(i, j, attackMap[i][j], map[i][j]));
				}
			}
		}
		
		Collections.sort(top);
		State att = top.remove(top.size() - 1);
		State de = top.remove(0);
		startRow = att.r;
		startCol = att.c;
		endRow = de.r;
		endCol = de.c;		
		map[startRow][startCol] = map[startRow][startCol] + (N + M);
	}
	
	private static int getStrong() {
		int max = 0;
		for (int i = 1; i < N + 1; i++) {
			for (int j = 1; j < M + 1; j++) {
				max = Math.max(max, map[i][j]);
			}
		}
		return max;
	}
	
	private static void print() {
		for (int i = 1; i < N + 1; i++) {
			for (int j = 1; j < M + 1; j++) {
				System.out.print(map[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
}