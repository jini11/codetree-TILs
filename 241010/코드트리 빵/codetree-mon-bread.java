import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	
	static int N, M, cnt;
	static int[][] map, go;
	static Point[] person, store;
	static List<Point> baseCamp;
	
	static int[] dr = {-1, 0, 0, 1};
	static int[] dc = {0, -1, 1, 0};
	
	static class Point implements Comparable<Point>{
		int r, c, dist;
		boolean isExist;
		public Point(int r, int c, int dist, boolean isExist) {
			this.r = r;
			this.c = c;
			this.dist = dist;
			this.isExist = isExist;
		}
		public int compareTo(Point o) {
			if (this.dist == o.dist) {
				if (this.r == o.r) {
					return this.c - o.c;
				}
				return this.r - o.r;
			}
			return this.dist - o.dist;
		}
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		map = new int[N + 1][N + 1];
		go = new int[N + 1][N + 1];
		person = new Point[M + 1];
		store = new Point[M + 1];
		baseCamp = new ArrayList();
		int idx = 1;
		
		for (int i = 1; i <= N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 1; j <= N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				if (map[i][j] == 1) {
					baseCamp.add(new Point(i, j, 0, true));
				}
			}
		}
		
		for (int i = 1; i <= M; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			store[i] = new Point(r, c, 0, true);
		}
		
		for (int i = 1; i <= M; i++) {
			person[i] = new Point(0, 0, 0, false);
		}
		
		int t = 1;
		while (true) {
			if (cnt >= M) break;
			
			// 본인이 가고 싶은 편의점 방향으로 1칸(1)
			for (int i = 1; i <= M; i++) {
				if (person[i] == null || !person[i].isExist) continue;
				movePerson(i);
			}
			
			// 편의점에 도착했으면 값 갱신(2)
			for (int i = 1; i <= M; i++) {
				if (!person[i].isExist) continue;
				if (person[i].r == store[i].r && person[i].c == store[i].c) {
					person[i].isExist = false;
					cnt++;
					map[store[i].r][store[i].c] = -1;
				}
			}
			
			if (cnt >= M) break;
			
			// 편의점과 가장 가까운 베이스캠프 배치(3)
			if (t <= M) {
				startPerson(t);
			}
			
			t++;
		}
		
		System.out.println(t);
		
	}
	
	private static void movePerson(int num) {
		// 가고자 하는 편의점에서 bfs 돌림
		// go에 칸 수 찍기
		Queue<int[]> queue = new ArrayDeque<int[]>();
		go = new int[N + 1][N + 1];
		queue.add(new int[] { store[num].r, store[num].c});
		go[store[num].r][store[num].c] = 1;
		
		while (!queue.isEmpty()) {
			int[] cur = queue.poll();
			for (int i = 0; i < 4; i++) {
				int nr = cur[0] + dr[i];
				int nc = cur[1] + dc[i];
				
				if (isOut(nr, nc) || go[nr][nc] != 0) continue;
				
				go[nr][nc] = go[cur[0]][cur[1]] + 1;
				queue.add(new int[] {nr, nc});
			}
		}
		
		int min = Integer.MAX_VALUE;
		int dir = 0;
		for (int i = 0; i < 4; i++) {
			int nr = person[num].r + dr[i];
			int nc = person[num].c + dc[i];
			
			if (isOut(nr, nc) || map[nr][nc] == -1) continue;
			
			if (min > go[nr][nc]) {
				min = go[nr][nc];
				dir = i;
			}
		}
		
		// 사람 이동
		person[num].r += dr[dir];
		person[num].c += dc[dir];
	}
	
	private static boolean isOut(int nr, int nc) {
		return nr < 1 || nc < 1 || nr > N || nc > N;
	}
	
	private static void startPerson(int num) {
		// 편의점에서 가장 가까운 베이스캠프 찾기
		PriorityQueue<Point> pq = new PriorityQueue<>();
		Queue<int[]> queue = new ArrayDeque<int[]>();
		boolean[][] visited = new boolean[N + 1][N + 1];
		visited[store[num].r][store[num].c] = true;
		queue.add(new int[] { store[num].r, store[num].c, 1 });
		
		while (!queue.isEmpty()) {
			int[] cur = queue.poll();
			if (map[cur[0]][cur[1]] == 1) {
				pq.add(new Point(cur[0], cur[1], cur[2], true));
			}
			for (int i = 0; i < 4; i++) {
				int nr = cur[0] + dr[i];
				int nc = cur[1] + dc[i];
				
				if (isOut(nr, nc) || map[nr][nc] == -1 || visited[nr][nc]) continue;
				
				queue.add(new int[] {nr, nc, cur[2] + 1});
				visited[nr][nc] = true;
			}
		}
		
		person[num] = pq.poll();
		map[person[num].r][person[num].c] = -1;
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