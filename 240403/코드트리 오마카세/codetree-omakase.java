import java.util.*;
import java.io.*;

public class Main {

    static class Query {
        int cmd, t, x, n;
        String name;
        public Query(int cmd, int t, int x, String name, int n) {
            this.cmd = cmd;
            this.t = t;
            this.x = x;
            this.name = name;
            this.n = n;
        }
    }

    static int L, Q;
    
    // 명령
    static List<Query> queries = new ArrayList<>();

    // 사람 
    static Set<String> names = new HashSet<>();

    // 초밥
    static Map<String, List<Query>> p_queries = new HashMap<>();

    // 입장 시간
    static Map<String, Integer> entry_time = new HashMap<>();

    // 위치
    static Map<String, Integer> position = new HashMap<>();

    // 퇴장 시간
    static Map<String, Integer> exit_time = new HashMap<>();

    public static boolean compare(Query q1, Query q2) {
        if (q1.t != q2.t) {
            return q1.t < q2.t;
        }
        return q1.cmd < q2.cmd;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        L = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());
            int command = Integer.parseInt(st.nextToken());
            int t = -1, x = -1, n = -1;
            String name = "";
            switch (command) {
                case 100:
                    t = Integer.parseInt(st.nextToken());
                    x = Integer.parseInt(st.nextToken());
                    name = st.nextToken();
                    break;
                case 200:
                    t = Integer.parseInt(st.nextToken());
                    x = Integer.parseInt(st.nextToken());
                    name = st.nextToken();
                    n = Integer.parseInt(st.nextToken());
                    break;
                case 300:
                    t = Integer.parseInt(st.nextToken());
                    break;
            }

            queries.add(new Query(command, t, x, name, n));

            if (command == 100) {
                if (p_queries.containsKey(name)) {
                    p_queries.get(name).add(new Query(command, t, x, name, n));
                } else {
                    List<Query> list = new ArrayList<>();
                    list.add(new Query(command, t, x, name, n));
                    p_queries.put(name, list);
                }
            } else if (command == 200) {
                names.add(name);
                entry_time.put(name, t);
                position.put(name, x);
            }
        }

        for (String name : names) {
            exit_time.put(name, 0);

            for (Query q : p_queries.get(name)) {
                int time_to_removed = 0;
                if (q.t < entry_time.get(name)) {
                    int t_sushi_x = (q.x  + (entry_time.get(name) - q.t)) % L;
                    int additional_time = (position.get(name) - t_sushi_x + L) % L;
                    time_to_removed = entry_time.get(name) + additional_time;
                } else {
                    int additional_time = (position.get(name) - q.x + L) % L;
                    time_to_removed = q.t + additional_time;
                }

                exit_time.put(name, Math.max(exit_time.get(name), time_to_removed));
                queries.add(new Query(111, time_to_removed, -1, name, -1));
            }
        }

        for (String name : names) {
            queries.add(new Query(222, exit_time.get(name), -1, name, -1));
        }

        queries.sort((q1, q2) -> compare(q1, q2) ? -1 : 1);

        int people_num = 0, sushi_num = 0;
        for (int i = 0; i < queries.size(); i++) {
            if (queries.get(i).cmd == 100) {
                sushi_num++;
            } else if (queries.get(i).cmd == 111) {
                sushi_num--;
            } else if (queries.get(i).cmd == 200) {
                people_num++;
            } else if (queries.get(i).cmd == 222) {
                people_num--;
            } else {
                System.out.println(people_num + " " + sushi_num);
            }
        }
            
        
    }
}