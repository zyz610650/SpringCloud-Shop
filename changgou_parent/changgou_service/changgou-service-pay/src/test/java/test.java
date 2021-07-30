import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class test {

    @Test
    public void test1()
    {
        SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-DD日HH:mm:ss");
        Date date=new Date();
        System.out.println(  sdf.format(date));;

    }
}
