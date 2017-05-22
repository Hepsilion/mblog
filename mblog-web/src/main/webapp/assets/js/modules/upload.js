/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/

define(function(require, exports, module) {
	J = jQuery;
	require('dmuploader');
	
	$.album = $.extend( {}, {
	    
	    addFile: function(id, i, file){
			var filename = file.name;
			if (filename && filename.length > 30) {
				filename = filename.substring(0, 30) + '...';
			}
			
			var i = $(id).attr('file-counter');
			if (!i){
				$(id).empty();
				i = 0;
			} else {
				i = parseInt(i);
			}
			
			var template = '<div id="album-file' + i + '" class="uploader-item">' +
							   '<button type="button" class="close uploader-close" data-action="remove-album"><span>×</span></button>' +
			                   '<div class="uploader-image-preview"><img src="http://placehold.it/48.png" /></div>' +
			                   '<span class="uploader-file-id">第' + (i + 1) + '张</span> - <span class="uploader-file-name">' + filename + '</span> <span class="uploader-file-size">(' + $.album.humanizeSize(file.size) + ')</span><br />状态: <span class="uploader-file-status">等待上传</span>'+
			                   '<div class="progress active">'+
			                       '<div class="progress-bar progress-bar-success" role="progressbar" style="width: 0%;">'+
			                           '<span class="sr-only">0% Complete</span>'+
			                       '</div>'+
			                   '</div>'+
			                   '<input type="hidden" name="delayImages" value=""/>' +
			               '</div>';
			               
			i++;
			
			$(id).attr('file-counter', i);
			
			$(id).prepend(template);
		},
		
		updateFileStatus: function(i, status, message){
			$('#album-file' + i).find('span.uploader-file-status').html(message).addClass('uploader-file-status-' + status);
		},
		
		updateFileData: function(i, data){
			$('#album-file' + i).find('input').val(data.data);
		},
		
		updateFileProgress: function(i, percent){
			$('#album-file' + i).find('div.progress-bar').width(percent);
			
			$('#album-file' + i).find('span.sr-only').html('已上传 ' + percent);
		},
		
		humanizeSize: function(size) {
	      var i = Math.floor( Math.log(size) / Math.log(1024) );
	      return ( size / Math.pow(1024, i) ).toFixed(2) * 1 + ' ' + ['B', 'kB', 'MB', 'GB', 'TB'][i];
	    }

	  }, $.album);
	
	$('#drag-and-drop-zone').dmUploader({
		url : _base_path + '/post/upload',
		dataType : 'json',
		allowedTypes : 'image/*',
		maxFiles : 6,
		onBeforeUpload : function(id) {
			$.album.updateFileStatus(id, 'default', '上传中...');
		},
		onNewFile : function(id, file) {
			$.album.addFile('#upload-albums', id, file);

			/*** 预览图片加载 ***/
			if (typeof FileReader !== "undefined") {

				var reader = new FileReader();

				// 获取最后添加的图片
				var img = $('#upload-albums').find('.uploader-image-preview>img').eq(0);

				reader.onload = function(e) {
					img.attr('src', e.target.result);
				};

				reader.readAsDataURL(file);

			} else {
				// 如果比支持 FileReader 清空所有预览
				$('#upload-albums').find('.uploader-image-preview').remove();
			}
		},
		onUploadProgress : function(id, percent) {
			var percentStr = percent + '%';
			$.album.updateFileProgress(id, percentStr);
		},
		onUploadSuccess : function(id, data) {
			$.album.updateFileStatus(id, 'success', '上传完成');
			$.album.updateFileProgress(id, '100%');
			$.album.updateFileData(id, data);
		},
		onUploadError : function(id, message) {
			$.album.updateFileStatus(id, 'error', '上传文件出错');
		},
		onFilesMaxError : function (file) {
			alert('图片个数已达限制');
		}
	});
	
	$(document).on('click', 'button[data-action="remove-album"]', function () {
		$(this).closest('div').remove();
		
		var i = $('#upload-albums').attr('file-counter');
		if (parseInt(i) > 0) {
			$('#upload-albums').attr('file-counter', i - 1);
		}
	});
	
	
});