# AndroidPhotoSelector

一个比较简洁的类似微信相册多选的库，代码量比较少，修改定制比较简单。

考虑到几乎所有app都必备图片加载框架，所以需要自己实现图片加载接口。

图片预览控件来自：TouchImageView https://github.com/MikeOrtiz/TouchImageView

    // 只要在调用前初始化就可以了
    PhotoSelector.init(new PhotoSelector.ImageLoader() {
        @Override
        public void loadImage(Context context, ImageView imageView, String filePath) {
            Glide.with(context).load(new File(filePath)).placeholder(R.drawable.aps_default_img).into(imageView);
        }
    });
        
    // 显示
    PhotoSelector.startSelector(this, REQUEST_PHOTO, 10);
        
        
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK) {
            // 获取结果
            ArrayList<String> photoPathList = PhotoSelector.getPhotoPathListFromIntentData(data);

            for (int i = 0, len = photoPathList.size(); i < len; i++) {
                Log.i("xxxxxx", photoPathList.get(i));
            }
        }
    }
