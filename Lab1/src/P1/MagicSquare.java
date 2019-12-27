package P1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class MagicSquare {
    public static void main(String[] args) {
        generateMagicSquare(11);
        try {
            System.out.println("1.txt: " + isLegalMagicSquare("1.txt"));
            System.out.println("2.txt: " + isLegalMagicSquare("2.txt"));
            System.out.println("3.txt: " + isLegalMagicSquare("3.txt"));
            System.out.println("4.txt: " + isLegalMagicSquare("4.txt"));
            System.out.println("5.txt: " + isLegalMagicSquare("5.txt"));
            System.out.println("6.txt: " + isLegalMagicSquare("6.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断
     *
     * @param fileName  文件名称
     * @return  如果是magicSquare就返回true，否则返回false，中断也返回false
     * @throws FileNotFoundException    未找到这个文件时抛出
     */
    public static boolean isLegalMagicSquare(String fileName) throws FileNotFoundException {

        /*
            先判断每一行，再判断每一列，最后判断两个对角线
            先记录第一行的和作为标准的和，以后的全都和它做比较，同时记录数字的数量
            每一行先转换为数组，然后循环length遍求和
         */

        File file = new File("src/P1/txt/" + fileName);
        Scanner scanner = new Scanner(file);
        String firstLine = scanner.nextLine();
        String[] numbersInFirstLine = firstLine.split("\t");
        int length = numbersInFirstLine.length;    // 计算数的个数

        int[][] numbers = new int[length][length];

        /*
            初始化数组
         */
        // 复制第一行，同时计算和，作为标准
        int firstSum = 0;
        for (int i = 0; i < length; i++) {
            try {
                int current = Integer.parseInt(numbersInFirstLine[i]);
                if (current <= 0) {
                    System.out.println("File \"" + fileName + "\" is illegal. Interrupted by negative number.");
                    return false;
                }
                numbers[0][i] = current;
                firstSum += current;
            } catch (NumberFormatException ex) {
                System.out.println("File \"" + fileName + "\" is illegal. Interrupted by float or other characters.");
                return false;
            }
        }

        // 复制剩下的行
        for (int row = 1; row < length; row++) {
            String line = scanner.nextLine();

            try {
                String[] numbersInLine = line.split("\t");
                for (int i = 0; i < length; i++) {
                    numbers[row][i] = Integer.parseInt(numbersInLine[i]);
                }
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException ex) {
                System.out.println("File \"" + fileName + "\" is illegal. Interrupted by float or other characters.");
                return false;
            }
        }

        if (scanner.hasNextInt()) {
            System.out.println("这不是方的");
            return false;
        }

        int sum;

        /*
            行计算和比较，因为把第一行作为标准，所以可以少算一行
         */
        for (int row = 1; row < length; row++) {
            sum = 0;
            int[] currentRowNumbers = numbers[row];
            for (int i : currentRowNumbers) {
                sum += i;
            }
            if (sum != firstSum) {
                return false;
            }
        }

        /*
            列计算和比较
         */
        for (int col = 0; col < length; col++) {
            sum = 0;
            for (int j = 0; j < length; j++) {
                sum += numbers[j][col];
            }
            if (sum != firstSum) {
                return false;
            }
        }

        /*
            对角线比较
         */
        int sum1 = 0, sum2 = 0;
        for (int i = 0; i < length; i++) {
            sum1 += numbers[i][i];
            sum2 += numbers[i][length - i - 1];
        }
        return sum1 == firstSum && sum2 == firstSum;
    }

    /**
     * 生成magicSquare并写入文件
     * @param n   magicSquare的阶数
     * @return    n是正奇数返回true，否则为false
     */
    public static boolean generateMagicSquare(int n) {
        if (n <= 0 || n % 2 == 0) return false;
        int[][] magic = new int[n][n];  // 创建数组

        // 从第0行中间开始放数字，然后每次向当前数字的右上角放
        int row = 0, col = n / 2, i, j, square = n * n;     // 从第0行的中间开始放数字
        for (i = 1; i <= square; i++) {     // 从1到n*n中的所有整数都能够填进去
            magic[row][col] = i;        // 放置数字
            if (i % n == 0)
                // 如果已经放置了数字就放到下一行
                row++;
            else {
                if (row == 0)
                    // 当已经是第0行时，挪到最后一行，即n - 1行
                    row = n - 1;
                else row--;             // 如果不是第0行，向上挪一行
                if (col == (n - 1))
                    // 如果是最后一列，就挪到第0列
                    col = 0;
                else col++;     // 否则向右边挪动
            }
        }

        // 准备写入
        PrintWriter out = null;
        try {
            // 创建输出流
            out = new PrintWriter("src/P1/txt/6.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert out != null;
        // 写入文件
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++)
                out.print(magic[i][j] + "\t");
            out.println();
        }

        out.close();
        return true;
    }
}



