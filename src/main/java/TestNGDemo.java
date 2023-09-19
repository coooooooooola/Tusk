import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Create by swtywang on 9/19/23 5:25 PM
 */

public class TestNGDemo {
    @Test
    public void testAdd() {
        String str = "TestNG is working fine";
        Assert.assertEquals("TestNG is working fine", str);
    }
}