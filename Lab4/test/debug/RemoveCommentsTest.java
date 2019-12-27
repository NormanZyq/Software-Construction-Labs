package debug;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RemoveCommentsTest {

    /**
     * 使用所提供的例子来测试
     */
    @Test
    public void removeComments() {
        RemoveComments removeComments = new RemoveComments();
        String[] a = new String[]{"/*Test program */", "int main()", "{ ", "  // variable declaration ", "int a, b, c;",
                "/* This is a test", "   multiline  ", "   comment for ", "   testing */", "a = b + c;", "}"};
        List<String> strings1 = removeComments.removeComments(a);
        String s1 = printList(strings1);
        assertEquals("[\"int main()\",\"{ \",\"  \",\"int a, b, c;\",\"a = b + c;\",\"}\"]",s1);


        String[] b =  new String[]{"a/*comment", "line", "more_comment*/b"};
        List<String> strings2 = removeComments.removeComments(b);
        String s2 = printList(strings2);
        assertEquals("[\"ab\"]",s2);
    }

    /**
     *
     */
    @Test
    public void removeComments2() {
        RemoveComments removeComments = new RemoveComments();
        String[] c =  new String[]{""};
        List<String> strings3 = removeComments.removeComments(c);
        assertEquals("[]",printList(strings3));

        String[] d =  new String[]{"/*aa","*/b/*","*/c"};
        List<String> strings4 = removeComments.removeComments(d);
        assertEquals("[\"bc\"]",printList(strings4));

        String[] e =  new String[]{"public class CircularOrbitAPIs<E extends PhysicalObject> {","/**\n" +
                "     *  calculate a circular orbit's Entropy","liuJigQua*/"+"a/*a*/a"};
        List<String> strings5 = removeComments.removeComments(e);
        assertEquals("[\"public class CircularOrbitAPIs<E extends PhysicalObject> {\",\"aa\"]",printList(strings5));
    }

    /**
     * 将输入的List 转成标准的格式输出
     * @param list 输入的list
     * @return  一个将List<String>转成标准的String
     */
    private String printList(List<String> list){
        StringBuilder s = new StringBuilder();
        s.append("[");
        for(int i=0;i<list.size();i++){
            if(list.get(i).equals("")){continue;}
            s.append("\"");
            s.append(list.get(i));
            s.append("\"");
            if(i!=list.size()-1){
                s.append(",");
            }
        }
        s.append("]");
        return String.valueOf(s);
    }
}