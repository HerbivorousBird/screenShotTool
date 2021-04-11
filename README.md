文档待完善

该项目用于中南大学地理信息系《面向对象程序设计》课程设计，仅供学习交流使用。

内含Baidu OCR API秘钥，可供测试使用，若长期使用建议自己免费申请；如若发现秘钥滥用，将停止该秘钥。

该项目release版本已上传，若想测试/使用，可以下载后按提示运行。


![功能演示](https://gitee.com/HerbivorousBird/pic-bed/raw/master/img/20210411_182956254.gif)

面向对象程序设计课程大作业

《截图小工具》结题报告



班级：   地信1801  

姓名：   刘乐   

学号：  8211180103  

指导：   王盼成   

2020年7月3日



**目录**

[**一、**   **概述**.... 1](#_Toc44954266)

[**1.**     **功能**.... 1](#_Toc44954267)

[**2.**     **范围界定**... 1](#_Toc44954268)

[**二、**   **系统结构及开发环境**... 1](#_Toc44954269)

[**1.**     **结构**.... 1](#_Toc44954270)

[**2.**     **开发环境**... 1](#_Toc44954271)

[**3.**     **外部依赖**... 2](#_Toc44954272)

[**4.**     **数据记录**... 2](#_Toc44954273)

[**三、**   **可行性分析及测试**... 2](#_Toc44954274)

[**1.**     **截图**.... 2](#_Toc44954275)

[**2.**     **QRCode****二维码识别**... 2](#_Toc44954276)

[**3.**     **OCR****图片文字识别**... 3](#_Toc44954277)

[**4.**     **数据库连接**... 4](#_Toc44954278)

[**四、**   **功能和界面介绍**... 5](#_Toc44954279)

[**1.**     **主界面：**... 5](#_Toc44954280)

[**2.**     **系统托盘**... 5](#_Toc44954281)

[**3.**     **截图界面**... 5](#_Toc44954282)

[**4.**     **识别结果界面**... 6](#_Toc44954283)

[**5.**     **历史记录界面**... 6](#_Toc44954284)

[**五、**   **开发实现**... 7](#_Toc44954285)

[**1.**     **图形实体类：（Package  ml.liule.screenShotTool.graelement****）**... 7](#_Toc44954286)

[**1.1.**   **类图**.... 7](#_Toc44954287)

[**1.2.**   **基类（GraElement****）**... 7](#_Toc44954288)

[**1.3.**   **子类举例**... 8](#_Toc44954289)

[**2.**     **界面类：（Package  ml.liule.screenShotTool.ui****）**... 10](#_Toc44954290)

[**2.1.**   **MainWindow (extends JWindow)**. 10](#_Toc44954291)

[**2.2.**   **RecordsWindow(extends JFrame)**. 11](#_Toc44954292)

[**2.3.**   **ScrShotWindow(extends JWindow)**. 13](#_Toc44954293)

[**2.4.**   **ControlWindow(extends JFrame)**. 13](#_Toc44954294)

[**2.5.**   **TextWindow(extends JFrame)**. 15](#_Toc44954295)

[**2.6.**   **ChosenRectangle(extends Rectangle)**. 15](#_Toc44954296)

[**六、**   **遇到的问题解决及优化**... 16](#_Toc44954297)

[**1.**     **截图分辨率降低**... 16](#_Toc44954298)

[**2.**     **添加按钮图标后编译报错**... 16](#_Toc44954299)

[**3.**     **批量添加RadioButton****事件**... 16](#_Toc44954300)

[**4.**     **多线程优化**... 17](#_Toc44954301)

[**5.**     **使用内部类MyDate(extends  Date)**. 17](#_Toc44954302)

[**6.**     **关于太极图案主界面**... 17](#_Toc44954303)

 

**一、**   **概述**

**1.** **功能**

为方便日常生活、办公等需求，拟开发一款截图小工具。该工具具备以下功能：

1)  能够对屏幕指定区域进行截取，并保存为图片文件或放入剪贴板中

2)  能够在截取时，对图像进行简单编辑。包括调整截取范围、自由画笔、绘制折线、添加文字。

3)  能够对截取的图片进行二维码扫描

4)  能够对截取的图片进行OCR识别

5)  截图后能够将截图时间、图片、识别结果进行记录保存

6)  能够根据截图时间对保存的记录进行查询、复原和删除

 

**2.** **范围界定**

为了开发的简单，对功能范围进行界定

1)  二维码扫描功能只识别图片中的单个QR二维码

2)  一次截图只记录二维码扫描和OCR识别中的一个结果

3)  截图图片以及json文本以文件形式存放，不存放数据库中

4)  添加文字仅限单行文字，且不进行字体设置

5)  不加入用户自定义设置

6)  不对记录的数据进行加密和备份

7)  不设置快捷键

**二、**   **系统结构及开发环境**

**1.** **结构**

本工具采用以下结构

​           ![image-20210411174110798](https://gitee.com/HerbivorousBird/pic-bed/raw/master/img/20210411_174112336.png)  

**2.** **开发环境**

| IDE             | NetBeans 11.3 |
| --------------- | ------------- |
| Database Server | SQLite 3      |
| Language        | Java 1.8      |

客户端采用Java + Swing开发，采用maven进行项目管理。由于工具的作用并不是管理数据，并且为本地轻量应用，故数据库采用SQLite 3。

图片文件采用jpg文件格式存储，对图片的操作信息使用json格式文件存储。

**3.** **外部依赖**

项目采用Maven管理，可以方便地添加外部依赖。当前项目添加的依赖如下：

| **groupID**          | **artifactId** | **版本** | **作用**          |
| -------------------- | -------------- | -------- | ----------------- |
| com.baidu.aip        | java-sdk       | 4.12.0   | 进行OCR           |
| org.xerial           | sqlite-jdbc    | 3.31.1   | 连接SQLite3数据库 |
| org.swinglabs.swingx | swingx-core    | 1.6.5-1  | 日期选择器控件    |
| com.google.zxing     | core，javase   | 3.4.0    | 进行QR二维码识别  |

**4.** **数据记录**

由于记录的时间戳精确到毫秒，故假设记录不会重复。由于id字段并不打算显示在jTable上，所以id字段实际可省，将时间戳作为唯一标识。

l **SQLite 3****记录**

| **字段名** | **字段类型** | **含义**        |
| ---------- | ------------ | --------------- |
| id         | INT          | 唯一自增ID      |
| time       | BIGINT       | 截图时的时间戳  |
| result     | TEXT         | OCR或二维码结果 |

l **图片及json文本存储**

| 路径   | SQLite 3同路径下的file文件夹      |
| ------ | --------------------------------- |
| 文件名 | 时间yy年MM月dd日HH时mm分ss秒+后缀 |

**三、**   **可行性分析及测试**

**1.** **截图**

java.awt.Robot类的createScreenCapture()方法可以获得屏幕截图。

经测试，该函数可将当前屏幕截取下来并保存，截图的速度及清晰度均能满足要求。

**2.** **QRCode****二维码识别**

使用com.google.zxing外部依赖可以实现二维码的识别。

经测试，上述代码可以对二维码进行解码，且成功率较高。若未识别二维码，会抛出NotFoundException。

**3.** **OCR****图片文字识别**

使用百度开发者平台提供的通用文字识别服务。

识别准确率高，且使用门槛低。该服务每天有50000次的免费额度，能满足该工具的使用。

**4.** **数据库连接**

需要对截图记录进行存取，采用SQLite 3就足够满足要求。

**四、**   **功能和界面介绍**

**1.** **主界面：**

  ![image-20210411182048382](https://gitee.com/HerbivorousBird/pic-bed/raw/master/img/20210411_182049907.png)

​        l 该窗口为半透明圆形窗口    l 点击白色区域，出现截图界面    l 点击黑色区域，出现历史记录界面    l 拖拽移动窗口；右键隐藏窗口     

**2.**  **系统托盘**  

![image-20210411182139897](https://gitee.com/HerbivorousBird/pic-bed/raw/master/img/20210411_182141374.png)

​        l 左键单击开始截图，右键单击出现弹出菜单    l 菜单各个选项对应其含义上功能        

**3.** **截图界面**

 ![image-20210411182201066](https://gitee.com/HerbivorousBird/pic-bed/raw/master/img/20210411_182202567.png)

该窗口为全屏，在窗口内进行框选，框选区域以外变暗。框选结束后出现下方按钮。各功能按钮如下：

![image-20210411182228812](https://gitee.com/HerbivorousBird/pic-bed/raw/master/img/20210411_182230289.png)

![image-20210411182313209](https://gitee.com/HerbivorousBird/pic-bed/raw/master/img/20210411_182314714.png)

**4.** **识别结果界面**

 ![image-20210411182326789](https://gitee.com/HerbivorousBird/pic-bed/raw/master/img/20210411_182328265.png)

**5.** **历史记录界面**

![image-20210411182336422](https://gitee.com/HerbivorousBird/pic-bed/raw/master/img/20210411_182337952.png) 

l 双击“全选”表头，可以全选当前记录；同时表头文字变为“反选”；双击“反选”表头，可以选择当前未选择的所有记录。

l 双击记录的时间，显示截屏界面，还原当时截图场景

l 双击OCR或二维码结果，弹出结果文本界面

l 点击最近三天，过滤显示近三天的记录

l 点击  ，弹出日期选择器。选择后筛选时间范围内的记录。

l 点击“删除选中”，删除当前选择的记录；点击“删除7天前”，则删除七天前的记录。

**五、**   **开发实现**

**1.** **图形实体类：****（Package ml.liule.screenShotTool.graelement）**

该包内包含四个类：GraElement，MyPolyline，MyRectangle，MyStringGra。其中GraElement为虚类，其他类都从其继承得到。继承得到的类实现了基类的toJson(),fromJson(),draw()方法，以及weight、color字段。

**1.1.****类图**

 ![image-20210411182359912](https://gitee.com/HerbivorousBird/pic-bed/raw/master/img/20210411_182401413.png)

**1.2.****基类（GraElement）**

**1.3.****子类举例**

l **MyRectangle****的toJson()方法**

l **MyPolyline****的draw()方法**

l **MyPolyline****的polyline2WKT()、WKT2polyline(String WKT)方法**

**2.** **界面类：****（Package ml.liule.screenShotTool.ui）**

**2.1.****MainWindow (extends JWindow)**

该类继承自JWindow，是程序的主入口。设置窗口的shape为Ellipse2D.Float对象使得其为圆形；设置Opacity使其半透明。

**2.1.1.**   **主要字段介绍**

l **public static Connection dbConnection**

用于保留与数据库的连接，一次连接，程序运行期间都可访问数据库。声明静态变量，方便其他类访问。

l **public static String recordsPath**

用于设定截图记录的文本及图片，以及SQLite3的db文件的父路径。

l **private Area yinArea**

太极黑色（阴）所在的区域。将由initTaiChiArea()进行初始化。该变量以及yangArea用于绘制太极并判断鼠标所在的区域。

**2.1.2.**   **主要方法介绍**

l **initComponents()**

初始化主窗口。包括设置窗口大小、形状、透明度等属性，并添加事件处理函数。

初始化托盘。包括设置图标、添加PopupMenu及其事件处理。

**核心代码：**

l **initTaiChiArea()**

通过若干Shape对象的交并补运算获得太极的阴阳区域，方便判断鼠标点击的区域是阴还是阳。不同的区域执行不同的事件处理。

l **initFile()**

初始化存储记录的路径，调用connectToDB()函数初始化数据库连接。如果路径不存在，则新建路径并弹出首次使用的about文字窗口简单介绍软件的操作使用。

l **connectToDB()**

连接到数据库，并完成初始建表工作。将数据库的连接保存在Connection对象dbConnection，后续需要读写数据库时不用再进行连接。数据库连接在关闭程序前释放。

**2.2.1.**   **主要方法介绍**

l **initTable()**

初始化tableModel并添加事件处理函数。该事件监听可以精确到点击的每一个单元格。

l **deleteByDate(MyDate date)**

通过日期作为查询条件值删除记录并删除对应的记录文件。整个类中关于删除记录的操作都基于这个函数实现。包括按时间段删除记录。如果是按时间段删除，SQL使用WHERE语句BETWEEN AND判断，效率更高，但是不便于同步删除记录的文件。

点击表头“全选”，全选当前所有记录并设置为“反选”；点击“反选”时将选择当前所有未选择，并且表头又变回“全选”。

**2.3.****ScrShotWindow(extends JWindow)**

该类继承自JWindow，是截图绘图的总窗口，响应了大量的事件。包含一个ControlWindow对象。该对象对其进行绘图和操作。

**2.3.1.**   **主要方法介绍**

l **onMouseWheelMoved(MouseWheelEvent e)**

该函数用于捕捉鼠标滚轮事件来调整画笔粗细以及字体大小

**2.4.****ControlWindow(extends JFrame)**

该类主要是对ScrShotWindow的状态等进行控制。在最初进行设计时，该类叫做ToolsWindow，只显示工具条界面并处理界面相关的事件，对ScrShotWindow上绘制图形元素则由一个叫做GrasControl的类完成。但是在编写过程中发现其与GrasControl类高度耦合，为了满足“高内聚低耦合”的理念，最终将两个类合二为一。

**2.4.1.**   **主要方法介绍**

l **fromJson(JSONObject control)**

从一个JSONObject中解析各个图形元素，从而达到复原截图时场景。这些图元都是通过基类GraElement继承而来。通过向下转换，使得GraElement转换成对应的图元。

l **draw()**

将各个图元绘制到tempImage中，然后将tempImage绘制到ScrShotWindow窗口中。

l **saveImageClipboard()**

将对象放到剪贴板中需要该对象实现Transferable接口，而BufferedImage并没有实现，因此需要利用匿名内部类实现这个接口。

**2.5.****TextWindow(extends JFrame)**

该类比较简单，仅有一个在JScrollPane布局下的JTextArea用于显示多行文字。

**2.5.1.**   **主要方法介绍：**

l **copyToClipboard()**

将String保存到剪贴板的方法较BufferedImage简单地多。

**2.6.**  **ChosenRectangle(extends Rectangle)**

该类继承自Rectangle。主要用于形成一个可调整的矩形用于截图的框选区域。最初直接使用Rectangle类，单其功能不足，例如其outcode方法只能获得WESN四个方向，而在调整截图区域时需要8个方位。故继承Rectangle并对部分函数进行覆写。

**2.6.1.**   **主要方法介绍：**

l **reSizeBy(Point p)**

通过一个点对其大小方位进行调整。

l **toString****和fromString方法**

便于在ControlWindow类的toJson和fromJson中将其保存为JSON格式，并从中还原。

**六、**   **遇到的问题解决及优化**

**1.** **截图分辨率降低**

在jdk 13下且设置了屏幕缩放的windows环境，Robot对象的createScreenCapture()方法获得的截图会模糊。分辨率只有  。将jdk目录下java.exe兼容性高DPI设置为“系统”，或者用jdk 8即可解决。

**2.** **添加按钮图标后编译报错**

使用NetBeans等IDE为界面的按钮等控件添加图片时，实际上会使用getClass().getResource(相对于包名根目录的路径)，而编译的class文件在项目的target文件夹下，这会使得在运行时找不到图片资源。将图片资源拷贝到target目录下的对应位置即可解决。

**3.** **批量添加RadioButton事件**

设置颜色选择RadioButton时，由于有10个RadioButton，如果手动为每个按钮添加事件处理，则工作量大且影响可读性。添加ButtonGroup后可以实现group里单选，但是仍无法统一添加事件。尝试获得ButtonGroup里所有按钮，然后遍历为其添加事件（应该有更好的方法）。

**4.** **多线程优化**

由于OCR识别访问服务器需要一定的时间，点击OCR识别后会造成程序“假死”，故需要单独开一个线程处理OCR识别。

**5.** **使用内部类MyDate(extends Date)**

在RecordsWindow类中定义了MyDate这个类。构建这个类是为了让TableModel存储Date类型并且能按照指定格式显示。之所以要存储Date类型，而不直接存储String，是因为在数据库中删除记录的相关操作需要将时间戳作为查询条件。如果存储字符串，则还需字符串转Date，然后再转时间戳来进行查。不过数据库里也能存放date类型，但我没有去尝试，而是只存储了时间戳。如果不定义这个类，对toString进行重载，则JTable显示的是世界标准时的格式，而不是指定格式。

**6.** **关于太极图案主界面**

最开始设计时，主窗口就两个按钮——“开始截图”和“查看记录”。想对界面进行个性话但又没合适的想法。大概是刚从前一阵子体育太极拳考试的支配中走出来，突然想到可以用太极的阴阳去对应两个按钮。在某种意义上，阳代表“新”，刚好与开始截图的操作相对应；阴代表“旧”，与查看历史记录的操作相对应。当然这只是很牵强的说法，并没有比较强的设计理念，单纯觉得好看并且刚好对应两个按钮（如果有五个按钮，我或许会做个五角星）并且好画罢了。

**具体实现：**

由于在之前对NetBeans使用操作中已经大致明白窗口属性的常见属性设置，注意到有setShape(Shape s)方法，只需要传入实现Shape接口的对象就可设置窗口的形状。通过jdk文档，查阅Shape，发现其接口下实现了Ellipse2D.Float类，即浮点精度的椭圆类。通过   即可将窗口设为圆形，直径为窗口的宽。

在科学计算与Python语言这门课中，我曾利用turtle库绘制过太极图形。尽管turtle的绘画逻辑与Graphics的逻辑不同，但稍微改改就能用了。Graphics绘制好太极图案后，想实现点击黑白区域产生不同效果，最初的想法是判断鼠标点击位置的颜色，但觉得实现起来效率太低。然后想起在写ChosenRectangle这个类时，从Rectangle继承得到，其中Rectangle有contains(Point p)的方法用来判断一个点不在在矩形内。又经过查找后，找到Area类，可以通过Shape得到，并且还能进行图像的交并补等运算。于是通过Area对象的交并补获得了阴阳两个形状，方便判断鼠标所在位置在哪个区域内，由于Area类也实现了Shape接口，所以用Graphics绘制时也可直接绘制这两个区域。
