# Lab1-1173710229
Lab1-1173710229 created by GitHub Classroom
2019.3

## P1 Magic Square
貌似完成了

## P2 Turtle
貌似完成了

## P3 FriendshipGraph
- 貌似完成了，已经添加测试用例；
- 可以扩展到有向图；
- 添加相同名称的人时能够抛出异常，但有仍bug
- 貌似实验手册中没有提到说能够新加文件，所以检测重名在每次新建graph对象时会重置。

## P4 Twitter
- 貌似完成了，添加了测试用例，计算influencer的正确性还不能保证，仍然有待优化
- 添加了getSmarter的方法和测试

附：我真实测试Twitter的@符号何时识别为mention和其识别结果，好奇的可以自行研究，但是实验中没有要求那么多！**实际应该根据实验手册和网页上的要求编写！**

| 识别内容 | 是否识别为@  | 识别结果 |
| :-: | :-: | :-: |
| abcd@name | False | - |
| @https:/ | True | @https |
| @name:/ | True  | @name |
| @abc:// | False | - |
| @https://[xxx] | False | - |
| @hit.edu.cn | True | @hit |

