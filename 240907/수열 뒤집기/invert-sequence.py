import sys

def main():
    input = sys.stdin.read
    data = input().splitlines()
    
    N, K = map(int, data[0].split())
    K = K % 4  # K를 4로 나눈 나머지를 구함
    arr = list(range(1, N + 1))  # 1부터 N까지의 리스트 생성
    
    A1, A2 = map(int, data[1].split())
    B1, B2 = map(int, data[2].split())

    def swap(left, right):
        while left < right:
            arr[left - 1], arr[right - 1] = arr[right - 1], arr[left - 1]
            left += 1
            right -= 1

    for _ in range(K):
        swap(A1, A2)
        swap(B1, B2)
    
    for num in arr:
        print(num)

if __name__ == "__main__":
    main()