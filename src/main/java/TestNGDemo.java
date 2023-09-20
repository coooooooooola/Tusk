import org.testng.Assert;
import org.testng.annotations.*;

//https://testng.org/doc/documentation-main.html#introduction
/**
 * 常用注解：
 * @Test: 标记需要运行的测试方法；
 *
 * @BeforeMethod: 在每一个@Test测试方法运行之前执行；
 * @AfterMethod: 在每一个@Test测试方法完成之后执行；
 *
 * @BeforeTest: 在testng.xml中<test>标志代表的测试事务开始之前执行，先于@BeforeClass执行；
 * @BeforeClass: 在类中第一个测试方法被执行之前执行，晚于@BeforeTest执行；
 *
 * @AfterClass: 在类中所有方法运行完成后执行；
 * @AfterTest: 在testng.xml中<test>标志代表的测试事务完成后执行；
 *
 * @BeforeSuite: 在testng.xml中<suite>标志代表的测试集合开始前执行；
 * @AfterSuite: 在testng.xml中<suite>标志代表的测试集合完成后执行；
 *
 * @BeforeGroup: 在相同组的第一个测试用例开始前执行；
 * @AfterGroup: 在相同组的所有测试用例完成后执行；
 */

/**
 * Create by swtywang on 9/19/23 5:25 PM
 */

public class TestNGDemo {

    @BeforeSuite
    public void beforeSuit(){
        System.out.println("before suit");
    }
    @BeforeTest
    public void before() {
        System.out.println("beforeTest of TestNGDemo");
    }
    @BeforeClass
    public void beforeClass() {
        System.out.println("beforeClassTest");
    }
    @BeforeMethod
    public void beforeMethodClass() {
        System.out.println("beforeMethodClassTest");
    }

    @Test
    public void testAdd() {
        String str = "TestNG is working fine";
        Assert.assertEquals("TestNG is working fine", str);
        System.out.println("testAdd....");
    }

    @AfterTest
    public  void after() {
        System.out.println("after ");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("AfterClassTest");
    }

    @AfterMethod
    public void afterMethodClass() {
        System.out.println("afterMethodClassTest");
    }

}