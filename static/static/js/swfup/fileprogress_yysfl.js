/*
	A simple class for displaying file information and progress
	Note: This is a demonstration only and not part of SWFUpload.
	Note: Some have had problems adapting this class in IE7. It may not be suitable for your application.
*/

// Constructor
// file is a SWFUpload file object
// targetID is the HTML element id attribute that the FileProgress HTML structure will be added to.
// Instantiating a new FileProgress object with an existing file will reuse/update the existing DOM elements
function FileProgress(file, targetID) {
	this.fileProgressID = file.id;

	this.opacity = 100;
	this.height = 0;

    var filename = shorten_filename(file.name);

	this.fileProgressWrapper = document.getElementById(this.fileProgressID);
	if (!this.fileProgressWrapper) {
		this.fileProgressWrapper = document.createElement("div");
		this.fileProgressWrapper.className = "progressWrapper";
		this.fileProgressWrapper.id = this.fileProgressID;

		this.fileProgressElement = document.createElement("div");
		this.fileProgressElement.className = "progressContainer";

		var progressCancel = document.createElement("a");
		progressCancel.className = "progressCancel";
		progressCancel.href = "#";
		progressCancel.style.visibility = "hidden";
		progressCancel.appendChild(document.createTextNode(" "));

		var progressText = document.createElement("div");
		progressText.className = "progressName";
		progressText.appendChild(document.createTextNode(filename));

		var progressBar = document.createElement("div");
		progressBar.className = "progressBarInProgressWrapper";
        progressBar.innerHTML = '<div></div>';

		var progressStatus = document.createElement("div");
		progressStatus.className = "progressBarStatus";
		progressStatus.innerHTML = "&nbsp;";

		this.fileProgressElement.appendChild(progressCancel);
		this.fileProgressElement.appendChild(progressText);
		this.fileProgressElement.appendChild(progressStatus);
		this.fileProgressElement.appendChild(progressBar);

		this.fileProgressWrapper.appendChild(this.fileProgressElement);

		document.getElementById(targetID).appendChild(this.fileProgressWrapper);
	} else {
		this.fileProgressElement = this.fileProgressWrapper.firstChild;
		this.reset();
	}

	this.height = this.fileProgressWrapper.offsetHeight;
	this.setTimer(null);


}

FileProgress.prototype.setTimer = function (timer) {
	this.fileProgressElement["FP_TIMER"] = timer;
};
FileProgress.prototype.getTimer = function (timer) {
	return this.fileProgressElement["FP_TIMER"] || null;
};

FileProgress.prototype.reset = function () {
	this.fileProgressElement.className = "progressContainer";

	this.fileProgressElement.childNodes[2].innerHTML = "&nbsp;";
	this.fileProgressElement.childNodes[2].className = "progressBarStatus";
	
	this.fileProgressElement.childNodes[3].className = "progressBarInProgressWrapper";
	this.fileProgressElement.childNodes[3].childNodes[0].style.width = "0%";
	
	this.appear();	
};

FileProgress.prototype.setProgress = function (percentage) {
	this.fileProgressElement.className = "progressContainer blue";
	this.fileProgressElement.childNodes[3].childNodes[0].className = "progressBarInProgress";
	this.fileProgressElement.childNodes[3].childNodes[0].style.width = percentage + "%";

	this.appear();	
};
FileProgress.prototype.setComplete = function () {
	this.fileProgressElement.className = "progressContainer blue fl-complete";
	this.fileProgressElement.childNodes[3].className = "progressBarComplete";
	this.fileProgressElement.childNodes[3].style.width = "";

	// var oSelf = this;
	// this.setTimer(setTimeout(function () {
	// 	oSelf.disappear();
	// }, 1000));
};
FileProgress.prototype.setError = function () {
	this.fileProgressElement.className = "progressContainer red";
	this.fileProgressElement.childNodes[3].className = "progressBarError";
	this.fileProgressElement.childNodes[3].style.width = "";

	var oSelf = this;
	this.setTimer(setTimeout(function () {
		oSelf.disappear();
	}, 8000));
};
FileProgress.prototype.setCancelled = function () {
	this.fileProgressElement.className = "progressContainer";
	this.fileProgressElement.childNodes[3].className = "progressBarError";
	this.fileProgressElement.childNodes[3].style.width = "";

	var oSelf = this;
	this.setTimer(setTimeout(function () {
		oSelf.disappear();
	}, 2000));
};
FileProgress.prototype.setStatus = function (status) {
	this.fileProgressElement.childNodes[2].innerHTML = status;
};

// Show/Hide the cancel button
FileProgress.prototype.toggleCancel = function (show, swfUploadInstance) {
	this.fileProgressElement.childNodes[0].style.visibility = show ? "visible" : "hidden";
	if (swfUploadInstance) {
		var fileID = this.fileProgressID;
		this.fileProgressElement.childNodes[0].onclick = function () {
			swfUploadInstance.cancelUpload(fileID);
			return false;
		};
	}
};

FileProgress.prototype.appear = function () {
	if (this.getTimer() !== null) {
		clearTimeout(this.getTimer());
		this.setTimer(null);
	}
	
	if (this.fileProgressWrapper.filters) {
		try {
			this.fileProgressWrapper.filters.item("DXImageTransform.Microsoft.Alpha").opacity = 100;
		} catch (e) {
			// If it is not set initially, the browser will throw an error.  This will set it if it is not set yet.
			this.fileProgressWrapper.style.filter = "progid:DXImageTransform.Microsoft.Alpha(opacity=100)";
		}
	} else {
		this.fileProgressWrapper.style.opacity = 1;
	}
		
	this.fileProgressWrapper.style.height = "";
	
	this.height = this.fileProgressWrapper.offsetHeight;
	this.opacity = 100;
	this.fileProgressWrapper.style.display = "";
	
};

// Fades out and clips away the FileProgress box.
FileProgress.prototype.disappear = function () {
    this.fileProgressWrapper.style.display = "none";
};


function fileQueueError(file, errorCode, message) {
	try {
        var errorText;
		if (errorCode === SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED) {
			alert("You have attempted to queue too many files.\n" + (message === 0 ? "You have reached the upload limit." : "You may select " + (message > 1 ? "up to " + message + " files." : "one file.")));
			return;
		}

		var progress = new FileProgress(file, this.customSettings.progressTarget);
        progress.disappear();
		progress.setError();
		progress.toggleCancel(false);

		switch (errorCode) {
		case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
            errorText = "文件过大";
			break;
		case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
            errorText = "文件大小为0";
			break;
		case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
            errorText = "不支持的类型";
			break;
		case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
			alert("超过数量限制  " +  (message > 1 ? "only " +  message + " files" : " can not add any more."));
			break;
		default:
			if (file !== null) {
                errorText = "未知错误";
			}
			break;
		}
	} catch (ex) {
        this.debug(ex);
    }

    $('.fl-uploader-error:hidden').show();
    $('.fl-uploader-errfiles').append('<div>'+shorten_filename(file.name)+'&#12288;'+errorText+'</div>');
}

function uploadError(file, errorCode, message) {
	try {
		var progress = new FileProgress(file, this.customSettings.progressTarget);
        var errorText;
        progress.disappear();
/*
		switch (errorCode) {
		case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
            errorText = 'Upload Error: ' + message;
			break;
		case SWFUpload.UPLOAD_ERROR.MISSING_UPLOAD_URL:
            errorText = "Config Error";
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
            errorText = "Upload Failed.";
			break;
		case SWFUpload.UPLOAD_ERROR.IO_ERROR:
            errorText ="Server (IO) Error";
			break;
		case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
            errorText ="Security Error";
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
            errorText ="Upload limit exceeded.";
			break;
		case SWFUpload.UPLOAD_ERROR.SPECIFIED_FILE_ID_NOT_FOUND:
            errorText ="File not found.";
			break;
		case SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED:
            errorText ="Failed Validation.  Upload skipped.";
			break;
		case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
			if (this.getStats().files_queued === 0) {
				document.getElementById(this.customSettings.cancelButtonId).disabled = true;
			}
            errorText = 'Cancelled';
			progress.setCancelled();
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
            errorText = 'Stopped';
			break;
		default:
            errorText = "Unhandled Error: " +errorCode;
			break;
		}
        */
        errorText = "服务器错误";


        $('.fl-uploader-error:hidden').show();
        $('.fl-uploader-errfiles').append('<div>'+shorten_filename(file.name)+'&#12288;'+errorText+'</div>');

	} catch (ex) {
        this.debug(ex);
    }
}

function uploadSuccess(file, serverData) {
	try {
		var progress = new FileProgress(file, this.customSettings.progressTarget);
        // $('.fl-uploader-error:visible').hide();
		progress.setComplete();
		progress.setStatus("上传成功");
		// progress.toggleCancel(false);

		if(typeof(this.customSettings.callback) != "undefined"){
			this.customSettings.callback(file,serverData);
		}

	} catch (ex) {
		this.debug(ex);
	}
}

function fileDialogStart(){
    emptyErrorLog();
    $('.fl-uploader-error:visible').hide();
}
/*
function fileDialogComplete(numFilesSelected, numFilesQueued) {
	try {
		if (numFilesQueued > 0) {
            if(numFilesSelected == numFilesQueued){
                emptyErrorLog();
                $('.fl-uploader-error:visible').hide();
            }
			this.startUpload();
		}
	} catch (ex)  {
        this.debug(ex);
	}
}
*/

function emptyErrorLog(){
    $('.fl-uploader-errfiles').empty();
}

function trip_ext(oldname){
    var regexp = /(.+)\.\w+/gi;
    var matched = regexp.exec(oldname);
    if(matched && matched.length>=2){
        return matched[1]; 
    }
}

function shorten_filename(str){
    if(str.length < 13) return str;
    var regexp = /^(.{6}).*(.{2})\.(\w+)/gi;
    var matched = regexp.exec(str);
    if(matched && matched.length==4){
        return matched[1]+'...'+matched[2]+'.'+matched[3];
    }else{
        throw Error('文件名处理出错 Function: shorten_filename()');
    }
}
