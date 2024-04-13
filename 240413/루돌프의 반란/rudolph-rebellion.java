import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
	
	static int N, M, P, C, D, totalSanta;
	static int[][] map;
	static int rooR, rooC;
	static State[] santa;
	static int[] score;
	
	static int[] dr = {-1, -1, 0, 1, 1, 1, 0, -1};
	static int[] dc  = {0, 1, 1, 1, 0, -1, -1, -1};
	
	static class State implements Comparable<State> {
		int r, c, k;
		boolean alive;
		
		public State (int r, int c, int k, boolean alive) {
			this.r = r;
			this.c = c;
			this.k = k;
			this.alive = alive;
		}
		
		public int compareTo(State o) {
			int dis1 = (int) (Math.pow(this.r - rooR, 2) + Math.pow(this.c - rooC, 2));
			int dis2 = (int) (Math.pow(o.r - rooR, 2) + Math.pow(o.c - rooC, 2));
			if (dis1 == dis2) {
				if (this.r == o.r) {
					return o.c - this.c;
				}
				return o.r - this.r;
			}
			return dis1 - dis2;
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
		
		map = new int[N][N];
		santa = new State[P + 1];
		score = new int[P + 1];
		
		st = new StringTokenizer(br.readLine());
		rooR = Integer.parseInt(st.nextToken()) - 1;
		rooC = Integer.parseInt(st.nextToken()) - 1;
		totalSanta = P;
		
		for (int i = 0; i < P; i++) {
			st = new StringTokenizer(br.readLine());
			int num = Integer.parseInt(st.nextToken());
			int r = Integer.parseInt(st.nextToken()) - 1;
			int c = Integer.parseInt(st.nextToken()) - 1;
			santa[num] = new State(r, c, 0, true);
			map[r][c] = num;
		}
		
		for (int i = 0; i < M; i++) {
			if (totalSanta < 1) break;
			
			// 루돌프 움직임
			moveRoo();
//			System.out.println("루돌프 위치: " + rooR + rooC);
			
			// 1~P번 산타 움직임
			for (int j = 1; j < P + 1; j++) {
				if (!santa[j].alive || santa[j].k > 0) continue;
				moveSanta(j);
			}
//			print();
			
			// 기절 갱신
			changeK();
			
			// 살아있는 산타 + 1
			getScore();
		}
		
		for (int i = 1; i < P + 1; i++) {
			System.out.print(score[i] + " ");
		}
	}
	
	private static void moveSanta(int num) {
		// 루돌프에게 가까워지는 방향 찾기
		int curDis = getDis(santa[num], rooR, rooC);
		int minDis = curDis;
		int minDir = -1;
		for (int i = 0; i < 8; i += 2) {
			int nr = santa[num].r + dr[i];
			int nc = santa[num].c + dc[i];
			
			if (isOut(nr, nc) || map[nr][nc] != 0) continue;
			
			int dis = getDis(new State(nr, nc, 0, true), rooR, rooC);
			if (minDis > dis) {
				minDis = dis;
				minDir = i;
			}
		}
		
		if (minDis == curDis) return;
		
		// 갈 수 있는 방향 있으면 가기
		map[santa[num].r][santa[num].c] = 0;
		santa[num].r += dr[minDir];
		santa[num].c += dc[minDir];
		
		// 해당 방향에 루돌프가 있으면 충돌!!!
		if (santa[num].r == rooR && santa[num].c == rooC) {
			// 충돌
			attack(num, D, (minDir + 4) % 8);
			score[num] += D;
			santa[num].k = 2;
		} else {
			map[santa[num].r][santa[num].c] = num;
		}
			
	}
	
	private static void moveRoo() {
		// 가장 가까운 산타 찾기
		PriorityQueue<State> pq = new PriorityQueue<>();
		for (int i = 1; i < P + 1; i++) {
			if (santa[i].alive) {
				pq.add(santa[i]);
			}
		}
		State near = pq.poll();
		
		// 가장 가까운 산타로 가는 방향 찾기
		int minDis = Integer.MAX_VALUE;
		int dir = -1;
		for (int i = 0; i < 8; i++) {
			int nr = rooR + dr[i];
			int nc = rooC + dc[i];
			
			if (isOut(nr, nc)) continue;
			
			int dis = getDis(near, nr, nc);
			if (minDis > dis) {
				minDis = dis;
				dir = i;
			}
		}
		
		// 다음 칸이 산타이면 충돌!! 
		rooR += dr[dir];
		rooC += dc[dir];
		if (map[rooR][rooC] != 0) {
			// 충돌
			// C만큼 산타 밀기
			// 또 다음칸에 산타 있으면 1만큼 밀기
			attack(map[rooR][rooC], C, dir);
			score[map[rooR][rooC]] += C;
			santa[map[rooR][rooC]].k = 2;
			map[rooR][rooC] = 0;
		}
		
	}
	
	private static void attack(int num, int cnt, int d) {
		int nr = santa[num].r + (dr[d] * cnt);
		int nc = santa[num].c + (dc[d] * cnt);
		
		// 격자밖이면 끝
		if (isOut(nr, nc)) {
			santa[num].alive = false;
			totalSanta--;
			return;
		}
		// 다음칸 빈칸이면 끝
		if (map[nr][nc] == 0) {
			map[nr][nc] = num;
			santa[num].r = nr;
			santa[num].c = nc;
			return;
		}
		
		// 다음칸에서 산타 있으면
		if (map[nr][nc] != 0) {
			int next = map[nr][nc];
			map[nr][nc] = num;
			santa[num].r = nr;
			santa[num].c = nc;
			attack(next, 1, d);
		}
	}
	
	private static boolean isOut(int r, int c) {
		return r < 0 || c < 0 || r >= N || c >= N;
	}
	
	private static int getDis(State santa, int nr, int nc) {
		return (int) (Math.pow(santa.r - nr, 2) + Math.pow(santa.c - nc, 2));
	}
	
	private static void changeK() {
		for (int i = 1; i < P + 1; i++) {
			if (santa[i].k > 0) {
				santa[i].k--;
			}
		}
	}
	
	private static void getScore() {
		for (int i = 1; i < P + 1; i++) {
			if (santa[i].alive) {
				score[i]++;
			}
		}
	}
	
	private static void print() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				System.out.print(map[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
}