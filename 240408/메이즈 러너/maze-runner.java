import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	
	static int N, M, K, outCnt;
	static int[][] map;
	static State[] person;
	static State exit;
	static int startRow, startCol, size;
	
	static int[] dr = {-1, 1, 0, 0};
	static int[] dc = {0, 0, 1, -1};
	
	static class State {
		int num, r, c, move;
		boolean out;
		
		public State(int num, int r, int c, int move, boolean out) {
			this.num = num;
			this.r = r;
			this.c = c;
			this.move = move;
			this.out = out;
		}
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		map = new int[N + 1][N + 1];
		for (int i = 1; i < N + 1; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 1; j < N + 1; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		person = new State[M + 1];
		person[0] = new State(0, 0, 0, 0, true);
		for (int i = 1; i < M + 1; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			person[i] = new State(i, r, c, 0, false);
		}
		
		st = new StringTokenizer(br.readLine());
		int exitR = Integer.parseInt(st.nextToken());
		int exitC = Integer.parseInt(st.nextToken());
		exit = new State(-1, exitR, exitC, 0, false);
		
		for (int i = 0; i < K; i++) {
			if (outCnt >= M) break;
	
			// 이동
			if (movePerson()) {
				break;
			}
			
			// 가장 작은 정사각형 찾기
			findSquare();
			// 회전
			rotate();

//			System.out.println(startRow +"행 " + startCol + "열 " + size);
//			System.out.println("출구: " + exit.r + ", " + exit.c);
//			for (State p : person) {
//				System.out.println(p.num + "번 사람: " + p.r + " " + p.c + " " + p.out);
//			}
//			print(map);
		}
		
		int sum = 0;
		for (State p : person) {
			sum += p.move;
		}
		
		System.out.println(sum);
		System.out.println(exit.r + " " + exit.c);
	}
	
	private static void rotate() {
		// 시계 방향 90도 회전
		// 벽 내구도 -1
		int[][] temp = new int[N + 1][N + 1];
		for (int i = startRow; i < startRow + size; i++) {
			for (int j = startCol; j < startCol + size; j++) {
				int ox = i - startRow, oy = j - startCol;
				int rx = oy, ry = size - ox - 1;
				temp[rx + startRow][ry + startCol] = (map[i][j] > 0 ? map[i][j] - 1 : map[i][j]);
//				temp[j][(size - i - startRow + 1) + startRow] = (map[i][j] > 0 ? map[i][j] - 1 : map[i][j]);
			}
		}
		
//		int[][] temp = new int[size][size];
		
		
		for (int i = startRow; i < startRow + size; i++) {
			for (int j = startCol; j < startCol + size; j++) {
				map[i][j] = temp[i][j];
			}
		}
		
//		print(map);
		// 사람, 출구 위치 변경
		for (State p : person) {
			if (p.out || p.num == 0) continue;
			if (startRow <= p.r && p.r < startRow + size && startCol <= p.c && p.c < startCol + size) {
				int ox = p.r - startRow, oy = p.c - startCol;
				int rx = oy, ry = size - ox - 1;
				p.r = rx + startRow;
				p.c = ry + startCol;
			}
		}
		if (startRow <= exit.r && exit.r < startRow + size && startCol <= exit.c && exit.c < startCol + size) {
			int ox = exit.r - startRow, oy = exit.c - startCol;
			int rx = oy, ry = size - ox - 1;
			exit.r = rx + startRow;
			exit.c = ry + startCol;
		}
//		System.out.println(exit.r + " " + exit.c);
	}
	
	private static void findSquare() {
		for (int len = 2; len <= N; len++) {
			for (int i = 1; i <= N - len + 1; i++) {
				for (int j = 1; j <= N - len + 1; j++) {
					int nr = i + len - 1;
					int nc = j + len - 1;
					
					if (!(i <= exit.r && exit.r <= nr && j <= exit.c && exit.c <= nc)) continue;
					
					boolean isIn = false;
					for (State p : person) {
						if (p.out || p.num == 0) continue;
						if (i <= p.r && p.r <= nr && j <= p.c && p.c <= nc) {
							isIn = true;
						}
					}
					
					if (isIn) {
						startRow = i;
						startCol = j;
						size = len;
						return;
					}
				}
			}
		}
	}
	
	private static boolean movePerson() {
		for (State p: person) {
			if (p.out || p.num == 0) continue;
			
			// 다음 칸은 이전보다 출구에 더 가까워야 함
			int distance = Math.abs(p.r - exit.r) + Math.abs(p.c - exit.c);
			int minDis = distance;
			int dir = 0;
			for (int i = 0; i < 4; i++) {
				int nr = p.r + dr[i];
				int nc = p.c + dc[i];
				
				if (nr < 1 || nc < 1 || nr > N || nc > N || map[nr][nc] > 0) continue;
				int dis = Math.abs(nr - exit.r) + Math.abs(nc - exit.c);
				if (dis < distance) {
					if (dis < minDis) {
						minDis = dis;
						dir = i;
					}
				}
			}
			if (minDis == distance) continue;
			p.r += dr[dir];
			p.c += dc[dir];
			p.move++;
			if (p.r == exit.r && p.c == exit.c) {
				p.out = true;
				outCnt++;
				if (outCnt >= M) return true;
			}
		}
		return false;
	}
	
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