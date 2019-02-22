#Android关于OOM的解决方案 ##OOM

内存溢出（Out Of Memory）
也就是说内存占有量超过了VM所分配的最大

##出现OOM的原因
加载对象过大
相应资源过多，来不及释放

##如何解决
1.在内存引用上做些处理，常用的有软引用、强化引用、弱引用
1).软引用（SoftReference）的含义是，如果一个对象只具有软引用，而当前虚拟机堆内存空间足够，那么垃圾回收器就不会回收它，反之就会回收这些软引用指向的对象。
2).弱引用（WeakReference）与软引用的区别在于，垃圾回收器一旦发现某块内存上只有弱引用（一定请注意只有弱引用，没强引用），不管当前内存空间是否足够，那么都会回收这块内存。

2.在内存中加载图片时直接在内存中作处理，如边界压缩
1).尺度压缩是指：设置采样率，减少像素点，达到图片的压缩；
/**
	 *    对图片进行尺度压缩
	 */
	public static Bitmap zoomBitmap(String path){
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, newOpts);
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		newOpts.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, newOpts);
	}
           图片压缩关键点是：设置边界属性为true,然后设置采样比，最后设置边界属性，读取图片。边界数据设置为true，Google这样解释的：
   /**
         * If set to true, the decoder will return null (no bitmap), but
         * the out... fields will still be set, allowing the caller to query
         * the bitmap without having to allocate the memory for its pixels.
         */
        public boolean inJustDecodeBounds;
      就是不会图片分配内存，但是我们可以获取图片的属性，高和宽。采样比的计算：原图片的图片的高和宽和我们想要显示的图片高宽大小的比值最小。计算如下：

	public static int computeImageSampleSize(int srcWidth, int srcHeight, int targetWidth,
			int targetHeight) {
		int widthScale = (int) Math.ceil((float) srcWidth / targetWidth);
		int heightScale = (int) Math.ceil((float) srcHeight / targetHeight);
		return Math.min(widthScale, heightScale); 
	}
 2).质量压缩：减少图片的透明度，和饱和度，而Bitmap内存大小不会变，就是图片的高度和宽度不会变；

	/**
	 * 对bitmap进行质量压缩
	 * */
	private static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}
质量压缩，只要设置压缩率：上面的代码是把图片质量压缩到100Kb以内，最后返回bitmap。

3.动态回收内存

4.优化Dalvik虚拟机的堆内存分配
优化Dalvik虚拟机的堆内存分配 


对于Android平台来说，其托管层使用的Dalvik Java VM从目前的表现来看还有很多地方可以优化处理，比如我们在开发一些大型游戏或耗资源的应用中可能考虑手动干涉GC处理，使用 dalvik.system.VMRuntime类提供的setTargetHeapUtilization方法可以增强程序堆内存的处理效率。

当然具体原理我们可以参考开源工程，这里我们仅说下使用方法:  
private final static float TARGET_HEAP_UTILIZATION = 0.75f; 在程序onCreate时就可以调用 VMRuntime.getRuntime().setTargetHeapUtilization(TARGET_HEAP_UTILIZATION); 即可。

5.自定义堆内存大小
对于一些大型Android项目或游戏来说在算法处理上没有问题外，影响性能瓶颈的主要是Android自己内存管理机制问题，目前手机厂商对RAM都比较吝啬，对于软件的流畅性来说RAM对性能的影响十分敏感，除了 优化Dalvik虚拟机的堆内存分配 外，我们还可以强制定义自己软件的对内存大小，

我们使用Dalvik提供的 dalvik.system.VMRuntime类来设置最小堆内存为例:

Dalvik.VMRuntime类，提供对虚拟机全局，Dalvik的特定功能的接口。Android为每个程序分配的对内存可以通过Runtime类的 totalMemory() freeMemory() 两个方法获取VM的一些内存信息

 

private final static int CWJ_HEAP_SIZE = 6* 1024* 1024 ;

 

VMRuntime.getRuntime().setMinimumHeapSize(CWJ_HEAP_SIZE); //设置最小heap内存为6MB大小。

 

当然对于内存吃紧来说还可以通过手动干涉GC去处理

比如关于图片的处理:

Android上的Bitmap对象销毁，可以借助recycle()方法显示让GC回收一个Bitmap对象，通常对一个不用的Bitmap可以使用下面的方式，如

if(bitmapObject.isRecycled()==false) //如果没有回收   

bitmapObject.recycle();  

 

注意事项:

max heap size, min heap size, heap utilization（堆利用率）。

 

Max Heap Size，是堆内存的上限值，Android的缺省值是16M（某些机型是24M），对于普通应用这是不能改的。

 

函数setMinimumHeapSize其实只是改变了堆的下限值，它可以防止过于频繁的堆内存分配，当设置最小堆内存大小超过上限值时仍然采用堆的上限值(16M)，对于内存不足没什么作用。

 

setTargetHeapUtilization(float newTarget) 可以设定内存利用率的百分比，当实际的利用率偏离这个百分比的时候，虚拟机会在GC的时候调整堆内存大小，让实际占用率向个百分比靠拢。
