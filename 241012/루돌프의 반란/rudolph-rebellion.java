import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
	
	static int N, M, P, C, D, deadCnt;
	static State rudolf;
	static Santa[] santas;
	static int[][] map;
	static int[] dr = {-1, 0, 1, 0, 1, 1, -1, -1};
	static int[] dc = {0, 1, 0, -1, -1, 1, 1, -1};
	
	static class State implements Comparable<State>{
		int r, c, distance;
		public State(int r, int c, int distance) {
			this.r = r;
			this.c = c;
			this.distance = distance;
		}
		
		public int compareTo(State o) {
			if (this.distance == o.distance) {
				if (this.r == o.r) {
					return o.c - this.c;
				}
				return o.r - this.r;
			}
			return this.distance - o.distance;
		}
	}
	
	static class Santa {
		int num, r, c, score, sleep;
		boolean alive;
		public Santa(int num, int r, int c, int score, int sleep, boolean alive) {
			this.num = num;
			this.r = r;
			this.c = c;
			this.score = score;
			this.sleep = sleep;
			this.alive = alive;
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
		
		santas = new Santa[P + 1];
		st = new StringTokenizer(br.readLine());
		int r = Integer.parseInt(st.nextToken());
		int c = Integer.parseInt(st.nextToken());
		rudolf = new State(r, c, 0);
		
		for (int i = 0; i < P; i++) {
			st = new StringTokenizer(br.readLine());
			int num = Integer.parseInt(st.nextToken());
			r = Integer.parseInt(st.nextToken());
			c = Integer.parseInt(st.nextToken());
			santas[num] = new Santa(num, r, c, 0, 0, true);
		}
		
		for (int i = 0; i < M; i++) {
//			System.out.println((i+1) + "번째 턴 시작");
			map = new int[N + 1][N + 1];
			
			// 산타 표시
			for (Santa santa : santas) {
				if (santa == null || !santa.alive) continue;
				map[santa.r][santa.c] = santa.num;
			}
			
			// 루돌프 이동
			moveRudolf();
			
//			System.out.println("루돌프 이동함");
//			System.out.println("루돌프 위치: " + rudolf.r + " " + rudolf.c);
			
			for (Santa santa : santas) {
				if (santa == null || !santa.alive || santa.sleep > 0) continue;
				// 산타 이동
				moveSanta(santa);
			}
			
			if (deadCnt >= P) {
				break;
			}
			
			// 재정비
			getScore();
			
			// 기절해 있는 산타 갱신
			for (Santa santa : santas) {
				if (santa == null || !santa.alive) continue;
				if (santa.sleep > 0) {
					santa.sleep -= 1;
				}
			}
			
//			System.out.println("턴 끝");
//			printMap(map);
		}
		
		for (Santa santa : santas) {
			if (santa == null) continue;
			System.out.print(santa.score + " ");
		}
	}
	
	private static void moveSanta(Santa santa) {
//		System.out.println(santa.num + "번 산타");
		// 루돌프에게 거리가 가장 가까워지는 방향 찾기
		boolean isChange = false;
		int dir = 0;
		int curDistance = (int) (Math.pow(Math.abs(santa.r - rudolf.r), 2) + Math.pow(Math.abs(santa.c - rudolf.c), 2));
		for (int i = 0; i < 4; i++) {
			int nr = santa.r + dr[i];
			int nc = santa.c + dc[i];
			if (isOut(nr, nc) || map[nr][nc] != 0) continue;
			int newDistance = (int) (Math.pow(Math.abs(nr - rudolf.r), 2) + Math.pow(Math.abs(nc - rudolf.c), 2));
			if (curDistance > newDistance) {
//				System.out.println(santa.num + "번 산타 움직일 수 있음");
				curDistance = newDistance;
				isChange = true;
				dir = i;
			}
		}
		
		if (isChange) {
			map[santa.r][santa.c] = 0;
			santa.r += dr[dir];
			santa.c += dc[dir];
			map[santa.r][santa.c] = santa.num;
			// 루돌프랑 충돌했을 때
			if (santa.r == rudolf.r && santa.c == rudolf.c) {
//				System.out.println("루돌프 충돌");
				// 기절 처리
				santas[map[santa.r][santa.c]].sleep = 2;
				santas[map[santa.r][santa.c]].score += D;
				map[santa.r][santa.c] = 0;
				// 충돌함수 호출
				dfs(santa.num, D, (dir + 2) % 4);
			}
//			System.out.println("움직임!!");
//			System.out.println(santa.r + " " + santa.c);
		}
		
	}
	
	private static void moveRudolf() {
//		System.out.println("움직이기 전 루돌프 위치: " + rudolf.r + " " + rudolf.c);
		// 탈락하지 않은 산타 중에서 가장 가까운 산타 찾기
		// 해당 산타 방향으로 1칸 이동
		PriorityQueue<State> pq = new PriorityQueue<>();
		for (Santa santa : santas) {
			if (santa == null || !santa.alive) continue;
			int dis = (int) (Math.pow(Math.abs(santa.r - rudolf.r), 2) + Math.pow(Math.abs(santa.c - rudolf.c), 2));
			pq.add(new State(santa.r, santa.c, dis));
		}
		
		State near = pq.poll();
//		System.out.println("near: " + map[near.r][near.c] + "번 산타가 가장 가까움"+ near.distance);
		int dir = 0;
		for (int i = 0; i < 8; i++) {
			int nr = rudolf.r + dr[i];
			int nc = rudolf.c + dc[i];
			if (isOut(nr, nc)) continue;
			int dis = (int) (Math.pow(Math.abs(nr - near.r), 2) + Math.pow(Math.abs(nc - near.c), 2));
//			System.out.println(i + "번 방향일 때 거리: " + dis);
			if (dis <= near.distance) {
//				rudolf.r = nr;
//				rudolf.c = nc;
				near.distance = dis;
				dir = i;
//				break;
			}
		}
		
		rudolf.r += dr[dir];
		rudolf.c += dc[dir];
		
		// 충돌 발생
		if (map[rudolf.r][rudolf.c] != 0) {
			// 충돌 함수 호출
			santas[map[rudolf.r][rudolf.c]].score += C;
			// 기절 처리
			santas[map[rudolf.r][rudolf.c]].sleep = 2;
			
			dfs(map[rudolf.r][rudolf.c], C, dir);
			map[rudolf.r][rudolf.c] = 0;
		}
		
	}
	// 몇 칸(cnt), 어느 방향(dir), 어떤 산타(num)
	private static void dfs(int num, int cnt, int dir) {
		// 다음칸
		int nr = santas[num].r + (dr[dir] * cnt);
		int nc = santas[num].c + (dc[dir] * cnt);
				
		// 다음칸이 밖이면 out 처리
		if (isOut(nr, nc)) {
//			System.out.println("1111");
			map[santas[num].r][santas[num].c] = 0;
			santas[num].alive = false;
			deadCnt++;
			return;
		}
		// 다음칸이 아무것도 없으면
		if (map[nr][nc] == 0) {
//			System.out.println("2222");
			map[santas[num].r][santas[num].c] = 0;
			map[nr][nc] = num;
			santas[num].r = nr;
			santas[num].c = nc;
			return;
		}
		// 다음칸에 산타 있으면
		if (map[nr][nc] != 0) {
//			System.out.println("3333");
			int before = map[nr][nc];
			santas[num].r = nr;
			santas[num].c = nc;
			dfs(before, 1, dir);
			map[nr][nc] = num;
		}
	}
	
	private static boolean isOut(int nr, int nc) {
		return nr < 1 || nc < 1 || nr > N || nc > N;
	}
	
	private static void getScore() {
		for (Santa santa : santas) {
			if (santa == null) continue;
			if (santa.alive) {
				santa.score += 1;
			}
		}
	}
	
	private static void printMap(int[][] map) {
		for (int i = 1; i <= N; i++) {
			for (int j = 1; j <= N; j++) {
				System.out.print(map[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
}