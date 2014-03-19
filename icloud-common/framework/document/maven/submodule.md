${basedir} (short for ${project.basedir}) is a directory where project's pom.xml file resides. 


Therefore, for child modules this property is going to contain paths to module1 and module2 directories correspondingly.

If you want to configure javadoc plugin in the parent pom.xml so it would be effective for child modules, 
you should use ../src/main/javadoc/stylesheet.css.



http://www.sonatype.com/books/mvnref-book/reference/pom-relationships-sect-pom-best-practice.html

3.6.2. Multi-module vs. Inheritance
There is a difference between inheriting from a parent project and being managed by a multimodule project. A parent project is one that passes its values to its children. A multimodule project simply manages a group of other subprojects or modules. The multimodule relationship is defined from the topmost level downwards. When setting up a multimodule project, you are simply telling a project that its build should include the specified modules. Multimodule builds are to be used to group modules together in a single build. The parent-child relationship is defined from the leaf node upwards. The parent-child relationship deals more with the definition of a particular project. When you associate a child with its parent, you are telling Maven that a project’s POM is derived from another.

To illustrate the decision process that goes into choosing a design that uses inheritance vs. multi-module or both approaches consider the following two examples: the Maven project used to generate this book and a hypothetical project that contains a number of logically grouped modules.
