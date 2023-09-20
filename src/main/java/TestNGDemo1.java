import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Create by swtywang on 9/20/23 1:35 PM
 */
public class TestNGDemo1 {
    @BeforeTest
    public void before() {
        System.out.println("beforeTest of TestNGDemo1");
    }

    @AfterTest
    public void after() {
        System.out.println("afterTest of TestNGDemo1");
    }

    @Test
    public void testMinius() {
        String str = "TestNG is working fine";
        Assert.assertEquals("TestNG is working fine", str);
        System.out.println("testMinius....");
    }

}
