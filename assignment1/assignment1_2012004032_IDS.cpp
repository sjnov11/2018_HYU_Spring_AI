#include <fstream>
#include <iostream>
#include <string>
using namespace std;

int** map;

int M, N;
int s_row, s_col;
int cnt = 0;

bool checkMove(int row, int col, int** visited) {
	if (row < 0 || row >= M || col < 0 || col >= N
		|| map[row][col] == 1 || visited[row][col] == 1) {
		return false;
	}
	else
		return true;
}

bool IDS(int row, int col, int cur, int depth, int** visited) {
	// Increase IDS function call times
	cnt++;
	// Mark current position as visited
	visited[row][col] = 1;
	// Escape conditions
	// 1. Found
	if (map[row][col] == 4)
		return true;
	// 2. Not Found
	if (cur == depth) {
		visited[row][col] = 0;
		return false;
	}

	//bool find = false;
	// ╩С
	if (checkMove(row - 1, col, visited)) {
		if (IDS(row - 1, col, cur + 1, depth, visited)) {
			if (!(row == s_row && col == s_col))
				map[row][col] = 5;
			return true;
		}
	}

	// го
	if (checkMove(row + 1, col, visited)) {
		if (IDS(row + 1, col, cur + 1, depth, visited)) {
			if (!(row == s_row && col == s_col))
				map[row][col] = 5;
			return true;
		}
	}

	// аб
	if (checkMove(row, col - 1, visited)) {
		if (IDS(row, col - 1, cur + 1, depth, visited)) {
			if (!(row == s_row && col == s_col))
				map[row][col] = 5;
			return true;
		}
	}

	// ©Л
	if (checkMove(row, col + 1, visited)) {
		if (IDS(row, col + 1, cur + 1, depth, visited)) {
			if (!(row == s_row && col == s_col))
				map[row][col] = 5;
			return true;
		}
	}
	 
	visited[row][col] = 0;
	return false;

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
	//ifstream inFile("input_ex3.txt");

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
		}
	}

	
	// Init visited
	for (int i = 0; i < M; i++) {
		for (int j = 0; j < N; j++) {
			visited[i][j] = 0;
		}
	}

	int length = 0;
	for (length = 1; ; length++) {
		//cnt = 0;
		if (IDS(s_row, s_col, 0, length, visited))
			break;
	
	}
	
	ofstream outFile(path + "\\output.txt");
	//ofstream outFile("IDS_output_ex3.txt");
	for (int i = 0; i < M; i++) {
		for (int j = 0; j < N; j++) {
			outFile << map[i][j] << " ";
		}
		outFile << endl;
	}
	outFile << "-----" << endl;
	outFile << "length=" << length - 1<< endl;
	outFile << "time=" << cnt << endl;
	outFile.close();
}