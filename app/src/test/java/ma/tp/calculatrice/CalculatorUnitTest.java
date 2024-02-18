package ma.tp.calculatrice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class CalculatorUnitTest {
    MainActivity mainActivity = new MainActivity();
    @Test
    public void replaceOperator() {
        assertEquals("2*2", mainActivity.replaceOperators("2x2"));
    }
}