#include <fstream>
#include <iostream>
#include <algorithm>
#include <queue>
#include <string>
#define INFINITE 99999999

using namespace std;

int** map;
int M, N;
int s_row, s_col;
int** dest;
int num_dest = 0;
int length = 0;
int cnt = 0;

class Node {
	int row;
	int col;
	bool visited;
	int heuristic;
	Node* parent;
public:
	Node(int r, int c) {
		row = r;
		col = c;
		visited = false;
		heuristic = INFINITE;
	}
	int getRow() const { return row; }
	int getCol() const { return col; }
	bool isVisited() const { return visited; }
	int getHeuristic() const { return heuristic; }
	Node* getParent() const { return parent; }
	void setPos(int r, int c) { row = r; col = c; }
	void setVisited(bool v) { visited = v; }
	void setHeuristic() { 
		int min_val = INFINITE;
		for (int i = 0; i < num_dest; i++) {
			min_val = min(min_val, abs(dest[i][0] - row) + abs(dest[i][1] - col));
		}
		heuristic = min_val;
	}
	void initNode(int r, int c) {
		row = r; col = c; 
		visited = false; 
		setHeuristic(); 
		parent = NULL; 
	}
	void setParent(Node* n) {
		parent = n;
	}
};

class Comparator
{
public:
	int operator() (Node* n1, Node* n2){
		return n1->getHeuristic() > n2->getHeuristic();
	}
};

priority_queue<Node*, vector<Node*>, Comparator> min_heap;



bool checkMove(int row, int col, int** visited) {
	if (row < 0 || row >= M || col < 0 || col >= N
		|| map[row][col] == 1 || visited[row][col] == 1) 
		return false;
	else
		return true;
}

int heuristic(int row, int col) {
	int min_val = INFINITE;
	for (int i = 0; i < num_dest; i++) {
		min_val = min(min_val, abs(dest[i][0] - row) + abs(dest[i][1] - col));
	}
	return min_val;
}

void GBS(int row, int col, int** visited) {

	Node* start = (Node*)malloc(sizeof(Node));
	start->initNode(row, col);

	min_heap.push(start);

	Node* cur;
	// �ݺ�
	while (true) {

		cur = min_heap.top();
		//cout << "(" << cur->getRow() << ", " << cur->getCol() << ")" << endl;
		min_heap.pop();
		cur->setVisited(true);
		cnt++;
		// min heap���� ���ɼ��� ���� ���������� Ž���� ������ ����
		// �ּ��� ��쿡�� �̹� Ž���� ������ ���̹Ƿ�
		// ����, Ž�� �� dead end �߻��ÿ��� �ʱ�ȭ ��������.
		visited[cur->getRow()][cur->getCol()] = 1;
		if (map[cur->getRow()][cur->getCol()] == 4)
			break;
		// expand node
		if (checkMove(cur->getRow() - 1, cur->getCol(), visited)) {
			Node* up = (Node*)malloc(sizeof(Node));
			up->initNode(cur->getRow() - 1, cur->getCol());
			up->setParent(cur);
			min_heap.push(up);
		}
		if (checkMove(cur->getRow() + 1, cur->getCol(), visited)) {
			Node* down = (Node*)malloc(sizeof(Node));
			down->initNode(cur->getRow() + 1, cur->getCol());
			down->setParent(cur);
			min_heap.push(down);
		}
		if (checkMove(cur->getRow(), cur->getCol() - 1, visited)) {
			Node* left = (Node*)malloc(sizeof(Node));
			left->initNode(cur->getRow(), cur->getCol() - 1);
			left->setParent(cur);
			min_heap.push(left);
		}
		if (checkMove(cur->getRow(), cur->getCol() + 1, visited)) {
			Node* right = (Node*)malloc(sizeof(Node));
			right->initNode(cur->getRow(), cur->getCol() + 1);
			right->setParent(cur);
			min_heap.push(right);
		}
	}
	cur = cur->getParent();
	while (!(cur->getRow() == s_row && cur->getCol() == s_col)) {
		length++;
		map[cur->getRow()][cur->getCol()] = 5;
		cur = cur->getParent();
	}

	
}

int main() {
	string path;
	cin >> path;
	//cout << path << endl;
	ifstream inFile;
	inFile.exceptions(ifstream::failbit | ifstream::badbit);
	try {
		inFile.open(path + "\\input.txt");
	}
	catch (const ifstream::failure& e) {
		cout << "Exception opening file" << endl;
		return 0;
	}
	//ifstream inFile(path + "\\input_ex1.txt");


	inFile >> M >> N;

	// Allocate 2d array for the map
	map = (int**)malloc(sizeof(int*) * M);
	for (int i = 0; i < M; i++) {
		map[i] = (int*)malloc(sizeof(int) * N);
	}

	int** visited;
	// Allocate 2d array for the visited
	visited = (int**)malloc(sizeof(int*) * M);
	for (int i = 0; i < M; i++) {
		visited[i] = (int*)malloc(sizeof(int) * N);
	}
	
	// Get map info
	for (int i = 0; i < M; i++) {
		for (int j = 0; j < N; j++) {
			inFile >> map[i][j];
			if (map[i][j] == 3) {
				s_row = i;
				s_col = j;
			}
			if (map[i][j] == 4) {
				num_dest++;
			}
		}
	}
	dest = (int**)malloc(sizeof(int*) * num_dest);
	for (int i = 0; i < num_dest; i++) {
		dest[i] = (int*)malloc(sizeof(int) * 2);
	}

	int k = 0;
	for (int i = 0; i < M; i++) {
		for (int j = 0; j < N; j++) {
			if (map[i][j] == 4) {
				dest[k][0] = i;
				dest[k][1] = j;
				k++;
			}
		}
	}


	// Init visited
	for (int i = 0; i < M; i++) {
		for (int j = 0; j < N; j++) {
			visited[i][j] = 0;
		}
	}

	GBS(s_row, s_col, visited);

	ofstream outFile(path + "\\output.txt");
	//ofstream outFile("GBS_output_ex3.txt");
	for (int i = 0; i < M; i++) {
		for (int j = 0; j < N; j++) {
			outFile << map[i][j] << " ";
		}
		outFile << endl;
	}	
	outFile << "-----" << endl;
	outFile << "length=" << length << endl;
	outFile << "time=" << cnt << endl;
	outFile.close();

}