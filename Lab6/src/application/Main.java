package application;

import abs.selector.CanAccessFirstLadderSelector;
import abs.selector.EmptyLadderSelector;
import abs.selector.LadderSelector;
import abs.selector.RandomSelector;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 这个类是命令行版本的猴子过河
 */
public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("请选择梯子的选择方法：\n1. 优先选择方向相同的梯子\n"
                + "2. 优先选择空梯子");

        int choice = scanner.nextInt();

        LadderSelector selector;

        if (choice == 1) {
            selector = CanAccessFirstLadderSelector.getSelector();
        } else if (choice == 2) {
            selector = EmptyLadderSelector.getSelector();
        } else {
            System.out.println("请作出正确选择");
            return;
        }

        System.out.println("请输入文件名称（例如Competition_1.txt）：");
        String filepath = "src/txt/" + scanner.next();

//        String filepath = "src/txt/Competition_1.txt";
        try {
            MonkeyPasser.passerByMonkeyFile(filepath)
                    .passRiver(selector);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("未在src/txt/目录下找到此文件");
        }
//        MonkeyPasser.randomMonkeyPasser().passRiver(CanAccessFirstLadderSelector.getSelector());

        scanner.close();

    }

}
