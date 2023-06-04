# GG-CustomizeFunction
对GameGuardian进行自定义的函数,目前只添加绘制函数（在悬浮窗对View进行自定义绘制）和多线程函数。如需运行在非gg luaj上，只需要修改Thousand_Dust/Tools.getContext()方法即可。
## 制作不易给个☆star吧，谢谢啦！！！

# gg版本
此项目基于96版gg的jar进行编写，需要101版移步：
https://github.com/Thousand-Dust/GG-CustomizeFunction-101
代码不同但函数库效果是一样的

# 函数使用教程
函数教程 CSDN：https://blog.csdn.net/qq_45925230/article/details/108716660
其他平台暂无

# 给其他gg添加此函数库教程
视频录制者文艺：https://b23.tv/LcXSQap

# 成品安装包
https://wwt.lanzoul.com/b02vn048j
密码:draw

# 测试脚本

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
