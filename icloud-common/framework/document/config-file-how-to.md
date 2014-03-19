
在appxxx.properties 文件里写最基本的配置文件，appxxx.properties放在 src/main/resources/test|product文件夹下。

并通过 SimplePropertiesUtil 读取，

复杂配置， 特别是涉及  数字， 布尔型的， 使用 yaml格式进行配置 ，

代码参考 
/repo/yunreader/jproxy-bridge/src/main/java/com/travelzen/jproxybridge/cometd/Configurer.java


    @Bean
    public JProxyBridgeConfig jProxyBridgeConfig(){
    	
    	String configUrl = System.getProperty("jproxybridge.config");
		if (configUrl == null)
			configUrl = JProxyBridgeConfig.DEFAULT_CONFIGURATION;
		
		InputStream input = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream( configUrl);
	 
		org.yaml.snakeyaml.constructor.Constructor constructor = new org.yaml.snakeyaml.constructor.Constructor(
				JProxyBridgeConfig.class);

		Yaml yaml = new Yaml(constructor);
		JProxyBridgeConfig instance = (JProxyBridgeConfig) yaml.load(input);
		
		return instance;

    }
    