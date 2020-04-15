# unitjar

## usage

```bash
$ mvn clean package -DskipTests
$ java -jar target/unitjar-0.0.1-fat-tests.jar                                                                                                                                       [Wed Apr 15 12:48:00 2020]
Running tests!
.UnitJarTest test

Time: 0.005

OK (1 test)

```

## stackoverflow article

demo an executable jar for testing to include all tests 

according to [stackoverflow](https://stackoverflow.com/a/36047816), i create this repo to verify its operation.

You should not access test classes from your application code, but rather create a main (the same main) 
in the test scope and create an additional artifact for your project.

However, in this additional artifact (jar) you would need to have:

- The test classes
- The application code classes
- External dependencies required by application code (in compile scope)
- External dependencies required by the test code (in test scope)

Which basically means a fat jar with the addition of test classes (and their dependencies). 
The [Maven Jar Plugin](https://maven.apache.org/plugins/maven-jar-plugin/) 
and its [test-jar](https://maven.apache.org/plugins/maven-jar-plugin/) goal would not suit this need. 
The [Maven Shade Plugin](https://maven.apache.org/plugins/maven-shade-plugin/) 
and its [shadeTestJar](https://maven.apache.org/plugins/maven-shade-plugin/shade-mojo.html#shadeTestJar) option would not help neither.

So, how to create in Maven a fat jar with test classes and external dependencies?

The Maven Assembly Plugin is a perfect candidate in this case.

Here is [a minimal POM sample](doc/minpom.xml).

The configuration above is setting the main class defined by you in your test classes. But that's not enough.

You also need to create a [descriptor file](https://maven.apache.org/plugins/maven-assembly-plugin/descriptor-refs.html), 
in the `src\main\assembly` folder an [assembly.xml](src/main/assembly/assembly.xml) file with the following content.

The configuration above is:

- setting external dependencies to be taken from the test scope (which will also take the compile scope as well)
- setting a fileset to include compiled test classes as part of the packaged fat jar
- setting a final jar with fat-tests classifier (hence your final file will be something like sampleproject-1.0-SNAPSHOT-fat-tests.jar).

You can then invoke the main as following (from the target folder):

`java -jar sampleproject-1.0-SNAPSHOT-fat-tests.jar`

From such a main, you could also invoke all of your test cases as following:

- Create a JUni test suite
- Add to the test suite the concerned tests
- Invoke the test suite from your plain Java main

Example of test suite:

```java
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AppTest.class })
public class AllTests {

}
```

Note: in this case the test suite is only concerning the AppTest sample test.

Then you could have a main class as following:

```java
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

public class MainAppTest {
    public static void main(String[] args) {
        System.out.println("Running tests!");

        JUnitCore engine = new JUnitCore();
        engine.addListener(new TextListener(System.out)); // required to print reports
        engine.run(AllTests.class);
    }
}
```

The main above would then execute the test suite which will in chain execute all of the configured tests.

