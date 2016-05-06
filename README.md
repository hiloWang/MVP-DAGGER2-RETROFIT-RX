# MVP-DAGGER2-RETROFIT-RX
a simple demo about MVP + DAGGER2 + RETROFIT + RX

# 导入项目时，注意事项
1. 项目需要JAVA8支持，如果报java8... no value 1错误
    
    解决：配置jdk8 环境变量：
  
    例：新建`JAVA8_HOME 值为C:\Program Files\Java\jdk1.8.0_77；`
      `Path下添加.;%JAVA8_HOME%\bin;`
      
2. gradle dsl method not found android() 错误
    
    解决：找到project下的gradle 删除有关android{}和dependencies{}下所有;

    例：`android {`
            `compileSdkVersion 2`
            `buildToolsVersion '23.0.0'`
        `}`
        `dependencies {}`
  
3. 有些人会报 java.lang.VerifyError 错误
    
    解决： 将project下的gradle2.0.0 修改成1.5.0即可，
  
    例：`classpath 'com.android.tools.build:gradle:2.0.0'`
替换成`classpath 'com.android.tools.build:gradle:1.5.0'`
并将GradleVersion2.10 修改成2.8 `gradleVersion '2.8'`
      
