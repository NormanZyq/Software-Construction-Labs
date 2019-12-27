package P3;

import P3.Chess.ChessBoard;
import P3.Chess.ChessPlayer;
import P3.Chess.Pieces.King;
import P3.Interface.Board;
import P3.Weiqi.WeiqiBoard;
import P3.Weiqi.WeiqiPlayer;

import java.util.Scanner;

public class Game {
    private static final Scanner scanner = new Scanner(System.in);

    private static Piece[] parsePositions(Board board) {
        String a = scanner.nextLine();
        if (a.equals("end")) {
            System.exit(0);
        }
        String[] v = a.split("\\s");
//        Position[] results = new Position[2];

        Piece[] results = new Piece[2];

        for (int i = 0; i < 2; i++) {
            String[] split = v[i].split(",") ;
            results[i] = board.getPieceOnBoard(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        }

        return results;
    }

    private static Piece parsePosition(Board board) {
        String a = scanner.nextLine();
        String[] split = a.split(",");
        return board.getPieceOnBoard(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }


    public static void main(String[] args) {
        System.out.println("请输入你要进行的游戏\n国际象棋(chess)\n围棋(go)");
//        if (!scanner.hasNextInt()) {
//            System.out.println("请输入正确指令");
//        }
//        int choice = scanner.nextInt();
//        scanner.nextLine();
        String game = scanner.nextLine();
        if (game.equals("chess")) {
            // 国际象棋
            System.out.println("请黑方输入姓名：");
            String playerAName = scanner.nextLine();
            System.out.println("请白方输入姓名：");
            String playerBName = scanner.nextLine();

            ChessPlayer playerA = new ChessPlayer(playerAName, 1);
            ChessPlayer playerB = new ChessPlayer(playerBName, 2);

            ChessBoard cb = new ChessBoard(playerA, playerB);

            King kingA = (King) cb.getPieceOnBoard(7, 3);

            King kingB = (King) cb.getPieceOnBoard(0, 3);

            while (playerA.hasPiece(kingA) && playerB.hasPiece(kingB)) {
                cb.printBoard();
                if (cb.getCurrentPlayer() == playerA) {
                    System.out.println("当前是黑方（上方）执棋，请玩家" + playerAName + "输入操作，输入end结束");
                } else {
                    System.out.println("当前是白方（下方）执棋，请玩家" + playerBName + "输入操作，输入end结束");
                }
                System.out.println("1. 行棋\n2. 查询占用\n3. 统计棋子数目");

                if (scanner.hasNext("end")) {
                    System.exit(0);
                }

                if (!scanner.hasNextInt()) {
                    System.out.println("请输入正确指令");
                    scanner.nextLine();
                    continue;
                }
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 1) {
                    // 行棋
                    System.out.println("请输入您的移动操作，输入坐标(row, col)，以空格分隔起点和终点，例如：\"3,4 3,5\"");
                    Piece[] pieces = parsePositions(cb);        // 获取position
                    Piece opePiece = pieces[0];      // 获取棋子
//                    if (!cb.movePieceOnBoard(opePiece, pieces[0].getPosition(), pieces[1].getPosition())) {
                    if (!cb.movePieceOnBoard(opePiece, pieces[0].getPosition(), pieces[1].getPosition())) {
                        System.out.println("不合规则的行棋，请重试！");
                        scanner.nextLine();
                    }
                } else if (choice == 2) {
                    // 查询占用
                    System.out.println("请输入查询位置 row,col，例如 3,4 ");
                    System.out.println(cb.queryCapture(parsePosition(cb).getPosition()));
                    scanner.nextLine();
                } else if (choice == 3) {
                    // 统计数目
                    System.out.println(cb.getCurrentPlayer().toString());
                    scanner.nextLine();

                } else {
                    System.out.println("请输入正确指令");
                    scanner.nextLine();
                }


            }
        } else if (game.equals("go")) {
            // 围棋
            System.out.println("请黑方输入姓名：");
            String playerAName = scanner.nextLine();
            System.out.println("请白方输入姓名：");
            String playerBName = scanner.nextLine();

            WeiqiPlayer playerBlack = new WeiqiPlayer(playerAName, 1);
            WeiqiPlayer playerWhite = new WeiqiPlayer(playerBName, 2);

            WeiqiBoard wb = new WeiqiBoard(playerBlack, playerWhite);

            WeiqiPlayer current;

            label:
            while (true) {
                wb.printBoard();
                current = wb.getCurrent();
                if (current == playerBlack) {
                    System.out.println("当前是黑方" + playerAName + "执棋");
                } else {
                    System.out.println("当前是白方" + playerBName + "执棋");
                }
                System.out.println("请输入要执行的操作：\n1. 下子\n2. 提子\n3. 虚着（放弃本回合）\n4. 查询占用\n5. 统计我的棋子\n结束游戏请输入end");
                int choice1;
                if (scanner.hasNext("[1-5]")) {
                    choice1 = scanner.nextInt();
                    switch (choice1) {
                        case 1:
                            // 下子
                            System.out.println("请输入下子的坐标(row,col)如(1,2)");

                            if (scanner.hasNext("[0-9]{1,2},[0-9]{1,2}")) {
                                String[] split = scanner.next().split(",");
                                int row = Integer.parseInt(split[0]);
                                int col = Integer.parseInt(split[1]);
                                if (!wb.addPiece(current, row, col)) {
                                    System.out.println("该操作不合法，请检查您输入的行和列数");
                                    scanner.next();
                                }
                            } else {
                                System.out.println("输入不合法");
                                scanner.nextLine();
                            }

                            break;
                        case 2:
                            // 提子
                            System.out.println("请输入提子的坐标(row,col)如(1,2)");

                            if (scanner.hasNext("[0-9],[0-9]")) {
                                String[] split = scanner.next().split(",");
                                int row = Integer.parseInt(split[0]);
                                int col = Integer.parseInt(split[1]);
                                if (!wb.removePiece(current, row, col)) {
                                    System.out.println("该操作不合法，请检查您输入的行和列数");
                                    scanner.nextLine();
                                }
                            } else {
                                System.out.println("输入不合法");
                                scanner.nextLine();
                            }

                            break;
                        case 3:
                            System.out.println("虚着");
                            wb.giveUp();
                            break;
                        case 4:
                            // 查询占用
                            scanner.nextLine();
                            System.out.println("请输入查询位置 row,col，例如 3,4 ");
                            System.out.println(wb.queryCapture(parsePosition(wb).getPosition()));
                            scanner.nextLine();
                            break;
                        case 5:
                            // 统计己方棋子数目
                            System.out.println(wb.getCurrent().toString());
                            scanner.nextLine();
                            scanner.nextLine();
                            break;
                        default:
                            System.out.println("不合法的输入");
                            scanner.nextLine();
                            break;
                    }
                } else if (scanner.hasNext("end")) {
                    System.exit(0);
                } else {
                    System.out.println("请输入1-5或end");
//                    scanner.nextLine();
                    scanner.nextLine();
                }

            }


        } else {
            System.out.println("请输入chess或go");
        }
    }


}
