eclipse {


  wtp {
    component {
        
        resource sourcePath: 'src/main/webapp', deployPath: '/'                                                       
                    
        file {          
                withXml {
                    it.asNode()'wb-module'[0].children()                                                              
                        .findAll { it.name() == 'dependent-module' }
                        .findAll { it.attribute('handle').startsWith 'module:/resource' }                             
                        .each {
                            ext['jarName'] = it.attribute('handle').tokenize('/').last().replace('_','-') + '.jar'    
                            it.attributes().put('archiveName', jarName)                                               
                         }                                                                                            
                }                                                                                                     
            }                                                                                                         
    } 
  }   
}                                                                                                                     
      
      
                                                                                                                      
war { 
    webXml = file('src/main/webapp/WEB-INF/web.xml') 
      
}                                                                                                                     
      
      
if(!project.hasProperty('profile')){                                                                                  
        project.ext['profile'] = "shtest"
}     
                                                                                                                      
switch(project.profile){
      
        default:
     
                 sourceSets{                                                                                          
                        main{
                                resources{
                                        srcDir 'src/main/resources-'+project.profile                                  
                                }                                                                                     
                        }                                                                                             
                }                                                                                                     
        break                                                                                                         
                                                                                                                      

}   
