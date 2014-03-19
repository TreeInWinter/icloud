默认情况下， war项目不会被打包为jar ，不能install和 deploy jar包，

加入以下配置后就可以了。

<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-jar-plugin</artifactId>
				<executions>
					<execution>
						<id>make-a-jar</id>
						<phase>compile</phase>
						<goals>
							<goal>jar</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
			<!--  install jar to local repository -->
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-install-plugin</artifactId>
				<executions>
					<execution>
						<phase>install</phase>
						<goals>
							<goal>install-file</goal>
						</goals>
						<configuration>
							<packaging>jar</packaging>
							<artifactId>${project.artifactId}</artifactId>
							<groupId>${project.groupId}</groupId>
							<version>${project.version}</version>
							<file>
								${project.build.directory}/${project.artifactId}-${project.version}.jar
							</file>
						</configuration>
					</execution>
				</executions>
			</plugin>
			<!--  deploy jar to remote repository -->
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-deploy-plugin</artifactId>
				<executions>
					<execution>
						<phase>deploy</phase>
						<goals>
							<goal>deploy-file</goal>
						</goals>
						<configuration>
							<packaging>jar</packaging>
							<generatePom>true</generatePom>
							<!--<url>${project.distributionManagement.repository.url}</url>-->
							<url>${project.distributionManagement.snapshotRepository.url}</url>
							<artifactId>${project.artifactId}</artifactId>
							<groupId>${project.groupId}</groupId>
							<version>${project.version}</version>
							<file>${project.build.directory}/${project.artifactId}.jar</file>
						</configuration>
					</execution>
				</executions>
			</plugin>

 
<plugin>  
                <groupId>org.apache.maven.plugins</groupId>  
                <artifactId>maven-jar-plugin</artifactId>  
                <version>2.3.1</version>  
                <executions>  
                    <execution>  
                        <id>my-jar</id>  
                        <phase>package</phase>  
                        <goals>  
                            <goal>jar</goal>  
                        </goals>  
                        <configuration>  
                            <classifier>api</classifier>  
                        </configuration>  
                    </execution>  
                </executions>  
            </plugin>  
 

其中要注意的是，声明 classifier 为任何你需要的字符。



因为这样可以避免一个问题：

如果在 pom 中定义 package 为 war，

运行 maven package 后，在项目 target 目录下成功产生 XX.jar 和 XX.war ，

但运行 maven install，却只把 XX.jar 安装到 repositoy 中的  XX.war，其中内容仅是 jar 包内容。

而XX.war 就不install 到 repositoy 了。

 

加入 classifier 后产生： XX-api.jar 和 XX.war,

那么maven install 会将这两个文件同时安装到本地 repository 中，不会产生上述问题。


