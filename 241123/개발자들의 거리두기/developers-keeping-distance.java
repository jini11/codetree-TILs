import java.util.*;
import java.io.*;

public class Main {

    static class State implements Comparable<State> {
        int location, infection;

        public State(int location, int infection) {
            this.location = location;
            this.infection = infection;
        }

        @Override
        public int compareTo(State o) {
            return this.location - o.location;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        List<State> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            list.add(new State(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())));
        }

        Collections.sort(list);
        // list.sort((p1, p2) -> p1.x - p2.x);

        int infectDistance = Integer.MAX_VALUE;
        for (int i = 0; i < list.size(); i++) {
            State p = list.get(i);
            if (p.infection == 0) {
                if (i != 0) {
                    if (list.get(i - 1).infection == 1)
                        infectDistance = Math.min(infectDistance, p.location - list.get(i - 1).location - 1);
                }
                if (i != list.size() - 1) {
                    if (list.get(i + 1).infection == 1)
                        infectDistance = Math.min(infectDistance, list.get(i + 1).location - p.location - 1);
                }
            }
        }

        int lastInfected = Integer.MIN_VALUE;
        int answer = 0;
        for (State p : list) {
            if (p.infection == 0)
                continue;
            if (lastInfected + infectDistance < p.location) {
                answer++;
            }
            lastInfected = p.location;
        }

        System.out.println(answer);
    }
}