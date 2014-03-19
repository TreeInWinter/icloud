##代码段的写法：

先顶行写一些字， 然后空一行， 然后行首一个tab或者4个空格


比如:     

	public void dataAccessCode(){ 
	try{ 
	..some code that throws SQLException 
	}catch(SQLException ex){ 
	throw new RuntimeException(ex); 
	} 
	} 
	
	
	
列表项目可以包含多个段落，每个项目下的段落都必须缩进 4 个空格或是 1 个制表符：

1.  This is a list item with two paragraphs. Lorem ipsum dolor
    sit amet, consectetuer adipiscing elit. Aliquam hendrerit
    mi posuere lectus.

    Vestibulum enim wisi, viverra nec, fringilla in, laoreet
    vitae, risus. Donec sit amet nisl. Aliquam semper ipsum
    sit amet velit.

2.  Suspendisse id sem consectetuer libero luctus adipiscing.

如果你每行都有缩进，看起来会看好很多，当然，再次地，如果你很懒惰，Markdown 也允许：

*   This is a list item with two paragraphs.

    This is the second paragraph in the list item. You're
only required to indent the first line. Lorem ipsum dolor
sit amet, consectetuer adipiscing elit.

*   Another item in the same list.
如果要在列表项目内放进引用，那 > 就需要缩进：

*   A list item with a blockquote:

    > This is a blockquote
    > inside a list item.
如果要放代码区块的话，该区块就需要缩进两次，也就是 8 个空格或是 2 个制表符：

*   一列表项包含一个列表区块：

        <代码写在这>
        
        


Markdown 语法介绍及使用说明
在 13/04/12 01:53 AM 由 COSTONY 发表
在教程正式开始之前，请先打开 Markdown 测试工具 ，这里面的所有示例都可以直接在该测试工具中测试。

Markdown 是这样两种东西，首先，他是一个格式化语法结构，其次他是一个使用Perl语言编写的用来实现Markdown至HTML转换的工具，它的设计目标就是让写作者尽可能使用容易阅读的语法书写，而不必被繁琐的HTML标签结构所纠缠，就像下面这样就可以创建一个一级标题的HTML结构：

# Markdown 介绍 #
Markdown 基本语法详解

使用 Markdown 一般情况下是没有可视化编辑界面的（Markdown与HTML的不同点在于它的设计初忠就是要写作者使用转Markdown代码（这里称之为代码吧，其实不是，它就是纯文本）），有一些网站在使用的时候，写作界面分为两个区域，预览区域和编辑区域，编辑区域就是写 Markdown，预览区会动态的将编辑区的修改即时的转换为HTML后展示出来以达到可视化编辑 的效果，但是我们这里所讲的，仅仅只是Markdown的语法。

段落、标题以及块引用
要使用 Markdown创建段落应该是最简单的了，只需要某一段文字顶头书写然后前后空行即可，这里不再做过多说明。

标题的创建也十分简单，这里有两种方法，最常用的就是在要作为标题的文字下一行（不空行）添加超一个的= 号或者 - 号，例如：

这是一个一级标题
==============

这是一个二级标题
---------------------------
将被转换成为：

<h1>这是一个一级标题</h1>

<h2>这是一个二级标题</h2>
另外一种创建标题的方法能创建出所有六级标题，它们分别是在要创建标题的那一行方案最前面顶行创建一个或者多个 # 号，并以相同数量的 # 结尾，比如：

### 这是三级标题 ###

##### 这是五级标题
将被转换成为：

<h3>这是三级标题</h3>

<h5>这是五级标题</h5>
你可以看到，后面结尾的 # 是可以省略的。

块级引用的创建是在需要作为块引用的所有文本行前面加上 > 符号，比如：

> 这是块引用的第一行，将会成为块引用中的一个段落
> 
> 这是块引用的第二段
> ## 这是块引用中的一个二级标题
转被转换成为下面这样的HTML代码：

<blockquote>
<p>这是块引用的第一行，将会成为块引用中的一个段落</p>

<p>这是块引用的第二段</p>

<h2>这是块引用中的一个二级标题</h2>
</blockquote>
加强与强调
Markdown 使用 星号(*) 与 下划线(_) 表示加强与强调，如下所示：

胡潇说：”我 *不想* 和鸟蛋睡一起！“
刘钊说：“我 _更不想_ 和潇老娘子睡一起！”

然后潘韬就说了：“你们两个都 **不要睡了** 吧！”
潘韬还说了：“ __都跟__ 哥睡！”
将被转换成为：

<p>胡潇说：”我 <em>不想</em> 和鸟蛋睡一起！“
刘钊说：“我 <em>更不想</em> 和潇老娘子睡一起！”</p>

<p>然后潘韬就说了：“你们两个都 <strong>不要睡了</strong> 吧！”
潘韬还说了：“ <strong>都跟</strong> 哥睡！”</p>
列表
无序列表可以使用星号，加号或者减号（*、+、-）创建，它们三者效果都是一样的，比如下的示例：

* 第一项
* 第二项
* 第三项
和

+ 第一项
+ 第二项
+ 第三项
还有

- 第一项
- 第二项
- 第三项
都会被转换成为：

<ul>
<li>第一项</li>
<li>第二项</li>
<li>第三项</li>
</li>
有序列表则直接在每一项前面加上阿拉伯数字与小数点即可，如下：

1. 有序列表第一项
2. 有序列表第二项
3. 有序列表第三项
将被转换成为：

<ol>
<li>有序列表第一项</li>
<li>有序列表第二项</li>
<li>有序列表第三项</li>
</ol>
如果您在每一个列表项之间添加了一个空行，那么主会在每一个列表项中创建一个段落，比如：

* 这是一个列表项

  这是第一列表项中的第二个段落

* 这是列表的另一个项目
将被转换成为：

<ul>
<li><p>这是一个列表项</p>
<p>这是第一列表项中的第二个段落</p></li>
<li><p>这是列表的另一个项目</p></li>
</ul>
链接
Markdown使用两种方法创建链接，行内创建与引用创建，行内创建一般使用得比较多，它的创建语法是下面这样的：

这是一个示例[链接](http://aitine.com)。
转换成为：

<p>这是一个示例<a href="http://aitine.com">链接</a>。</p>
如果要为链接指定 title 属性，刚可以使用下面这样的：

这是一个示例[链接](http://aitine.com "这是一个标题")。
而以引用的方式创建链接一般用在学术论文上面比较多，或者另一种情况，如果某一个链接在文章中多处使用，那么使用引用 的方式创建链接将非常好，它可以让你对链接进行统一的管理，这有点类似于 LaTeX 对文档引用的管理，创建方法如下：

我最喜欢的几个网站是[Google][1] 、[艾天科技][2] 以及 [自己的博客][3]，但是最喜欢的却是 [艾天项目管理平台][atoa]，因为 [谷歌][1] 老是不能被访问到。

[1]:http://www.google.com "Google"
[2]:http://aitine.com "Aitine Technology"
[3]:http://note.costony.com "Notes of Cos Tony"
[atoa]:http://us.aitine.com "艾天项目管理平台"
转换后得到：

<p>我最喜欢的几个网站是<a href="http://www.google.com" title="Google">Google</a> 、<a href="http://aitine.com" title="Aitine Technology">艾天科技</a> 以及 <a href="http://note.costony.com" title="Notes of Cos Tony">自己的博客</a>，但是最喜欢的却是 <a href="http://us.aitine.com" title="艾天项目管理平台">艾天项目管理平台</a>，因为 <a href="http://www.google.com" title="Google">谷歌</a> 老是不能被访问到。</p>
从Markdown链接的引用创建方法中，你应该了解到一些基本的引用管理知识，要知道，专业的印刷行业里面、学术论文写作里面一般都不是使用 Word 或者什么可视化编辑工具来书写的，因为这些工具的引用管理功能都太弱了，而且使用起来十分的麻烦，一般都是使用 Tex 或者 LaTex ，有兴趣的可以去了解一下。

图片
图片的语法与链接的十分相似，行内创建时：

![Alt 文本](/path/to/img.jpg "图片 Title")
转换成为：

<p><img src="/path/to/img.jpg" alt="Alt 文本" title="图片 Title" /></p>
引用式创建也很相似：

![Alt 文本][id]

[id]:/path/to/img.jpg "标题"
转换结果是：

<p><img src="/path/to/img.jpg" alt="Alt 文本" title="标题" /></p>
可以看到图片与链接的引用方式创建时，引用格式是一样的，所以，一个引用可以同时被图片和链接所使用。

添加代码段
添加代码段应该是最简单的了，只需要把你想发布的任何代码不要顶头写就行，一般在前面留四个空格，比如：

我们的代码如下：

    <p>这是HTML代码</p>
转换后得到：

<p>我们的代码如下：</p>

<pre>&amp;lt;p&amp;gt;这是HTML代码&amp;lt;/p&amp;gt;&#10;    </pre>
所有以如特殊符号都会被自动转义。

Markdown 语法进阶

在 Markdown 中嵌入原生HTML代码
在 Markdown 代码中嵌入 HTML代码，如果你想直接在 Markdown 中嵌入HTML代码，那么你只需要将代码直接写在需要的地方即可：

这是一个段落。

<table>
    <tr>
        <td>这是用原生的HTML代码写的表格。</td>
    </tr>
</table>

这是另一个段落。
转换之后得到：

<p>这是一个段落。</p>

<table>
    <tr>
        <td>这是用原生的HTML代码写的表格。</td>
    </tr>
</table>

<p>这是另一个段落。</p>
你还需要注意的一点是，Markdown 是不处理HTML的块级元素中的内容的，比如你可以直接在 <p> 标签中使用 * 号：

<p>这是一个 *段落*</p>
在转换之后还是：

<p>这是一个 *段落*</p>
相反的，HTML的行内标签却可以在 Markdown 的语法的任何地方使用（除了代码段中）：

这是一个文本段，<span>这个被span标签</span> 包裹了。
转换后得到：

<p>这是一个文本段，<span>这个被span标签</span> 包裹了。</p>
行内元素包括 span、cite、del 等。

自动转义特殊字符
在HTML中，有两个字符是十分特殊的，他们是 < 和 & 符号，< 表示一个HTML标签的开始，&表示HTML特殊字符转义符的开始，在 Markdown 中这两个字符将会自动被转义，就如你刚才所看到的这一段话：

在HTML中，有两个字符是十分特殊的，他们是 < 和 & 符号，< 表示一个HTML标签的开始，&表示HTML特殊字符转义符的开始，在 Markdown 中这两个字符将会自动被转义，就如你刚才所看到的这一段话：
Markdown 转换之后得到的将是：

<p>在HTML中，有两个字符是十分特殊的，他们是 &lt; 和 &amp; 符号，&lt; 表示一个HTML标签的开始，&amp;表示HTML特殊字符转义符的开始，在 Ma
