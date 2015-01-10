基于惯性大水滴滴水算法和支持向量机的验证码识别
==========

Inertial big drop fall algorithm, libsvm
------------------------

### Introduction: 
Recognize the CAPTCHA of some specified websites
(http://blog.csdn.net/yinchuandong2/article/details/40340735)

### Functions:
    1.	Download the CAPTCHA from specified websites
    2.	Segment and recognize the distorted and sticky characters from picture
    
### Technologies:
    1.	Use Java multi-thread and HTTP protocol to download CAPTCHA
    2.	Use Otsu threshold to binarize the original image
    3.	Use CFS (Color Filling Segmentation) algorithm to implement the first segmentation
    4.	Use BIDF (Big Inertial Drop Fall) algorithm to implement the second segmentation
    5.	Use LibSvm to build the model and predict the class of characters

### 根目录目录结构
    |—— JavaVerify
    |   |—— download 从网上下载下来的原验证码图片，未经过处理
    |   |—— 1_gray 灰度化处理过的图片
    |   |—— 2_cfs 使用CFS连通域切割之后的图片
    |   |—— 3_drop 使用滴水算法切割之后的图片
    |   |—— 4_scale 缩放成16X16像素之后的图片
    |   |—— svm 使用livsvm建立的训练集，模型，以及分类的结果都保存在这里
    |   |—— src java的源代码
    |   |—— 其它的目录都是用来测试的
    
### java包名
    src
      |—— Base 基类包（暂时没有用到）
      |   |—— Base.java 将一些图片处理的公共方法放到这里
      |
      |—— Ui 项目的入口
      |   |—— IndexFrame.java
      |
      |—— Widget 自定义控件包
      |   |—— AlphaLabel.java 半透明label
      |   |—— AlphaPane.java 半透明Pane
      |   |—— AlphaScrollpane.java 半透明ScrollPane
      |   |—— AlphaTextField.java
      |   |—— ScaleIcon.java 图片可缩放的icon
      |   |—— SliderUi.java 自定义的滚动滑块
      |
      |—— Model 实体类
      |   |—— Poin.java
      |   |—— SubImage.java
      |
      |—— svmHelper Libsvm自带的帮助类
      |   |—— svm_predict.java 
      |   |—— svm_scale.java
      |   |—— svm_train.java
      |
      |—— train **切分图片和训练模型**
      |   |—— BinaryTest.java 测试二值化图片
      |   |—— Crawl.java 从12306上面爬取验证码图片，并保存到本地
      |   |—— Extremum.java 求极值的帮助类
      |   |—— Identy.java **最终的识别类，可由外部界面调用**
      |   |—— ImageUtil.java 图片操作的帮助类
      |   |—— Predict.java LibSvm进行预测的类
      |   |—— Train.java LibSvm进行训练的类
      |   |—— PreProcess.java 图片预处理，二值化等操作
      |   |—— SegCfg.java Cfg切割类
      |   |—— SegWaterDrop.java 滴水算法切割
      |   |—— ScaleImage.java 对图片进行缩放
      |
      |—— test1 用来测试其他的，不用管它
 
      
      
      
    
