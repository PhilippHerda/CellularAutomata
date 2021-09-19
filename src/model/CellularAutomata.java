package model;

import controller.Handler;

public class CellularAutomata {

    private Handler handler;

    public CellularAutomata(int ruleNumber, int WIDTH, int HEIGHT, Handler handler) {
        this.handler = handler;
        calculateRule(ruleNumber, WIDTH, HEIGHT);
    }

    private Block[][] calculateRule(int ruleNumber, int WIDTH, int HEIGHT) {
        Block[][] matrix = generateBlockMatrix(WIDTH, HEIGHT);
        matrix[WIDTH/2][0].setActive(true);
        for(int i = 1; i<WIDTH-1; i++) {
            for(int j = 1; j<HEIGHT-1; j++) {
                applyRule(ruleNumber, matrix, i, j);
            }
        }
        return matrix;
    }

    private Block[][] generateBlockMatrix(int WIDTH, int HEIGHT) {
        Block[][] gameMatrix = new Block[WIDTH][HEIGHT];
        for(int i = 0; i < WIDTH; i++) {
            for(int j = 0; j < HEIGHT; j++) {
                Block block = new Block(i,j, ID.BLOCK);
                gameMatrix[i][j] = block;
                handler.addObject(block);
            }
        }
        return gameMatrix;
    }

    private void applyRule(int ruleNumber, Block[][] matrix, int i, int j) {
        for(int k = 0; k<Integer.toBinaryString(ruleNumber).length(); k++) {
            if((Character.getNumericValue(Integer.toBinaryString(ruleNumber).charAt(Integer.toBinaryString(ruleNumber).length()-(k+1)))) == 1) {
                if(isBitXactive(k, matrix, i, j)) {
                    matrix[j][i].setActive(true);
                }
            }
        }
    }

    private boolean isBitXactive(int bitNumber, Block[][] matrix, int i, int j) {
        return switch (bitNumber) {
            case 0 -> !matrix[j - 1][i - 1].isActive() && !matrix[j][i - 1].isActive() && !matrix[j + 1][i - 1].isActive();
            case 1 -> !matrix[j - 1][i - 1].isActive() && !matrix[j][i - 1].isActive() && matrix[j + 1][i - 1].isActive();
            case 2 -> !matrix[j - 1][i - 1].isActive() && matrix[j][i - 1].isActive() && !matrix[j + 1][i - 1].isActive();
            case 3 -> !matrix[j - 1][i - 1].isActive() && matrix[j][i - 1].isActive() && matrix[j + 1][i - 1].isActive();
            case 4 -> matrix[j - 1][i - 1].isActive() && !matrix[j][i - 1].isActive() && !matrix[j + 1][i - 1].isActive();
            case 5 -> matrix[j - 1][i - 1].isActive() && !matrix[j][i - 1].isActive() && matrix[j + 1][i - 1].isActive();
            case 6 -> matrix[j - 1][i - 1].isActive() && matrix[j][i - 1].isActive() && !matrix[j + 1][i - 1].isActive();
            case 7 -> matrix[j - 1][i - 1].isActive() && matrix[j][i - 1].isActive() && matrix[j + 1][i - 1].isActive();
            default -> true;
        };
    }


}
