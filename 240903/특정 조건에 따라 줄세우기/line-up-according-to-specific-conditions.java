import java.util.*;
import java.io.*;

/*
0. N개 동안 나온 이름 boolean 처리 해놓기
1. arraylist 사용해서 인접한 것끼리 묶기
2. arraylist 각각 정렬해서 처음 이름꺼 기준으로 넣고 정렬
*/
public class Main {
    static String[] name = {"Beatrice", "Belinda", "Bella", "Bessie", "Betsy", "Blue", "Buttercup", "Sue"};
    static List<List<String>> list;
    static HashMap<String, Integer> map;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        map = new HashMap<>();
        list = new ArrayList<>();

        int idx = 0;
        for (int i = 0; i < N; i++) {
            String line = br.readLine();
            String[] friend = line.split(" must be milked beside ");
            if (map.containsKey(friend[0])) {
                int index = map.get(friend[0]);
                list.get(index).remove(friend[0]);
                list.get(index).add(friend[1]);
                Collections.sort(list.get(index));
                list.get(index).add(1, friend[0]);
                map.put(friend[1], map.get(friend[0]));
            } else if (map.containsKey(friend[1])) {
                int index = map.get(friend[1]);
                list.get(index).remove(friend[1]);
                list.get(index).add(friend[0]);
                Collections.sort(list.get(index));
                list.get(index).add(1, friend[1]);
                map.put(friend[0], map.get(friend[1]));
            } else {
                list.add(new ArrayList<>());
                list.get(idx).add(friend[0]);
                list.get(idx).add(friend[1]);
                map.put(friend[0], idx);
                map.put(friend[1], idx);
                idx++;
            }
        }

        boolean[] visited = new boolean[list.size()];
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).size() < 3) {
                Collections.sort(list.get(i));
            }
        }

        for (int i = 0; i < name.length; i++) {
            if (map.containsKey(name[i])) {
                int index = map.get(name[i]);
                if (visited[index]) {
                    continue;
                } else if (list.get(index).get(0).equals(name[i])) {
                    for (String n : list.get(index)) {
                        System.out.println(n);
                    }
                    visited[index] = true;
                }
            } else {
                System.out.println(name[i]);
            }
        }
    }
}