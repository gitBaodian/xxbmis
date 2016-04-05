

1、进入项目目录project后
    将eclipse目录里面的文件拷贝到project目录，然后再在eclipse上导入

2、加入tomcat时，需要将Libraries中tomcat环境变量修改下。

3、修改tomcat发布目录，
  a、双击servers中的tomcat，打开tomcat配置页面
  b、在Server Options中，勾选第一项serve modules without publishing，
  	使Server Locations变成可以编辑状态
  c、在Server Locations中选中第二项Use Tomcat installation
  d、把Deploy path更改为webapps
  e、然后把第二步，把Server Options的第一项去掉
  f、保存即可