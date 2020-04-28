# Download_Library
Library for uploading files and images

Example- 
RepoDelegate delegate = RepoDelegate.getInstance();
      File file = new ImageFile(url, new DownloadCallback() {
			@Override
			public void onStart(File file) {

			}

			@Override
			public void onSuccess(File file) {
				spinner.hide();
				spinner.setVisibility(View.GONE);
				view.setImageBitmap(((ImageFile)file).getBitmap());
			}

			@Override
			public void onFailure(File file, int statusCode, byte[] errorResponse, Throwable e) {
				spinner.hide();
				spinner.setVisibility(View.GONE);
				view.setScaleType(ImageView.ScaleType.FIT_XY);
				view.setImageResource(no_image);
			}

			@Override
			public void onCancelled(File file) {

			}
		});
