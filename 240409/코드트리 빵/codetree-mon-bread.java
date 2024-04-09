import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	
	static int N, M, peopleCnt, time;
	static int[][] map;
	static boolean[][] go;
	
	static int[] dr = {-1, 0, 0, 1};
	static int[] dc = {0, -1, 1, 0};
	
	static List<State> baseCamp;
	static State[] store, person;
	
	static class State implements Comparable<State> {
		int r, c, dis;
		boolean exist;
		
		public State(int r, int c, int dis, boolean exist) {
			this.r = r;
			this.c = c;
			this.dis = dis;
			this.exist = exist;
		}
		
		public int compareTo(State o) {
			if (this.dis == o.dis) {
				if (this.r == o.r) {
					return this.c - o.c;
				}
				return this.r - o.r;
			}
			return this.dis - o.dis;
		}
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		map = new int[N + 1][N + 1];
		go = new boolean[N + 1][N + 1];
		store = new State[M + 1];
		person = new State[M + 1];
		baseCamp = new ArrayList<>();
		
		for (int i = 1; i < N + 1; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 1; j < N + 1; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				if (map[i][j] == 1) {
					baseCamp.add(new State(i, j, 0, true));
				}
			}
		}
		
		for (int i = 1; i < M + 1; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			store[i] = new State(r, c, 0, true);
		}
		
		for (int i = 1; i < M + 1; i++) {
			person[i] = new State(0, 0, 0, false);
		}
		
		time = 1;
		while (true) {			
			// 격자 안 사람들 -> 가고 싶은 편의점으로 1칸 이동
			for (int i = 1; i < M + 1; i++) {
				if (person[i].exist) {
					movePerson(i);
				}
			}
			// 편의점 도착하면 해당 편의점 못 지나감, peopleCnt++;
			for (int i = 1; i < M + 1; i++) {
				if (person[i].exist) {
					if (person[i].r == store[i].r && person[i].c == store[i].c) {
						person[i].exist = false;
						store[i].exist = false;
						go[store[i].r][store[i].c] = true;
						peopleCnt++;
					}
				}
			}
			
			// time <= m 이면 가고 싶은 편의점과 가장 가까이 있는 베이스 캠프에 위치
			if (time <= M) {
				startPerson(time);
			}
			if (peopleCnt >= M) {
				break;
			}
			time++;
		}
		
		System.out.println(time);
	}
	
	private static void startPerson(int num) {
		PriorityQueue<State> pq = new PriorityQueue<>();
		Queue<int[]> queue = new ArrayDeque<int[]>();
		boolean[][] visited = new boolean[N + 1][N + 1];
		queue.add(new int[] { store[num].r, store[num].c, 0 });
		
		int minDis = Integer.MAX_VALUE;
//		System.out.println(num + "번 편의점 위치: " + store[num].r + " " + store[num].c);
		while (!queue.isEmpty()) {
			int[] cur = queue.poll();
			if (map[cur[0]][cur[1]] == 1) {
//				person[num].r = cur[0];
//				person[num].c = cur[1];
//				person[num].exist = true;
//				map[cur[0]][cur[1]] = 0;
//				go[cur[0]][cur[1]] = true;
//				System.out.println(num+"번 시작 " + cur[0] + " " + cur[1]);
//				return;
				if (minDis >= cur[2]) {
					minDis = cur[2];
					pq.add(new State(cur[0], cur[1], cur[2], true));
				}
				if (minDis < cur[2]) {
					break;
				}
			}
			
			for (int i = 0; i < 4; i++) {
				int nr = cur[0] + dr[i];
				int nc = cur[1] + dc[i];
				
				if (nr < 1 || nc < 1 || nr > N || nc > N) continue;
				if (go[nr][nc] || visited[nr][nc]) continue;
				
				visited[nr][nc] = true;
				queue.add(new int[] { nr, nc, cur[2] + 1 });
			}
		}
//		System.out.println("size: " + pq.size());
		State st = pq.poll();
//		System.out.println(pq.peek().r + " " + pq.peek().c + " " + pq.peek().dis);
		person[num].r = st.r;
		person[num].c = st.c;
		person[num].exist = true;
		map[st.r][st.c] = 0;
		go[st.r][st.c] = true;
//		System.out.println(num+"번 시작 " + st.r + " " + st.c + " " + st.dis);
		
	}
	
	private static void movePerson(int num) { // bfs?
		// 편의점에서 사람까지 가는 최단 거리
		Queue<int[]> queue = new ArrayDeque<int[]>();
		boolean[][] visited = new boolean[N + 1][N + 1];
		queue.add(new int[] { store[num].r, store[num].c, store[num].r, store[num].c });
//		visited[store[num].r][store[num].c] = true;
//		System.out.println(num + "번 사람");
		while (!queue.isEmpty()) {
			int[] cur = queue.poll();
			
			for (int i = 0; i < 4; i++) {
				int nr = cur[0] + dr[i];
				int nc = cur[1] + dc[i];
				
				if (nr < 1 || nc < 1 || nr > N || nc > N || visited[nr][nc]) continue;
				if (nr == person[num].r && nc == person[num].c) {
					person[num].r = cur[0];
					person[num].c = cur[1];
					return;
				}
				if (go[nr][nc]) continue;
				
				visited[nr][nc] = true;
				queue.add(new int[] { nr, nc, cur[0], cur[1] });
			}
		}		
	}

}