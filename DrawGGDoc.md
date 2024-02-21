## DrawGG函数教程
这里是DrawGG函数教程

简介：基于gg luaj包编写的扩展函数，包含绘制函数等。也可以用在官方luaj包上，只需要改一下Tools类的getContext方法即可。
		   

**提示：由于以下函数比较占内存，编写代码时建议赋值为局部变量。可以有效加快脚本运行速度**

**[函数演示效果代码和视频](#_382)**
**[源码和成品](#_454)**

@[TOC](函数教程)

下面开始函数教程：

**v1.1新增函数**
由于android12以上系统限制，使用穿透触摸事件的悬浮窗需要无障碍权限，但无障碍由于无障碍的不便捷性，新增了对非无障碍的支持
在脚本开头调用如下函数，即可在android12及以上系统免于开启无障碍

```lua
--强制禁用android12以上的无障碍申请
disDrawAcc()
```
原理：将悬浮窗的透明度强制锁定为0.8f，代价是所有绘制的内容透明度都会降低

**-----下面是画笔函数-----**
# 画笔函数
**1、新建画笔：newPaint**
函数作用：新建并返回一个画笔

```lua
-- 新建画笔
paint = newPaint()
```
## 设置抗锯齿
**1.1、setAntialias**
函数作用：开启和关闭画笔的抗锯齿功能。参数：true或false
true为开启抗锯齿，false为关闭抗锯齿。开启抗锯齿后会增加计算量。画笔默认关闭抗锯齿。
开启抗锯齿后绘制内容不会出现锯齿，更加圆润。
下图仔细看就能看出开了抗锯齿和没开的区别：
![画笔开了抗锯齿和没开的区别](https://img-blog.csdnimg.cn/20200921201937320.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ1OTI1MjMw,size_16,color_FFFFFF,t_70#pic_center)

示例：

```lua
-- 初始化画笔
paint = newPaint()
paint:setAntialias(true) -- 开启抗锯齿
```
## 设置颜色
**1.2、setColor**
函数作用：设置画笔画出的内容颜色。参数：ARGB十六进制字符串
比如：#FF998877，FF表示的是透明度，FF不透明，00未完全透明。99为红色深浅度，88为绿色深浅度，77为蓝色深浅度。透明值不写默认为FF。
详情请百度。

示例：

```lua
-- 初始化画笔
paint = newPaint()
paint:setColor("#FFFFFF") -- 设置画笔白色
```
## 设置样式
**1.3、设置画笔样式：setStyle**
函数作用：设置画笔画出的内容样式。参数：画笔样式
样式值分别有：**0**：描边，**1**：填充，**2**：描边并填充；
						 "**描边**"：描边， "**填充**"：填充，"**描边并填充**"：描边并填充
示例：

```lua
-- 初始化画笔
paint = newPaint()
paint:setStyle(0) -- 设置画笔样式为描边
--或者
paint:setStyle("描边")
```
## 设置画笔画出的字符串大小
**1.4、setTextSize**
函数作用：设置画笔画出来的文本大小。参数：大小值
示例：

```lua
-- 初始化画笔
paint = newPaint()
paint:setTextSize(5) -- 设置画笔绘制的文本大小为5
```
## 设置画笔笔触宽度
**1.5、setWidth**
函数作用：设置画笔笔触宽度。参数：宽度大小值，参数类型：数字。返回值：无
画笔笔触默认值为：1（应该是，我也不太清楚）
示例：

```lua
-- 初始化画笔
paint = newPaint()
paint:setWidth(10) -- 设置笔触宽度为10
```
**----------------------好了，以上就是画笔的所有函数了------------------------**
**下面是图片的函数**
# 图片函数
**2.1、从文件加载图片：loadFile**
函数作用：新建一个图片对象并返回。参数：(图片文件路径)或(图片字符串)，参数类型：文件路径 或 文件字符串
示例：

```lua
-- 新建图片方式一
bitmap = loadFile("图片文件路径")
```
## 从LuaString加载图片
**2.2、load**
函数作用：从lua的字符串中加载图片
和从文件加载图片函数类似，我就不多说了。
示例：

```lua
-- 新建图片方式二，从LuaString读取图片
file = io.open("图片文件路径", "r")
content = file:read("*a")
file:close()
bitmap = load(content)
```
## 设置图片的宽
**2.3、setWidth**
函数作用：按比例将图片缩放到设置的宽。参数：要设置的宽。
示例：

```lua
bitmap = loadFile("图片文件路径")
bitmap:setWidth(500) --将图片的宽设置为500像素
```
## 设置图片的高
**2.4、setHeight**
函数作用：按比例将图片缩放到设置的高。参数：要设置的高。
示例：

```lua
bitmap = loadFile("图片文件路径")
bitmap:setHeight(500) --将图片的高设置为500像素
```

## 获取图片的宽高
**2.6、getWH**
函数作用：获取图片的宽和高
示例：

```lua
bitmap = loadFile("图片文件路径")
-- 获取图片宽搞
wh = bitmap:getWH()
print("宽为："..wh[1].."高为："..wh[2])
```
## 删除图片
**2.8、remove**
函数作用：删除图片，释放内存。当某个图片对象不再需要使用时调用此方法删除图片
调用次函数后的Bitma对象将无法再继续使用。
示例：

```lua
bitmap = loadFile("图片文件路径")
--删除图片，并释放内存
bitmap:remove()
```

**———————————以上就是图片函数的所有内容了—————————————**
**下面是画布函数，也是最主要的函数**
# 画布函数
提示：画布不需要手动创建，写一个带有一个参数的方法。用view:show调用此方法就会传入画布（后面有教程）。每次传入的画布都是不同的，所以不需要把画布保存
示例：

```lua
--带有一个参数的方法，用来接收画布并进行绘制操作
function onDraw(canvas) --方法名随便写，没有要求
--创建画笔
paint = newPaint()
--使用传入的画布参数进行绘制一个圆的操作
canvas:drawCircle(200,200,100,paint) --在坐标（x200，y200）的地方为圆心画一个半径为100像素的圆
end
--View函数，后面有教程
view = newView()
view:show(onDraw) -- 显示绘制并自动刷新
sleep(1000) --休眠1000ms，避免脚本运行结束后绘制内容被系统回收
view:close()
```
效果图
## 绘制线段
**3.1、drawLine**
函数作用：在屏幕上绘制一条线段。参数（线段开头x坐标，y坐标，线段结尾x坐标，y坐标）。
```lua
paint = newPaint()
canvas:drawLine(0,0,300,200,paint)
```
下面的绘制函数也是这样使用自定义画笔进行绘制内容
## 绘制多条线段
**3.2、drawLines**
函数作用：绘制多条线段。参数：{线段开头x坐标，y坐标，结尾x坐标，y坐标，......}
参数个数无限制
```lua
-- 初始化画布
paint = newPaint()
canvas:drawLines({50, 600, 400, 600, 400, 600, 400, 50,400, 50, 50, 50, 50, 50, 50, 600}, paint)
```
执行后就会出现如下效果。

![drawLines解释](https://img-blog.csdnimg.cn/20200922183947851.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQ1OTI1MjMw,size_16,color_FFFFFF,t_70#pic_center)
以此类推，参数个数无限制！
如果实在不会的话，那就跳过吧！你们应该用不上
## 绘制矩形
**3.3、drawRect**
函数作用：绘制一个矩形。参数：（{矩形左上角x坐标，y坐标，矩形右下角x坐标，y坐标}，画笔）。
示例：

```lua
-- 初始化canvas
paint = newPaint()
canvas:drawRect({100,100,400,200}, paint)
```

## 绘制圆
**3.4、drawCircle**
函数作用：绘制一个圆。参数：（圆心x坐标，y坐标，圆的半径，画笔）。

```lua
-- 初始化画布
paint = newPaint()
canvas:drawCorcle(400,400,200, paint)
```
## 绘制背景颜色
**3.5、drawColor**
函数作用：绘制一个颜色作为背景。参数：颜色值。参数类型：字符串。
此方法不接收画笔对象！
示例：

```lua
-- 绘制一个半透明的白色作为背景
paint = newPaint()
canvas.drawColor("#80FFFFFF")
```
## 绘制文本
**3.5、drawText**
函数作用：绘制一个文本。参数：（要绘制的字符串，要绘制的左上角x坐标，y坐标，画笔）。
示例：

```lua
-- 初始化画布
paint = newPaint()
canvas.drawText("绘制测试", 200,200, paint)
```
## 绘制Bitmap图片
**3.6、draweBitmap**
函数作用：绘制一个Bitmap图片。参数：（bitmap对象，x坐标，y坐标，画笔）。
示例：

```lua-- 初始化bitmap图片
paint = newPaint()
bitmap = loadFile("图片文件路径")
canvas.drawBitmap(bitmap, 200,200, paint)
```

## 绘制圆弧
**3.8、drawArc**
函数作用：绘制一个圆弧。参数：（{**圆弧的绘制范围左上角x坐标，y坐标，右下角x坐标，y坐标**}，圆弧开始角度，经过角度，是否经过圆心，画笔）。。
这个相对比较复杂，看不懂可以跳过！
示例：

```lua
-- 初始化画布
paint = newPaint()
canvas:drawArc({100,100,400,400},0,90,false, paint)
```
## 裁剪绘制内容
**3.9、clipRect**
函数作用：将绘制范围裁剪为一个四边形。只有在四边形内的绘制内容才能显示。参数和drawRect一样：（{{四边形左上角x坐标，y坐标，右下角x坐标，y坐标}，画笔）
示例：

```lua
paint = newPaint()
canvas:clipRect({100,100,200,200}, paint)
```
## 移动画布
**3.10、translate**
函数作用：移动画布原点。参数：原点x坐标，y坐标
画布原点默认为x：0, y:0
示例：

```lua
canvas:translate(100,100)
```
## 旋转画布
**3.11、rotate**
函数作用：将画布顺时针旋转。参数（旋转角度）或者（旋转角度，旋转中心x坐标，y坐标）
示例：

```lua
canvas:rotate(90) --以画布原点为中心顺时针旋转90度
canvas:rotate(90, 300, 300) --以坐标（x:300，y:300）为中心顺时针旋转90度
```
## 保存画布状态
**3.12、save**
函数作用：保存画布的当前状态。用于对画布状态进行自定义前保存状态，配合恢复状态函数使用。
## 恢复画布状态
**3.13、restore**
函数作用：将上一次保存的画布状态恢复。用于配合保存画布状态函数使用。
示例：
```lua
canvas:save() --保存画布状态
canvas:translte(200,200) --移动画布原点到x：200，y:200
canvas:drawRect({0,0,100,100}, newPaint()) --绘制一个四边形
canvas:restore() --恢复状态
```
# 视图函数
视图函数是绘制内容的载体，用于加载显示和刷新绘制内容

## 新建视图
**1.1、newView()**
函数作用：新建一个视图对象并返回
示例：

```lua
view = newView()
```
## 显示绘制
**1.2、show**
函数作用：加载Lua绘制方法。并进行初始显示。参数：Lua的绘制方法, 帧数(可选)
一个视图对象只能使用一个Lua函数。下一次show将会把上一次的覆盖

**注意：绘制方法内不应进行耗时操作，因为绘制方法是在主线程被调用，执行的耗时直接决定了帧率的上限！**
示例：
```lua
paint = newPaint()
function onDraw(canvas) --方法名随意，没有要求。必须要有一个参数用来接收画布
--特别注意：绘制方法内不应该进行耗时操作和创建视图、画笔、图片等。会造成性能问题，应当在绘制方法外部先声明所需的变量
canvas:drawCircle(200,200,100, paint) --画圆
end
view = newView() --新建视图对象
view:show(onDraw) --调用onDraw方法进行绘制，并进行初始显示
--或者如下，增加有一个帧率参数，就会自动以60帧刷新绘制
view:show(onDraw, 60)
```
以上代码，在调用了view:show(onDraw)后，系统将会自动的多次调用onDraw方法，调用频率取决于帧率，所以绘制的内容也就是canvas函数需要写在onDraw(绘制方法)方法中。注意：onDraw(绘制方法) 中不要调用newPaint函数，加载图片，休眠等耗时操作。会影响刷新率
## 手动刷新绘制
**1.3、invalidate**
函数作用：调用已经show的方法进行刷新绘制。
注：调用show函数后会自动刷新绘图，特殊情况下才需要使用invalidate刷新绘图
示例：

```lua
view = newView()
i = 0
paint = newPaint()
view:show(function(canvas)
canvas:drawCircle(i,i,50,paint())
end) --将匿名方法作为绘制函数

for i = 1,200 do
i = i+2
invalidate()
sleep(20)
end
```
## 删除视图中的绘制内容
**1.4、close**
函数作用：删除视图中的所有绘制内容。
示例：

```lua
view = newView()
view:show(function(canvas)
--绘制操作
end)
view:close()
```
## 删除所有绘制视图
**1.5、removeAll**

```lua
view = newView()
view:show(function(canvas)
--绘制操作
end)
removeAllView()
```

# 代码演示和视频
```lua
--强制禁用android12以上的无障碍申请
disDrawAcc()

--创建画笔
paint = newPaint()
--画笔红色
paint:setColor("#FF0000")
--画笔笔触宽度10像素
paint:setWidth(10)
--创建绘制载体视图
view = newView()

--绘制方法
y = 0
function onDraw(canvas)
--移动画布原点
canvas:translate(200, y)
--画圆
canvas:drawCircle(0, 100, 100, paint)
--画矩形
canvas:drawRect({200, 0, 400, 200}, paint)
y = y + 5
end

--以60帧刷新率显示绘制
view:show(onDraw, 60)
gg.sleep(3000)
```


[video(video-oxoe0UmK-1679757389544)(type-csdn)(url-https://live.csdn.net/v/embed/245908)(image-https://video-community.csdnimg.cn/vod-84deb4/f6ac6e502c05434b9270fd72f8a3abb4/snapshots/bab47786f4a543658f2fe0d74e77ff8a-00001.jpg?auth_key=4819537868-0-0-b429d81f07b93211f3519377af86f5cb)(title-代码演示效果)]



# 其他函数
## 多线程函数
**1.1、thread**
函数作用：启动新线程调用Lua方法
多线程函数有个bug暂时无法修复。不能多个线程同时调用sleep函数。否则会报错。
示例：

```lua
function run()
--要在新线程里执行的内容
for i = 1,1000 do
print(i)
end
end
thread(run)
----------------------------------------
--或者使用匿名方法
thread(function()
--要在新线程中执行的内容
for i = 1, 1000 do
print(i)
end
end)
```

## 获取屏幕分辨率
**1.4、getWH**
函数作用：获取屏幕可显示范围的长和宽
示例：

```lua
wh = getWH()
print("屏幕的宽:"..wh[1])
print("屏幕的高为:"..wh[2])
```

## 源码和成品
以上就是全部内容了，项目源码地址如下：
gg96版：https://github.com/Thousand-Dust/GG-CustomizeFunction
gg101版：https://github.com/Thousand-Dust/GG-CustomizeFunction-101
成品下载地址也在里面