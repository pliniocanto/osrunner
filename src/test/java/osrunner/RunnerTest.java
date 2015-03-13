package osrunner;

import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import br.com.pmac.osrunner.Runner;

public class RunnerTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {

		Runner runner = new Runner();

		String retorno = "";

		if (System.getProperty("os.name").toLowerCase().contains("linux")) {
			retorno = runner.exec("ls");
		} else {
			runner.exec("dir");
		}

		if (runner.getExitVal() == 0) {
			System.out.println("Runner executado com sucesso: \n >>>>> \n".concat(runner.getOutput()));
		} else {
			System.out.println("Runner executado, porem com erro de sistema: " + runner.getOutput());
		}

		assertNotEquals(runner.getOutput(), 0);

	}

}
