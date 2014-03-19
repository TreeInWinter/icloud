
https://sourcer.travelzen.com/svn/yr/projects/mobile_ads/mobile/branches/develop/workspace/searchads_logrouter/build.gradle

这段可以强制修改  生成的wtp文件的内容

eclipse {
wtp {
    component {


        minusConfigurations += configurations.myexclude


        file {
                withXml {
                    root -> root.asNode()'wb-module'[0].children()
                        .findAll { dep -> dep.name() == 'dependent-module' }
                        .findAll { dep -> dep.attribute('handle').startsWith 'module:/classpath' }
                        .each {
                                //println it.attribute('handle')
                            dep ->
                                    //if( dep.asNode().attribute('handle').startsWith ('module:/classpath')){
                                        if( dep.attribute('handle').contains ('commons-logging' )
                                           || dep.attribute('handle').contains ('slf4j-log4j12' )
                                                ){
                                                //println dep
                                                root.asNode()'wb-module'[0].remove(dep)
                                        }


                                        ext['jarName'] = dep.attribute('handle').tokenize('/').last().replace('_','-') + '.jar'
                                        dep.attributes().put('archiveName', jarName)
                                //}
                        }
            }

    }
  }
}

}


