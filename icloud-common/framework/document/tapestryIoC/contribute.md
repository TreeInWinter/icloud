http://tapestry.apache.org/tapestry-ioc-configuration.htm

public static void contributeFileServicerDispatcher(MappedConfiguration<String,FileServicer> configuration,
  
    @InjectService("TextFileServicer") FileServicer textFileServicer,
    @InjectService("PDFFileServicer") FileServicer pdfFileServicer)
  {
    configuration.add("txt", textFileServicer);
    configuration.add("pdf", pdfFileServicer);
  }  
  
  
  
  
 Unordered Collection
A service builder method can collect an unordered list of values by defining a parameter of type java.util.Collection. Further, you should parameterize the type of collection. Tapestry will identify the parameterized type and ensure that all contributions match.

One thing to remember is that the order in which contributions occur is unspecified. There will be a possibly large number of modules, each having zero or more methods that contribute into the service. The order in which these methods are invoked is unknown.

For example, here's a kind of Startup service that needs some Runnable objects. It doesn't care what order the Runnable objects are executed in.

  public static Runnable buildStartup(final Collection<Runnable> configuration)
  {
    return new Runnable()
    {
      public void run()
      {
        for (Runnable contribution : configuration)
          contribution.run();
      }
    };
  }  
Here we don't even need a separate class for the implementation, we use an inner class for the implementation. The point is, the configuration is provided to the builder method, which passes it along to the implementation of the service.

On the contribution side, a service contribution method sees a Configuration object:

  public static void contributeStartup(Configuration<Runnable> configuration)
  {
    configuration.add(new JMSStartup());
    configuration.add(new FileSystemStartup());
  }    
  
  
  
  
  http://tapestry.apache.org/ioc-cookbook-service-configurations.html
  
  